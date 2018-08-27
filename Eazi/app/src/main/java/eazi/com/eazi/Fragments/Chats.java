package eazi.com.eazi.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import eazi.com.eazi.ChatActivity;
import eazi.com.eazi.ChatsElements.ChatAppMsgAdapter;
import eazi.com.eazi.ChatsElements.ChatAppMsgDTO;
import eazi.com.eazi.ProfilePage;
import eazi.com.eazi.R;
import eazi.com.eazi.StartChat;
import eazi.com.eazi.database.DataBaseHelper;
import eazi.com.eazi.database.Users;
import eazi.com.eazi.utils.CommonMethods;
import eazi.com.eazi.utils.Constants;


public class Chats extends Fragment {


    private LinearLayout listView;
    private String params;

    public void setPArams(String params) {
        System.out.println("forwardMsgList1234 params "+params);

        this.params = params;
    }

    public Chats() {
        // Required empty public constructor
    }


    private RecyclerView mMessageRecycler;

    View view;
    public static List<Users> users = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chats, container, false);
        initUI();
        initList();
        return view;
    }

    public void initUI(){
        listView = (LinearLayout)view.findViewById(R.id.listView);
    }

    public void initList(){
        DataBaseHelper dbh = new DataBaseHelper(getActivity());
        users = dbh.getUsers();
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listView.removeAllViews();
        for (int i = 0; i < users.size(); i++) {
            View child = inflater.inflate(R.layout.chat_list_item, null);
            child.setTag(i);
            final String user_name = CommonMethods.getcontactName(getActivity(),users.get(i).getPhoneNumber());

            final TextView name = (TextView)child.findViewById(R.id.name);
            if(user_name != null && user_name.length() > 0){
                name.setText(user_name);

            } else {
                name.setText(users.get(i).getPhoneNumber().replace("@eazi.ai",""));
            }
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = (int)v.getTag();
                    Intent intent = new Intent(getActivity(), StartChat.class);
                    intent.putExtra("user_phone",users.get(id).getPhoneNumber());
                    intent.putExtra("user_name",name.getText().toString());
                    if(!CommonMethods.isEmpty(params)) {
                        intent.putExtra(Constants.forwardMsgList, params);
                    }
                    getActivity().startActivity(intent);
                }
            });
            listView.addView(child);
        }

    }

}
