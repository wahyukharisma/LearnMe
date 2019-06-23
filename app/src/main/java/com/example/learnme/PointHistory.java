package com.example.learnme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnme.API.APIInterface;
import com.example.learnme.API.ResponsePoint;
import com.example.learnme.Adapter.PointHistoryAdapter;
import com.example.learnme.Model.Point;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PointHistory extends AppCompatActivity {

    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;
    private List<Point> listPoint = new ArrayList<>();
    private ProgressDialog progressDialog;
    private String id_user="";
    private Spinner spinner_month,spinner_year;
    private Button btn_search;
    private RelativeLayout rl_no_data;
    private ImageView img_close;
    private TextView txt_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_history);

        Intent intent = getIntent();
        id_user = intent.getStringExtra("id");

        //get preference language
        final SharedPreferences pref       = getApplicationContext().getSharedPreferences("myPrefsLanguage",MODE_PRIVATE);
        String getPref = pref.getString("language","English");

        //View ini
        btn_search = (findViewById(R.id.btn_search_point));
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        String thisMonth = convertMonth(month);
        rl_no_data = (RelativeLayout) findViewById(R.id.rl_no_data);
        img_close = (ImageView) findViewById(R.id.btn_close_point);
        txt_title = (TextView) findViewById(R.id.txt_title_point);

        spinner_month = (Spinner) findViewById(R.id.spin_month);
        ArrayList<String> array_year = new ArrayList<>();
        spinner_year  = (Spinner) findViewById(R.id.spin_year);

        initViewLanguage(getPref);

        //spinner ini
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.month,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_month.setAdapter(adapter);

        for(int i=2019;i<=year;i++){
            array_year.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,array_year);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_year.setAdapter(adapter1);

        //asset
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        getPointHistory(id_user,String.valueOf(year),thisMonth);


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempMonth = spinner_month.getSelectedItem().toString();
                String tempYear = spinner_year.getSelectedItem().toString();
                listPoint.clear();
                getPointHistory(id_user,tempYear,tempMonth);
            }
        });

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String convertMonth(Integer input){
        String temp="";
        if(input==0){
            temp="January";
        }else if(input==1){
            temp="February";
        }else if(input==2){
            temp="March";
        }else if(input==3){
            temp="April";
        }else if(input==4){
            temp="May";
        }else if(input==5){
            temp="June";
        }else if(input==6){
            temp="July";
        }else if(input==7){
            temp="August";
        }else if(input==8){
            temp="September";
        }else if(input==9){
            temp="October";
        }else if(input==10){
            temp="November";
        }else{
            temp="December";
        }

        return temp;
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

    private void getPointHistory(final String id,final String year, final String month){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponsePoint> mService = mApiService.getPointHistoryUser(id,month,year);
        mService.enqueue(new Callback<ResponsePoint>() {
            @Override
            public void onResponse(Call<ResponsePoint> call, Response<ResponsePoint> response) {
                if (response.isSuccessful()){
                    listPoint.clear();

                    for(int i=0;i<response.body().getData().size();i++){
                        listPoint.add(response.body().getData().get(i));
                    }
                    if(listPoint.isEmpty()){
                        rl_no_data.setVisibility(View.VISIBLE);
                    }else {
                        rl_no_data.setVisibility(View.GONE);
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view_point);
                        PointHistoryAdapter myAdapter   = new PointHistoryAdapter(PointHistory.this,listPoint);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PointHistory.this);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(myAdapter);
                    }
                    progressDialog.dismiss();

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(PointHistory.this,"Refresh", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePoint> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(PointHistory.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViewLanguage(String language){
        if(language.equals("Indonesia")){
            txt_title.setText("Jejak Poin");
            btn_search.setText("Cari");
        }
    }
}
