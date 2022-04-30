package com.aatesting.bugtracker.activities.Projects;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.Roadmaps.RoadmapCreateEpicActivity;
import com.aatesting.bugtracker.dialogs.Dialogs;
import com.aatesting.bugtracker.fragments.FragmentSettings;
import com.aatesting.bugtracker.fragments.Main.DashboardFragment;
import com.aatesting.bugtracker.fragments.Main.GridFragment.GridFragment;
import com.aatesting.bugtracker.fragments.ProjectSettings.ProjectSettingsFragment;
import com.aatesting.bugtracker.fragments.Main.RoadmapFragment;
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
    private Context context;

    public View bottomBar;
    public View mainBtn;
    public ModifiedFragment thisFragment;
    private int fragmentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_main);

        try {
            projectName = getIntent().getExtras().getString("projectName");
        } catch (Exception e){
            Log.wtf("ERROR", "Failed to get data");
            Message.defErrMessage(this);
            projectName = "ERROR";
        }
        context = this;

        mainBtn = findViewById(R.id.mainBtn);
        bottomBar = findViewById(R.id.bottomAppBar);

        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
        bottomBar.setBackground(null);

        new AppBarConfiguration.Builder(
                R.id.navigation_dashboard, R.id.navigation_roadmap, R.id.navigation_project_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navView, navController);

        mainBtn.setVisibility(View.GONE);
        bottomBar.setVisibility(View.GONE);


        //DEBUGGING
        new Thread(() -> {
            try {
                Thread.sleep(100);
                Bundle bundle = new Bundle();
                GridFragment gridFragment = new GridFragment();
                gridFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = thisFragment.getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.navHostFragment, gridFragment);
                fragmentTransaction.disallowAddToBackStack();
                fragmentTransaction.commit();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void listeners(View view, int fragment, FragmentManager fragmentManager){ //NOTE this is called from the fragments
        ImageButton backBtn = view.findViewById(R.id.backBtn);
        ImageButton addBtn = view.findViewById(R.id.addBtn);
        ImageButton moreBtn = view.findViewById(R.id.moreVerticalBtn);

        TextView titleTopBar = view.findViewById(R.id.titleTopbar);

        fragmentPos = fragment;

        String projectNameFirstUppercase = projectName.substring(0, 1).toUpperCase() + projectName.substring(1);

        if (titleTopBar != null)
            titleTopBar.setText(projectNameFirstUppercase);

        topbarSelected(view, fragment, fragmentManager);

        backBtn.setOnClickListener(v -> {
            if (
                    fragment == FragmentSettings.AUTH_FRAGMENT_ID
                    || fragment == FragmentSettings.USERS_FRAGMENT_ID
                    || fragment == FragmentSettings.MANAGE_USERS_FRAGMENT_ID
                    || fragment == FragmentSettings.GRID_FRAGMENT_ID
            ){
                swapToSettingsFragment(fragmentManager);
            }
            else
                finish();
        });

        addBtn.setOnClickListener(v -> {
            if (fragment == FragmentSettings.DASHBOARD_FRAGMENT_ID)
                Dialogs.NewColumnDialog(context, "Add column", "ADD",
                        "Cancel", thisFragment);
            else if (fragment == FragmentSettings.ROADMAP_FRAGMENT_ID){
                Intent intent = new Intent(ProjectsMainActivity.this, RoadmapCreateEpicActivity.class);
                intent.putExtra("projectName", projectName);
                startActivity(intent);
            }
            else {
                Message.defErrMessage(context);
                Log.wtf("ERROR", "invalid fragment selected. the + create btn on top bar won't work, fragment selected is " + fragment);
            }
        });

        moreBtn.setOnClickListener(v -> {
            if (
                    fragment == FragmentSettings.DASHBOARD_FRAGMENT_ID
                    || fragment == FragmentSettings.ROADMAP_FRAGMENT_ID
            ) {}
            else {
                Message.defErrMessage(context);
                Log.wtf("ERROR", "invalid fragment selected. the + create btn on top bar won't work, fragment selected is " + fragment);
            }
        });
    }

    /**
     * activate fragment in the topbar and deactivate if any other has been activated
     * @param fragment the fragment we want to activate
     * @apiNote only visuals and prevention of selecting the same fragment multiple times is done here
     */
    private void topbarSelected(View view, int fragment, FragmentManager fragmentManager) {
        TextView boardText = view.findViewById(R.id.boardText);
        TextView roadmapText = view.findViewById(R.id.roadmapText);
        TextView settingsText = view.findViewById(R.id.settingsText);

        View boardTextUnderline = view.findViewById(R.id.boardTextUnderline);
        View roadmapTextUnderline = view.findViewById(R.id.roadmapTextUnderline);
        View settingsTextUnderline = view.findViewById(R.id.settingsTextUnderline);

        ImageButton addBtn = view.findViewById(R.id.addBtn);
        ImageButton moreBtn = view.findViewById(R.id.moreVerticalBtn);

        if (fragment == FragmentSettings.DASHBOARD_FRAGMENT_ID)
        {
            boardText.setTextColor(getResources().getColor(R.color.purple_200, getTheme()));
            roadmapText.setTextColor(getResources().getColor(R.color.white60, getTheme()));
            settingsText.setTextColor(getResources().getColor(R.color.white60, getTheme()));

            boardTextUnderline.setVisibility(View.VISIBLE);
            roadmapTextUnderline.setVisibility(View.GONE);
            settingsTextUnderline.setVisibility(View.GONE);

            addBtn.setVisibility(View.VISIBLE);
            moreBtn.setVisibility(View.VISIBLE);

            roadmapText.setOnClickListener(roadmapTextListener(fragmentManager));
            settingsText.setOnClickListener(settingsTextListener(fragmentManager));
        } else if (fragment == FragmentSettings.ROADMAP_FRAGMENT_ID){
            boardText.setTextColor(getResources().getColor(R.color.white60, getTheme()));
            roadmapText.setTextColor(getResources().getColor(R.color.purple_200, getTheme()));
            settingsText.setTextColor(getResources().getColor(R.color.white60, getTheme()));

            boardTextUnderline.setVisibility(View.GONE);
            roadmapTextUnderline.setVisibility(View.VISIBLE);
            settingsTextUnderline.setVisibility(View.GONE);

            addBtn.setVisibility(View.VISIBLE);
            moreBtn.setVisibility(View.VISIBLE);

            boardText.setOnClickListener(boardTextListener(fragmentManager));
            settingsText.setOnClickListener(settingsTextListener(fragmentManager));
        } else if (
                fragment == FragmentSettings.PROJECT_SETTINGS_FRAGMENT_ID
                || fragment == FragmentSettings.AUTH_FRAGMENT_ID
                || fragment == FragmentSettings.USERS_FRAGMENT_ID
                || fragment == FragmentSettings.MANAGE_USERS_FRAGMENT_ID
                || fragment == FragmentSettings.GRID_FRAGMENT_ID
        ){
            boardText.setTextColor(getResources().getColor(R.color.white60, getTheme()));
            roadmapText.setTextColor(getResources().getColor(R.color.white60, getTheme()));
            settingsText.setTextColor(getResources().getColor(R.color.purple_200, getTheme()));

            boardTextUnderline.setVisibility(View.GONE);
            roadmapTextUnderline.setVisibility(View.GONE);
            settingsTextUnderline.setVisibility(View.VISIBLE);

            addBtn.setVisibility(View.GONE);
            moreBtn.setVisibility(View.GONE);

            boardText.setOnClickListener(boardTextListener(fragmentManager));
            roadmapText.setOnClickListener(roadmapTextListener(fragmentManager));
        }
    }

    private View.OnClickListener boardTextListener(FragmentManager fragmentManager){
        return v -> {
            Bundle bundle = new Bundle();
            DashboardFragment dashboardFragment = new DashboardFragment();
            dashboardFragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.navHostFragment, dashboardFragment);
            fragmentTransaction.disallowAddToBackStack();
            fragmentTransaction.commit();
        };
    }

    private View.OnClickListener roadmapTextListener(FragmentManager fragmentManager){
        return v -> {
            Bundle bundle = new Bundle();
            RoadmapFragment roadmapFragment = new RoadmapFragment();
            roadmapFragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.navHostFragment, roadmapFragment);
            fragmentTransaction.disallowAddToBackStack();
            fragmentTransaction.commit();
        };
    }
    private View.OnClickListener settingsTextListener(FragmentManager fragmentManager){
        return v -> swapToSettingsFragment(fragmentManager);
    }

    private void swapToSettingsFragment(FragmentManager fragmentManager) {
        Bundle bundle = new Bundle();
        ProjectSettingsFragment projectSettingsFragment = new ProjectSettingsFragment();
        projectSettingsFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.navHostFragment, projectSettingsFragment);
        fragmentTransaction.disallowAddToBackStack();
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        //for the nested
        if (
                fragmentPos == FragmentSettings.AUTH_FRAGMENT_ID
                || fragmentPos == FragmentSettings.USERS_FRAGMENT_ID
                || fragmentPos == FragmentSettings.MANAGE_USERS_FRAGMENT_ID
        ){
            swapToSettingsFragment(thisFragment.getParentFragmentManager());
        } else
            super.onBackPressed();
    }
}