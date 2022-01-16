package com.aatesting.bugtracker.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.dialogs.Dialogs;
import com.aatesting.bugtracker.fragments.DashboardFragment;
import com.aatesting.bugtracker.fragments.ProjectSettingsFragment;
import com.aatesting.bugtracker.fragments.ProjectsFragment;
import com.aatesting.bugtracker.fragments.RoadmapFragment;
import com.aatesting.bugtracker.modifiedClasses.ModifiedFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class ProjectsMainActivity extends AppCompatActivity {
    private String projectName;
    private ProjectsMainActivity projectsMainActivity;
    private Context context;

    public View bottomBar;
    public View mainBtn;
    public ModifiedFragment thisFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_main);

        projectName = getIntent().getExtras().getString("projectName");
        projectsMainActivity = this;
        context = this;

        mainBtn = findViewById(R.id.mainBtn);
        bottomBar = findViewById(R.id.bottomAppBar);

        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
        bottomBar.setBackground(null);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_dashboard,  R.id.navigation_roadmap, R.id.navigation_project_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navView, navController);

        mainBtn.setVisibility(View.GONE);
        bottomBar.setVisibility(View.GONE);
    }

    public void listeners(View view, int fragment, FragmentManager fragmentManager){ //NOTE this is called from the fragments
        ImageButton backBtn = view.findViewById(R.id.backBtn);
        ImageButton addBtn = view.findViewById(R.id.addBtn);
        TextView boardText = view.findViewById(R.id.boardText);
        TextView roadmapText = view.findViewById(R.id.roadmapText);
        TextView settingsText = view.findViewById(R.id.settingsText);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment == 0)
                    Dialogs.NewColumnDialog(context, "Add column", "ADD",
                            "Cancel", thisFragment);
                else if (fragment == 1){
                    Intent intent = new Intent(ProjectsMainActivity.this, RoadmapCreateEpicActivity.class);
                    intent.putExtra("projectName", projectName);
                    startActivity(intent);
                }
                else
                {
                    Message.defErrMessage(context);
                    Log.wtf("ERROR", "invalid fragment selected. the + create btn on top bar won't work, fragment selected is " + fragment);
                }
            }
        });

        if (fragment != 0)
            boardText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    DashboardFragment dashboardFragment = new DashboardFragment();
                    dashboardFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.navHostFragment, dashboardFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        if (fragment != 1){
            roadmapText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    RoadmapFragment roadmapFragment = new RoadmapFragment();
                    roadmapFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.navHostFragment, roadmapFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        }
        if (fragment != 2){
            settingsText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    ProjectSettingsFragment projectSettingsFragment = new ProjectSettingsFragment();
                    projectSettingsFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.navHostFragment, projectSettingsFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        }
    }

    public void oldListeners(int fragmentSelected){ //NOTE this is called from the fragments
        if (fragmentSelected == 0) {
            mainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialogs.NewColumnDialog(context, "Add column", "ADD",
                            "Cancel", thisFragment);
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