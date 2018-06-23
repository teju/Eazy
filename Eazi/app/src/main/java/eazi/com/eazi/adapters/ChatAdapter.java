package eazi.com.eazi.adapters;

/**
 * Created by Khushvinders on 15-Nov-16.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import eazi.com.eazi.R;
import eazi.com.eazi.database.DataBaseHelper;
import eazi.com.eazi.database.Messages;

public class ChatAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    List<Messages> chatMessageList;

    public ChatAdapter(Context activity,String user) {
        DataBaseHelper db = new DataBaseHelper(activity);
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Messages message = (Messages) chatMessageList.get(position);
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.chatbubble, null);

        TextView msg = (TextView) vi.findViewById(R.id.message_text);
        msg.setText(message.getMessage_Text());
        LinearLayout layout = (LinearLayout) vi
                .findViewById(R.id.bubble_layout);
        LinearLayout parent_layout = (LinearLayout) vi
                .findViewById(R.id.bubble_layout_parent);

        // if message is mine then align to right
        if (message.getISMine().equals("true")) {
            layout.setBackgroundResource(R.drawable.bubble2);
            parent_layout.setGravity(Gravity.RIGHT);
        } else {
            layout.setBackgroundResource(R.drawable.bubble1);
            parent_layout.setGravity(Gravity.LEFT);
        }
        msg.setTextColor(Color.BLACK);
        return vi;
    }

    public void add(Context activity,String user) {
        DataBaseHelper db = new DataBaseHelper(activity);
        chatMessageList = db.getMessages(user);
    }
}