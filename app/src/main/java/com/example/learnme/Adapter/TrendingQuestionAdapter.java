package com.example.learnme.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnme.Model.Trending;
import com.example.learnme.R;

import java.util.List;

public class TrendingQuestionAdapter extends RecyclerView.Adapter<TrendingQuestionAdapter.TrendingQuestionViewHolder> {

    private Context mContext;
    private List<Trending> dataList;

    public TrendingQuestionAdapter(Context mContext, List<Trending> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public TrendingQuestionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view                     = mInflater.inflate(R.layout.list_item_trendings,viewGroup,false);
        return new TrendingQuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingQuestionViewHolder trendingQuestionViewHolder, final int i) {
        trendingQuestionViewHolder.txt_title.setText(dataList.get(i).getTitle());
        trendingQuestionViewHolder.txt_desc.setText(dataList.get(i).getDescription());
        trendingQuestionViewHolder.txt_comment.setText(Integer.toString(dataList.get(i).getComment()));
        trendingQuestionViewHolder.txt_like.setText(Integer.toString(dataList.get(i).getLike()));
        trendingQuestionViewHolder.txt_dislike.setText(Integer.toString(dataList.get(i).getDislike()));

        trendingQuestionViewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("clicked",Integer.toString(dataList.get(i).getId()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class TrendingQuestionViewHolder extends RecyclerView.ViewHolder {

        TextView txt_title, txt_desc, txt_comment, txt_like, txt_dislike;
        CardView item;

        public TrendingQuestionViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_title   = (TextView) itemView.findViewById(R.id.txt_title_trend);
            txt_desc    = (TextView) itemView.findViewById(R.id.txt_desc_trend);
            txt_comment = (TextView) itemView.findViewById(R.id.txt_comment_trend);
            txt_like    = (TextView) itemView.findViewById(R.id.txt_like_trend);
            txt_dislike = (TextView) itemView.findViewById(R.id.txt_dislike_trend);
            item        = (CardView) itemView.findViewById(R.id.cv_trending);
        }
    }
}
