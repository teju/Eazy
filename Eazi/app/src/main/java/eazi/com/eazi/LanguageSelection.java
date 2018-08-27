package eazi.com.eazi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aigestudio.wheelpicker.WheelPicker;

/**
 * @author AigeStudio 2015-12-06
 * @author AigeStudio 2016-07-08
 */
public class LanguageSelection extends Activity implements WheelPicker.OnItemSelectedListener, View.OnClickListener {

    private WheelPicker wheelCenter;

    private Integer gotoBtnItemIndex;
    private Button choose_language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_selection);
        choose_language = (Button)findViewById(R.id.choose_language);
        choose_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LanguageSelection.this,RegisterPhoneNumber.class);
                startActivity(i);
            }
        });

        wheelCenter = (WheelPicker) findViewById(R.id.main_wheel_left);
        wheelCenter.setOnItemSelectedListener(this);
        wheelCenter.setSelectedItemPosition(6,true);
        randomlySetGotoBtnIndex();
    }

    private void randomlySetGotoBtnIndex() {
        gotoBtnItemIndex = (int) (Math.random() * wheelCenter.getData().size());
    }

    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        String text = "";
        switch (picker.getId()) {

            case R.id.main_wheel_left:
                text = "Center:";
                break;

        }
    }

    @Override
    public void onClick(View v) {
        wheelCenter.setSelectedItemPosition(gotoBtnItemIndex);
        randomlySetGotoBtnIndex();
    }

}