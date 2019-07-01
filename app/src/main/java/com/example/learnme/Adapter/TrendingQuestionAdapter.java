package com.example.learnme.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnme.Model.Trending;
import com.example.learnme.QuestionExpand;
import com.example.learnme.R;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class TrendingQuestionAdapter extends RecyclerView.Adapter<TrendingQuestionAdapter.TrendingQuestionViewHolder> {

    private Context mContext;
    private String id_user;
    private List<Trending> dataList;

    public TrendingQuestionAdapter(Context mContext, List<Trending> dataList, String id_user) {
        this.mContext = mContext;
        this.dataList = dataList;
        this.id_user = id_user;
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
    public void onBindViewHolder(@NonNull final TrendingQuestionViewHolder trendingQuestionViewHolder, final int i) {

        //get preference language
        final SharedPreferences pref       = mContext.getSharedPreferences("myPrefsLanguage",MODE_PRIVATE);
        String getPref = pref.getString("language","English");

        trendingQuestionViewHolder.txt_title.setText(dataList.get(i).getTitle());
        trendingQuestionViewHolder.txt_desc.setText(dataList.get(i).getDescription());
        trendingQuestionViewHolder.txt_comment.setText(Integer.toString(dataList.get(i).getComment()));
        trendingQuestionViewHolder.txt_like.setText(Integer.toString(dataList.get(i).getLike()));
        trendingQuestionViewHolder.txt_dislike.setText(Integer.toString(dataList.get(i).getDislike()));

        if (getPref.equals("Indonesia")){
            trendingQuestionViewHolder.txt_comment_trending.setText("Komentar");
        }

        trendingQuestionViewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, QuestionExpand.class);
                intent.putExtra("id",String.valueOf(dataList.get(i).getId()));
                intent.putExtra("id_user",id_user);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class TrendingQuestionViewHolder extends RecyclerView.ViewHolder {

        TextView txt_title, txt_desc, txt_comment, txt_like, txt_dislike,txt_comment_trending;
        CardView item;

        public TrendingQuestionViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_title   = (TextView) itemView.findViewById(R.id.txt_title_trend);
            txt_desc    = (TextView) itemView.findViewById(R.id.txt_desc_trend);
            txt_comment = (TextView) itemView.findViewById(R.id.txt_comment_trend);
            txt_like    = (TextView) itemView.findViewById(R.id.txt_like_trend);
            txt_dislike = (TextView) itemView.findViewById(R.id.txt_dislike_trend);
            item        = (CardView) itemView.findViewById(R.id.cv_trending);
            txt_comment_trending = (TextView) itemView.findViewById(R.id.txt_comment_trending);
        }
    }
}
