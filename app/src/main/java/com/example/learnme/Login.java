package com.example.learnme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnme.API.APIInterface;
import com.example.learnme.API.Response;
import com.example.learnme.Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Login extends AppCompatActivity {

    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;

    private EditText username,password;
    private TextView txtSignUp,txtForgotPass;
    private Button btnSignIn;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // check previous state
        if(restorePrefData()){
            Intent mainActivity = new Intent(getApplicationContext(),HomePage.class);
            startActivity(mainActivity);
            finish();
        }

        //asset
        final Drawable et_drawable_right         = getApplicationContext().getResources().getDrawable(R.drawable.ic_error_red_24dp);
        final Drawable et_drawable_left_username = getApplicationContext().getResources().getDrawable(R.drawable.ic_person_black_24dp);
        final Drawable et_drawable_left_password = getApplicationContext().getResources().getDrawable(R.drawable.ic_lock_black_24dp);
        final Animation shake = AnimationUtils.loadAnimation(Login.this,R.anim.shake);

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // view ini
        username = findViewById(R.id.et_username_signin);
        password = findViewById(R.id.et_password_signin);
        txtSignUp = findViewById(R.id.txt_sign_up);
        btnSignIn = findViewById(R.id.btn_sign_in);
        txtForgotPass = findViewById(R.id.txt_forgot_password);


        // listener
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username.setCompoundDrawablesWithIntrinsicBounds(et_drawable_left_username,null,null,null);
                password.setCompoundDrawablesWithIntrinsicBounds(et_drawable_left_password,null,null,null);

                if(username.getText().toString().equals("") || password.getText().toString().equals("")){
                    if(username.getText().toString().equals("")){
                        username.startAnimation(shake);
                        username.setCompoundDrawablesWithIntrinsicBounds(et_drawable_left_username,null,et_drawable_right,null);
                    }
                    if (password.getText().toString().equals("")){
                        password.startAnimation(shake);
                        password.setCompoundDrawablesWithIntrinsicBounds(et_drawable_left_password,null,et_drawable_right,null);
                    }
                }
                else{
                    progressDialog.show();
                    loginInterface(username.getText().toString(),password.getText().toString());
                }

            }
        });

        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        txtForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Login.this,ForgotPassword.class);
                startActivity(intent);
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

    private void loginInterface(final String username, final String password) {
        APIInterface mApiService = this.getInterfaceService();
        Call<Response> mService = mApiService.loginUser(username, password);
        mService.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if(response.body().getValue() == 1){
                            Intent intent = new Intent(getApplicationContext(), HomePage.class);
                            intent.putExtra("id",response.body().getData().get(0).getId());
                            savePrefsData(response.body().getData().get(0).getId());
                            startActivity(intent);
                            finish();
                    }else{
                        Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Login.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Login.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void savePrefsData(final String id){
        SharedPreferences pref          = getApplicationContext().getSharedPreferences("myPrefs2",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpen",true);
        editor.putString("id",id);
        editor.commit();
    }

    private boolean restorePrefData(){
        SharedPreferences pref       = getApplicationContext().getSharedPreferences("myPrefs2",MODE_PRIVATE);
        Boolean isIntroActiviyOpened = pref.getBoolean("isIntroOpen",false);
        return isIntroActiviyOpened;
    }

}
