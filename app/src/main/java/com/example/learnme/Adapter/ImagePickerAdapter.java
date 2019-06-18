package com.example.learnme.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.learnme.API.APIInterface;
import com.example.learnme.API.ResponseImage;
import com.example.learnme.Model.Image;
import com.example.learnme.Profile;
import com.example.learnme.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImagePickerAdapter extends RecyclerView.Adapter<ImagePickerAdapter.ImagePickerViewHolder> {
    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;

    Context mContext;
    String id;
    List<Image> mList;

    public ImagePickerAdapter(Context mContext, String id, List<Image> mList) {
        this.mContext = mContext;
        this.id = id;
        this.mList = mList;
    }

    @NonNull
    @Override
    public ImagePickerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view                     = mInflater.inflate(R.layout.item_avatar_picker,viewGroup,false);
        return new ImagePickerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImagePickerViewHolder imagePickerViewHolder, int i) {
        Glide.with(mContext.getApplicationContext()).load(mList.get(i).getImage()).into(imagePickerViewHolder.img_picker);
        final Integer index =i;
        imagePickerViewHolder.img_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIInterface mApiService = getInterfaceService();
                Call<ResponseImage> mService = mApiService.updateImage(id,mList.get(index).getId());
                mService.enqueue(new Callback<ResponseImage>() {
                    @Override
                    public void onResponse(Call<ResponseImage> call, Response<ResponseImage> response) {
                        Intent intent = new Intent(mContext.getApplicationContext(), Profile.class);
                        intent.putExtra("id",id);
                        mContext.startActivity(intent);
                        ((Activity)mContext).finish();
                    }

                    @Override
                    public void onFailure(Call<ResponseImage> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ImagePickerViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_picker;
        public ImagePickerViewHolder(@NonNull View itemView) {
            super(itemView);

            img_picker = (ImageView) itemView.findViewById(R.id.img_picker);
        }
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
}
