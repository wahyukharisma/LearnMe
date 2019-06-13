package com.example.learnme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.animation.ImageMatrixProperty;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.learnme.API.APIInterface;
import com.example.learnme.API.Response;
import com.example.learnme.API.ResponsePerson;
import com.example.learnme.Model.Person;
import com.example.learnme.Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditProfile extends AppCompatActivity {

    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;
    private ProgressDialog progressDialog;

    private ImageView btn_close;
    private Button btn_confirm;
    private EditText et_username,et_email,et_first_name,et_last_name,et_address,et_phone;

    private String id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent= getIntent();
        id = intent.getStringExtra("id");


        // view ini
        btn_close    = (ImageView) findViewById(R.id.btn_close_edit);
        btn_confirm  = (Button) findViewById(R.id.btn_edit_profile);

        //asset
        final Animation shake = AnimationUtils.loadAnimation(EditProfile.this,R.anim.shake);
        progressDialog = new ProgressDialog(EditProfile.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        getUserBy(id);
        getPersonBy(id);
        getUser(id);



        //listener
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Confirm",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void initUser(List<User> mUser){
        et_username  = (EditText) findViewById(R.id.et_username_change);
        et_email     = (EditText) findViewById(R.id.et_email_change);

        et_username.setText(mUser.get(0).getUsername());
        et_email.setText(mUser.get(0).getEmail());
    }

    public void initPerson(List<Person> mPerson){
        et_first_name  = (EditText) findViewById(R.id.et_first_name);
        et_last_name   = (EditText) findViewById(R.id.et_last_name);
        et_address     = (EditText) findViewById(R.id.et_address);
        et_phone       = (EditText) findViewById(R.id.et_phone);

        et_address.setText(mPerson.get(0).getAddress());
        et_first_name.setText(mPerson.get(0).getFirstName());
        et_last_name.setText(mPerson.get(0).getLastName());
        et_phone.setText(mPerson.get(0).getPhone());
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

    private void getUserBy(final String id){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<Response> mService = mApiService.getUserBy(id);
        mService.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(response.isSuccessful()){
                    //initUser(response.body().getData());
                }else {
                    Toast.makeText(EditProfile.this, "Refresh", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditProfile.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUser(final String id){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<Response> mService = mApiService.getUserBy(id);
        mService.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                initUser(response.body().getData());
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

            }
        });
    }

    private void getPersonBy(final String id){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponsePerson> mService = mApiService.getPersonBy(id);
        mService.enqueue(new Callback<ResponsePerson>() {
            @Override
            public void onResponse(Call<ResponsePerson> call, retrofit2.Response<ResponsePerson> response) {
                if(response.isSuccessful()){
                    initPerson(response.body().getData());
                }else {
                    Toast.makeText(EditProfile.this, "Refresh", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponsePerson> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditProfile.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
