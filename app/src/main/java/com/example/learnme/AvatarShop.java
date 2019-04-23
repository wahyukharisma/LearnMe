package com.example.learnme;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.learnme.Controller.AvatarAdapter;
import com.example.learnme.Fragment.FragmentHome;
import com.example.learnme.Model.Item;

import java.util.ArrayList;
import java.util.List;

public class AvatarShop extends AppCompatActivity {

    List<Item> listItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar_shop);

        // avatar shop ini
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavShop);

        // initialize recycle view item
        listItem = new ArrayList<>();
        addItem(listItem);

        // recycle view listener
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle_view_avatar);
        AvatarAdapter myAdapter   = new AvatarAdapter(this,listItem);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(myAdapter);



        // listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_filter:
                        //Toast.makeText(getApplicationContext(),"Action Filter",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_sort:
                        //Toast.makeText(getApplicationContext(),"Action Sort",Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

    }

    private void addItem(List<Item> listItem){
        for(int i=0;i<20;i++){
            listItem.add(new Item(R.drawable.avatar_banner,200,"Name","Description"));
        }

    }
}
