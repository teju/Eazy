package eazi.com.eazi;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;


import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import java.util.ArrayList;
import java.util.List;

import eazi.com.eazi.Fragments.Chats;
import eazi.com.eazi.Fragments.Contacts;
import eazi.com.eazi.Fragments.Favourite;
import eazi.com.eazi.Fragments.Groups;
import eazi.com.eazi.Fragments.MenuListFragment;
import eazi.com.eazi.configure.LocalBinder;
import eazi.com.eazi.configure.MyService;
import eazi.com.eazi.model.Contact;
import eazi.com.eazi.utils.CommonMethods;


public class Invite extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1 ;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FlowingDrawer mDrawer;
    private RelativeLayout main_layout;
    private List<Contact> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        try {

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            main_layout = (RelativeLayout) findViewById(R.id.main_layout);
            main_layout.setAlpha(1);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Invite.this, RadialMenuActivity.class);
                    startActivity(i);
                }
            });
            viewPager = (ViewPager) findViewById(R.id.viewpager);
            setupViewPager(viewPager);

            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            mDrawer = (FlowingDrawer) findViewById(R.id.drawerlayout);
            mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
            mDrawer.setMenuSize(width / 2 + 150);
            setupMenu();

            Button logout = (Button)findViewById(R.id.logout);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonMethods.clearSharedPrefValue(Invite.this);
                    doUnbindService();

                    Intent i = new Intent(Invite.this,MainActivity.class);
                    startActivity(i);
                    finish();

                }
            });
        } catch (Exception e) {
            System.out.println("Exception1234 " + e.toString());

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        doBindService();
        setupViewPager(viewPager);
    }

    void doBindService() {

        bindService(new Intent(this, MyService.class), mConnection,
                Context.BIND_AUTO_CREATE);
    }

    void doUnbindService() {
        if (mConnection != null) {
            System.out.println("connection1232r43rd3we onDestroy");

            unbindService(mConnection);
        }
    }

    public MyService getmService() {
        return mService;
    }

    private void setupMenu() {
        FragmentManager fm = getSupportFragmentManager();
        MenuListFragment mMenuFragment = (MenuListFragment) fm.findFragmentById(R.id.id_container_menu);
        if (mMenuFragment == null) {
            mMenuFragment = new MenuListFragment();
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment).commit();
        }

        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    main_layout.setAlpha(1);

                } else {
                    main_layout.setAlpha(.5f);

                    //mDrawer.setBackgroundColor(getResources().getColor(R.color.orange));
                }
            }

            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        try {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(new Chats(), "Chat");
            adapter.addFragment(new Contacts(), "Contacts");
            adapter.addFragment(new Favourite(), "Favourite");
            adapter.addFragment(new Groups(), "Groups");
            viewPager.setAdapter(adapter);
        } catch (Exception e){
            System.out.println("Exception1234 setupViewPager"+e.toString());

        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isMenuVisible()) {
            mDrawer.closeMenu();
        } else {
            Intent i = new Intent(Invite.this,RadialMenuActivity.class);
            startActivity(i);
        }
    }

    private MyService mService;
    private boolean mBounded;
    private final ServiceConnection mConnection = new ServiceConnection() {

        @SuppressWarnings("unchecked")
        @Override
        public void onServiceConnected(final ComponentName name,
                                       final IBinder service) {
            mService = ((LocalBinder<MyService>) service).getService();
            mBounded = true;
        }

        @Override
        public void onServiceDisconnected(final ComponentName name) {
            mService = null;
            mBounded = false;
        }
    };

}