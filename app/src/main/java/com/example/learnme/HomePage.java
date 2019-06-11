package com.example.learnme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.learnme.Fragment.FragmentHome;
import com.example.learnme.Fragment.FragmentReward;
import com.example.learnme.Fragment.FragmentShop;
import com.example.learnme.Model.User;

import java.io.Serializable;

public class HomePage extends AppCompatActivity {

    private String id_user="";

    private Fragment myFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //testing bundle
        SharedPreferences pref       = getApplicationContext().getSharedPreferences("myPrefs2",MODE_PRIVATE);
        id_user = pref.getString("id","");

        // homepage ini
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        myFragment = new FragmentHome();

        Bundle bundle = new Bundle();
        bundle.putString("user",id_user);

        myFragment = new FragmentHome();
        myFragment.setArguments(bundle);
        FragmentTransaction(myFragment);

        //Listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_home:
                        Bundle bundle = new Bundle();
                        bundle.putString("user",id_user);

                        myFragment = new FragmentHome();
                        myFragment.setArguments(bundle);
                        FragmentTransaction(myFragment);
                        break;
                    case R.id.action_reward:
                        myFragment = new FragmentReward();
                        FragmentTransaction(myFragment);
                        break;
                    case R.id.action_shop:
                        myFragment = new FragmentShop();
                        FragmentTransaction(myFragment);
                        break;
                }
                return true;
            }
        });
    }

    private void FragmentTransaction(Fragment myFragment){
        FragmentTransaction fragmentTransaction   = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place_holder,myFragment);
        fragmentTransaction.commit();
    }

}
