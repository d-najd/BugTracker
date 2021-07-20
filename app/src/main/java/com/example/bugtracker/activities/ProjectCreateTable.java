package com.example.bugtracker.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.Message;
import com.example.bugtracker.R;
import com.example.bugtracker.StringToList;
import com.example.bugtracker.recyclerview.Adapters.ProjectTableCreate_RecyclerAdapter;
import com.example.bugtracker.recyclerview.RecyclerData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ProjectCreateTable extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<RecyclerData> recyclerDataArrayList;
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<Integer> imgIds = new ArrayList<>();
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private String projectName; //data is passed through intent

    private ArrayList<Boolean> hasBeenCreated = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_board);
        recyclerDataArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.mainRecyclerView);
        String tag = recyclerView.getTag().toString();
        projectName = getIntent().getExtras().getString("projectName");

        MakeFolders();

        //titles.add("TEST");
        //titles.add("TEST");
        //imgIds.add(R.drawable.ic_launcher_background);
        //imgIds.add(R.drawable.ic_launcher_foreground);

        //saveData(titles, imgIds, "TO DO", projectName);

        //removeData(2, projectName);

        String data = GetData(projectName);

        //for getting the data nad putting it in arrayList so it can be used by the adapter
        if (data == null){
            Log.wtf("DATA IS EMPTY", "the data is null there is problem");
        } else {
            String[] parts = data.split("/");

            for (int i = 0; i < parts.length / 3; i++){
                titles = StringToList.StringToList(parts[1 + (i * 3)], null);
                imgIds = StringToList.StringToList(parts[2 + (i * 3)], 0);

                recyclerDataArrayList.add(new RecyclerData(parts[i * 3], titles, imgIds, String.valueOf(i), tag));
            }
        }

        recyclerDataArrayList.add(new RecyclerData(this.getString(R.string.add_column), tag));

        ProjectTableCreate_RecyclerAdapter adapter = new ProjectTableCreate_RecyclerAdapter(recyclerDataArrayList, this);

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

    //region Storage
    public String GetData(String projectName){
        String data = null;

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


    public void SaveData(ArrayList<String> titles, ArrayList<Integer> imgIds, String title, String projectName){
        BufferedWriter writer = null;
        int id = 0;

        File f = new File(this.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "ProjectBoard", projectName + ".txt");

        String dataOld = GetData(projectName);
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


    public void RemoveData(int id, String projectName){
        BufferedWriter writer = null;
        File f = new File(this.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "ProjectBoard", projectName + ".txt");

        String data = GetData(projectName);
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

        try {
            writer = new BufferedWriter(new FileWriter(f, false));
            writer.write(data);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                writer.close();

                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Boolean> GetTest(){
        return hasBeenCreated;
    }

    public void ReplaceData(String newItemTitle, String projectName, int id, boolean keepPreviousItems){
        BufferedWriter writer = null;
        String dataOld = GetData(projectName);
        String data = "";

        if (dataOld == null){
            Log.wtf("the data seems to be null", "Stop the activity");
            Message.message(this, "The data seems to be null, stop the activity");
        }

        String[] parts = dataOld.split("/");


        File f = new File(this.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "ProjectBoard", projectName + ".txt");

        //old title without the ] at the end
        String title = parts[(id * 3) + 1].substring(0, parts[(id * 3) + 1].length() - 1);
        String imgId = parts[(id * 3) + 2].substring(0, parts[(id * 3) + 2].length() - 1);

        //adding the new data
        //also this checks if the length is 1 because if the list is empty it will be left with [
        // and if we add the ", " it will end up like [, data] which will cause some trouble down
        // the road
        if (title.length() != 1) {
            title += (", " + newItemTitle + "]");
            imgId += (", " + "2131165294" + "]"); // the number is for a drawable, can change later
        } else {
            title += (newItemTitle + "]");
            imgId += ("2131165294" + "]");
        }

        parts[(id * 3) + 1] = title;
        parts[(id * 3) + 2] = imgId;

        if (keepPreviousItems) {
            for (int i = 0; i < parts.length; i++) {
                data += (parts[i] + "/");
            }
        } else
        {
            Log.wtf("", "add a case where it wont keep the previous items");
            Message.message(this, "add a case where it wont keep the previous items");
        }
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

    private void MakeFolders(){
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

    //endregion
}
