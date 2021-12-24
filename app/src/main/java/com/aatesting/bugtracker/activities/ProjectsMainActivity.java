package com.aatesting.bugtracker.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.dialogs.Dialogs;
import com.aatesting.bugtracker.modifiedClasses.ModifiedFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class ProjectsMainActivity extends AppCompatActivity {

    private String projectName;
    private ProjectsMainActivity projectsMainActivity;
    private Context context;

    public ModifiedFragment thisFragment;
    private String type = "boards";

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
                            "Cancel", thisFragment, type);
                }
            });
        }
        else if (fragmentSelected == 1) {
            mainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProjectsMainActivity.this, RoadmapCreateEpicActivity.class);
                    intent.putExtra("projectName", projectName);
                    startActivity(intent);
                }
            });
        }
    }
}