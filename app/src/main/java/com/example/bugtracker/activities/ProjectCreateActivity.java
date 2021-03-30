package com.example.bugtracker.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.R;
import com.example.bugtracker.adapters.ProjectsRecyclerAdapter;
import com.example.bugtracker.adapters.RecyclerData;

import java.util.ArrayList;

public class ProjectCreateActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private ArrayList<RecyclerData> recyclerDataArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_create_project);
        getSupportActionBar().hide();

        recyclerDataArrayList = new ArrayList<>();

        recyclerDataArrayList.add(new RecyclerData("Add item", "Description", R.drawable.ic_launcher_background));
        recyclerDataArrayList.add(new RecyclerData("Add item", "Description", R.drawable.ic_launcher_background));
        recyclerDataArrayList.add(new RecyclerData("Add item", "Description", R.drawable.ic_launcher_background));

        recyclerView = root.findViewById(R.id.recyclerView_Act_checklist);

        // added data from arraylist to adapter class.
        ProjectsRecyclerAdapter adapter = new ProjectsRecyclerAdapter(recyclerDataArrayList, requireContext());

        // setting grid layout manager to implement grid view.
        // in this method '1' represents number of columns to be displayed in grid view.
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 1);

        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}