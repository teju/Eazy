package eazi.com.eazi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mukesh.OtpListener;
import com.mukesh.OtpView;

public class SmsVerification extends AppCompatActivity {
    private OtpView otpView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_verification);
        Button resend_sms = (Button) findViewById(R.id.resend_sms);
        resend_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(SmsVerification.this,RegisterPhoneNumber.class);
                startActivity(i);
                finish();
            }
        });
        otpView = (OtpView) findViewById(R.id.otp_view);
        otpView.setListener(new OtpListener() {
            @Override public void onOtpEntered(String otp) {

            }
        });
        Button call = (Button) findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(SmsVerification.this,RadialMenuActivity.class);
                startActivity(i);
              //  finish();
            }
        });
    }
}
