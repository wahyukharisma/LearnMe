package com.example.learnme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnme.API.APIInterface;
import com.example.learnme.API.Response;
import com.example.learnme.API.UpdatePoint;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AskQuestion extends AppCompatActivity {
    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;

    private ImageView btn_close;
    private EditText et_title,et_description;
    private Button btn_submit;
    private Spinner spinner_tag;
    private ProgressDialog progressDialog;
    private TextView txt_title_ask,txt_tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);

        //get preference language
        final SharedPreferences pref       = getApplicationContext().getSharedPreferences("myPrefsLanguage",MODE_PRIVATE);
        String getPref = pref.getString("language","English");

        Intent intent = getIntent();
        final String idUser = intent.getStringExtra("user");

        // view ini
        btn_close = (ImageView) findViewById(R.id.btn_close_ask_question);
        et_title = (EditText) findViewById(R.id.et_title);
        et_description = (EditText) findViewById(R.id.et_desc);
        btn_submit = (Button) findViewById(R.id.btn_submit_question);
        spinner_tag = (Spinner) findViewById(R.id.spinner_tag);
        txt_title_ask = (TextView) findViewById(R.id.txt_title_ask);
        txt_tag       = (TextView) findViewById(R.id.txt_tag);

        et_description.setImeOptions(EditorInfo.IME_ACTION_DONE);

        ArrayList<String> array_tag = new ArrayList<>();

        if(getPref.equals("Indonesia")){
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.tag_arrays_indo,R.layout.support_simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinner_tag.setAdapter(adapter);
        }else{
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.tag_arrays,R.layout.support_simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            spinner_tag.setAdapter(adapter);
        }

        //init View Language
        initViewLanguage(getPref);

        //Asset
        final Animation shake = AnimationUtils.loadAnimation(AskQuestion.this,R.anim.shake);
        final Drawable et_drawable_right         = getApplicationContext().getResources().getDrawable(R.drawable.ic_error_red_24dp);
        progressDialog = new ProgressDialog(AskQuestion.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //listener
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_title.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                et_description.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);

                if(et_description.getText().toString().equals("") || et_title.getText().toString().equals("")){
                    if(et_description.getText().toString().equals("")){
                        et_description.setCompoundDrawablesWithIntrinsicBounds(null,null,et_drawable_right,null);
                        et_description.setAnimation(shake);
                    }else {
                        et_description.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                    }
                    if(et_title.getText().toString().equals("")){
                        et_title.setCompoundDrawablesWithIntrinsicBounds(null,null,et_drawable_right,null);
                        et_title.setAnimation(shake);
                    }else {
                        et_title.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                    }
                }else {
                    storeQuestion(et_title.getText().toString(),et_description.getText().toString(),String.valueOf(spinner_tag.getSelectedItemPosition()+1),idUser,"1");
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

    public void storeQuestion(final String title,final String description,final String tag,final String user,final String image){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<Response> mService = mApiService.storeQuestion(title,description,tag,user,image);
        mService.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    Toast.makeText(AskQuestion.this, "Question has been submit", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AskQuestion.this,QuestionExpand.class);
                    Log.d("message",response.body().getMessage());
                    intent.putExtra("id",response.body().getMessage());
                    intent.putExtra("id_user",user);

                    // adding point
                    UpdatePoint updatePoint = new UpdatePoint();
                    updatePoint.updatePoint(user,"1","0","Ask Question, "+title);

                    startActivity(intent);

                    finish();

                }else{
                    Toast.makeText(AskQuestion.this, "Refresh", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AskQuestion.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initViewLanguage(String language){
        if(language.equals("Indonesia")){
            txt_title_ask.setText("Bertanya");
            btn_submit.setText("Tanya");
            et_title.setHint("Judul");
            et_description.setHint("Deskripsi");
            txt_tag.setText("Pilih kategori");
        }
    }
}
