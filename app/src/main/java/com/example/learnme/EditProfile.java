package com.example.learnme;

import android.support.design.animation.ImageMatrixProperty;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class EditProfile extends AppCompatActivity {

    ImageView btn_close;
    Button btn_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // view ini
        btn_close   = (ImageView) findViewById(R.id.btn_close_edit);
        btn_confirm = (Button) findViewById(R.id.btn_edit_profile);

        //listener
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Confirm",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
