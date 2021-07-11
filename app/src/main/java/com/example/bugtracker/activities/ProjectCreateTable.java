package com.example.bugtracker.activities;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.ArrayListToBytes;
import com.example.bugtracker.R;
import com.example.bugtracker.StringToList;
import com.example.bugtracker.recyclerview.Message;
import com.example.bugtracker.recyclerview.ProjectTableCreate_RecyclerAdapter;
import com.example.bugtracker.recyclerview.RecyclerData;
import com.example.bugtracker.recyclerview.myDbAdapter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

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

        titles.add("TEST");
        titles.add("TEST");

        imgIds.add(R.drawable.ic_launcher_background);
        imgIds.add(R.drawable.ic_launcher_foreground);

        //recyclerDataArrayList.add(new RecyclerData("TO DO", titles, imgIds, tag));

        //SaveData(titles, imgIds, "TO DO", this);

        //for data

        String data = null;
        //String data = GetData(this);

        if (data == null){
            Log.wtf("DATA IS EMPTY", "the data is null there is problem");
            finishAndRemoveTask();
        }

        String [] parts = data.split("/");

        titles = StringToList.StringToList(parts[1], null);
        imgIds = StringToList.StringToList(parts[2], 0);

        recyclerDataArrayList.add(new RecyclerData(parts[0], titles, imgIds, tag));
        recyclerDataArrayList.add(new RecyclerData(parts[0], titles, imgIds, tag));

        recyclerDataArrayList.add(new RecyclerData(this.getString(R.string.add_column), tag));

        ProjectTableCreate_RecyclerAdapter adapter = new
                ProjectTableCreate_RecyclerAdapter(recyclerDataArrayList, this);

        // setting grid layout manager to implement grid view.
        // in this method '1' represents number of columns to be displayed in grid view.

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);

        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setRecycledViewPool(viewPool);
    }


    public void SaveData(ArrayList<String> titles, ArrayList<Integer> imgIds, String title, String projectName){
        BufferedWriter writer = null;

        String data = title + "/" + titles.toString() + "/" + imgIds + "/";
        File f = new File(this.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "ProjectBoard", projectName + ".txt");

        try {
            writer = new BufferedWriter(new FileWriter(f, false));
            writer.write(data);

            // Refresh the data so it can seen when the device is plugged in a
            // computer. You may have to unplug and replug the device to see the
            // latest changes. This is not necessary if the user should not modify
            // the files.
            MediaScannerConnection.scanFile(this,
                    new String[]{f.toString()},
                    null,
                    null);
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

    public String GetData(String projectName){
        String data = null;

        try {
            FileInputStream fileInputStream = this.openFileInput("ProjectTable.txt");
            int read = -1;
            StringBuffer buffer = new StringBuffer();
            while((read = fileInputStream.read())!= -1){
                buffer.append((char)read);
            }
            data = buffer.toString();
            Log.wtf("Code", data);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    private void makeFolders(){
        //makes folders where the data is stored
        //TODO add function which checks if premissions for writing data are allowed

        File folder = new File(this.getFilesDir(), "ProjectData");

        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }

        folder = new File(this.getFilesDir() + File.separator + "ProjectData",
                "ProjectBoard");

        success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
    }
}
