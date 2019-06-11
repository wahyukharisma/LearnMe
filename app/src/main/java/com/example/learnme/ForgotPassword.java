package com.example.learnme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgotPassword extends AppCompatActivity {
    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;

    private Button btnSubmit;
    private EditText et_username,et_email;
    private ImageView img_back;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //view ini
        btnSubmit = findViewById(R.id.btn_submit_forgot);
        et_username = findViewById(R.id.et_username_input);
        et_email = findViewById(R.id.et_email_input);
        img_back = findViewById(R.id.img_back);

        //asset
        final Drawable et_drawable_right         = getApplicationContext().getResources().getDrawable(R.drawable.ic_error_red_24dp);
        final Drawable et_drawable_left_username = getApplicationContext().getResources().getDrawable(R.drawable.ic_person_blue_24dp);
        final Drawable et_drawable_left_email = getApplicationContext().getResources().getDrawable(R.drawable.ic_email_black_24dp);
        final Animation shake = AnimationUtils.loadAnimation(ForgotPassword.this,R.anim.shake);
        progressDialog = new ProgressDialog(ForgotPassword.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        //listener
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(ForgotPassword.this,Login.class);
                startActivity(intent);
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                et_username.setCompoundDrawablesWithIntrinsicBounds(et_drawable_left_username,null,null,null);
                et_email.setCompoundDrawablesWithIntrinsicBounds(et_drawable_left_email,null,null,null);

                if(et_email.getText().toString().equals("") || et_username.getText().toString().equals("") || !isEmailValid(et_email.getText().toString())){
                    if(et_username.getText().toString().equals("")){
                        et_username.startAnimation(shake);
                        et_username.setCompoundDrawablesWithIntrinsicBounds(et_drawable_left_username,null,et_drawable_right,null);
                    }
                    if(et_email.getText().toString().equals("")){
                        et_email.startAnimation(shake);
                        et_email.setCompoundDrawablesWithIntrinsicBounds(et_drawable_left_email,null,et_drawable_right,null);
                    }
                    if(!isEmailValid(et_email.getText().toString())){
                        Toast.makeText(ForgotPassword.this,"Wrong email format",Toast.LENGTH_SHORT).show();
                        et_email.startAnimation(shake);
                        et_email.setCompoundDrawablesWithIntrinsicBounds(et_drawable_left_email,null,et_drawable_right,null);
                    }
                }else{
                    progressDialog.show();
                    forgotPasswordInterface(et_username.getText().toString(),et_email.getText().toString());
                }
            }
        });

    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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

    private void forgotPasswordInterface(final String username, final String email){
        APIInterface mApiService = this.getInterfaceService();
        Call<Response> mService = mApiService.forgotPassword(username,email);
        mService.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    if(response.body().getValue()==1){
                        Intent intent = new Intent(ForgotPassword.this,EmailSuccess.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(ForgotPassword.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ForgotPassword.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("message",t.getMessage());
                Toast.makeText(ForgotPassword.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
