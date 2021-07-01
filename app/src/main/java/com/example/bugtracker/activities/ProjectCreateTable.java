package com.example.bugtracker.activities;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.R;
import com.example.bugtracker.recyclerview.RecyclerAdapter;
import com.example.bugtracker.recyclerview.RecyclerData;

import java.util.ArrayList;
import java.util.Calendar;

public class ProjectCreateTable extends AppCompatActivity {

    private RecyclerView mainRecyclerView;
    private ArrayList<RecyclerData> recyclerDataArrayList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_projects_board);

        recyclerDataArrayList = new ArrayList<>();
        mainRecyclerView = findViewById(R.id.main_recyclerview);
        String tag = mainRecyclerView.getTag().toString();

        //types are epic for lot of tasks and task.
        recyclerDataArrayList.add(new RecyclerData();

        // added data from arraylist to adapter class.
        RecyclerAdapter adapter = new RecyclerAdapter(recyclerDataArrayList, this, "2");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        // at last set adapter to recycler view.
        mainRecyclerView.setLayoutManager(gridLayoutManager);
        mainRecyclerView.setAdapter(adapter);
    }
}
