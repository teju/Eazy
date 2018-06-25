package eazi.com.eazi;

import android.content.Intent;
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
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.resend_sms:
                i =new Intent(SmsVerification.this,RegisterPhoneNumber.class);
                startActivity(i);
                finish();
                break;
            case R.id.call:
                i =new Intent(SmsVerification.this,Invite.class);
                startActivity(i);
                break;
        }
    }
}
