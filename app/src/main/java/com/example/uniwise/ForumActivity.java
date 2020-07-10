package com.example.uniwise;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ForumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListner);
        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1, new ForumFragment()).commit();
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListner = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()){
                case R.id.bforum:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,new ForumFragment()).commit();
                    break;
                case R.id.bhome:
                    Intent intent=new Intent(getApplicationContext(),DashboardActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.badd:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,new AddButtonQue()).commit();
                    break;
                case R.id.bsearch:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,new SearchQuestionFragment()).commit();
                    break;
                case R.id.bfindf:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container1,new FindFriend()).commit();
                    break;
            }
            return true;
        }
    };

}
