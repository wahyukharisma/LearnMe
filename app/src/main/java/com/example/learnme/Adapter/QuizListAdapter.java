package com.example.learnme.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learnme.Model.Quiz;
import com.example.learnme.QuizExpand;
import com.example.learnme.R;

import java.util.List;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.QuizListViewHolder> {

    private Context mContex;
    private List<Quiz> mData;
    private String id;
    private String request;

    public QuizListAdapter(Context mContex, List<Quiz> mData, String id,String request) {
        this.mContex = mContex;
        this.mData = mData;
        this.id = id;
        this.request = request;
    }

    @NonNull
    @Override
    public QuizListAdapter.QuizListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContex);
        View view                     = layoutInflater.inflate(R.layout.list_quiz,viewGroup,false);

        return new QuizListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizListAdapter.QuizListViewHolder quizListViewHolder, int i) {
        quizListViewHolder.txt_point.setText(mData.get(i).getPoint());
        quizListViewHolder.tx_title.setText(mData.get(i).getTitle());

        if(request.equals("1")){
            quizListViewHolder.txt_status_quiz.setText("Free Quiz");
        }else{
            quizListViewHolder.txt_status_quiz.setText("Premium Quiz");
        }

        final Integer index = i;
        quizListViewHolder.item_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContex, QuizExpand.class);
                intent.putExtra("id",mData.get(index).getId());
                intent.putExtra("title",mData.get(index).getTitle());
                intent.putExtra("description",mData.get(index).getDescription());
                intent.putExtra("point",mData.get(index).getPoint());
                intent.putExtra("attempt",mData.get(index).getAttempt());
                intent.putExtra("id_user",id);
                intent.putExtra("request",request);
                mContex.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class QuizListViewHolder extends RecyclerView.ViewHolder {

        private TextView tx_title,txt_point,txt_status_quiz;
        private LinearLayout item_quiz;


        public QuizListViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_status_quiz = (TextView) itemView.findViewById(R.id.txt_status_quiz);
            tx_title = (TextView) itemView.findViewById(R.id.txt_title_quiz_list);
            txt_point = (TextView) itemView.findViewById(R.id.txt_quiz_point);
            item_quiz = (LinearLayout) itemView.findViewById(R.id.item_quiz);
        }
    }
}
