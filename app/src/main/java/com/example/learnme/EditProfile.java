package com.example.learnme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.design.animation.ImageMatrixProperty;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnme.API.APIInterface;
import com.example.learnme.API.Response;
import com.example.learnme.API.ResponsePerson;
import com.example.learnme.Model.Person;
import com.example.learnme.Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditProfile extends AppCompatActivity {

    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;
    private ProgressDialog progressDialog;

    private ImageView btn_close;
    private Button btn_edit;
    private EditText et_email,et_first_name,et_last_name,et_address,et_phone;
    private TextView et_username,txt_edit_profile;
    private TextInputLayout txt_phone,txt_first_name,txt_last_name,txt_address;

    private String id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Intent intent= getIntent();
        id = intent.getStringExtra("id");
        Log.d("message",id);

        //get preference language
        final SharedPreferences pref       = getApplicationContext().getSharedPreferences("myPrefsLanguage",MODE_PRIVATE);
        String getPref = pref.getString("language","English");

        //View ini
        txt_edit_profile = (TextView) findViewById(R.id.txt_edit_profile_title);
        et_first_name  = (EditText) findViewById(R.id.et_first_name);
        et_last_name   = (EditText) findViewById(R.id.et_last_name);
        et_address     = (EditText) findViewById(R.id.et_address);
        et_phone       = (EditText) findViewById(R.id.et_phone);
        et_username  = (TextView) findViewById(R.id.et_username_change);
        et_email     = (EditText) findViewById(R.id.et_email_change);
        btn_close    = (ImageView) findViewById(R.id.btn_close_edit);
        btn_edit     = (Button) findViewById(R.id.btn_edit_profile);
        txt_phone    = (TextInputLayout) findViewById(R.id.txt_phone);
        txt_address  = (TextInputLayout) findViewById(R.id.txt_address);
        txt_first_name    = (TextInputLayout) findViewById(R.id.txt_first_name);
        txt_last_name     = (TextInputLayout) findViewById(R.id.txt_last_name);

        setViewLanguage(getPref);

        //asset
        final Animation shake = AnimationUtils.loadAnimation(EditProfile.this,R.anim.shake);
        final Drawable et_drawable_right         = getApplicationContext().getResources().getDrawable(R.drawable.ic_error_red_24dp);
        progressDialog = new ProgressDialog(EditProfile.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);


        getUserBy(id);
        getPersonBy(id);


        //listener
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_email.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                et_address.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                et_first_name.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                et_last_name.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                et_phone.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);

                if( et_email.getText().toString().equals("") || et_address.getText().toString().equals("")
                        || et_first_name.getText().toString().equals("") || et_last_name.getText().toString().equals("") || et_phone.getText().toString().equals("")
                        || !isEmailValid(et_email.getText().toString())){

                    if(et_phone.getText().toString().equals("")){
                        et_phone.startAnimation(shake);
                        et_phone.setCompoundDrawablesWithIntrinsicBounds(null,null,et_drawable_right,null);
                    }
                    if(et_last_name.getText().toString().equals("")){
                        et_last_name.startAnimation(shake);
                        et_last_name.setCompoundDrawablesWithIntrinsicBounds(null,null,et_drawable_right,null);
                    }
                    if(et_first_name.getText().toString().equals("")){
                        et_first_name.startAnimation(shake);
                        et_first_name.setCompoundDrawablesWithIntrinsicBounds(null,null,et_drawable_right,null);
                    }
                    if(et_address.getText().toString().equals("")){
                        et_address.startAnimation(shake);
                        et_address.setCompoundDrawablesWithIntrinsicBounds(null,null,et_drawable_right,null);
                    }
                    if(et_email.getText().toString().equals("") || !isEmailValid(et_email.getText().toString())){
                        if(!isEmailValid(et_email.getText().toString())){
                            Toast.makeText(EditProfile.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                        }
                        et_email.startAnimation(shake);
                        et_email.setCompoundDrawablesWithIntrinsicBounds(null,null,et_drawable_right,null);
                    }
                }else{
                    updatePerson(id,et_username.getText().toString(),et_last_name.getText().toString(),et_first_name.getText().toString(),et_address.getText().toString(),et_phone.getText().toString(),et_email.getText().toString());
                    finish();
                }
            }
        });
    }

    public void initUser(List<User> mUser){
        et_username.setText(mUser.get(0).getUsername());
        et_email.setText(mUser.get(0).getEmail());
    }

    public void initPerson(List<Person> mPerson){
        et_address.setText(mPerson.get(0).getAddress());
        et_first_name.setText(mPerson.get(0).getFirstName());
        et_last_name.setText(mPerson.get(0).getLastName());
        et_phone.setText(mPerson.get(0).getPhone());

        et_phone.setImeOptions(EditorInfo.IME_ACTION_DONE);

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
                    initUser(response.body().getData());
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

    private void updatePerson(final String id,final String username,final String last_name,final String first_name,final String address,final String phone,final String email){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<Response> mService = mApiService.updatePerson(id,first_name,last_name,address,phone,username,email);
        mService.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(response.isSuccessful()){
                    Toast.makeText(EditProfile.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void setViewLanguage(String language){
        if(language.equals("Indonesia")){
            txt_edit_profile.setText("Pengaturan Profil");
            btn_edit.setText("Perbarui");
            txt_phone.setHint("Telepon");
            txt_address.setHint("Alamat");
            txt_first_name.setHint("Nama Depan");
            txt_last_name.setHint("Nama Belakang");
        }

    }
}
