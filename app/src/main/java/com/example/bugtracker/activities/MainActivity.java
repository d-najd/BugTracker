package com.example.bugtracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bugtracker.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View bottomBar = findViewById(R.id.bottomAppBar);
        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
        bottomBar.setBackground(null);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_projects, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        Listeners();

    }

    private void Listeners(){
        View mainBtn = findViewById(R.id.mainBtn);

        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this, ProjectCreateActivity.class);
                startActivity(intent);
            }
        });
    }
}