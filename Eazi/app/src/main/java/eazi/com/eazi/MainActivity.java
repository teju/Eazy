package eazi.com.eazi;

import android.content.Intent;
import android.os.Handler;
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

import java.util.Timer;
import java.util.TimerTask;

import eazi.com.eazi.utils.CommonMethods;
import eazi.com.eazi.utils.Constants;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ImageView pager_introduction;
    private ImageView[] dots;
    private int dotsCount;
    private LinearLayout pager_indicator;
    public int currentimageindex = 0;
    private int[] images = {R.mipmap.ic_launcher, R.drawable.introduction_image_2,R.drawable.introduction_image_3};
    String[] description = {"Your World take control","Express Yourself","What's around you and beyond"};
    String[] titles = {"","Chat","Search nearby"};
    Animation animation;
    private Timer timer;
    private TextView title,desc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pager_introduction = (ImageView)findViewById(R.id.pager_introduction);
        pager_indicator = (LinearLayout)findViewById(R.id.viewPagerCountDots);
        title = (TextView)findViewById(R.id.title);
        desc = (TextView)findViewById(R.id.desc);
        Button agree =(Button)findViewById(R.id.agree);
        System.out.println(" CommonMethods "
                +CommonMethods.getSharedPrefValue(MainActivity.this, Constants.user_verified));
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommonMethods.getSharedPrefValue(MainActivity.this, Constants.user_verified).equals("false")
                        || CommonMethods.isEmpty(CommonMethods.getSharedPrefValue(MainActivity.this,Constants.user_verified))) {
                    Intent i = new Intent(MainActivity.this,LanguageSelection.class);
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
            Picasso.with(this)
                    .load(images[currentimageindex])
                    .placeholder(R.drawable.noimg) // optional
                    .error(R.drawable.noimg)         // optional
                    .into(pager_introduction);
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


}
