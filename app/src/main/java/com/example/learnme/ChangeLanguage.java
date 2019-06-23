package com.example.learnme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeLanguage extends AppCompatActivity {

    private ImageView img_back;
    private RadioGroup rg_language;
    private RadioButton rb_INA,rb_ENG;
    private Button btn_apply;
    private TextView txt_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);


        //get preference language
        final SharedPreferences pref       = getApplicationContext().getSharedPreferences("myPrefsLanguage",MODE_PRIVATE);
        String getPref = pref.getString("language","English");

        //View ini
        img_back = (ImageView) findViewById(R.id.btn_close_language);
        btn_apply = (Button) findViewById(R.id.btn_change_language);
        rg_language    = (RadioGroup) findViewById(R.id.radio_group_language);
        rb_INA      = (RadioButton) findViewById(R.id.radio_button_INA);
        rb_ENG      = (RadioButton) findViewById(R.id.radio_button_ENG);
        txt_title   = (TextView) findViewById(R.id.txt_language_title);

        if(getPref.equals("English")){
            rb_ENG.setChecked(true);
        }else{
            rb_INA.setChecked(true);
        }

        //View ini by Language
        Log.d("message",getPref);
        initViewLanguage(getPref);

        //Temp Variable RB
        final String[] tempLanguage = {getPref};



        //Listener
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeLanguage.this,HomePage.class);
                startActivity(intent);
                finish();
            }
        });

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref.edit().clear().commit();
                savePrefsData(tempLanguage[0]);
                initViewLanguage(tempLanguage[0]);
                Toast.makeText(ChangeLanguage.this, tempLanguage[0], Toast.LENGTH_SHORT).show();
            }
        });

        rg_language.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_button_INA:
                        tempLanguage[0] = rb_INA.getText().toString();
                        break;
                    case R.id.radio_button_ENG:
                        tempLanguage[0] = rb_ENG.getText().toString();
                        break;
                }

            }
        });
    }


    //Shared Preference Language
    private void savePrefsData(final String language){
        SharedPreferences pref          = getApplicationContext().getSharedPreferences("myPrefsLanguage",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isChange",true);
        editor.putString("language",language);
        editor.commit();
    }

    private void initViewLanguage(String language){
        if(language.equals("Indonesia")){
            txt_title.setText("Bahasa");
            btn_apply.setText("Terapkan");
        }else{
            txt_title.setText("Language");
            btn_apply.setText("Apply");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if((keyCode == KeyEvent.KEYCODE_BACK)){
            Intent intent = new Intent(ChangeLanguage.this,HomePage.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
