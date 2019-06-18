package com.example.learnme.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.learnme.API.APIInterface;
import com.example.learnme.API.ResponseItem;
import com.example.learnme.ItemExpand;
import com.example.learnme.Model.Item;
import com.example.learnme.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder> {
    public static final String BASE_URL = com.example.learnme.API.BASE_URL.URL;

    private Context mContext;
    private List<Item> dataList;
    private String id;

    public AvatarAdapter(Context mContext, List<Item> dataList,String id) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.id = id;
    }

    @NonNull
    @Override
    public AvatarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view                     = mInflater.inflate(R.layout.list_item_avatar,viewGroup,false);
        return new AvatarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarViewHolder avatarViewHolder, final int i) {
        avatarViewHolder.txt_name.setText(dataList.get(i).getName());
        avatarViewHolder.txt_price.setText(dataList.get(i).getPoint());
        Glide.with(mContext.getApplicationContext()).load(dataList.get(i).getImage()).into(avatarViewHolder.img_item);
        //avatarViewHolder.img_item.setImageResource(R.drawable.noimage);

        //listener
        avatarViewHolder.avatar_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIInterface mApiService = getInterfaceService();
                Call<ResponseItem> mService = mApiService.statusItem(id,dataList.get(i).getId());
                mService.enqueue(new Callback<ResponseItem>() {
                    @Override
                    public void onResponse(Call<ResponseItem> call, Response<ResponseItem> response) {
                        final Intent intent = new Intent(mContext, ItemExpand.class);
                        intent.putExtra("name",dataList.get(i).getName());
                        intent.putExtra("image",dataList.get(i).getImage());
                        intent.putExtra("price",dataList.get(i).getPoint());
                        intent.putExtra("id",dataList.get(i).getId());
                        intent.putExtra("description",dataList.get(i).getDescription());
                        intent.putExtra("id_user",id);
                        intent.putExtra("date",dataList.get(i).getValid());
                        intent.putExtra("sold",dataList.get(i).getSold());
                        intent.putExtra("status",response.body().getMessage());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<ResponseItem> call, Throwable t) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class AvatarViewHolder extends RecyclerView.ViewHolder {

        TextView txt_name, txt_price;
        ImageView img_item;
        CardView avatar_item;

        public AvatarViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_name    = (TextView) itemView.findViewById(R.id.txt_name_avatar);
            txt_price   = (TextView) itemView.findViewById(R.id.txt_price_avatar);
            img_item    = (ImageView) itemView.findViewById(R.id.img_avatar);
            avatar_item = (CardView) itemView.findViewById(R.id.avatar_item_id);
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
