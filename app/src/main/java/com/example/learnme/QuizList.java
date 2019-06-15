package com.example.learnme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.learnme.API.APIInterface;
import com.example.learnme.API.ResponseQuiz;
import com.example.learnme.Adapter.QuizListAdapter;
import com.example.learnme.Model.Quiz;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuizList extends AppCompatActivity {

    private List<Quiz> listQuiz= new ArrayList<>();
    private ImageView btn_close;

    private ProgressDialog progressDialog;
    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;
    private String id_user="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);

        //View ini
        btn_close = (ImageView) findViewById(R.id.btn_close);

        Intent intent =  getIntent();
        id_user = intent.getStringExtra("id");
        String request = intent.getStringExtra("request");

        // avatar shop ini
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavShop);
        btn_close = (ImageView) findViewById(R.id.btn_close);

        // initialize recycle view item
        listQuiz = new ArrayList<>();

        //assets
        progressDialog = new ProgressDialog(QuizList.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        getQuiz(id_user,request);

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        final Integer[] index = {0};
        // listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_sort:
                        if(index[0] ==0){
                            index[0] =1;
                        }else{
                            index[0] = 0;
                        }

                        break;
                }
                return false;
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

    private void getQuiz(final String id,final String request){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseQuiz> mService = mApiService.getQuiz();
        mService.enqueue(new Callback<ResponseQuiz>() {
            @Override
            public void onResponse(Call<ResponseQuiz> call, Response<ResponseQuiz> response) {
                if(response.isSuccessful()){
                    listQuiz.clear();

                    for(int i=0;i<response.body().getData().size();i++){
                        if(request.equals("2")){
                            if(Integer.valueOf(response.body().getData().get(i).getPoint())>0){
                                listQuiz.add(new Quiz(response.body().getData().get(i).getId(),
                                        response.body().getData().get(i).getTitle(),
                                        response.body().getData().get(i).getDescription(),
                                        response.body().getData().get(i).getPoint(),
                                        response.body().getData().get(i).getAttempt()));
                            }
                        }else{
                            if(Integer.valueOf(response.body().getData().get(i).getPoint())==0){
                                listQuiz.add(new Quiz(response.body().getData().get(i).getId(),
                                        response.body().getData().get(i).getTitle(),
                                        response.body().getData().get(i).getDescription(),
                                        response.body().getData().get(i).getPoint(),
                                        response.body().getData().get(i).getAttempt()));
                            }
                        }

                    }

                    // recycle view listener
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view_quiz);
                    QuizListAdapter myAdapter   = new QuizListAdapter(QuizList.this,listQuiz,id,request);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(QuizList.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(myAdapter);
                }else{
                    Toast.makeText(QuizList.this, "Refresh", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ResponseQuiz> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(QuizList.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
