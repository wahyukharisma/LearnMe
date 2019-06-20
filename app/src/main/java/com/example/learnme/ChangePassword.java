package com.example.learnme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.learnme.API.APIInterface;
import com.example.learnme.API.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePassword extends AppCompatActivity {

    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;
    private ProgressDialog progressDialog;

    private ImageView btn_close;
    private Button btn_change;
    private EditText et_old_pass,et_new_pass,et_confirm_pass;
    private String id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");


        //view ini
        btn_close   = (ImageView) findViewById(R.id.btn_close_edit_pass);
        btn_change  = (Button) findViewById(R.id.btn_change_pass);
        et_old_pass = (EditText) findViewById(R.id.et_password_old);
        et_new_pass = (EditText) findViewById(R.id.et_new_pass);
        et_confirm_pass = (EditText) findViewById(R.id.et_confirm_pass);

        et_confirm_pass.setImeOptions(EditorInfo.IME_ACTION_DONE);



        //asset
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        final Animation shake = AnimationUtils.loadAnimation(ChangePassword.this,R.anim.shake);

        //listener
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_confirm_pass.getText().toString().equals("") || et_new_pass.getText().toString().equals("") || et_old_pass.getText().toString().equals("")){
                    if(et_confirm_pass.getText().toString().equals("")){
                        et_confirm_pass.startAnimation(shake);
                    }
                    if(et_new_pass.getText().toString().equals("")){
                        et_new_pass.startAnimation(shake);
                    }
                    if(et_old_pass.getText().toString().equals("")){
                        et_old_pass.startAnimation(shake);
                    }
                }else{
                    if(!et_new_pass.getText().toString().equals(et_confirm_pass.getText().toString())){
                        Toast.makeText(ChangePassword.this, "Wrong password confirm", Toast.LENGTH_SHORT).show();
                    }else{
                        changePassword(id,et_old_pass.getText().toString(),et_new_pass.getText().toString());
                    }

                }

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

    private void changePassword(final String id, final String oldpassword, final String newpassword){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<Response> mService = mApiService.updatePassword(id,oldpassword,newpassword);
        mService.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    if(response.body().getValue()==1){
                        finish();
                    }
                    Toast.makeText(ChangePassword.this,response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ChangePassword.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(ChangePassword.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
