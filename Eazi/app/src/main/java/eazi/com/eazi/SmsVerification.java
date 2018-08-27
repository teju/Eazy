package eazi.com.eazi;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mukesh.OtpListener;
import com.mukesh.OtpView;

import eazi.com.eazi.utils.CommonMethods;
import eazi.com.eazi.utils.Constants;

public class SmsVerification extends AppCompatActivity implements View.OnClickListener {
    private OtpView otpView;
    private Button resend_sms;
    private Button call;
    private TextView phone_number;
    private TextView timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_verification);
        initUI();
        initData();
    }

    public void initUI(){
        resend_sms = (Button) findViewById(R.id.resend_sms);
        phone_number = (TextView) findViewById(R.id.phone_number);
        otpView = (OtpView) findViewById(R.id.otp_view);
        call = (Button) findViewById(R.id.call);
        timer = (TextView) findViewById(R.id.timer);
    }

    public void initData(){
        resend_sms.setOnClickListener(this);
        call.setOnClickListener(this);
        otpView.setListener(new OtpListener() {
            @Override public void onOtpEntered(String otp) {

            }
        });
        phone_number.setText(CommonMethods.getSharedPrefValue(this, Constants.user_name));
        CommonMethods.addSharedPref(this,Constants.user_verified,"true");
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 1000);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.resend_sms:

                break;
            case R.id.call:
                i =new Intent(SmsVerification.this,RadialMenuActivity.class);
                startActivity(i);
                break;
        }
    }

    Handler timerHandler = new Handler();
    long startTime = 0;
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            if(seconds < 31 ){
                timer.setText(String.format("%d:%02d", minutes, seconds));
            } else {
                startTime = System.currentTimeMillis();
                timerHandler.postDelayed(timerRunnable, 1000);
            }
            timerHandler.postDelayed(this, 500);
        }
    };

}
