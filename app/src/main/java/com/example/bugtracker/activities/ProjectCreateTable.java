package com.example.bugtracker.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ProjectCreateTable extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<RecyclerData> recyclerDataArrayList;
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<Integer> imgIds = new ArrayList<>();
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

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

        //recyclerDataArrayList.add(new RecyclerData("TO DO", titles, imgIds, tag));

        //SaveData(titles, imgIds, "TO DO", this);

        //for data
        String data = GetData(this);

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

    public void SaveData(ArrayList<String> titles, ArrayList<Integer> imgIds, String title, Context context) {
        FileOutputStream fileOutputStream = null;

        String data = title + "/" + titles.toString() + "/" + imgIds + "/";
        File f = new File(context.getFilesDir(), "ProjectTable.txt");

        try {
            //TODO for not removing data?
            //fileOutputStream = context.openFileOutput("ProjectTable.txt", Context.MODE_APPEND);
            fileOutputStream = context.openFileOutput("ProjectTable.txt", Context.MODE_PRIVATE);
            fileOutputStream.write(data.getBytes());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String GetData(Context context){
        String data = null;

        try {
            FileInputStream fileInputStream = context.openFileInput("ProjectTable.txt");
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
}
