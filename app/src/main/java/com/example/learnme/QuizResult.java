package com.example.learnme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.learnme.Model.QuizQuestion;

public class QuizResult extends AppCompatActivity {

    private TextView txt_point;
    private Button btn_homepage;
    private TextView txt_gotpoint;
    private Animation txtAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

        final Intent intent = getIntent();
        String score = intent.getStringExtra("score");
        String totalPoint = intent.getStringExtra("totalpoint");

        txt_point = (TextView) findViewById(R.id.txt_my_score);
        btn_homepage = (Button) findViewById(R.id.btn_homepage);
        txt_gotpoint = (TextView) findViewById(R.id.gotpoint);

        txt_point.setText(score);
        txt_gotpoint.setText(totalPoint);

        txtAnimation       = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);

        txt_point.setAnimation(txtAnimation);

        btn_homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
