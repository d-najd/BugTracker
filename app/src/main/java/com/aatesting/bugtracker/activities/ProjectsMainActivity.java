package com.aatesting.bugtracker.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
        ImageButton moreBtn = view.findViewById(R.id.moreVerticalBtn);
        TextView boardText = view.findViewById(R.id.boardText);
        TextView roadmapText = view.findViewById(R.id.roadmapText);
        TextView settingsText = view.findViewById(R.id.settingsText);

        topbarSelected(view, fragment, boardText, roadmapText, settingsText);

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
                else if (fragment == 2) {}
                else
                {
                    Message.defErrMessage(context);
                    Log.wtf("ERROR", "invalid fragment selected. the + create btn on top bar won't work, fragment selected is " + fragment);
                }
            }
        });

        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment == 0 || fragment == 1) {}
                else if (fragment == 2){
                    Message.message(context, "add radioGroup with the items add and view users in project");
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
                    fragmentTransaction.disallowAddToBackStack();
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
                    fragmentTransaction.disallowAddToBackStack();
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
                    fragmentTransaction.disallowAddToBackStack();
                    fragmentTransaction.commit();
                }
            });
        }
    }

    private void topbarSelected(View view, int fragment, TextView boardText, TextView roadmapText, TextView settingsText) {
        View boardTextUnderline = view.findViewById(R.id.boardTextUnderline);
        View roadmapTextUnderline = view.findViewById(R.id.roadmapTextUnderline);
        View settingsTextUnderline = view.findViewById(R.id.settingsTextUnderline);

        if (fragment == 0)
        {
            boardText.setTextColor(getResources().getColor(R.color.purple_200, getTheme()));
            roadmapText.setTextColor(getResources().getColor(R.color.white60, getTheme()));
            settingsText.setTextColor(getResources().getColor(R.color.white60, getTheme()));

            boardTextUnderline.setVisibility(View.VISIBLE);
            roadmapTextUnderline.setVisibility(View.GONE);
            settingsTextUnderline.setVisibility(View.GONE);
        } else if (fragment == 1){
            boardText.setTextColor(getResources().getColor(R.color.white60, getTheme()));
            roadmapText.setTextColor(getResources().getColor(R.color.purple_200, getTheme()));
            settingsText.setTextColor(getResources().getColor(R.color.white60, getTheme()));

            boardTextUnderline.setVisibility(View.GONE);
            roadmapTextUnderline.setVisibility(View.VISIBLE);
            settingsTextUnderline.setVisibility(View.GONE);
        } else if (fragment == 2){
            boardText.setTextColor(getResources().getColor(R.color.white60, getTheme()));
            roadmapText.setTextColor(getResources().getColor(R.color.white60, getTheme()));
            settingsText.setTextColor(getResources().getColor(R.color.purple_200, getTheme()));

            boardTextUnderline.setVisibility(View.GONE);
            roadmapTextUnderline.setVisibility(View.GONE);
            settingsTextUnderline.setVisibility(View.VISIBLE);
        }
    }
}