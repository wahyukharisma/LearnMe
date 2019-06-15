package com.example.learnme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnme.API.APIInterface;
import com.example.learnme.API.Response;
import com.example.learnme.Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Profile extends AppCompatActivity {

    private ImageView btn_close,img_profile;
    private CardView cd_lq,cd_la,cd_cpass,cd_ph, cd_ep;
    private String id="";
    private TextView txt_username,txt_point,txt_reputation;

    private ProgressDialog progressDialog;
    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final Intent intent=getIntent();
        id= intent.getStringExtra("id");

        // view ini
        btn_close = (ImageView) findViewById(R.id.btn_close_profile);
        cd_lq     = (CardView)findViewById(R.id.cd_lq);
        cd_la     = (CardView)findViewById(R.id.cd_la);
        cd_cpass  = (CardView) findViewById(R.id.cd_cpass);
        cd_ph     = (CardView) findViewById(R.id.cd_ph);
        cd_ep     = (CardView) findViewById(R.id.cd_ep);
        img_profile = (ImageView) findViewById(R.id.img_profile);

        img_profile.setImageDrawable(getResources().getDrawable(R.drawable.noimage));

        //assets
        progressDialog = new ProgressDialog(Profile.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        getUser(id);


        // listener
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cd_lq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Profile.this,ListOfQuestionUser.class);
                intent1.putExtra("id",id);
                startActivity(intent1);
            }
        });

        cd_ph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Profile.this,PointHistory.class);
                intent1.putExtra("id",id);
                startActivity(intent1);
            }
        });

        cd_la.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Profile.this,ListOfAnswerUser.class);
                intent1.putExtra("id",id);
                startActivity(intent1);
            }
        });

        cd_cpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ChangePassword.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        cd_ep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),EditProfile.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
    }

    public void initView(final User user){
        txt_point = (TextView) findViewById(R.id.txt_userpoint);
        txt_reputation = (TextView) findViewById(R.id.txt_reputation);
        txt_username = (TextView) findViewById(R.id.txt_username);

        txt_username.setText(user.getUsername());
        txt_reputation.setText(user.getReputation());
        txt_point.setText(user.getPoint());
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
        Call<Response> mService = mApiService.getUserBy(id);
        mService.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(response.isSuccessful()){
                    initView(response.body().getData().get(0));
                }else{
                    Toast.makeText(Profile.this, "Refresh", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Profile.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
