package com.example.bugtracker.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.R;
import com.example.bugtracker.recyclerview.ProjectTableCreate_RecyclerAdapter;
import com.example.bugtracker.recyclerview.RecyclerAdapter;
import com.example.bugtracker.recyclerview.RecyclerData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProjectCreateTable extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<RecyclerData> recyclerDataArrayList;
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<Integer> imgIds = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_board);
        recyclerDataArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.main_recyclerview);
        String tag = recyclerView.getTag().toString();

        titles.add("Example 1");
        titles.add("Example 2");
        titles.add("Example 2");
        titles.add("Example 2");

        imgIds.add(R.drawable.ic_launcher_background);
        imgIds.add(R.drawable.ic_launcher_foreground);
        imgIds.add(R.drawable.ic_launcher_foreground);
        imgIds.add(R.drawable.ic_launcher_foreground);

        //types are epic for lot of tasks and task.
        // added data from arraylist to adapter class.
        recyclerDataArrayList.add(new RecyclerData("TO DO", titles, imgIds, tag));

        ProjectTableCreate_RecyclerAdapter adapter = new ProjectTableCreate_RecyclerAdapter(recyclerDataArrayList, this);

        // setting grid layout manager to implement grid view.
        // in this method '1' represents number of columns to be displayed in grid view.

        //TODO this affects the size needs to be fixed maybe using fragments?
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);

        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

       // recyclerDataArrayList.get(0)
    }
}
