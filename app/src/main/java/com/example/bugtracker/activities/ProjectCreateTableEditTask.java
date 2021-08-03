package com.example.bugtracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bugtracker.Message;
import com.example.bugtracker.ProjectCreateTableData;
import com.example.bugtracker.R;

import org.w3c.dom.Text;

public class ProjectCreateTableEditTask extends AppCompatActivity {
    private TextView editDescriptionTxt;
    private TextView titleMiddle;
    private Button columnSelector;
    public String newData; //the data (string) for the description
    //to get the correct descrition and stuff instead of carrying it for ages.
    private String projectName;
    private int itemPos;
    private int columnPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_create_edit_task);

        projectName = getIntent().getExtras().getString("projectName");
        columnPos = getIntent().getExtras().getInt("columnPos");
        itemPos = getIntent().getExtras().getInt("itemPos");
        String columnName = getIntent().getExtras().getString("columnName");
        String itemName = getIntent().getExtras().getString("itemName");

        editDescriptionTxt = findViewById(R.id.descriptionTxt);
        titleMiddle = findViewById(R.id.titleMiddle);
        columnSelector = findViewById(R.id.columnSelector);

        String oldDescription = ProjectCreateTableData.GetDescription(projectName, columnPos, itemPos,this);

        columnSelector.setText(columnName);
        titleMiddle.setText(itemName);

        //setting the description
        if (oldDescription == null) {
            editDescriptionTxt.setText(getString(R.string.description));
            editDescriptionTxt.setTextColor(getColor(R.color.white38));
        }
        else {
            editDescriptionTxt.setText(oldDescription);
            editDescriptionTxt.setTextColor(getColor(R.color.white60));
        }

        editDescriptionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( ProjectCreateTableEditTask.this, ProjectCreateTableEditDescription.class);
                intent.putExtra("oldData", oldDescription);
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
                ProjectCreateTableData.SaveDescription(projectName, newData, columnPos, itemPos, this);
                Message.message(getBaseContext(), newData);

                //updating the description
                if (newData == null) {
                    editDescriptionTxt.setText(getString(R.string.description));
                    editDescriptionTxt.setTextColor(getColor(R.color.white38));
                }
                else {
                    editDescriptionTxt.setText(newData);
                    editDescriptionTxt.setTextColor(getColor(R.color.white60));
                }
            }
        }
    }
}
