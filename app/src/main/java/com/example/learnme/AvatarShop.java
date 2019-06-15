package com.example.learnme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.learnme.API.APIInterface;
import com.example.learnme.API.ResponseItem;
import com.example.learnme.Adapter.AvatarAdapter;
import com.example.learnme.Model.Item;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AvatarShop extends AppCompatActivity {

    private List<Item> listItem = new ArrayList<>();
    private ImageView btn_close;

    private ProgressDialog progressDialog;
    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;
    private String id_user="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_shop);

        Intent intent = getIntent();
        id_user = intent.getStringExtra("id");

        // avatar shop ini
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavShop);
        btn_close = (ImageView) findViewById(R.id.btn_close);

        // initialize recycle view item
        listItem = new ArrayList<>();

        //assets
        progressDialog = new ProgressDialog(AvatarShop.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        getItem(id_user);
        final Integer[] index = {0};



        // listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_sort:
                        if(index[0] ==0){
                            sortingItem("desc",id_user);
                            index[0] =1;
                        }else{
                            sortingItem("asc",id_user);
                            index[0] = 0;
                        }

                        break;
                }
                return false;
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    private void getItem(final String id)
    {
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseItem> mService = mApiService.getItem();
        mService.enqueue(new Callback<ResponseItem>() {
            @Override
            public void onResponse(Call<ResponseItem> call, Response<ResponseItem> response) {
                if(response.isSuccessful()){
                    for(int i =0;i<response.body().getData().size();i++){
                        listItem.add(new Item(response.body().getData().get(i).getId(),
                                response.body().getData().get(i).getImage(),
                                response.body().getData().get(i).getName(),
                                response.body().getData().get(i).getDescription(),
                                response.body().getData().get(i).getPoint(),
                                response.body().getData().get(i).getValid(),
                                response.body().getData().get(i).getSold()));
                    }

                    // recycle view listener
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view_avatar);
                    AvatarAdapter myAdapter   = new AvatarAdapter(AvatarShop.this,listItem,id);
                    recyclerView.setLayoutManager(new GridLayoutManager(AvatarShop.this,2));
                    recyclerView.setAdapter(myAdapter);

                }else{
                    Toast.makeText(AvatarShop.this, "Refresh", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseItem> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AvatarShop.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sortingItem(final String sort,final String id){
        progressDialog.show();
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseItem> mService = mApiService.sortingItem(sort);
        mService.enqueue(new Callback<ResponseItem>() {
            @Override
            public void onResponse(Call<ResponseItem> call, Response<ResponseItem> response) {
                if(response.isSuccessful()){
                    listItem.clear();
                    for(int i =0;i<response.body().getData().size();i++){
                        listItem.add(new Item(response.body().getData().get(i).getId(),
                                response.body().getData().get(i).getImage(),
                                response.body().getData().get(i).getName(),
                                response.body().getData().get(i).getDescription(),
                                response.body().getData().get(i).getPoint(),
                                response.body().getData().get(i).getValid(),
                                response.body().getData().get(i).getSold()));
                    }

                    // recycle view listener
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view_avatar);
                    AvatarAdapter myAdapter   = new AvatarAdapter(AvatarShop.this,listItem,id);
                    recyclerView.setLayoutManager(new GridLayoutManager(AvatarShop.this,2));
                    recyclerView.setAdapter(myAdapter);

                }else{
                    Toast.makeText(AvatarShop.this, "Refresh", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseItem> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(AvatarShop.this, "Connection Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
