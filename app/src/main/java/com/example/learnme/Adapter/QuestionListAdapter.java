package com.example.learnme.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnme.Model.Question;
import com.example.learnme.QuestionActivity;
import com.example.learnme.QuestionExpand;
import com.example.learnme.R;

import java.util.List;

public class QuestionListAdapter extends RecyclerView.Adapter<QuestionListAdapter.QuestionViewHolder> {

    private List<Question> mList;
    private Context mContext;

    public QuestionListAdapter(List<Question> mList, Context context) {
        this.mList = mList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view                     = mInflater.inflate(R.layout.list_item_question,viewGroup,false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder questionViewHolder, final int i) {

        questionViewHolder.txt_title.setText(mList.get(i).getTitle());
        questionViewHolder.txt_description.setText(mList.get(i).getDescription());
        questionViewHolder.txt_date.setText(mList.get(i).getDate());
        questionViewHolder.txt_like.setText(Integer.toString(mList.get(i).getLike()));
        questionViewHolder.txt_comment.setText(Integer.toString(mList.get(i).getComment()));

        questionViewHolder.item_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, QuestionExpand.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Title",mList.get(i).getTitle());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {

        TextView txt_title,txt_description,txt_date,txt_like,txt_comment;
        CardView item_question;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);

            item_question   = (CardView) itemView.findViewById(R.id.item_question);
            txt_title       = (TextView) itemView.findViewById(R.id.txt_title_question);
            txt_description = (TextView) itemView.findViewById(R.id.txt_desc_question);
            txt_date        = (TextView) itemView.findViewById(R.id.txt_date_question);
            txt_like        = (TextView) itemView.findViewById(R.id.txt_like);
            txt_comment     = (TextView) itemView.findViewById(R.id.txt_comment);
        }
    }
}
