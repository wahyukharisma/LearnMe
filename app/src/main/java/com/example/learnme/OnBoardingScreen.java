package com.example.learnme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.learnme.Adapter.IntroViewPagerAdapter;
import com.example.learnme.Model.ScreenItem;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingScreen extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;

    Button btnNext;
    Button btnGetStarted;

    int position = 0;

    Animation btnAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // check previous state
       /* if(restorePrefData()){
            Intent mainActivity = new Intent(getApplicationContext(),Login.class);
            startActivity(mainActivity);
            finish();
        }*/

        // set content layout
        setContentView(R.layout.activity_on_boarding_screen);

        //hide the action bar
        //getSupportActionBar().hide();

        // ini views
        btnNext       = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator  = findViewById(R.id.tab_indicator);
        btnAnim       = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);

        // fill list screen
        String temp = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam id neque lorem. Praesent luctus eros ac fermentum hendrerit. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Cras ac lacus porttitor, tincidunt metus euismod, euismod massa.";
        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Screen 1",temp,R.drawable.intro_1));
        mList.add(new ScreenItem("Screen 2",temp,R.drawable.intro_3));
        mList.add(new ScreenItem("Screen 3",temp,R.drawable.intro_2));

        // setup viewpager
        screenPager           = findViewById(R.id.viewPager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);

        //setup table layout with  viewpager
        tabIndicator.setupWithViewPager(screenPager);

        // next button click listener
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position = screenPager.getCurrentItem();
                if(position < mList.size()){
                    position++;
                    screenPager.setCurrentItem(position);
                }

                if(position == mList.size()-1){
                    // show the GETSTARTED button and hide indicator and the next button
                    loadLastScreen();
                }
            }
        });

        // tablayout add change listener
        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == mList.size()-1){
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // get button clicked
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(getApplicationContext(),Login.class);
                startActivity(mainActivity);

                //save state
                savePrefsData();
                finish();
            }
        });

    }


    private void loadLastScreen(){ // show the GETSTARTED button and hide indicator and the next button
        btnGetStarted.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.VISIBLE);

        // animation get started button
        btnGetStarted.setAnimation(btnAnim);
    }

    private void savePrefsData(){
        SharedPreferences pref          = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpen",true);
        editor.commit();
    }

    private boolean restorePrefData(){
        SharedPreferences pref          = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActiviyOpened = pref.getBoolean("isIntroOpen",false);
        return isIntroActiviyOpened;
    }
}
