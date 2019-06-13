package com.example.learnme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.learnme.API.APIInterface;
import com.example.learnme.API.ResponseTrendsQuestion;
import com.example.learnme.Adapter.QuestionListAdapter;
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

public class ListOfQuestionUser extends AppCompatActivity {

    private ImageView img_back;
    private Button btn_search;
    private EditText et_search;

    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;

    private List<TrendingQuestion> listTrending = new ArrayList<>();
    private ProgressDialog progressDialog;

    private String id_user="";
    private RelativeLayout rl_nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_question_user);

        Intent intent =  getIntent();
        id_user       = intent.getStringExtra("id");

        //View ini
        img_back   = (ImageView) findViewById(R.id.btn_close_question);
        btn_search = (Button) findViewById(R.id.btn_search_hp);
        et_search  = (EditText) findViewById(R.id.et_search_hp);
        rl_nodata   = (RelativeLayout) findViewById(R.id.rl_nodata);

        //asset
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //Intialize
        getUserQuestion(id_user);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_search.getText().toString().equals("")){
                    Toast.makeText(ListOfQuestionUser.this,"Insert keyword first",Toast.LENGTH_SHORT).show();
                }else{
                    listTrending = new ArrayList<>();
                    getQuestionKeywordUser(id_user,et_search.getText().toString());
                }
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

    private void getUserQuestion(final String id){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseTrendsQuestion> mService =  mApiService.getUserQuestion(id);
        mService.enqueue(new Callback<ResponseTrendsQuestion>() {
            @Override
            public void onResponse(Call<ResponseTrendsQuestion> call, Response<ResponseTrendsQuestion> response) {
                if(response.isSuccessful()){
                    for(int i=0;i<response.body().getData().size();i++){
                        listTrending.add(response.body().getData().get(i));
                    }
                    if(listTrending.isEmpty()){
                        rl_nodata.setVisibility(View.VISIBLE);
                    }else{
                        rl_nodata.setVisibility(View.INVISIBLE);
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view_question);
                        QuestionListAdapter myAdapter = new QuestionListAdapter(listTrending,id,getApplicationContext());
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(myAdapter);
                    }
                    progressDialog.dismiss();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(ListOfQuestionUser.this,"Refresh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseTrendsQuestion> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ListOfQuestionUser.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getQuestionKeywordUser(final String id,final String keyword){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseTrendsQuestion> mService = mApiService.getQuestionKeywordUser(id,keyword);
        mService.enqueue(new Callback<ResponseTrendsQuestion>() {
            @Override
            public void onResponse(Call<ResponseTrendsQuestion> call, Response<ResponseTrendsQuestion> response) {
                if(response.isSuccessful()){
                    for(int i=0;i<response.body().getData().size();i++){
                        listTrending.add(response.body().getData().get(i));
                    }
                    if(listTrending.isEmpty()){
                        rl_nodata.setVisibility(View.VISIBLE);
                    }else{
                        rl_nodata.setVisibility(View.INVISIBLE);
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view_question);
                        QuestionListAdapter myAdapter = new QuestionListAdapter(listTrending,id,getApplicationContext());
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(myAdapter);
                    }
                    progressDialog.dismiss();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(ListOfQuestionUser.this,"Refresh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseTrendsQuestion> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ListOfQuestionUser.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
