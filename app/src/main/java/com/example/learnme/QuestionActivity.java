package com.example.learnme;

import android.annotation.SuppressLint;
import android.support.design.internal.NavigationMenu;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.learnme.Adapter.QuestionListAdapter;
import com.example.learnme.Model.Question;

import java.util.ArrayList;
import java.util.List;

import io.github.yavski.fabspeeddial.FabSpeedDial;

public class QuestionActivity extends AppCompatActivity {

    List<Question> mData;
    ImageView btn_close;
    RelativeLayout semi_transparent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        //View ini
        btn_close = (ImageView) findViewById(R.id.btn_close_question);
        final FabSpeedDial floating_menu = (FabSpeedDial) findViewById(R.id.floating_menu_question);
        semi_transparent = (RelativeLayout) findViewById(R.id.semi_white_bg_question);

        // initialize recycle view item trending
        mData = new ArrayList<>();
        addData(mData);

        // recycle view trending listener
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view_question);
        QuestionListAdapter myAdapter   = new QuestionListAdapter(mData,this.getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(myAdapter);

        // listener
        floating_menu.setMenuListener(new FabSpeedDial.MenuListener() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                semi_transparent.setVisibility(View.VISIBLE);
                return true;
            }

            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.question:
                        Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_SHORT).show();
                }
                semi_transparent.setVisibility(View.GONE);
                return false;
            }

            @Override
            public void onMenuClosed() {
                semi_transparent.setVisibility(View.GONE);
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addData(List<Question> mData){
        for(int i=0;i<20;i++){
            this.mData.add(new Question("Animal who ovovivivar","Lorem Ipsum Dolor","1 May 2019",i+20,5,i+5));
        }
    }
}
