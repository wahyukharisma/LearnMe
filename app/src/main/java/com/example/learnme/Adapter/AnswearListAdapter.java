package com.example.learnme.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.learnme.API.APIInterface;
import com.example.learnme.API.ResponseAnswer;
import com.example.learnme.Model.Answer;
import com.example.learnme.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class AnswearListAdapter extends RecyclerView.Adapter<AnswearListAdapter.AnswearListViewHolder> {

    Context mContext;
    List<Answer> mData;


    public AnswearListAdapter(Context mContext, List<Answer> mData) {
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
        //get preference language
        final SharedPreferences pref       = mContext.getSharedPreferences("myPrefsLanguage",MODE_PRIVATE);
        String getPref = pref.getString("language","English");

        if(getPref.equals("Indonesia")){
            answearListViewHolder.txt_comment_answer.setText("Komentar, ");
        }

        answearListViewHolder.txt_desc.setText(mData.get(i).getAnswer());
        answearListViewHolder.txt_name.setText(mData.get(i).getIdUser());
        answearListViewHolder.txt_dislike.setText(mData.get(i).getDislikes());
        answearListViewHolder.txt_like.setText(mData.get(i).getLikes());
        answearListViewHolder.txt_date.setText(mData.get(i).getDate());
        Glide.with(mContext.getApplicationContext()).load(mData.get(i).getImage()).into(answearListViewHolder.img_profile);

        final Integer index =i;
        final Integer[] countLike = {1};
        final Integer[] countDislike = {1};
        answearListViewHolder.img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countLike[0] ==1){
                    updateLike(mData.get(index).getId(),"like");
                    answearListViewHolder.img_like.setImageResource(R.drawable.ic_thumb_up_dark_blue_24dp);
                    Integer increment = Integer.parseInt(mData.get(index).getLikes())+1;
                    answearListViewHolder.txt_like.setText(String.valueOf(increment));
                    answearListViewHolder.img_dislike.setEnabled(false);
                    countLike[0] =0;
                }else{
                    updateLike(mData.get(index).getId(),"unlike");
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
                    updateDislike(mData.get(index).getId(),"dislike");
                    answearListViewHolder.img_dislike.setImageResource(R.drawable.ic_thumb_down_red_24dp);
                    Integer increment = Integer.parseInt(mData.get(index).getDislikes())+1;
                    answearListViewHolder.txt_dislike.setText(String.valueOf(increment));
                    answearListViewHolder.img_like.setEnabled(false);
                    countDislike[0] = 0;
                }else{
                    updateDislike(mData.get(index).getId(),"undislike");
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

        TextView txt_name,txt_desc,txt_date,txt_like,txt_dislike,txt_comment_answer;
        ImageView img_like,img_dislike,img_profile;

        public AnswearListViewHolder(@NonNull View itemView) {
            super(itemView);

            img_like    = (ImageView) itemView.findViewById(R.id.img_like);
            img_dislike = (ImageView) itemView.findViewById(R.id.img_dislike);
            txt_name    = (TextView) itemView.findViewById(R.id.txt_user_comment);
            txt_desc    = (TextView) itemView.findViewById(R.id.txt_desc_comment);
            txt_date    = (TextView) itemView.findViewById(R.id.txt_date_comment);
            txt_like    = (TextView) itemView.findViewById(R.id.txt_like);
            txt_dislike = (TextView) itemView.findViewById(R.id.txt_dislike);
            img_profile = (ImageView) itemView.findViewById(R.id.img_profile_answer);
            txt_comment_answer = (TextView) itemView.findViewById(R.id.txt_comment_answer);
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
                Log.d("message",t.getMessage());
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
                Log.d("message",t.getMessage());
            }
        });
    }
}
