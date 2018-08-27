package eazi.com.eazi.adapters;

/**
 * Created by Khushvinders on 15-Nov-16.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import eazi.com.eazi.R;
import eazi.com.eazi.database.DataBaseHelper;
import eazi.com.eazi.database.Messages;
import eazi.com.eazi.utils.Constants;

public class ChatAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private final Context activity;
    List<Messages> chatMessageList;

    public ChatAdapter(Context activity,String user) {
        DataBaseHelper db = new DataBaseHelper(activity);
        this.activity = activity;
        chatMessageList = db.getMessages(user);
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        System.out.println("ChatAdapter "+chatMessageList.size());
        return chatMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Messages message = (Messages) chatMessageList.get(position);
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.chatbubble, null);

        TextView msg = (TextView) vi.findViewById(R.id.message_text);
        TextView message_maps = (TextView) vi.findViewById(R.id.message_maps);
        msg.setText(message.getMessage_Text());
        //LinearLayout layout = (LinearLayout) vi.findViewById(R.id.bubble_layout);
        RelativeLayout parent_layout = (RelativeLayout) vi
                .findViewById(R.id.bubble_layout_parent);
        vi.setBackgroundColor(Color.parseColor("#e1e1e1"));
        // if message is mine then align to right

        if (message.getISMine().equals("true")) {
           // layout.setBackgroundResource(R.drawable.bubble2);
            parent_layout.setGravity(Gravity.RIGHT);
            if(message.getType()!=null && message.getType().equals(Constants.MessageTypeMaps)) {
                msg.setBackground(activity.getResources().getDrawable(R.drawable.maps));
                message_maps.setVisibility(View.VISIBLE);
                msg.setVisibility(View.GONE);
            } else {
                msg.setBackground(activity.getResources().getDrawable(R.drawable.rounded_button_white));
                message_maps.setVisibility(View.GONE);
                msg.setVisibility(View.VISIBLE);
            }
        } else {
           // layout.setBackgroundResource(R.drawable.bubble1);
            parent_layout.setGravity(Gravity.LEFT);
            if(message.getType()!=null && message.getType().equals(Constants.MessageTypeMaps)) {
                msg.setBackground(activity.getResources().getDrawable(R.drawable.maps));
                message_maps.setVisibility(View.VISIBLE);
                msg.setVisibility(View.GONE);
            } else {
                msg.setBackground(activity.getResources().getDrawable(R.drawable.rounded_button));
                message_maps.setVisibility(View.GONE);
                msg.setVisibility(View.VISIBLE);
            }
        }
        msg.setTextColor(Color.BLACK);
        return vi;
    }

    public void add(Context activity,String user) {
        DataBaseHelper db = new DataBaseHelper(activity);
        chatMessageList = db.getMessages(user);
    }

}