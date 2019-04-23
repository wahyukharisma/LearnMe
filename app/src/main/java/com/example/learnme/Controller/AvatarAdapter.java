package com.example.learnme.Controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.learnme.Model.Item;
import com.example.learnme.R;

import java.util.ArrayList;
import java.util.List;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder> {

    private Context mContext;
    private List<Item> dataList;

    public AvatarAdapter(Context mContext, List<Item> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
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
    public void onBindViewHolder(@NonNull AvatarViewHolder avatarViewHolder, int i) {
        avatarViewHolder.txt_name.setText(dataList.get(i).getName());
        avatarViewHolder.txt_price.setText(Integer.toString(dataList.get(i).getPrice()));
        avatarViewHolder.img_item.setImageResource(dataList.get(i).getId());

        //listener
        avatarViewHolder.avatar_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
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
}
