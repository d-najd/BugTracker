package com.aatesting.bugtracker.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Calendar;

import com.aatesting.bugtracker.ProjectCreateTableData;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.recyclerview.Adapters.CreateProjectsAdapter;
import com.aatesting.bugtracker.recyclerview.RecyclerData;
import com.aatesting.bugtracker.databases.ProjectsDatabase;

import java.util.ArrayList;

public class ProjectCreateActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private ArrayList<RecyclerData> recyclerDataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);

        recyclerDataArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        String tag = recyclerView.getTag().toString();

        //will prob nee to find another way to difference between layouts
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.description), "Tap to add description",  R.drawable.ic_note_24dp, true, tag));
        //need to make a menu like the subTask/list menu from tasks app, something like that but modified
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.newTask),  R.drawable.ic_sublist_24dp, tag));
        //add subTasks for the selected Task
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.tasks), "No Task selected",  R.drawable.ic_list_24dp, tag));
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.roadmap),  R.drawable.ic_calendar_24dp, tag));
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.dueDate), "Tap to add reminder",  R.drawable.ic_alarm_24dp, tag));
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.reminder), "Tap to add reminder",  R.drawable.ic_notifications_full_24dp, tag));
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.highlight), "Make the project stand out from the rest",  R.drawable.ic_empty_star_24dp, tag));
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.created), Calendar.getInstance().getTime().toString(), R.drawable.ic_calendar_24dp, tag));
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.reminderType), "Notification",  R.drawable.ic_notifications_24dp, tag));
        recyclerDataArrayList.add(new RecyclerData(getString(R.string.repeat), "Does not repeat",  R.drawable.ic_repeat_24dp, tag));


        // added data from arraylist to adapter class.
        CreateProjectsAdapter adapter = new CreateProjectsAdapter(recyclerDataArrayList, this);

        // setting grid layout manager to implement grid view.
        // in this method '1' represents number of columns to be displayed in grid view.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        Listeners();
    }

    private void Listeners(){
        View mainBtn = findViewById(R.id.mainBtn);
        ImageButton backBtn = findViewById(R.id.cancelButton);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProject(v);
            }
        });
    }

    public void AddProject(View view)
    {
        ProjectsDatabase helper = new ProjectsDatabase(this);
        EditText Name = (EditText) findViewById(R.id.edtCreateProject);
        String Pass = recyclerDataArrayList.get(0).getDescription();

        String t1 = Name.getText().toString();
        String t2 = Pass;
        if(t1.isEmpty() || t2.isEmpty())
        {
            Message.message(this,"Enter Both Project Name and Description");
        }
        else
        {
            long id = helper.InsertData(t1,t2);
            ProjectCreateTableData.CreatingNewProject(t1, this);
            if(id<=0)
            {
                Message.message(this,"Insertion Unsuccessful");
            }
        }
    }

    public void ViewData()
    {
        ProjectsDatabase helper = new ProjectsDatabase(this);

        String data = helper.GetData();
        Message.message(this,data);
    }

    /*
    public void Update( View view)
    {
        String u1 = updateold.getText().toString();
        String u2 = updatenew.getText().toString();
        if(u1.isEmpty() || u2.isEmpty())
        {
            Message.message(getApplicationContext(),"Enter Data");
        }
        else
        {
            int a= helper.updateName( u1, u2);
            if(a<=0)
            {
                Message.message(getApplicationContext(),"Unsuccessful");
                updateold.setText(""); //shouldnt these be removed
                updatenew.setText("");
            } else {
                Message.message(getApplicationContext(),"Updated");
                updateold.setText("");
                updatenew.setText("");
            }
        }

    }
    public void Delete( View view)
    {
        String uname = delete.getText().toString();
        if(uname.isEmpty())
        {
            Message.message(getApplicationContext(),"Enter Data");
        }
        else{
            int a= helper.delete(uname);
            if(a<=0)
            {
                Message.message(getApplicationContext(),"Unsuccessful");
                delete.setText(""); //shouldnt this be removed
            }
            else
            {
                Message.message(this, "DELETED");
                delete.setText("");
            }
        }
    }
     */
}