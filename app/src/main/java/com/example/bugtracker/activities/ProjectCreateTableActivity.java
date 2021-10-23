package com.example.bugtracker.activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.GlobalValues;
import com.example.bugtracker.ProjectCreateTableData;
import com.example.bugtracker.R;
import com.example.bugtracker.StringToList;
import com.example.bugtracker.recyclerview.Adapters.ProjectTableCreateAdapter;
import com.example.bugtracker.recyclerview.RecyclerData;

import java.util.ArrayList;

public class ProjectCreateTableActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<RecyclerData> recyclerDataArrayList;
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<Integer> imgIds = new ArrayList<>();
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private String projectName; //data is passed through intent
    private int amountOfPartsInData;

    private ArrayList<Boolean> hasBeenCreated = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_board);
        recyclerDataArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.mainRecyclerView);
        String tag = recyclerView.getTag().toString();
        projectName = getIntent().getExtras().getString("projectName");

        ProjectCreateTableData.MakeFolders(this);

        //titles.add("TEST");
        //titles.add("TEST");
        //imgIds.add(R.drawable.ic_launcher_background);
        //imgIds.add(R.drawable.ic_launcher_foreground);

        //saveData(titles, imgIds, "TO DO", projectName);

        //removeData(2, projectName);

        String data = ProjectCreateTableData.GetData(projectName, this);
        amountOfPartsInData = ProjectCreateTableData.amountOfPartsInData;

        //for getting the data nad putting it in arrayList so it can be used by the adapter
        if (data == null){
            Log.wtf("DATA IS EMPTY", "the data is null there is problem");
        } else {
            String[] parts = data.split("::");

            for (int i = 0; i < parts.length / amountOfPartsInData; i++){
                titles = StringToList.StringToList(parts[1 + (i * amountOfPartsInData)], null);
                imgIds = StringToList.StringToList(parts[2 + (i * amountOfPartsInData)], 0);

                recyclerDataArrayList.add(new RecyclerData(parts[i * amountOfPartsInData], titles, imgIds, String.valueOf(i), tag));
            }
        }

        recyclerDataArrayList.add(new RecyclerData(this.getString(R.string.add_column), tag));

        ProjectTableCreateAdapter adapter = new ProjectTableCreateAdapter(recyclerDataArrayList, this);

        // setting grid layout manager to implement grid view.
        // in this method '1' represents number of columns to be displayed in grid view.

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.projectCreateTableActivity = this;
        adapter.projectName = projectName;
        adapter.intent = getIntent();
        recyclerView.setRecycledViewPool(viewPool);

    }

    public void RefreshActivity(){
        //TODO this is gonna break in the future, need to check if the items are saved before doing this instead of waiting
        Handler h = new Handler() ;
        h.postDelayed(new Runnable() {
            public void run() {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        }, 250);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //stupid fix but hey it works
        boolean reloadedActivity = GlobalValues.reloadedActivity;
        if (!reloadedActivity) {
            GlobalValues.reloadedActivity = true;
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        } else
            GlobalValues.reloadedActivity = false;
    }

    //endregion
}
