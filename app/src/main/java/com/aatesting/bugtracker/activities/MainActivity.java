package com.aatesting.bugtracker.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.data.ProjectTableData;
import com.aatesting.bugtracker.data.ProjectsDatabase;
import com.aatesting.bugtracker.dialogs.Dialogs;
import com.aatesting.bugtracker.fragments.ProjectsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    public int fragmentSelected = 0; //0-1 from left to right
    public ProjectsFragment projectsFragment;
    private MainActivity mainActivity;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = this;
        context = this;

        View bottomBar = findViewById(R.id.bottomAppBar);
        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
        bottomBar.setBackground(null);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_projects,  R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        Listeners(0);
        //((MainActivity)getActivity()).Listeners(0);
    }

    public void Listeners(int fragmentSelected){ //NOTE this is called from the fragments
        View mainBtn = findViewById(R.id.mainBtn);

        if (fragmentSelected == 0) {
            mainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialogs.NewProjectDialog(context, "Create Project",
                            "CREATE", "CANCEL", mainActivity);
                    //Intent intent = new Intent(MainActivity.this, ProjectCreateActivity.class);
                    //startActivity(intent);
                }
            });
        }
        else if (fragmentSelected == 1) {
            mainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Intent intent = new Intent(MainActivity.this, RoadmapCreateEpicActivity.class);
                    //startActivity(intent);
                }
            });
        }
    }

    public void AddProject(String title)
    {
        ProjectsDatabase helper = new ProjectsDatabase(this);
        String description = "NULL";
        if(title.isEmpty())
        {
            Message.message(this,"Enter Both Project Name and Description");
        }
        else
        {
            long id = helper.InsertData(title, description);
            ProjectTableData.CreatingNewProject(title, this);
            projectsFragment.NotifyItemAdded();
            if(id<=0)
            {
                Message.message(this,"Insertion Unsuccessful");
            }
        }
    }
}