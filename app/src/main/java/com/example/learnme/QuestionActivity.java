package com.example.learnme;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.internal.NavigationMenu;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.learnme.API.APIInterface;
import com.example.learnme.API.ResponseQuestion;
import com.example.learnme.API.ResponseTrendsQuestion;
import com.example.learnme.Adapter.QuestionListAdapter;
import com.example.learnme.Model.Question;
import com.example.learnme.Model.TrendingQuestion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuestionActivity extends AppCompatActivity {
    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;

    private List<TrendingQuestion> listTrending = new ArrayList<>();
    private ProgressDialog progressDialog;
    private RelativeLayout rl_nodata;

    private ImageView btn_close;
    private RelativeLayout semi_transparent;
    private String value="";
    private String id="";
    private EditText et_search;
    private Button btn_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        Intent intent = getIntent();
        value = intent.getStringExtra("Value");
        id    = intent.getStringExtra("id");

        //asset
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //View ini
        btn_close = (ImageView) findViewById(R.id.btn_close_question);
        final FabSpeedDial floating_menu = (FabSpeedDial) findViewById(R.id.floating_menu_question);
        semi_transparent = (RelativeLayout) findViewById(R.id.semi_white_bg_question);
        et_search = (EditText) findViewById(R.id.et_search_hp);
        btn_search = (Button) findViewById(R.id.btn_search_hp);
        rl_nodata = (RelativeLayout) findViewById(R.id.rl_nodata);

        if(!value.equals("1") && !value.equals("2") && !value.equals("3") && !value.equals("4") ){
            showKeyword(value,id);
        }else{
            showTag(value,id);
        }


        // listener

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_search.getText().toString().equals("")){
                    Toast.makeText(QuestionActivity.this,"Please input keyword first",Toast.LENGTH_SHORT).show();
                }else{
                    listTrending = new ArrayList<>();
                    if(!value.equals("1") && !value.equals("2") && !value.equals("3") && !value.equals("4")){
                        showKeyword(et_search.getText().toString(),id);
                    }else{
                        showKeywordTag(et_search.getText().toString(),value);
                    }

                }
            }
        });

        floating_menu.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                semi_transparent.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.question:
                        Intent intent = new Intent(QuestionActivity.this, AskQuestion.class);
                        intent.putExtra("user",id);
                        startActivity(intent);
                }
                semi_transparent.setVisibility(View.GONE);
                return false;
            }

            @Override
            public void onMenuClosed() {
                semi_transparent.setVisibility(View.GONE);
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
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

    public void showTag(final String tag,final String id){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseTrendsQuestion> mService = mApiService.getQuestionTag(tag);
        mService.enqueue(new Callback<ResponseTrendsQuestion>() {
            @Override
            public void onResponse(Call<ResponseTrendsQuestion> call, Response<ResponseTrendsQuestion> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().getData().size(); i++) {
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
                    Log.d("message",response.errorBody().toString());
                    Toast.makeText(QuestionActivity.this, "Refresh", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseTrendsQuestion> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(QuestionActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showKeyword(final String value,final String id){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseTrendsQuestion> mService = mApiService.getQuestionKeyword(value);
        mService.enqueue(new Callback<ResponseTrendsQuestion>() {
            @Override
            public void onResponse(Call<ResponseTrendsQuestion> call, Response<ResponseTrendsQuestion> response) {
                if(response.isSuccessful()){
                    for(int i=0;i<response.body().getData().size();i++){
                        listTrending.add(response.body().getData().get(i));
                    }
                    et_search.setText("");

                    if(listTrending.isEmpty()){
                        rl_nodata.setVisibility(View.VISIBLE);
                    }else {
                        rl_nodata.setVisibility(View.INVISIBLE);
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view_question);
                        QuestionListAdapter myAdapter = new QuestionListAdapter(listTrending, id, getApplicationContext());
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(myAdapter);
                    }
                    progressDialog.dismiss();
                }else{
                    progressDialog.dismiss();
                    Log.d("message",response.errorBody().toString());
                    Toast.makeText(QuestionActivity.this, "Refresh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseTrendsQuestion> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(QuestionActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void showKeywordTag(final String keyword,final String tag){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseTrendsQuestion> mService = mApiService.getQuestionKeywordTag(tag,keyword);
        mService.enqueue(new Callback<ResponseTrendsQuestion>() {
            @Override
            public void onResponse(Call<ResponseTrendsQuestion> call, Response<ResponseTrendsQuestion> response) {
                if (response.isSuccessful()){
                    for(int i=0;i<response.body().getData().size();i++){
                        listTrending.add(response.body().getData().get(i));
                    }
                    et_search.setText("");

                    if(listTrending.isEmpty()){
                        rl_nodata.setVisibility(View.VISIBLE);
                    }else {
                        rl_nodata.setVisibility(View.INVISIBLE);
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view_question);
                        QuestionListAdapter myAdapter = new QuestionListAdapter(listTrending, id, getApplicationContext());
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(myAdapter);
                    }
                    progressDialog.dismiss();
                }else {
                    progressDialog.dismiss();
                    Log.d("message",response.errorBody().toString());
                    Toast.makeText(QuestionActivity.this, "Refresh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseTrendsQuestion> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(QuestionActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
