package com.aatesting.bugtracker.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.recyclerview.Adapters.MainRecyclerAdapter;
import com.aatesting.bugtracker.recyclerview.RecyclerData;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateTaskActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<RecyclerData> recyclerDataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        recyclerDataArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.weeksRecyclerView);
        String tag = recyclerView.getTag().toString();

        //types are epic for lot of tasks and task.
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.task), R.drawable.ic_task_24dp, R.drawable.ic_arrow_down_24dp,  tag));
        //need to make a menu like the subTask/list menu from tasks app, something like that but modified
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.summary),"Tap to enter Summary", R.drawable.ic_text_24dp, true, tag));
        //add subTasks for the selected Task
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.description), "Tap to enter Description", R.drawable.ic_note_24dp, true, tag));
        recyclerDataArrayList.add(new RecyclerData("Take photo, record vid etc.", R.drawable.ic_null_background, tag));
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.dueDate), "Tap to add reminder",  R.drawable.ic_alarm_24dp, tag));
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.reminder), "Tap to add reminder",  R.drawable.ic_notifications_full_24dp, tag));
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.assigned), "Assigned", R.drawable.ic_null_foreground, tag));
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.reporter), "Tap to add reminder", R.drawable.ic_null_foreground, tag));
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.labels), "Tap to add reminder", R.drawable.ic_list_24dp, tag));
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.highlight), "Make the project stand out from the rest", R.drawable.ic_empty_star_24dp, tag));
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.created), Calendar.getInstance().getTime().toString(), R.drawable.ic_calendar_24dp, tag));

        MainRecyclerAdapter adapter = new MainRecyclerAdapter(recyclerDataArrayList, this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        Listeners();
    }

    private void Listeners() {

    }
}