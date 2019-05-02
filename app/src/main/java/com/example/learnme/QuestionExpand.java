package com.example.learnme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.learnme.Adapter.AnswearListAdapter;
import com.example.learnme.Adapter.QuestionListAdapter;
import com.example.learnme.Model.Answear;

import java.util.ArrayList;
import java.util.List;

public class QuestionExpand extends AppCompatActivity {

    List<Answear> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_expand);

        Intent intent = getIntent();

        // initialize recycle view item trending
        mData = new ArrayList<>();
        addData(mData);

        // recycle view answear listener
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view_answear);
        AnswearListAdapter myAdapter   = new AnswearListAdapter(this.getApplicationContext(),mData);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);
    }

    private void addData(List<Answear> mData){
        for(int i=0;i<5;i++){
            mData.add(new Answear("Wahyu Kharisma","Lorem Ipsum Dolor Sit Amet, Solasido Doremi Fasolasido Lumpur Lapindo, Proyek Batubara, Lorem Ipsum Dolor Sit Amet, Solasido Doremi Fasolasido Lumpur Lapindo, Proyek Batubara","1 May 2019",2,1));
        }
    }
}
