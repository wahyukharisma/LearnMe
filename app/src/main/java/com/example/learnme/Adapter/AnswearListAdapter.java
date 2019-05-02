package com.example.learnme.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.learnme.Model.Answear;
import com.example.learnme.R;

import java.util.List;

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
    public void onBindViewHolder(@NonNull AnswearListViewHolder answearListViewHolder, int i) {

        answearListViewHolder.txt_desc.setText(mData.get(i).getAnswear());
        answearListViewHolder.txt_name.setText(mData.get(i).getName());
        answearListViewHolder.txt_dislike.setText(Integer.toString(mData.get(i).getDislike()));
        answearListViewHolder.txt_like.setText(Integer.toString(mData.get(i).getLike()));
        answearListViewHolder.txt_date.setText(mData.get(i).getDate());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class AnswearListViewHolder extends RecyclerView.ViewHolder {

        TextView txt_name,txt_desc,txt_date,txt_like,txt_dislike;

        public AnswearListViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_name    = (TextView) itemView.findViewById(R.id.txt_user_comment);
            txt_desc    = (TextView) itemView.findViewById(R.id.txt_desc_comment);
            txt_date    = (TextView) itemView.findViewById(R.id.txt_date_comment);
            txt_like    = (TextView) itemView.findViewById(R.id.txt_like);
            txt_dislike = (TextView) itemView.findViewById(R.id.txt_dislike);
        }
    }
}
