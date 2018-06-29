package eazi.com.eazi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import eazi.com.eazi.utils.CommonMethods;
import eazi.com.eazi.utils.Constants;

/**
 * Created by tejaswini on 19/05/18.
 */

public class PhoneVerification extends AppCompatActivity implements View.OnClickListener {

    private Button edit;
    private TextView phone_number;
    private Button ok;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_verification);
        initUI();
        initData();

    }
    public void initUI(){
        edit = (Button) findViewById(R.id.edit);
        phone_number = (TextView) findViewById(R.id.phone_number);
        ok = (Button) findViewById(R.id.ok);
    }

    public void initData(){
        edit.setOnClickListener(this);
        phone_number.setText(CommonMethods.getSharedPrefValue(this, Constants.user_name));
        ok.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent i ;

        switch (v.getId()){
            case R.id.edit:
                 i =new Intent(PhoneVerification.this,RegisterPhoneNumber.class);
                 i.putExtra("phone_number",phone_number.getText().toString());
                startActivity(i);
                finish();
                break;
            case R.id.ok:
                 i =new Intent(PhoneVerification.this,SmsVerification.class);
                startActivity(i);
                finish();
                break;
        }
    }
}
