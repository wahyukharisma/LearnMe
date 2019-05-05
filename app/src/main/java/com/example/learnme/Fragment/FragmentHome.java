package com.example.learnme.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenu;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.learnme.Adapter.TrendingQuestionAdapter;
import com.example.learnme.AskQuestion;
import com.example.learnme.Model.Trending;
import com.example.learnme.QuestionActivity;
import com.example.learnme.R;

import java.util.ArrayList;
import java.util.List;

import io.github.yavski.fabspeeddial.FabSpeedDial;

public class FragmentHome extends Fragment {

    List<Trending> listTrending;
    CardView cd_es,cd_jhs,cd_shs,cd_g;
    Button btn_search;
    RelativeLayout semi_transparent;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_home,container,false);

        // View ini
        btn_search = (Button) view.findViewById(R.id.btn_search_hp);
        final FabSpeedDial floating_menu = (FabSpeedDial) view.findViewById(R.id.floating_menu);
        semi_transparent = (RelativeLayout) view.findViewById(R.id.semi_white_bg);

        // initialize recycle view item trending
        listTrending = new ArrayList<>();
        addTrending(listTrending);

        // cardview ini
        cd_es  = (CardView) view.findViewById(R.id.cd_es);
        cd_jhs = (CardView) view.findViewById(R.id.cd_jhs);
        cd_shs = (CardView) view.findViewById(R.id.cd_shs);
        cd_g   = (CardView) view.findViewById(R.id.cd_g);

        // recycle view trending listener
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_trending_question);
        TrendingQuestionAdapter myAdapter   = new TrendingQuestionAdapter(getContext(),listTrending);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);

        // listener
        cd_es.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), QuestionActivity.class);
                intent.putExtra("Value",1);
                startActivity(intent);
            }
        });

        cd_jhs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), QuestionActivity.class);
                intent.putExtra("Value",1);
                startActivity(intent);
            }
        });

        cd_shs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), QuestionActivity.class);
                intent.putExtra("Value",2);
                startActivity(intent);
            }
        });

        cd_g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), QuestionActivity.class);
                intent.putExtra("Value",3);
                startActivity(intent);
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), QuestionActivity.class);
                intent.putExtra("Value",4);
                startActivity(intent);
            }
        });

        floating_menu.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                semi_transparent.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.quiz:
                        Toast.makeText(getContext(),"Quiz clicked !!!",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.question:
                        Intent intent = new Intent(getContext(), AskQuestion.class);
                        startActivity(intent);
                        break;
                }
                semi_transparent.setVisibility(View.GONE);
                return false;
            }

            @Override
            public void onMenuClosed() {
                semi_transparent.setVisibility(View.GONE);
            }
        });


        return view;
    }

    private void addTrending(List<Trending> listTrending){
        for(int i=0;i<5;i++){
            this.listTrending.add(new Trending("Title","Description",20+i,i,10+i));
        }
    }
}
