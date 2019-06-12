package com.example.learnme.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnme.API.APIInterface;
import com.example.learnme.API.ResponseAnswer;
import com.example.learnme.Model.Answear;
import com.example.learnme.QuestionExpand;
import com.example.learnme.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AnswearListAdapter extends RecyclerView.Adapter<AnswearListAdapter.AnswearListViewHolder> {

    Context mContext;
    List<Answear> mData;


    public AnswearListAdapter(Context mContext, List<Answear> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public AnswearListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view                     = mInflater.inflate(R.layout.list_item_answear,viewGroup,false);
        return new AnswearListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AnswearListViewHolder answearListViewHolder, int i) {

        answearListViewHolder.txt_desc.setText(mData.get(i).getAnswer());
        answearListViewHolder.txt_name.setText(mData.get(i).getIdUser());
        answearListViewHolder.txt_dislike.setText(mData.get(i).getDislikes());
        answearListViewHolder.txt_like.setText(mData.get(i).getLikes());
        answearListViewHolder.txt_date.setText(mData.get(i).getDate());

        final Integer index =i;
        final Integer[] countLike = {1};
        final Integer[] countDislike = {1};
        answearListViewHolder.img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countLike[0] ==1){
                    updateLike(mData.get(index).getId(),countLike[0].toString());
                    answearListViewHolder.img_like.setImageResource(R.drawable.ic_thumb_up_dark_blue_24dp);
                    Integer increment = Integer.parseInt(mData.get(index).getLikes())+1;
                    answearListViewHolder.txt_like.setText(String.valueOf(increment));
                    answearListViewHolder.img_dislike.setEnabled(false);
                    countLike[0] =0;
                }else{
                    updateLike(mData.get(index).getId(),countLike[0].toString());
                    answearListViewHolder.img_like.setImageResource(R.drawable.ic_thumb_up_not_active_24dp);
                    Integer increment = Integer.parseInt(answearListViewHolder.txt_like.getText().toString())-1;
                    answearListViewHolder.txt_like.setText(String.valueOf(increment));
                    answearListViewHolder.img_dislike.setEnabled(true);
                    countLike[0] =1;
                }

            }
        });

        answearListViewHolder.img_dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countDislike[0] == 1){
                    updateDislike(mData.get(index).getId(),countDislike[0].toString());
                    answearListViewHolder.img_dislike.setImageResource(R.drawable.ic_thumb_down_red_24dp);
                    Integer increment = Integer.parseInt(mData.get(index).getDislikes())+1;
                    answearListViewHolder.txt_dislike.setText(String.valueOf(increment));
                    answearListViewHolder.img_like.setEnabled(false);
                    countDislike[0] = 0;
                }else{
                    updateDislike(mData.get(index).getId(),countDislike[0].toString());
                    answearListViewHolder.img_dislike.setImageResource(R.drawable.ic_thumb_down_not_active_24dp);
                    Integer increment = Integer.parseInt(answearListViewHolder.txt_dislike.getText().toString())-1;
                    answearListViewHolder.txt_dislike.setText(String.valueOf(increment));
                    answearListViewHolder.img_like.setEnabled(true);
                    countDislike[0] = 1;
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class AnswearListViewHolder extends RecyclerView.ViewHolder {

        TextView txt_name,txt_desc,txt_date,txt_like,txt_dislike;
        ImageView img_like,img_dislike;

        public AnswearListViewHolder(@NonNull View itemView) {
            super(itemView);

            img_like    = (ImageView) itemView.findViewById(R.id.img_like);
            img_dislike = (ImageView) itemView.findViewById(R.id.img_dislike);
            txt_name    = (TextView) itemView.findViewById(R.id.txt_user_comment);
            txt_desc    = (TextView) itemView.findViewById(R.id.txt_desc_comment);
            txt_date    = (TextView) itemView.findViewById(R.id.txt_date_comment);
            txt_like    = (TextView) itemView.findViewById(R.id.txt_like);
            txt_dislike = (TextView) itemView.findViewById(R.id.txt_dislike);
        }
    }

    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;
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

    private void updateLike(final String id, final String request){
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseAnswer> mService = mApiService.updateAnswerLike(id,request);
        mService.enqueue(new Callback<ResponseAnswer>() {
            @Override
            public void onResponse(Call<ResponseAnswer> call, Response<ResponseAnswer> response) {
                if(response.isSuccessful()){
                    Log.d("message","success");
                }else {
                    Log.d("message",response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseAnswer> call, Throwable t) {
                Log.d("message",t.getMessage().toString());
            }
        });
    }

    private void updateDislike(final String id,final String request){
        APIInterface mApiService = this.getInterfaceService();
        Call<ResponseAnswer> mService = mApiService.updateAnswerDislike(id,request);
        mService.enqueue(new Callback<ResponseAnswer>() {
            @Override
            public void onResponse(Call<ResponseAnswer> call, Response<ResponseAnswer> response) {
                if(response.isSuccessful()){
                    Log.d("message","success");
                }else {
                    Log.d("message",response.errorBody().toString());
                }

            }

            @Override
            public void onFailure(Call<ResponseAnswer> call, Throwable t) {
                Log.d("message",t.getMessage().toString());
            }
        });
    }
}
