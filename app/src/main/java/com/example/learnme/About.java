package com.example.learnme;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class About extends AppCompatActivity {

    private ImageView btn_about;
    private TextView txt_title,txt_about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        //get preference language
        final SharedPreferences pref       = getApplicationContext().getSharedPreferences("myPrefsLanguage",MODE_PRIVATE);
        String getPref = pref.getString("language","English");

        //view ini
        btn_about = (ImageView) findViewById(R.id.btn_close_about);
        txt_title = (TextView) findViewById(R.id.txt_about_title);
        txt_about = (TextView) findViewById(R.id.txt_about_desc);

        initViewLanguage(getPref);

        //listener
        btn_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViewLanguage(String language){
        if(language.equals("Indonesia")){
            txt_title.setText("Tentang");
            txt_about.setText(R.string.about_indo);
        }
    }
}
