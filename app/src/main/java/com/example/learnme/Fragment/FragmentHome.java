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
import android.widget.ProgressBar;

import com.example.learnme.Controller.TrendingQuestionAdapter;
import com.example.learnme.Model.Trending;
import com.example.learnme.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.github.ybq.android.spinkit.style.Wave;

import java.util.ArrayList;
import java.util.List;

public class FragmentHome extends Fragment {

    List<Trending> listTrending;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home,container,false);

        // initialize recycle view item trending
        listTrending = new ArrayList<>();
        addTrending(listTrending);

        // recycle view trending listener
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_trending_question);
        TrendingQuestionAdapter myAdapter   = new TrendingQuestionAdapter(getContext(),listTrending);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);


        return view;
    }

    private void addTrending(List<Trending> listTrending){
        for(int i=0;i<5;i++){
            this.listTrending.add(new Trending("Title","Description",20+i,i,10+i));
        }
    }
}
