package com.example.learnme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnme.API.APIInterface;
import com.example.learnme.API.ResponseAnswer;
import com.example.learnme.API.ResponseTrendsQuestion;
import com.example.learnme.API.UpdatePoint;
import com.example.learnme.Adapter.AnswearListAdapter;
import com.example.learnme.Model.Answer;
import com.example.learnme.Model.TrendingQuestion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuestionExpand extends AppCompatActivity {

    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;
    private ProgressDialog progressDialog;

    private List<Answer> mData = new ArrayList<>();
    private ImageView btn_close,thumb_up,thumb_down;

    //Question component
    private TextView txt_title,txt_tag,txt_desc,txt_like,txt_dislike,txt_date,txt_username,txt_comment,txt_id_user_question;
    private Button btn_comment;
    private EditText et_answer;

    private String status="";
    private String id_user = "";
    private String id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_expand);

        //view ini
        btn_close = (ImageView) findViewById(R.id.btn_close_question);
        et_answer = (EditText) findViewById(R.id.et_answer);
        btn_comment = (Button) findViewById(R.id.btn_answer);
        thumb_up = (ImageView) findViewById(R.id.img_thumb_up);
        thumb_down = (ImageView) findViewById(R.id.img_thumb_down);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        id_user = intent.getStringExtra("id_user");

        //asset
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        final Animation shake = AnimationUtils.loadAnimation(QuestionExpand.this,R.anim.shake);
        final Drawable thumb_up_active = getApplicationContext().getResources().getDrawable(R.drawable.ic_thumb_up_dark_blue_24dp);
        final Drawable thumb_down_active = getApplicationContext().getResources().getDrawable(R.drawable.ic_thumb_down_red_24dp);
        final Drawable thumb_up_non_active = getApplicationContext().getResources().getDrawable(R.drawable.ic_thumb_up_not_active_24dp);
        final Drawable thumb_down_non_active = getApplicationContext().getResources().getDrawable(R.drawable.ic_thumb_down_not_active_24dp);

        thumb_down.setImageDrawable(thumb_down_non_active);
        thumb_up.setImageDrawable(thumb_up_non_active);


        if(restorePrefDataDislike()){
            SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefsQuestionDislike",MODE_PRIVATE);
            final String temp_user = pref.getString("id_user","");
            final String temp_question = pref.getString("id_question","");

            if(temp_user.equals(id_user) && temp_question.equals(id)){
                Log.d("message","inPrefDislike");
                thumb_down.setImageDrawable(thumb_down_active);
                status = "dislike";
                thumb_up.setEnabled(false);
            }
        }
        if(restorePrefDataLike()){
            SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefsQuestionLike",MODE_PRIVATE);
            final String temp_user = pref.getString("id_user","");
            final String temp_question = pref.getString("id_question","");


            if(temp_user.equals(id_user) && temp_question.equals(id)){
                Log.d("message","inPrefLike");
                thumb_up.setImageDrawable(thumb_up_active);
                status = "like";
                thumb_down.setEnabled(false);
            }
        }

        //Initialize custom view
        getQuestion(id);
        getAnswer(id);

        // initialize recycle view item trending
        mData = new ArrayList<>();
        //addData(mData);


        //Listener
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_answer.getText().toString().equals("")){
                    et_answer.setAnimation(shake);
                    Toast.makeText(QuestionExpand.this,"Please write the comment first",Toast.LENGTH_SHORT).show();
                }else{
                    addAnswer(et_answer.getText().toString(),id,id_user);
                    UpdatePoint updatePoint = new UpdatePoint();
                    updatePoint.updatePoint(id_user,"2","0","Give Answer, "+et_answer.getText().toString()+" into question id : "+id);
                }
            }
        });

        thumb_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // adding point
                if(status.equals("like")){
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefsQuestionLike",MODE_PRIVATE);
                    pref.edit().clear().commit();
                    updateLike(id,"dislike",id_user);
                    thumb_up.setImageDrawable(thumb_up_non_active);
                }else{
                    UpdatePoint updatePoint = new UpdatePoint();
                    updatePoint.updatePoint(txt_id_user_question.getText().toString(),"7","0","Question "+txt_title.getText().toString()+" cancel dislike by user id "+id_user);
                    updateLike(id,"like",id_user);
                    thumb_up.setImageDrawable(thumb_up_active);

                }
            }
        });

        thumb_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(status.equals("dislike")){
                    Log.d("message","inDislike");
                    SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefsQuestionDislike",MODE_PRIVATE);
                    pref.edit().clear().commit();
                    updateDislike(id,"like",id_user);

                }else{
                    updateDislike(id,"dislike",id_user);
                    thumb_down.setImageDrawable(thumb_down_active);
                    UpdatePoint updatePoint = new UpdatePoint();
                    updatePoint.updatePoint(txt_id_user_question.getText().toString(),"9","0","Question "+txt_title.getText().toString()+" cancel dislike by user id "+id_user);

                }
            }
        });
    }

    public void initViewQuestion(TrendingQuestion trendingQuestion){
        txt_title = (TextView) findViewById(R.id.txt_title_question_expand);
        txt_tag   = (TextView) findViewById(R.id.txt_tag_question);
        txt_desc  = (TextView) findViewById(R.id.txt_desc_question_expand);
        txt_like  = (TextView) findViewById(R.id.txt_like_trend);
        txt_dislike = (TextView) findViewById(R.id.txt_dislike_trend);
        txt_date  = (TextView) findViewById(R.id.txt_date_question_expand);
        txt_username = (TextView) findViewById(R.id.txt_user_name);
        txt_comment  = (TextView) findViewById(R.id.txt_total_answear);
        txt_id_user_question = (TextView) findViewById(R.id.txt_id_user_question);

        txt_id_user_question.setText(trendingQuestion.getUser());


        txt_title.setText(trendingQuestion.getTitle());
        String tag="";
        if(trendingQuestion.getIdTag().equals("1")){
            tag = "General";
        }else if((trendingQuestion.getIdTag().equals("2"))){
            tag = "Elementary School";
        }else if((trendingQuestion.getIdTag().equals("3"))){
            tag = "Junior High School";
        }else{
            tag = "Senior High School";
        }
        txt_tag.setText(tag);
        txt_desc.setText(trendingQuestion.getDescription());
        txt_like.setText(trendingQuestion.getLikes());
        txt_dislike.setText(trendingQuestion.getDislikes());
        txt_date.setText(trendingQuestion.getDate());
        txt_username.setText(trendingQuestion.getIdUser());
        txt_comment.setText(trendingQuestion.getComment());
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

    private void getQuestion(final String id){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseTrendsQuestion> mService = mApiService.getQuestionById(id);
        mService.enqueue(new Callback<ResponseTrendsQuestion>() {
            @Override
            public void onResponse(Call<ResponseTrendsQuestion> call, Response<ResponseTrendsQuestion> response) {
                if(response.isSuccessful()){
                    initViewQuestion(response.body().getData().get(0));
                    progressDialog.dismiss();
                }else{
                    progressDialog.dismiss();
                    Log.d("message",response.errorBody().toString());
                    Toast.makeText(QuestionExpand.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseTrendsQuestion> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(QuestionExpand.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAnswer(final String id){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseAnswer> mService = mApiService.getAnswerById(id);
        mService.enqueue(new Callback<ResponseAnswer>() {
            @Override
            public void onResponse(Call<ResponseAnswer> call, Response<ResponseAnswer> response) {
                if (response.isSuccessful()){
                    for(int i=0;i<response.body().getData().size();i++){
                        mData.add(response.body().getData().get(i));
                    }

                    // recycle view answear listener
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view_answear);
                    AnswearListAdapter myAdapter   = new AnswearListAdapter(QuestionExpand.this,mData);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(QuestionExpand.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(myAdapter);

                    progressDialog.dismiss();
                }else{
                    progressDialog.dismiss();
                    Log.d("message",response.errorBody().toString());
                    Toast.makeText(QuestionExpand.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseAnswer> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(QuestionExpand.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addAnswer(final String answer, final String id_question,final String id){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseAnswer> mService = mApiService.storeAnswer(id_question,id,answer);
        mService.enqueue(new Callback<ResponseAnswer>() {
            @Override
            public void onResponse(Call<ResponseAnswer> call, Response<ResponseAnswer> response) {
                if (response.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(QuestionExpand.this, "Answer has been send", Toast.LENGTH_SHORT).show();
                    refreshActivity();
                }else{
                    progressDialog.dismiss();
                    Log.d("message",response.errorBody().toString());
                    Toast.makeText(QuestionExpand.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseAnswer> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(QuestionExpand.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateLike(final String id,final String request,final String id_user){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseTrendsQuestion> mService = mApiService.updateQuestionLike(id,request);
        mService.enqueue(new Callback<ResponseTrendsQuestion>() {
            @Override
            public void onResponse(Call<ResponseTrendsQuestion> call, Response<ResponseTrendsQuestion> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    if(request.equals("like")){
                        savePrefsDataLike(id_user,id);
                        Toast.makeText(QuestionExpand.this, "You'r like this question", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(QuestionExpand.this, "Undo like this question", Toast.LENGTH_SHORT).show();
                    }
                    refreshActivity();
                }else{
                    progressDialog.dismiss();
                    Log.d("message",response.errorBody().toString());
                    Toast.makeText(QuestionExpand.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseTrendsQuestion> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(QuestionExpand.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateDislike(final String id,final String request,final String id_user){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseTrendsQuestion> mService = mApiService.updateQuestionDislike(id,request);
        mService.enqueue(new Callback<ResponseTrendsQuestion>() {
            @Override
            public void onResponse(Call<ResponseTrendsQuestion> call, Response<ResponseTrendsQuestion> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();

                    if(request.equals("dislike")){
                        savePrefsDataDislike(id_user,id);
                        Toast.makeText(QuestionExpand.this, "You'r dislike this question", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(QuestionExpand.this, "Undo dislike this question", Toast.LENGTH_SHORT).show();
                    }
                    refreshActivity();
                }else {
                    progressDialog.dismiss();
                    Log.d("message",response.errorBody().toString());
                    Toast.makeText(QuestionExpand.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseTrendsQuestion> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(QuestionExpand.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void savePrefsDataLike(final String id_user,final String id_question){
        SharedPreferences pref          = getApplicationContext().getSharedPreferences("myPrefsQuestionLike",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isLike",true);
        editor.putString("id_user",id_user);
        editor.putString("id_question",id_question);
        editor.commit();
    }

    private boolean restorePrefDataLike(){
        SharedPreferences pref       = getApplicationContext().getSharedPreferences("myPrefsQuestionLike",MODE_PRIVATE);
        Boolean isIntroActiviyOpened = pref.getBoolean("isLike",false);
        return isIntroActiviyOpened;
    }

    private void savePrefsDataDislike(final String id_user,final String id_question){
        SharedPreferences pref          = getApplicationContext().getSharedPreferences("myPrefsQuestionDislike",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isDislike",true);
        editor.putString("id_user",id_user);
        editor.putString("id_question",id_question);
        editor.commit();
    }

    private boolean restorePrefDataDislike(){
        SharedPreferences pref       = getApplicationContext().getSharedPreferences("myPrefsQuestionDislike",MODE_PRIVATE);
        Boolean isIntroActiviyOpened = pref.getBoolean("isDislike",false);
        return isIntroActiviyOpened;
    }

    private void refreshActivity(){
        finish();
        startActivity(getIntent());
    }
}
