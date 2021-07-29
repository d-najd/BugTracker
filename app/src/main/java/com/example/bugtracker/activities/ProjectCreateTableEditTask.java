package com.example.bugtracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bugtracker.Message;
import com.example.bugtracker.R;

public class ProjectCreateTableEditTask extends AppCompatActivity {
    private TextView editDescriptionTxt;
    public String newData; //the data (string) for the description
    //to get the correct descrition and stuff instead of carrying it for ages.
    private String projectName;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_create_edit_task);

        String oldDescription = GetOldDescription(); //just the description

        editDescriptionTxt = findViewById(R.id.descriptionTxt);
        editDescriptionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( ProjectCreateTableEditTask.this, ProjectCreateTableEditDescription.class);
                startActivityForResult(intent, 1); //for getting data back from the second activity
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //for getting data back from the second activity
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                newData = data.getStringExtra("newData");
                //TODO save the data.
                Message.message(getBaseContext(), newData);
            }
        }
    }

    private String GetOldDescription(){
        Intent intent = getIntent();
        projectName = intent.getExtras().getString("projectMame");
        position = intent.getExtras().getInt("position");

        ProjectCreateTable projectCreateTable = new ProjectCreateTable();
        String data = projectCreateTable.GetData(projectName);

        //TODO finish this
    }
}
