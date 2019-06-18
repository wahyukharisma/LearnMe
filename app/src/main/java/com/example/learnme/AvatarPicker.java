package com.example.learnme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.learnme.API.APIInterface;
import com.example.learnme.API.ResponseImage;
import com.example.learnme.Adapter.ImagePickerAdapter;
import com.example.learnme.Model.Image;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AvatarPicker extends AppCompatActivity {

    private ProgressDialog progressDialog;
    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;
    private List<Image> mList = new ArrayList<>();
    private ImageView img_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_picker);

        Intent intent=getIntent();
        String id = intent.getStringExtra("id");

        img_close = (ImageView) findViewById(R.id.btn_close_picker);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //assets
        progressDialog = new ProgressDialog(AvatarPicker.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        getImage(id);
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
    private void getImage(final String id){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseImage> mService = mApiService.getImageBy(id);
        mService.enqueue(new Callback<ResponseImage>() {
            @Override
            public void onResponse(Call<ResponseImage> call, Response<ResponseImage> response) {
                if (response.isSuccessful()){
                    mList.clear();
                    for(int i=0;i<response.body().getData().size();i++){
                        mList.add(new Image(response.body().getData().get(i).getId(),
                                 response.body().getData().get(i).getImage()));
                    }
                }
                progressDialog.dismiss();

                // recycle view listener
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view_picker);
                ImagePickerAdapter myAdapter   = new ImagePickerAdapter(AvatarPicker.this,id,mList);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AvatarPicker.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onFailure(Call<ResponseImage> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AvatarPicker.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
