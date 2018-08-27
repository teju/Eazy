
package eazi.com.eazi.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import eazi.com.eazi.*;
import eazi.com.eazi.model.Contact;
import eazi.com.eazi.utils.CommonMethods;
import eazi.com.eazi.utils.Constants;

/**
 * Created by mxn on 2016/12/13.
 * MenuListFragment
 */

public class MenuListFragment extends Fragment {


    private List<Contact> contactList = new ArrayList<>();
    private String params;

    public void setPArams(String params) {
        System.out.println("forwardMsgList1234 params "+params);

        this.params = params;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container,
                false);
        NavigationView vNavigation = (NavigationView) view.findViewById(R.id.vNavigation);
        vNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Toast.makeText(getActivity(),menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                return true;
            }
        }) ;
        contactList = CommonMethods.getContact(getActivity());

        ListView lst_menu_items = (ListView)view.findViewById(R.id.lst_menu_items);
        lst_menu_items.setAdapter(new MEnuItems());
        lst_menu_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("NavigationView "+contactList.get(position).getName());
                Intent i = new Intent(getActivity(), StartChat.class);
                i.putExtra("user_phone",contactList.get(position).getPhoneNo().replace("+",""));
                i.putExtra("user_name",contactList.get(position).getName().replace("+",""));
                if(!CommonMethods.isEmpty(params)) {
                    i.putExtra(Constants.forwardMsgList, params);
                }
                getActivity().startActivity(i);
            }
        });
       // setupHeader();
        return  view ;
    }



    class MEnuItems extends BaseAdapter {

        @Override
        public int getCount() {
            return contactList.size();
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
            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View child = inflater.inflate(R.layout.menu_item, null);
            TextView name = (TextView)child.findViewById(R.id.name);
            name.setText(contactList.get(position).getName());
            return child;
        }
    }
}
