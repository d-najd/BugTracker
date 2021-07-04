package com.example.bugtracker.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.R;
import com.example.bugtracker.recyclerview.RecyclerAdapter;
import com.example.bugtracker.recyclerview.RecyclerData;

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
        recyclerView = findViewById(R.id.recyclerView_Act_CreateProject);
        String tag = recyclerView.getTag().toString();

        //types are epic for lot of tasks and task.
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.task), R.drawable.ic_task_24dp, R.drawable.ic_arrow_down_24dp,  tag));
        //need to make a menu like the subTask/list menu from tasks app, something like that but modified
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.summary),"Tap to enter Summary", R.drawable.ic_text_24dp, true, tag));
        //add subTasks for the selected Task
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.description), "Tap to enter Description", R.drawable.ic_note_24dp, true, tag));
        recyclerDataArrayList.add(new RecyclerData("Take photo, record vid etc.", R.drawable.ic_null_background, tag));
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.due_date), "Tap to add reminder",  R.drawable.ic_alarm_24dp, tag));
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.reminder), "Tap to add reminder",  R.drawable.ic_notifications_full_24dp, tag));
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.assigned), "Assigned", R.drawable.ic_null_foreground, tag));
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.reporter), "Tap to add reminder", R.drawable.ic_null_foreground, tag));
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.labels), "Tap to add reminder", R.drawable.ic_list_24dp, tag));
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.highlight), "Make the project stand out from the rest", R.drawable.ic_empty_star_24dp, tag));
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.created), Calendar.getInstance().getTime().toString(), R.drawable.ic_calendar_24dp, tag));

        // added data from arraylist to adapter class.
        RecyclerAdapter adapter = new RecyclerAdapter(recyclerDataArrayList, this);

        // setting grid layout manager to implement grid view.
        // in this method '1' represents number of columns to be displayed in grid view.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        Listeners();
    }

    private void Listeners() {

        View mainBtn = findViewById(R.id.mainBtn);

        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}