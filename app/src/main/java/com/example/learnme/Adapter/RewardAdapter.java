package com.example.learnme.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.learnme.Model.Ranking;
import com.example.learnme.Model.User;
import com.example.learnme.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.RewardViewHolder> {

    private ArrayList<Ranking> dataList;
    private Context mContext;

    public RewardAdapter(ArrayList<Ranking> dataList, Context mContext) {
        this.dataList = dataList;
        this.mContext = mContext;
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

        //get preference language
        final SharedPreferences pref       = mContext.getSharedPreferences("myPrefsLanguage",MODE_PRIVATE);
        String getPref = pref.getString("language","English");

        rewardViewHolder.txtPoint.setText(dataList.get(i).getPoint());
        rewardViewHolder.txtName.setText(dataList.get(i).getUsername());
        rewardViewHolder.txtNumber.setText(String.valueOf(dataList.get(i).getIndex()));

        if(getPref.equals("Indonesia")){
            rewardViewHolder.txtPointReward.setText("Poin");
            rewardViewHolder.txtNameReward.setText("Nama");
        }
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class RewardViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNumber,txtName,txtPoint,txtNameReward,txtPointReward;

        public RewardViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNumber = (TextView) itemView.findViewById(R.id.txt_rank);
            txtName   = (TextView) itemView.findViewById(R.id.txt_name_user);
            txtPoint  = (TextView) itemView.findViewById(R.id.txt_name_point);
            txtNameReward = (TextView) itemView.findViewById(R.id.txt_name_reward);
            txtPointReward = (TextView) itemView.findViewById(R.id.txt_point_reward);
        }
    }
}
