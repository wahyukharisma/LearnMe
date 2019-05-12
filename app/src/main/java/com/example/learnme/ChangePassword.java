package com.example.learnme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ChangePassword extends AppCompatActivity {

    ImageView btn_close;
    Button btn_change;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //view ini
        btn_close  = (ImageView) findViewById(R.id.btn_close_edit_pass);
        btn_change = (Button) findViewById(R.id.btn_change_pass);

        //listener
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Change",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
