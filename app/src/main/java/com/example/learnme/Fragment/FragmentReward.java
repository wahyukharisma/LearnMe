package com.example.learnme.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.learnme.Adapter.RewardAdapter;
import com.example.learnme.Model.User;
import com.example.learnme.R;

import java.util.ArrayList;
import java.util.Calendar;

public class FragmentReward extends Fragment {

    private RecyclerView recyclerView;
    private RewardAdapter rewardAdapter;
    private ArrayList<User> userArrayList;
    Spinner spinner_month,spinner_year;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reward,container,false);

        //View ini
        int year = Calendar.getInstance().get(Calendar.YEAR);
        spinner_month = (Spinner) view.findViewById(R.id.spin_month);
        ArrayList<String> array_year = new ArrayList<>();
        spinner_year  = (Spinner) view.findViewById(R.id.spin_year);

        // recycle view ini
        addData();
        recyclerView  = (RecyclerView) view.findViewById(R.id.recycle_view_user);
        rewardAdapter = new RewardAdapter(userArrayList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(rewardAdapter);

        //spinner ini
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),R.array.month,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_month.setAdapter(adapter);

        for(int i=2019;i<=year;i++){
            array_year.add(Integer.toString(i));
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this.getActivity(),R.layout.support_simple_spinner_dropdown_item,array_year);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_year.setAdapter(adapter1);

        return view;
    }

    private void addData(){
        userArrayList = new ArrayList<>();
        // sample
        for(int i=0;i<20;i++){
            userArrayList.add(new User("Wahyu Kharisma","100","100","10","10","10","10","test"));
        }
    }

}
