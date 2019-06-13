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
import com.example.learnme.API.ResponseAnswer;
import com.example.learnme.Adapter.AnswearListAdapter;
import com.example.learnme.Adapter.AnswerListUserAdapter;
import com.example.learnme.Model.Answer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListOfAnswerUser extends AppCompatActivity {

    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;

    private List<Answer> listAnswer = new ArrayList<>();
    private ProgressDialog progressDialog;
    private String id_user="";
    private RelativeLayout rl_nodata;


    private EditText et_keyword;
    private Button btn_search;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_answer_user);

        Intent intent = getIntent();
        id_user = intent.getStringExtra("id");

        //View ini
        et_keyword = (EditText) findViewById(R.id.et_search_hp);
        btn_search = (Button) findViewById(R.id.btn_search_hp);
        img_back   = (ImageView) findViewById(R.id.btn_close_answer);
        rl_nodata   = (RelativeLayout) findViewById(R.id.rl_nodata);

        //asset
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //Initialization
        getAnswerUser(id_user);


        //listener
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_keyword.getText().toString().equals("")){
                    Toast.makeText(ListOfAnswerUser.this, "Fill keyword first", Toast.LENGTH_SHORT).show();
                }else {
                    listAnswer = new ArrayList<>();
                    getAnswerKeyword(id_user,et_keyword.getText().toString());
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

    private void getAnswerUser(final String id){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseAnswer> mService = mApiService.getUserAnswer(id);
        mService.enqueue(new Callback<ResponseAnswer>() {
            @Override
            public void onResponse(Call<ResponseAnswer> call, Response<ResponseAnswer> response) {
                if(response.isSuccessful()){
                    for(int i=0;i<response.body().getData().size();i++){
                        listAnswer.add(response.body().getData().get(i));
                        Log.d("message","in");
                    }
                    if(listAnswer.isEmpty()){
                        rl_nodata.setVisibility(View.VISIBLE);
                    }else {
                        // recycle view answear listener
                        rl_nodata.setVisibility(View.INVISIBLE);
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view_answer_user);
                        AnswerListUserAdapter myAdapter   = new AnswerListUserAdapter(ListOfAnswerUser.this,listAnswer);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ListOfAnswerUser.this);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(myAdapter);
                    }
                    progressDialog.dismiss();
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(ListOfAnswerUser.this,"Refresh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseAnswer> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ListOfAnswerUser.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAnswerKeyword(final String id,final String keyword){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseAnswer> mService = mApiService.getAnswerKeywordUser(id,keyword);
        mService.enqueue(new Callback<ResponseAnswer>() {
            @Override
            public void onResponse(Call<ResponseAnswer> call, Response<ResponseAnswer> response) {
                if(response.isSuccessful()){
                    for(int i=0;i<response.body().getData().size();i++){
                        listAnswer.add(response.body().getData().get(i));
                        Log.d("message","in");
                    }
                    if(listAnswer.isEmpty()){
                        rl_nodata.setVisibility(View.VISIBLE);
                    }else {
                        // recycle view answear listener
                        rl_nodata.setVisibility(View.INVISIBLE);
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view_answer_user);
                        AnswerListUserAdapter myAdapter   = new AnswerListUserAdapter(ListOfAnswerUser.this,listAnswer);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ListOfAnswerUser.this);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(myAdapter);
                    }
                    progressDialog.dismiss();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(ListOfAnswerUser.this,"Refresh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseAnswer> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ListOfAnswerUser.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
