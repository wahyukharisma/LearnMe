package com.example.learnme;

import android.animation.Animator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splash_screen extends AppCompatActivity {
    ImageView img_logo;
    Animation logo_anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Animation logo
        img_logo  = findViewById(R.id.img_logo);
        logo_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.logo_animation);
        img_logo.setAnimation(logo_anim);

        //Thread timer
        final Intent i = new Intent(this,OnBoardingScreen.class);
        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }
}
