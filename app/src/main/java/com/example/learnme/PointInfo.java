package com.example.learnme;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PointInfo extends AppCompatActivity {

    private ImageView img_close,img_info;
    private TextView txt_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_info);

        //get preference language
        final SharedPreferences pref       = getApplicationContext().getSharedPreferences("myPrefsLanguage",MODE_PRIVATE);
        String getPref = pref.getString("language","English");

        //view ini
        img_close = findViewById(R.id.btn_close_point_info);
        img_info  = findViewById(R.id.img_point_info);
        txt_info  = findViewById(R.id.txt_title_info);

        if(getPref.equals("Indonesia")){
            img_info.setImageResource(R.drawable.point_info_indo);
            txt_info.setText("Informasi Poin");
        }

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
