package com.example.learnme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnme.API.APIInterface;
import com.example.learnme.API.ResponseQuizQuestion;
import com.example.learnme.Model.QuizQuestion;
import com.example.learnme.Model.User;
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

public class QuizExpand extends AppCompatActivity {

    private ProgressDialog progressDialog;
    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;
    private List<User> mList= new ArrayList<>();

    private TextView txt_title,txt_price,txt_sold,txt_description,txt_quiz_category,txt_purchase_quiz;
    private Button btn_takequiz;
    private ImageView img_back,img_quiz;

    private RelativeLayout rl_confirmation;
    private Button btn_confirm,btn_back;
    private TextView txt_mypoint,txt_quizpoint,txt_confirmation,txt_my_point,txt_quizprice,txt_language,txt_language_select;
    private TextView txt_status,txt_amount,txt_attempt,txt_desc_quiz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_expand);

        //get preference language
        final SharedPreferences pref       = getApplicationContext().getSharedPreferences("myPrefsLanguage",MODE_PRIVATE);
        final String getPref = pref.getString("language","English");

        //Get Intent
        Intent intent = getIntent();
        final String id_user = intent.getStringExtra("id_user");
        final String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String point = intent.getStringExtra("point");
        String description = intent.getStringExtra("description");
        String sold = intent.getStringExtra("attempt");
        String request = intent.getStringExtra("request");
        String status = intent.getStringExtra("status");

        Log.d("message",id_user+"/"+id+"/"+title+"/"+point+"/"+description+"/"+status);

        //assets
        progressDialog = new ProgressDialog(QuizExpand.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        getUser(id_user);

        // View Ini
        txt_title = (TextView) findViewById(R.id.txt_title_quiz);
        txt_price = (TextView) findViewById(R.id.txt_price_quiz);
        txt_sold = (TextView) findViewById(R.id.txt_quiz_attempt);
        txt_description = (TextView) findViewById(R.id.txt_desc_quiz);
        btn_takequiz = (Button) findViewById(R.id.btn_take_quiz);
        img_back = (ImageView) findViewById(R.id.close_screen);
        img_quiz = (ImageView) findViewById(R.id.img_quiz);
        txt_quiz_category = (TextView) findViewById(R.id.txt_quiz_category);
        txt_purchase_quiz = (TextView) findViewById(R.id.txt_purchased_quiz);
        txt_amount = (TextView) findViewById(R.id.txt_amount);
        txt_attempt = (TextView) findViewById(R.id.txt_attempt);
        txt_desc_quiz = (TextView) findViewById(R.id.txt_description_quiz);
        txt_language = (TextView) findViewById(R.id.txt_language);
        txt_language_select = (TextView) findViewById(R.id.txt_language_select);

        // Init confirmation
        rl_confirmation = (RelativeLayout) findViewById(R.id.rl_confirmation_quiz_expand);
        txt_quizpoint   = (TextView) findViewById(R.id.txt_quiz_point);
        btn_confirm     = (Button) findViewById(R.id.btn_take);
        btn_back        = (Button) findViewById(R.id.btn_cancel_take);
        txt_status      = (TextView) findViewById(R.id.txt_status_take);
        txt_confirmation = (TextView) findViewById(R.id.txt_confirmation);
        txt_my_point    = (TextView) findViewById(R.id.txt_mypoint);
        txt_quizprice   = (TextView) findViewById(R.id.txt_quizprice);


        txt_quizpoint.setText(point);

        if (status.equals("0")) {
            btn_takequiz.setVisibility(View.INVISIBLE);
            txt_purchase_quiz.setVisibility(View.VISIBLE);
        }


        //Initialize View
        img_quiz.setImageResource(R.drawable.quizbackground);
        txt_title.setText(title);


        //Init view language
        initViewLanguage(getPref);

        if (point.equals("0")) {
            if(getPref.equals("Indonesia")){
                txt_price.setText("GRATIS");
            }else{
                txt_price.setText("FREE");
            }

        }else{
            txt_price.setText(point);
        }

        txt_description.setText(description);
        txt_sold.setText(sold);
        
        if (request.equals("1")) {
            if(getPref.equals("Indonesia")){
                txt_quiz_category.setText("Sekali");
            }else{
                txt_quiz_category.setText("Once");
            }

        } else {
            if(getPref.equals("Indonesia")){
                txt_quiz_category.setText("Tanpa Maksimum");
            }else{
                txt_quiz_category.setText("Unlimited");
            }

        }

        //listener
        btn_takequiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_confirmation.setVisibility(View.VISIBLE);

                if(Integer.valueOf(txt_mypoint.getText().toString())>= Integer.valueOf(txt_quizpoint.getText().toString())){
                    btn_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(getPref.equals("Indonesia")){
                                txt_status.setText("YA");
                            }else{
                                txt_status.setText("YES");
                            }
                            Intent intent = new Intent(QuizExpand.this, OnBoardingQuiz.class);
                            intent.putExtra("id_quiz", id);
                            intent.putExtra("id_user", id_user);
                            startActivity(intent);
                            finish();
                        }
                    });
                }else {
                    if(getPref.equals("Indonesia")){
                        txt_status.setText("TIDAK");
                    }else{
                        txt_status.setText("NO");
                    }

                    txt_status.setTextColor(getResources().getColor(R.color.soft_red));
                    btn_confirm.setVisibility(View.INVISIBLE);
                }
                btn_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rl_confirmation.setVisibility(View.GONE);
                    }
                });
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initConfirm(User user){
        txt_mypoint     = (TextView) findViewById(R.id.txt_my_point_quiz);
        txt_mypoint.setText(user.getPoint());
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

    private void getUser(final String id){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<com.example.learnme.API.Response> mService = mApiService.getUserBy(id);
        mService.enqueue(new Callback<com.example.learnme.API.Response>() {
            @Override
            public void onResponse(Call<com.example.learnme.API.Response> call, retrofit2.Response<com.example.learnme.API.Response> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()){
                    mList.add(response.body().getData().get(0));
                    initConfirm(mList.get(0));

                }else{
                    Toast.makeText(QuizExpand.this, "Refresh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<com.example.learnme.API.Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(QuizExpand.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViewLanguage(String language){
        if(language.equals("Indonesia")){
            txt_amount.setText("Jumlah Kuis Terambil");
            txt_attempt.setText("Pengambilan");
            txt_desc_quiz.setText("Deskripsi");
            btn_takequiz.setText("Ambil Kuis");
            txt_confirmation.setText("Konfirmasi Pengambilan");
            txt_my_point.setText("Poin Saya : ");
            txt_quizprice.setText("Harga Kuis : ");
            btn_back.setText("Kembali");
            btn_confirm.setText("Konfirmasi");
            txt_language.setText("Bahasa");
            txt_language_select.setText("Inggris");
            img_quiz.setImageResource(R.drawable.quiz_expand_indo);
        }
    }

}
