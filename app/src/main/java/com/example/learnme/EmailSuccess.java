package com.example.learnme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EmailSuccess extends AppCompatActivity {

    private Button btnMoveLogin;
    private TextView txt_email_success,txt_email_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_success);

        //get preference language
        final SharedPreferences pref       = getApplicationContext().getSharedPreferences("myPrefsLanguage",MODE_PRIVATE);
        String getPref = pref.getString("language","English");

        txt_email_success = (TextView) findViewById(R.id.txt_email_title);
        txt_email_info    = (TextView) findViewById(R.id.txt_email_info);
        btnMoveLogin = findViewById(R.id.btn_move_login);

        //init view language
        initViewLanguage(getPref);

        btnMoveLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmailSuccess.this,Login.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void initViewLanguage(String language){
        if(language.equals("Indonesia")){
            txt_email_success.setText("Email berhasil dikirim !");
            txt_email_info.setText("Tolong cek email anda untuk menyelesaikan proses");
            btnMoveLogin.setText("Kembali");
        }
    }
}
