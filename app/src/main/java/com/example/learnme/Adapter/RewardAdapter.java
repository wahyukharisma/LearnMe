package com.example.learnme.Adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.learnme.Model.User;
import com.example.learnme.R;

import java.util.ArrayList;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.RewardViewHolder> {

    private ArrayList<User> dataList;

    public RewardAdapter(ArrayList<User> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RewardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view                     = layoutInflater.inflate(R.layout.list_item_reward,viewGroup,false);

        return new RewardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder rewardViewHolder, int i) {
        rewardViewHolder.txtPoint.setText(dataList.get(i).getPoint());
        rewardViewHolder.txtName.setText(dataList.get(i).getUsername());
        rewardViewHolder.txtNumber.setText(dataList.get(i).getId());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class RewardViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNumber,txtName,txtPoint;

        public RewardViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNumber = (TextView) itemView.findViewById(R.id.txt_rank);
            txtName   = (TextView) itemView.findViewById(R.id.txt_name_user);
            txtPoint  = (TextView) itemView.findViewById(R.id.txt_name_point);
        }
    }
}
