package com.example.learnme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.learnme.API.APIInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuizExpand extends AppCompatActivity {

    private TextView txt_title,txt_price,txt_sold,txt_description,txt_quiz_category;
    private Button btn_takequiz;
    private ImageView img_back,img_quiz;

    private ProgressDialog progressDialog;
    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_expand);

        //Get Intent
        Intent intent      = getIntent();
        final String id_user     = intent.getStringExtra("id_user");
        final String id          = intent.getStringExtra("id");
        String title       = intent.getStringExtra("title");
        String point       = intent.getStringExtra("point");
        String description = intent.getStringExtra("description");
        String sold        = intent.getStringExtra("attempt");
        String request     = intent.getStringExtra("request");

        // View Ini
        txt_title = (TextView) findViewById(R.id.txt_title_quiz);
        txt_price = (TextView) findViewById(R.id.txt_price_quiz);
        txt_sold  = (TextView) findViewById(R.id.txt_quiz_attempt);
        txt_description = (TextView) findViewById(R.id.txt_desc_quiz);
        btn_takequiz = (Button) findViewById(R.id.btn_take_quiz);
        img_back = (ImageView) findViewById(R.id.close_screen);
        img_quiz  = (ImageView) findViewById(R.id.img_quiz);
        txt_quiz_category = (TextView) findViewById(R.id.txt_quiz_category);


        //Initialize View
        img_quiz.setImageResource(R.drawable.noimage);
        txt_title.setText(title);
        txt_price.setText(point);
        txt_description.setText(description);
        txt_sold.setText(sold);
        if(request.equals("1")){
            txt_quiz_category.setText("Once");
        }else{
            txt_quiz_category.setText("Unlimited");
        }


        //listener
        btn_takequiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizExpand.this,OnBoardingQuiz.class);
                intent.putExtra("id_quiz",id);
                intent.putExtra("id_user",id_user);
                startActivity(intent);
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


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
}
