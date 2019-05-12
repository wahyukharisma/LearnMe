package com.example.learnme;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class Profile extends AppCompatActivity {

    ImageView btn_close;
    CardView cd_lq,cd_la,cd_cpass,cd_ph, cd_ep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // view ini
        btn_close = (ImageView) findViewById(R.id.btn_close_profile);
        cd_lq     = (CardView)findViewById(R.id.cd_lq);
        cd_la     = (CardView)findViewById(R.id.cd_la);
        cd_cpass  = (CardView) findViewById(R.id.cd_cpass);
        cd_ph     = (CardView) findViewById(R.id.cd_ph);
        cd_ep     = (CardView) findViewById(R.id.cd_ep);

        // listener
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cd_lq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"List of Question",Toast.LENGTH_SHORT).show();
            }
        });

        cd_ph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Point History",Toast.LENGTH_SHORT).show();
            }
        });

        cd_la.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"List of Answer",Toast.LENGTH_SHORT).show();
            }
        });

        cd_cpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Change Password",Toast.LENGTH_SHORT).show();
            }
        });

        cd_ep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EditProfile.class);
                startActivity(intent);
            }
        });
    }
}
