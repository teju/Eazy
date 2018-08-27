package eazi.com.eazi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import eazi.com.eazi.adapters.ChatAdapter;
import eazi.com.eazi.database.DataBaseHelper;
import eazi.com.eazi.database.Messages;
import eazi.com.eazi.utils.CommonMethods;
import eazi.com.eazi.utils.Constants;
import eazi.com.eazi.utils.GPSTracker;

public class StartChat extends AppCompatActivity implements OnClickListener,
        AdapterView.OnItemLongClickListener,AdapterView.OnItemClickListener {

    private EditText msg_edittext;
    private String user1 = "";
    public static String USER_TWO = "";
    private Random random;
    public static ArrayList<Messages> chatlist;
    public static ChatAdapter chatAdapter;
    ListView msgListView;
    private TextView name;
    private String user_name;
    private ImageView back;
    private DataBaseHelper database;
    public boolean isMultipleSelect = false;
    ArrayList<Integer> selectedRows = new ArrayList<>();
    private GPSTracker gps;
    int sdk = android.os.Build.VERSION.SDK_INT;
    private List<Messages> chatMessageList = new ArrayList<>();
    private LinearLayout accessory;
    private LinearLayout profile_details;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);
        onNewIntent(getIntent());
        initUI();
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            if(extras.containsKey("user_phone")) {
                user1 = extras.getString("user_phone");
                System.out.println("USERLIST user1 "+user1+" ");
                initUI();
                initData();

            }
        }
    }


    public void initUI(){
        msg_edittext = (EditText)findViewById(R.id.messageEditText);
        accessory = (LinearLayout)findViewById(R.id.accessory);
        profile_details = (LinearLayout)findViewById(R.id.profile_details);
        back = (ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);
        msgListView = (ListView)findViewById(R.id.msgListView);
        msgListView.setOnItemLongClickListener(this);
        name = (TextView)findViewById(R.id.name);
        ImageButton sendButton = (ImageButton) findViewById(R.id.sendMessageButton);
        ImageButton share_loc = (ImageButton) findViewById(R.id.share_loc);
        ImageButton copy = (ImageButton) findViewById(R.id.copy);
        ImageButton forward = (ImageButton) findViewById(R.id.forward);
        ImageButton delete = (ImageButton) findViewById(R.id.delete);
        sendButton.setOnClickListener(this);
        share_loc.setOnClickListener(this);
        copy.setOnClickListener(this);
        forward.setOnClickListener(this);
        delete.setOnClickListener(this);

        // ----Set autoscroll of listview when a new message arrives----//
        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);
        msgListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        msgListView.setOnItemClickListener(this);



    }

    public void initData(){
        chatMessageList.clear();
        String params = getIntent().getStringExtra(Constants.forwardMsgList);
        if(!CommonMethods.isEmpty(params)) {
            msg_edittext.setText(params);
        }
        random = new Random();
        if(getIntent().getStringExtra("user_name") != null) {
            user_name = getIntent().getStringExtra("user_name");
        }

        if(getIntent().getStringExtra("user_phone") != null) {
            user1 = getIntent().getStringExtra("user_phone");
            user1 = user1.replaceAll("\\s","");
        }

        if(user_name == null) {
            user_name = CommonMethods.getcontactName(this,user1);
        }

        database = new DataBaseHelper(this);
        chatlist = new ArrayList<Messages>();

        if(!user1.contains("@eazi.ai")) {
            user1 = user1+"@eazi.ai";
        }

        if(!user1.startsWith("91")) {
            user1 = "91"+user1;
        }

        chatAdapter = new ChatAdapter(this,user1);

        msgListView.setAdapter(chatAdapter);

        if(user_name.length() > 0) {
            name.setText(user_name);
        } else if(user1 != null){
            name.setText(user1.replace("@eazi.ai",""));
        }
        chatMessageList = database.getMessages(user1);

    }

    public void sendTextMessage(String message, String type) {
        //String message = msg_edittext.getEditableText().toString();
        if (!message.equalsIgnoreCase("")) {
            database.putMessages(database,CommonMethods.getSharedPrefValue(this,Constants.user_name)
                    ,user1,message,CommonMethods.getDate(),"true",type);
            database.putUsers(database,user1);
            msg_edittext.setText("");
            chatAdapter.add(this,user1);
            chatAdapter.notifyDataSetChanged();
            Invite activity = new Invite();
            activity.getmService().xmpp.sendMessage(message,user1,type);
        }
    }

    @Override
    public void onBackPressed() {
        if(!isMultipleSelect) {

        } else {
           clearColor();
            isMultipleSelect = false;
        }
    }

    @Override
    public void onClick(View v) {
        StringBuilder sb = new StringBuilder();
        clearColor();
        isMultipleSelect = false;
        switch (v.getId()) {
            case R.id.sendMessageButton:
                sendTextMessage(msg_edittext.getEditableText().toString(),Constants.MessageTypeText);
                break;
            case R.id.copy:
                sb = new StringBuilder();
                for (int i=0;i<selectedRows.size();i++) {
                    if(i == 0) {
                        if(chatMessageList.get(selectedRows.get(i)).getType() != null && !chatMessageList.get(selectedRows.get(i)).getType().equals(Constants.MessageTypeMaps)) {
                            sb.append(chatMessageList.get(selectedRows.get(i)).getMessage_Text());
                        }
                    } else {
                        if(chatMessageList.get(selectedRows.get(i)).getType() != null && !chatMessageList.get(selectedRows.get(i)).getType().equals(Constants.MessageTypeMaps)) {
                            sb.append("\n" + chatMessageList.get(selectedRows.get(i)).getMessage_Text());
                        }
                    }
                }

                if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(sb.toString());
                    Toast.makeText(getApplicationContext(), "Text Copied to Clipboard", Toast.LENGTH_SHORT).show();
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Clip", sb.toString());
                    Toast.makeText(getApplicationContext(), "Text Copied to Clipboard", Toast.LENGTH_SHORT).show();
                    clipboard.setPrimaryClip(clip);
                }
                break;
            case R.id.forward:
                sb = new StringBuilder();
                for (int i=0;i<selectedRows.size();i++) {
                    if(i == 0) {
                        if(chatMessageList.get(selectedRows.get(i)).getType() != null && !chatMessageList.get(selectedRows.get(i)).getType().equals(Constants.MessageTypeMaps)) {
                            sb.append(chatMessageList.get(selectedRows.get(i)).getMessage_Text());
                        }

                    } else {
                        if(chatMessageList.get(selectedRows.get(i)).getType() != null && !chatMessageList.get(selectedRows.get(i)).getType().equals(Constants.MessageTypeMaps)) {
                            sb.append("\n" + chatMessageList.get(selectedRows.get(i)).getMessage_Text());
                        }
                    }
                }
                Intent intent = new Intent(this,Invite.class);
                intent.putExtra(Constants.forwardMsgList,sb.toString());
                startActivity(intent);
                break;
            case R.id.delete:
                for (int i=0;i<selectedRows.size();i++) {
                    database.deleteFrominbox(user1,chatMessageList.get(selectedRows.get(i)).getID());
                }
                if(StartChat.chatAdapter != null) {
                    StartChat.chatAdapter.add(this, user1);
                    StartChat.chatAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.share_loc:
                if (!gps.canGetLocation()) {
                    gps.showSettingsAlert(this);
                } else {
                    try {
                        JSONObject jobj = new JSONObject();
                        jobj.put("latitude", gps.getLatitude());
                        jobj.put("longitude", gps.getLongitude());
                        sendTextMessage(jobj.toString(), Constants.MessageTypeMaps);
                        System.out.println("JSONObject123456 " + jobj.toString());
                    } catch (Exception e) {

                    }
                }
                //shareLocation();
                break;
            case R.id.back:
                Intent i = new Intent(this,Invite.class);
                startActivity(i);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        gps = new GPSTracker(StartChat.this);


    }

    public void clearColor() {

        accessory.setVisibility(View.GONE);
        profile_details.setVisibility(View.VISIBLE);

        for (int i = 0; i < chatMessageList.size(); i++) {
            if(msgListView.getChildAt(i) != null) {
                msgListView.getChildAt(i).setBackgroundColor(Color.parseColor("#e1e1e1"));
            }
        }

    }

    public void shareLocation(int pos){
        if (!gps.canGetLocation()) {
            gps.showSettingsAlert(this);
        } else {
            try {
                JSONObject json = new JSONObject(chatMessageList.get(pos).getMessage_Text());
                String uri = String.format(Locale.ENGLISH, "geo:0,0?q=" + Double.parseDouble(json.getString("latitude"))
                        + "," + Double.parseDouble(json.getString("longitude")) + "(Treasure)?z=11");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }catch (Exception e){

            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        System.out.println("onItemLongClick123456 " + chatMessageList.size() +" "+position+""
                +msgListView.getChildAt(position) +" "+msgListView.getChildCount());
        chatMessageList.clear();
        chatMessageList = database.getMessages(user1);

        selectedRows.clear();
        accessory.setVisibility(View.VISIBLE);
        profile_details.setVisibility(View.GONE);
        if (!isMultipleSelect) {
            isMultipleSelect = true;
            if (msgListView != null) {
                view.setBackgroundColor(Color.parseColor("#c1cdda"));
            }
            selectedRows.add(position);
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isMultipleSelect) {
            view.setBackgroundColor(Color.parseColor("#c1cdda"));
            selectedRows.add(position);
        } else {
            chatMessageList.clear();
            chatMessageList = database.getMessages(user1);

            if (chatMessageList.get(position).getType() != null &&
                    chatMessageList.get(position).getType().equals(Constants.MessageTypeMaps)) {
                shareLocation(position);
            }
        }
    }
}