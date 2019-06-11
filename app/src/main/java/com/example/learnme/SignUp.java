package com.example.learnme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnme.API.APIInterface;
import com.example.learnme.API.Response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUp extends AppCompatActivity {
    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;

    private Toolbar toolbar;
    private static final int GALLERY_INTENT = 1;
    ImageView imageView;
    TextView signIn;
    Button signUp;
    EditText et_username,et_password,et_email;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // ini view
        toolbar = findViewById(R.id.toolbar_sign_up);
        setSupportActionBar(toolbar);
        signIn    = findViewById(R.id.txt_sign_in);
        signUp    = findViewById(R.id.btn_sign_up);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_email    = findViewById(R.id.et_email);

        //assets
        progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        final Animation shake = AnimationUtils.loadAnimation(SignUp.this,R.anim.shake);
        final Drawable et_drawable_right         = getApplicationContext().getResources().getDrawable(R.drawable.ic_error_red_24dp);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_username.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                et_password.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                et_email.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);

                if(et_username.getText().toString().equals("") || et_password.getText().toString().equals("") || et_email.getText().toString().equals("") || !isEmailValid(et_email.getText().toString())){
                    if(et_username.getText().toString().equals("")){
                        et_username.startAnimation(shake);
                        et_username.setCompoundDrawablesWithIntrinsicBounds(null,null,et_drawable_right,null);
                    }
                    if(et_password.getText().toString().equals("")){
                        et_password.startAnimation(shake);
                        et_password.setCompoundDrawablesWithIntrinsicBounds(null,null,et_drawable_right,null);
                    }
                    if(et_email.getText().toString().equals("")){
                        et_email.startAnimation(shake);
                        et_email.setCompoundDrawablesWithIntrinsicBounds(null,null,et_drawable_right,null);
                    }
                    if(!isEmailValid(et_email.getText().toString())){
                        Toast.makeText(SignUp.this,"Wrong email format",Toast.LENGTH_SHORT).show();
                        et_email.startAnimation(shake);
                        et_email.setCompoundDrawablesWithIntrinsicBounds(null,null,et_drawable_right,null);
                    }
                }else{
                    progressDialog.show();
                    registerUser(et_username.getText().toString(),et_password.getText().toString(),et_email.getText().toString());
                }

            }
        });


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_INTENT & resultCode == RESULT_OK){
            Uri uri = data.getData();
            imageView.setImageURI(uri);
            imageView.setPadding(0,0,0,0);
        }
    }

    private APIInterface getInterfaceService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final APIInterface mInterfaceService = retrofit.create(APIInterface.class);
        return mInterfaceService;
    }

    private void registerUser(final String username,final String password, final String email){
        APIInterface mApiService = this.getInterfaceService();
        Call<Response> mService = mApiService.registerUser(username,password,email);
        mService.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(response.isSuccessful()){
                    if(response.body().getValue()==1){
                        registerPerson(username);
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(SignUp.this,"Username must unique",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SignUp.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.d("message",t.getMessage());
                Toast.makeText(SignUp.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerPerson(final String username){
        APIInterface mApiService = this.getInterfaceService();
        Call<Response> mService = mApiService.registerPerson(username,"first name","last name","address","phone");
        mService.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    if(response.body().getValue()==1){
                        Toast.makeText(SignUp.this,"Sign Up Success",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(SignUp.this,"Filed create person",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SignUp.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("message",t.getMessage());
                Toast.makeText(SignUp.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
