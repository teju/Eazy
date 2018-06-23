package eazi.com.eazi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class LanguageSelection extends AppCompatActivity implements View.OnClickListener{

    String [] languages = {"English","Hindi","German","Spanish","Mandarin","Arabic","Japanese","Korean"};
    private LinearLayout language_list;
    private Button choose_language;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_selection);
        initUI();
        initData();
        setList();
    }

    public void initUI(){
        language_list = (LinearLayout)findViewById(R.id.language_list);
        choose_language = (Button)findViewById(R.id.choose_language);
    }

    public void initData() {
        choose_language.setOnClickListener(this);
    }

    public void setList(){
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < languages.length; i++) {
            View child = inflater.inflate(R.layout.language_item, null);
            TextView language = (TextView) child.findViewById(R.id.language);
            language.setText(languages[i]);
            language_list.addView(child);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.choose_language:
                Intent i = new Intent(LanguageSelection.this,RegisterPhoneNumber.class);
                startActivity(i);
                break;
        }
    }
}
