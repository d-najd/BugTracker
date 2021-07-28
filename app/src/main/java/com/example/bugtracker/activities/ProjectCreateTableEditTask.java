package com.example.bugtracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bugtracker.Message;
import com.example.bugtracker.R;

public class ProjectCreateTableEditTask extends AppCompatActivity {
    private Button editDescriptionBtn;
    public String newData; //the data (string) for the description

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_create_edit_task);

        editDescriptionBtn = findViewById(R.id.descriptionBtn);
        ProjectCreateTableEditDescription test = new ProjectCreateTableEditDescription();

        editDescriptionBtn.setOnClickListener(new View.OnClickListener() {
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
}
