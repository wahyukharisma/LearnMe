package com.example.learnme.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.learnme.Model.Point;
import com.example.learnme.R;

import java.util.List;

public class PointHistoryAdapter extends RecyclerView.Adapter<PointHistoryAdapter.PointHistoryViewHolder> {

    Context mContext;
    List<Point> mPoint;

    public PointHistoryAdapter(Context mContext, List<Point> mPoint) {
        this.mContext = mContext;
        this.mPoint = mPoint;
    }

    @NonNull
    @Override
    public PointHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view                     = mInflater.inflate(R.layout.list_item_point,viewGroup,false);
        return new PointHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PointHistoryViewHolder pointHistoryViewHolder, int i) {
        pointHistoryViewHolder.txt_point.setText(mPoint.get(i).getPonit());
        pointHistoryViewHolder.txt_description.setText(mPoint.get(i).getDescription());
        pointHistoryViewHolder.txt_date.setText(mPoint.get(i).getDate());
    }

    @Override
    public int getItemCount() {
        return mPoint.size();
    }

    public class PointHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView txt_point,txt_description,txt_date;
        public PointHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_point = (TextView) itemView.findViewById(R.id.txt_point);
            txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            txt_description = (TextView) itemView.findViewById(R.id.txt_description);
        }
    }
}
