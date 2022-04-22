package com.aatesting.bugtracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.aatesting.bugtracker.GlobalValues;
import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.Projects.ProjectsMainActivity;
import com.aatesting.bugtracker.fragments.Main.ProjectsFragment;
import com.aatesting.bugtracker.restApi.ApiJSONObject;
import com.aatesting.bugtracker.restApi.ApiSingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public ProjectsFragment projectsFragment;
    public View mainBtn;
    public View bottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mainBtn = findViewById(R.id.mainBtn);
        bottomBar = findViewById(R.id.bottomAppBar);
        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
        bottomBar.setBackground(null);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        new AppBarConfiguration.Builder(
                R.id.navigation_projects, R.id.navigation_account_signIn, R.id.navigation_account_signUp)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        mainBtn.setVisibility(View.VISIBLE);
        fixBottomMenu();


        //DEBUGGING

        Intent intent = new Intent(this, ProjectsMainActivity.class);

        intent.putExtra("projectName", "nan");
        startActivity(intent);
    }



    /**
     * @apiNote prevents the bottom menu to get dragged when the keyboard created
     */
    public void fixBottomMenu(){
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }, 100);
    }
}