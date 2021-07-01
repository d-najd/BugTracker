package com.example.bugtracker.activities;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.R;

public class ProjectCreateTable extends AppCompatActivity {

    private final RecyclerView mainRecyclerView = findViewById(R.id.main_recyclerview);


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_projects_board);

        mainRecyclerView.add
    }
}
