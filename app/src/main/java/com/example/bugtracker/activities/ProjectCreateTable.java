package com.example.bugtracker.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.ArrayListToBytes;
import com.example.bugtracker.R;
import com.example.bugtracker.recyclerview.Message;
import com.example.bugtracker.recyclerview.ProjectTableCreate_RecyclerAdapter;
import com.example.bugtracker.recyclerview.RecyclerData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

        //types are epic for lot of tasks and task.
        // added data from arraylist to adapter class.
        recyclerDataArrayList.add(new RecyclerData("TO DO", titles, imgIds, tag));

        //titles.add("Example 1");
        //titles.add("Example 2");
        //titles.add("Example 2");
        //titles.add("Example 2");


       // imgIds.add(R.drawable.ic_launcher_background);
        //imgIds.add(R.drawable.ic_launcher_foreground);
        //imgIds.add(R.drawable.ic_launcher_foreground);
        //imgIds.add(R.drawable.ic_launcher_foreground);


        recyclerDataArrayList.add(new RecyclerData("TO DO", titles, imgIds, tag));

        ProjectTableCreate_RecyclerAdapter adapter = new ProjectTableCreate_RecyclerAdapter(recyclerDataArrayList, this);

        // setting grid layout manager to implement grid view.
        // in this method '1' represents number of columns to be displayed in grid view.

        //TODO need to add ability to move from one to other, maybe using multiple fragments and
        //making 1 half visible and beign able to move from one to other?

        //TODO make each element seperatly scrollable, when you scroll it scrolls everything instead
        //of the inner recyclerview
        GridLayoutManager layoutManager = new GridLayoutManager(this, 100);

        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setRecycledViewPool(viewPool);
    }

    public void SaveData(ArrayList<String> titles, ArrayList<Integer> imgIds) {
        File file= null;
        FileOutputStream fileOutputStream = null;

        byte[] titlesbyte = ArrayListToBytes.arrayListToBytes(titles, null);
        byte[] imgIdsbyte = ArrayListToBytes.arrayListToBytes(imgIds, 0);

        try {
            Log.wtf("HETJGIFDJIHDGIDFJGHJDOUFHSDUIORDFGNIDFNGFODIFGIDGHIREHGIORHEOIGHREOUGEHGOIURHGUOEROHERGIOEU", "fileOutputStream");
            //Log.wtf("HETJGIFDJIHDGIDFJGHJDOUFHSDUIORDFGNIDFNGFODIFGIDGHIREHGIORHEOIGHREOUGEHGOIURHGUOEROHERGIOEU", "fileOutputStream");
            //Log.wtf("HETJGIFDJIHDGIDFJGHJDOUFHSDUIORDFGNIDFNGFODIFGIDGHIREHGIORHEOIGHREOUGEHGOIURHGUOEROHERGIOEU", fileOutputStream + "");
            //Log.wtf("HETJGIFDJIHDGIDFJGHJDOUFHSDUIORDFGNIDFNGFODIFGIDGHIREHGIORHEOIGHREOUGEHGOIURHGUOEROHERGIOEU", fileOutputStream + "");
            //Log.wtf("HETJGIFDJIHDGIDFJGHJDOUFHSDUIORDFGNIDFNGFODIFGIDGHIREHGIORHEOIGHREOUGEHGOIURHGUOEROHERGIOEU", fileOutputStream + "");


            file = getFilesDir();
            fileOutputStream = openFileOutput("ProjectTable.txt", Context.MODE_PRIVATE); //MODE PRIVATE

            Log.wtf("HETJGIFDJIHDGIDFJGHJDOUFHSDUIORDFGNIDFNGFODIFGIDGHIREHGIORHEOIGHREOUGEHGOIURHGUOEROHERGIOEU", fileOutputStream + "");

            fileOutputStream.write(titlesbyte);
            fileOutputStream.write(imgIdsbyte);
            Message.message(this,"Saved \n" + "Path --" + file + "\tCode.txt");
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

    public void GetData(){
        try {
            FileInputStream fileInputStream = openFileInput("ProjectTable.txt");
            int read = -1;
            StringBuffer buffer = new StringBuffer();
            while((read =fileInputStream.read())!= -1){
                buffer.append((char)read);
            }
            Log.d("Code", buffer.toString());
            String name = buffer.substring(0,buffer.indexOf(" "));
            String pass = buffer.substring(buffer.indexOf(" ")+1);
            Message.message(this, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
