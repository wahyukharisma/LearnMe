package com.example.learnme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EmailSuccess extends AppCompatActivity {

    Button btnMoveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_success);

        btnMoveLogin = findViewById(R.id.btn_move_login);
        btnMoveLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmailSuccess.this,Login.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
