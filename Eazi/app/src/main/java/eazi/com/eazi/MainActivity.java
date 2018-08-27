package eazi.com.eazi;

import android.*;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import eazi.com.eazi.adapters.SlidingImage_Adapter;
import eazi.com.eazi.utils.CommonMethods;
import eazi.com.eazi.utils.Constants;
import eazi.com.eazi.view.TermsPolicy;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener ,View.OnClickListener {

    private ViewPager pager_introduction;
    private ImageView[] dots;
    private int dotsCount;
    private LinearLayout pager_indicator;
    public int currentimageindex = 0;
    private int[] images = {R.drawable.eazi_logo, R.drawable.introduction_image_2, R.drawable.introduction_image_3};
    String[] description = {"Your World take control", "Express Yourself", "What's around you and beyond"};
    String[] titles = {"", "Chat", "Search nearby"};
    Animation animation;
    private Timer timer;
    private TextView title, desc;
    private static final int REQUEST_PERMISSION_CODE = 101;
    private Button agree;
    private TextView terms;
    private TextView policy;
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initData();

    }

    public void init() {
        List<String> permissionList = new ArrayList<>();
        permissionList.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (checkRuntimePermission(this, permissionList, REQUEST_PERMISSION_CODE)) {

        }

        pager_introduction = (ViewPager)findViewById(R.id.pager_introduction);
        pager_indicator = (LinearLayout)findViewById(R.id.viewPagerCountDots);
        title = (TextView) findViewById(R.id.title);
        terms = (TextView) findViewById(R.id.terms);
        policy = (TextView) findViewById(R.id.policy);
        terms.setOnClickListener(this);
        policy.setOnClickListener(this);
         desc = (TextView)findViewById(R.id.desc);
        agree = (Button) findViewById(R.id.agree);
    }

    public void initData() {
        NUM_PAGES =images.length;

        System.out.println(" CommonMethods "
                + CommonMethods.getSharedPrefValue(MainActivity.this, Constants.user_verified));

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonMethods.getSharedPrefValue(MainActivity.this, Constants.user_verified).equals("false")
                        || CommonMethods.isEmpty(CommonMethods.getSharedPrefValue(MainActivity.this, Constants.user_verified))) {
                    Intent i = new Intent(MainActivity.this, LanguageSelection.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(MainActivity.this, Invite.class);
                    startActivity(i);
                }
            }
        });
        AnimationSet set = new AnimationSet(true);
        animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        set.addAnimation(animation);
        final Runnable mUpdateResults = new Runnable() {
            public void run() {

                AnimateandSlideShow();

            }
        };
        final Handler mHandler = new Handler();

        int delay = 000; // delay for 1 sec.

        int period = 3000; // repeat every 4 sec.

        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {

                mHandler.post(mUpdateResults);

            }
        }, delay, period);

        animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f
        );

        animation.setDuration(900);
        set.addAnimation(animation);
        setUiPageViewController();
        for (int i = 0; i < images.length; i++)
            ImagesArray.add(images[i]);

        pager_introduction.setAdapter(new SlidingImage_Adapter(MainActivity.this, ImagesArray));

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                pager_introduction.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, delay, period);
    }

    private void AnimateandSlideShow() {
        if(currentimageindex == 3) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(MainActivity.this, LanguageSelection.class);
                    //   startActivity(i);
                    //timer.cancel();

                    currentimageindex = 0;
                }
            }, 100);
        } else {
            title.setText(titles[currentimageindex]);
            desc.setText(description[currentimageindex]);
            for (int i = 0; i < dotsCount; i++) {
                dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
            }

            dots[currentimageindex % images.length].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));

            Animation rotateimage = AnimationUtils.loadAnimation(this, R.anim.left_in);

            currentimageindex++;
            pager_introduction.startAnimation(rotateimage);
            desc.startAnimation(rotateimage);
        }

    }
    // to add dotes to the view pager
    private void setUiPageViewController() {

        dotsCount = images.length;
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(0, 0, 0, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }

        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
    @TargetApi(Build.VERSION_CODES.M)
    public boolean checkRuntimePermission(Activity activity, List<String> permissionNeededList, int requestCode) {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        for (String permission : permissionNeededList) {
            if (!addPermission(activity, permissionsList, permission))
                permissionsNeeded.add(permission);
        }
        if (permissionsList.size() > 0) {
            activity.requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), requestCode);
            return false;
        } else
            return true;
    }


    @TargetApi(Build.VERSION_CODES.M)
    public boolean addPermission(Activity activity, List<String> permissionsList, String permission) {
        if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!activity.shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(this, TermsPolicy.class);
        switch (v.getId()){
            case R.id.terms :
                startActivity(i);
                break;
            case R.id.policy :
                startActivity(i);

                break;
        }
    }
}
