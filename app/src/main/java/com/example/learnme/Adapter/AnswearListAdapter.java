package com.example.learnme.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.learnme.Model.Answear;

import java.util.List;

public class AnswearListAdapter extends RecyclerView.Adapter<AnswearListAdapter.AnswearListViewHolder> {

    List<Answear> mData;

    @NonNull
    @Override
    public AnswearListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AnswearListViewHolder answearListViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class AnswearListViewHolder extends RecyclerView.ViewHolder {
        public AnswearListViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
