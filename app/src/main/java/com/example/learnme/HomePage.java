package com.example.learnme;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.learnme.Fragment.FragmentHome;
import com.example.learnme.Fragment.FragmentReward;
import com.example.learnme.Fragment.FragmentShop;

public class HomePage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // homepage ini
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        FragmentTransaction(1);

        //Listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.action_home:
                        FragmentTransaction(1);
                        break;
                    case R.id.action_reward:
                        FragmentTransaction(2);
                        break;
                    case R.id.action_shop:
                        FragmentTransaction(3);
                        break;
                }
                return true;
            }
        });
    }

    private void FragmentTransaction(int id){
        FragmentTransaction fragmentTransaction   = getSupportFragmentManager().beginTransaction();

        if(id==1){fragmentTransaction.replace(R.id.fragment_place_holder,new FragmentHome());}
        else if(id==2){fragmentTransaction.replace(R.id.fragment_place_holder,new FragmentReward());}
        else{fragmentTransaction.replace(R.id.fragment_place_holder,new FragmentShop());};

        fragmentTransaction.commit();
    }
}
