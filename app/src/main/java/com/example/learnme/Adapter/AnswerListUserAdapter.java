package com.example.learnme.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.learnme.Model.Answer;
import com.example.learnme.R;

import java.util.List;

public class AnswerListUserAdapter extends RecyclerView.Adapter<AnswerListUserAdapter.AnswerListUserViewHolder> {

    Context mContext;
    List<Answer> mData;

    public AnswerListUserAdapter(Context mContext, List<Answer> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public AnswerListUserAdapter.AnswerListUserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view                     = mInflater.inflate(R.layout.list_item_answer_user,viewGroup,false);
        return new AnswerListUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerListUserAdapter.AnswerListUserViewHolder answerListUserViewHolder, int i) {
        answerListUserViewHolder.txt_desc.setText(mData.get(i).getAnswer());
        answerListUserViewHolder.txt_name.setText(mData.get(i).getIdUser());
        answerListUserViewHolder.txt_dislike.setText(mData.get(i).getDislikes());
        answerListUserViewHolder.txt_like.setText(mData.get(i).getLikes());
        answerListUserViewHolder.txt_date.setText(mData.get(i).getDate());
        answerListUserViewHolder.txt_id_question.setText(mData.get(i).getIdQuestion());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class AnswerListUserViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name,txt_desc,txt_date,txt_like,txt_dislike,txt_id_question;
        ImageView img_like,img_dislike;

        public AnswerListUserViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_id_question = (TextView) itemView.findViewById(R.id.txt_id_question);
            img_like    = (ImageView) itemView.findViewById(R.id.img_like);
            img_dislike = (ImageView) itemView.findViewById(R.id.img_dislike);
            txt_name    = (TextView) itemView.findViewById(R.id.txt_user_comment);
            txt_desc    = (TextView) itemView.findViewById(R.id.txt_desc_comment);
            txt_date    = (TextView) itemView.findViewById(R.id.txt_date_comment);
            txt_like    = (TextView) itemView.findViewById(R.id.txt_like);
            txt_dislike = (TextView) itemView.findViewById(R.id.txt_dislike);
        }
    }
}
