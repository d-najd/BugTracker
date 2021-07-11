package com.example.bugtracker.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.R;
import com.example.bugtracker.StringToList;
import com.example.bugtracker.recyclerview.ProjectTableCreate_RecyclerAdapter;
import com.example.bugtracker.recyclerview.RecyclerData;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ProjectCreateTable extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<RecyclerData> recyclerDataArrayList;
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<Integer> imgIds = new ArrayList<>();
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private String projectName; //data is passed through intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_board);
        recyclerDataArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.main_recyclerview);
        String tag = recyclerView.getTag().toString();
        projectName = getIntent().getExtras().getString("projectName");
        //TODO finish working on save data thingy

        makeFolders();

        //titles.add("rere");
        //titles.add("TEST");

        //imgIds.add(R.drawable.ic_launcher_background);
        //imgIds.add(R.drawable.ic_launcher_foreground);

        //recyclerDataArrayList.add(new RecyclerData("TO DO", titles, imgIds, tag));

        saveData(titles, imgIds, "TO DO", projectName);

        //for data
        //removeData(1, projectName);
        String data = getData(projectName);

        //ArrayList<String> dataList = new ArrayList<>(Arrays.asList(data.split("/")));
        //String test = dataList.toString().substring(1, dataList.toString().length() - 1)
        //        .replace(", ", "/") + "/";

        if (data == null){
            Log.wtf("DATA IS EMPTY", "the data is null there is problem");
        } else {
            String[] parts = data.split("/");

            for (int i = 0; i < parts.length / 3; i++){
                titles = StringToList.StringToList(parts[1 + (i * 3)], null);
                imgIds = StringToList.StringToList(parts[2 + (i * 3)], 0);

                recyclerDataArrayList.add(new RecyclerData(parts[i * 3], titles, imgIds, tag));
            }

            recyclerDataArrayList.add(new RecyclerData(this.getString(R.string.add_column), tag));

            ProjectTableCreate_RecyclerAdapter adapter = new
                    ProjectTableCreate_RecyclerAdapter(recyclerDataArrayList, this);

            // setting grid layout manager to implement grid view.
            // in this method '1' represents number of columns to be displayed in grid view.

            LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

            // at last set adapter to recycler view.
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            adapter.projectCreateTableActivity = this;
            adapter.projectName = projectName;
            recyclerView.setRecycledViewPool(viewPool);
        }
    }


    public String getData(String projectName){
        String data = null;

        BufferedInputStream reader = null; // here needs to be done work

        File f = new File(this.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "ProjectBoard", projectName + ".txt");

        try {
            FileInputStream fileInputStream = new FileInputStream(f);
            int read = -1;
            StringBuffer buffer = new StringBuffer();
            while((read = fileInputStream.read())!= -1){
                buffer.append((char)read);
            }
            data = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    public void saveData(ArrayList<String> titles, ArrayList<Integer> imgIds, String title, String projectName){
        BufferedWriter writer = null;
        int id = 0;

        File f = new File(this.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "ProjectBoard", projectName + ".txt");

        String dataOld = getData(projectName);
        if (dataOld != null) {
            String[] parts = dataOld.split("/");
            id = parts.length / 3;
        }

        String data = title + "/" + titles.toString() + "/" + imgIds + "/";

        try {
            writer = new BufferedWriter(new FileWriter(f, true));
            writer.write(data);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void removeData(int id, String projectName){

        //TODO there might be a problem with
        String data = getData(projectName);

        String[] parts = data.split("/");

        //this part is for splitting, ex if the id is 1, the startingStr will get data from
        //0 up until 1 and stop, and endstr will get data from 2 and on

        String endStr = "";
        String startStr = "";

        if (id != 0){
            for (int i = 0; i < id * 3; i++){
                startStr += parts[i] + "/";
            }
        }

        for (int i = (id + 1) * 3; i < parts.length; i++){
            endStr += parts[i] + "/";
        }

        data = startStr + endStr;

        BufferedWriter writer = null;

        File f = new File(this.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "ProjectBoard", projectName + ".txt");

        try {
            writer = new BufferedWriter(new FileWriter(f, false));
            writer.write(data);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void makeFolders(){
        //makes folders where the data is stored
        //TODO add function which checks if premissions for writing data are allowed

        File folder = new File(this.getFilesDir(), "ProjectData");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        folder = new File(this.getFilesDir() + File.separator + "ProjectData",
                "ProjectBoard");
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}
