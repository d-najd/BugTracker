package com.aatesting.bugtracker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.aatesting.bugtracker.GlobalValues;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.dialogs.Dialogs;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class ProjectsMainActivity extends AppCompatActivity {

    public int fragmentSelected = 0; //0,3 from left to right
    private String projectName;
    private ProjectsMainActivity projectsMainActivity;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_main);

        projectName = getIntent().getExtras().getString("projectName");
        projectsMainActivity = this;
        context = this;

        View bottomBar = findViewById(R.id.bottomAppBar);
        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
        bottomBar.setBackground(null);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_dashboard,  R.id.navigation_roadmap)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void Listeners(int fragmentSelected){ //NOTE this is called from the fragments
        View mainBtn = findViewById(R.id.mainBtn);

        if (fragmentSelected == 0) {
            mainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialogs.NewColumnDialog(context, "Add column", "ADD",
                            "Cancel", projectName, projectsMainActivity, getIntent());
                }
            });
        }
        else if (fragmentSelected == 1) {
            mainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProjectsMainActivity.this, RoadmapCreateEpicActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    public void RefreshActivity(){
        //TODO this is gonna break in the future, need to check if the items are saved before doing this instead of waiting
        Handler h = new Handler() ;
        h.postDelayed(new Runnable() {
            public void run() {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        }, 250);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //stupid fix but hey it works
        boolean reloadedActivity = GlobalValues.reloadedActivity;
        if (!reloadedActivity) {
            GlobalValues.reloadedActivity = true;
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        } else
            GlobalValues.reloadedActivity = false;
    }
}