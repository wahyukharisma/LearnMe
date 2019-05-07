package com.example.learnme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.learnme.Adapter.AnswearListAdapter;
import com.example.learnme.Adapter.QuestionListAdapter;
import com.example.learnme.Model.Answear;

import java.util.ArrayList;
import java.util.List;

public class QuestionExpand extends AppCompatActivity {

    List<Answear> mData;
    ImageView btn_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_expand);

        //view ini
        btn_close = (ImageView) findViewById(R.id.btn_close_question);

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

        //Listener
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addData(List<Answear> mData){
        for(int i=0;i<5;i++){
            mData.add(new Answear("Wahyu Kharisma","Lorem Ipsum Dolor Sit Amet, Solasido Doremi Fasolasido Lumpur Lapindo, Proyek Batubara, Lorem Ipsum Dolor Sit Amet, Solasido Doremi Fasolasido Lumpur Lapindo, Proyek Batubara","1 May 2019",2,1));
        }
    }
}
