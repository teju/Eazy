package eazi.com.eazi;

/**
 * Created by Khushvinders on 15-Nov-16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import eazi.com.eazi.adapters.ChatAdapter;
import eazi.com.eazi.database.DataBaseHelper;
import eazi.com.eazi.database.Messages;
import eazi.com.eazi.model.Contact;
import eazi.com.eazi.utils.CommonMethods;
import eazi.com.eazi.utils.Constants;

public class StartChat extends AppCompatActivity implements OnClickListener {

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
        back = (ImageView)findViewById(R.id.back);

        msgListView = (ListView)findViewById(R.id.msgListView);
        name = (TextView)findViewById(R.id.name);
        ImageButton sendButton = (ImageButton) findViewById(R.id.sendMessageButton);
        sendButton.setOnClickListener(this);

        // ----Set autoscroll of listview when a new message arrives----//
        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);
    }

    public void initData(){
        random = new Random();
        if(getIntent().getStringExtra("user_name") != null) {
            user_name = getIntent().getStringExtra("user_name");
        }

        if(getIntent().getStringExtra("user_phone") != null) {
            user1 = getIntent().getStringExtra("user_phone");
            user1 = user1.replaceAll("\\s","");
        }

        List<Contact> contactList = CommonMethods.getContact(this);
        if(user_name == null) {
            for (int i = 0;i<contactList.size();i++){
                if(user1.contains(contactList.get(i).getPhoneNo())) {
                    user_name = contactList.get(i).getName();
                    System.out.println("USERLIST user1 "+user1+" "+contactList.get(i).getPhoneNo());

                    break;
                }
            }
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


        if(user_name != null) {
            name.setText(user_name);
        } else if(user1 != null){
            name.setText(user1.replace("@eazi.ai",""));
        }
    }



    public void sendTextMessage(View v) {
        String message = msg_edittext.getEditableText().toString();

        if (!message.equalsIgnoreCase("")) {
            database.putMessages(database,CommonMethods.getSharedPrefValue(this,Constants.user_name)
                    ,user1,message,CommonMethods.getDate(),"true");
            msg_edittext.setText("");

            chatAdapter.add(this,user1);
            chatAdapter.notifyDataSetChanged();
            Invite activity = new Invite();
            activity.getmService().xmpp.sendMessage(message,user1);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this,Invite.class);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendMessageButton:
                sendTextMessage(v);
                break;
            case R.id.back:
                Intent i = new Intent(this,Invite.class);
                startActivity(i);
                break;
        }
    }
}