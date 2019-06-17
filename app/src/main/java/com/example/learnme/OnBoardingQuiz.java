package com.example.learnme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.example.learnme.API.APIInterface;
import com.example.learnme.API.ResponseQuiz;
import com.example.learnme.API.ResponseQuizQuestion;
import com.example.learnme.Adapter.IntroViewPagerAdapter;
import com.example.learnme.Model.QuizQuestion;
import com.example.learnme.Model.ScreenItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OnBoardingQuiz extends AppCompatActivity {

    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;
    private ProgressDialog progressDialog;

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;

    Button btnNext;
    Button btnGetStarted;

    int position = 0;

    Animation btnAnim;

    private List<QuizQuestion> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent= getIntent();
        final String id_quiz = intent.getStringExtra("id_quiz");
        final String id_user = intent.getStringExtra("id_user");

        // make the activity on full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        // check previous state
//        if(restorePrefData()){
//            Intent mainActivity = new Intent(getApplicationContext(),QuizActivity.class);
//            startActivity(mainActivity);
//            finish();
//        }

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
        String temp1 = "TUTORIAL #1";
        String temp2 = "TUTORIAL #2";
        String temp3 = "TUTORIAL #3";
        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("TITLE #1",temp1,R.drawable.intro_1));
        mList.add(new ScreenItem("TITLE #2",temp2,R.drawable.intro_3));
        mList.add(new ScreenItem("TITLE #3",temp3,R.drawable.intro_2));

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
                getQuizId(id_quiz,id_user);
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
        SharedPreferences pref          = getApplicationContext().getSharedPreferences("myPrefsQuiz",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.commit();
    }

    private boolean restorePrefData(){
        SharedPreferences pref        = getApplicationContext().getSharedPreferences("myPrefsQuiz",MODE_PRIVATE);
        Boolean isIntroActiviyOpened = pref.getBoolean("isIntroOpen",false);
        return isIntroActiviyOpened;
    }

    private APIInterface getInterfaceService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        final APIInterface mInterfaceService = retrofit.create(APIInterface.class);
        return mInterfaceService;
    }

    private void getQuizId(final String id,final String id_user){
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseQuiz> mService = mApiService.getQuizId(id);
        mService.enqueue(new Callback<ResponseQuiz>() {
            @Override
            public void onResponse(Call<ResponseQuiz> call, Response<ResponseQuiz> response) {
                if(response.isSuccessful()){
                    savePrefsData();
                    Intent intent1 = new Intent(OnBoardingQuiz.this,QuizActivity.class);
                    intent1.putExtra("id_user",id_user);
                    intent1.putExtra("id_quiz",id);
                    intent1.putExtra("title",response.body().getData().get(0).getTitle());
                    intent1.putExtra("description",response.body().getData().get(0).getDescription());
                    intent1.putExtra("point",response.body().getData().get(0).getPoint());
                    startActivity(intent1);
                    finish();
                }else{
                    Toast.makeText(OnBoardingQuiz.this, "Refresh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseQuiz> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(OnBoardingQuiz.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
