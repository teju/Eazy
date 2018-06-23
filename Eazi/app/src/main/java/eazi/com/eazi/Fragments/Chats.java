package eazi.com.eazi.Fragments;

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

import java.util.ArrayList;
import java.util.List;

import eazi.com.eazi.ChatActivity;
import eazi.com.eazi.ChatsElements.ChatAppMsgAdapter;
import eazi.com.eazi.ChatsElements.ChatAppMsgDTO;
import eazi.com.eazi.ProfilePage;
import eazi.com.eazi.R;


public class Chats extends Fragment {



    public Chats() {
        // Required empty public constructor
    }
    private RecyclerView mMessageRecycler;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        Button chat_send_msg = (Button)view.findViewById(R.id.chat_send_msg);
        chat_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ProfilePage.class);
                startActivity(i);
            }
        });

        return view;
    }




}
