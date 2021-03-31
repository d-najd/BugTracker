package com.example.bugtracker.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Calendar;

import com.example.bugtracker.R;
import com.example.bugtracker.recyclerview.RecyclerAdapter;
import com.example.bugtracker.recyclerview.RecyclerData;

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

        //will prob nee to find another way to difference between layouts
        recyclerDataArrayList.add(new RecyclerData("Notes", "Tap to add notes", R.drawable.ic_note_24dp, true, 1));
        //need to make a menu like the subTask/list menu from tasks app, something like that but modified
        recyclerDataArrayList.add(new RecyclerData("Tasks", R.drawable.ic_sublist_24dp,1));
        //add subTasks for the selected Task
        recyclerDataArrayList.add(new RecyclerData("Sub Tasks", "No Task selected", R.drawable.ic_list_24dp,1));
        recyclerDataArrayList.add(new RecyclerData("Roadmap", R.drawable.ic_calendar_24dp, 1));
        recyclerDataArrayList.add(new RecyclerData("Reminder", "Tap to add reminder", R.drawable.ic_notifications_24dp, 1));
        recyclerDataArrayList.add(new RecyclerData("Highlight", "Make the project stand out from the rest", R.drawable.ic_star_24dp, 1));
        recyclerDataArrayList.add(new RecyclerData("Created", Calendar.getInstance().getTime().toString(), R.drawable.ic_calendar_24dp, 1));

        recyclerView = findViewById(R.id.recyclerView_Act_checklist);

        // added data from arraylist to adapter class.
        RecyclerAdapter adapter = new RecyclerAdapter(recyclerDataArrayList, this);

        // setting grid layout manager to implement grid view.
        // in this method '1' represents number of columns to be displayed in grid view.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}