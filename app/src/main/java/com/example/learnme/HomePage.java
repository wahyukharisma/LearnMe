package com.example.learnme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.learnme.Fragment.FragmentHome;
import com.example.learnme.Fragment.FragmentReward;
import com.example.learnme.Fragment.FragmentShop;
import com.example.learnme.Model.User;

import java.io.Serializable;

public class HomePage extends AppCompatActivity {

    private String id_user = "";
    private Fragment myFragment;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //get preference language
//        final SharedPreferences pref       = getApplicationContext().getSharedPreferences("myPrefsLanguage",MODE_PRIVATE);
//        String getPref = pref.getString("language","English");
//        if(getPref.equals("Indonesia")){
            menu.findItem(R.id.action_home).setTitle("Awal");
            menu.findItem(R.id.question).setTitle("Bertanya");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        //testing bundle
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs2", MODE_PRIVATE);
        id_user = pref.getString("id", "");

        invalidateOptionsMenu();


        // homepage ini
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        myFragment = new FragmentHome();

        Bundle bundle = new Bundle();
        bundle.putString("user", id_user);

        myFragment = new FragmentHome();
        myFragment.setArguments(bundle);
        FragmentTransaction(myFragment);

        //Listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        Bundle bundle = new Bundle();
                        bundle.putString("user", id_user);

                        myFragment = new FragmentHome();
                        myFragment.setArguments(bundle);
                        FragmentTransaction(myFragment);
                        break;
                    case R.id.action_reward:
                        Bundle bundleReward = new Bundle();
                        bundleReward.putString("user", id_user);

                        myFragment = new FragmentReward();
                        myFragment.setArguments(bundleReward);
                        FragmentTransaction(myFragment);
                        break;
                    case R.id.action_shop:
                        Bundle bundleShop = new Bundle();
                        bundleShop.putString("user", id_user);

                        myFragment = new FragmentShop();
                        myFragment.setArguments(bundleShop);
                        FragmentTransaction(myFragment);
                        break;
                }
                return true;
            }
        });


    }


    private void FragmentTransaction(Fragment myFragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_place_holder, myFragment);
        fragmentTransaction.commit();
    }
}
