package com.example.learnme.API;

import android.util.Log;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public  class UpdatePoint {
    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;

    public void updatePoint(final String id,final String status,final String point,final String description){
        APIInterface mApiService = this.getInterfaceService();
        Call<Response> mService = mApiService.updateUserPoint(id,status,point,description);
        mService.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Log.d("message",response.body().getMessage());
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.d("message","connection failed");
            }
        });
    }

    APIInterface getInterfaceService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final APIInterface mInterfaceService = retrofit.create(APIInterface.class);
        return mInterfaceService;
    }
}
