package com.example.bugtracker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.Message;
import com.example.bugtracker.ProjectCreateTableData;
import com.example.bugtracker.R;
import com.example.bugtracker.recyclerview.Adapters.RecyclerAdapter;
import com.example.bugtracker.recyclerview.RecyclerData;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class ProjectCreateTableEditTask extends AppCompatActivity {
    private TextView editDescriptionTxt;
    private TextView titleMiddle;
    private Button columnSelector;
    public String newData; //the data (string) for the description
    //to get the correct descrition and stuff instead of carrying it for ages.
    private String projectName;
    private String oldDescription;
    private int itemPos;
    private int columnPos;
    private ArrayList<RecyclerData> recyclerDataArrayList = new ArrayList<>();


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

        oldDescription = ProjectCreateTableData.GetDescription(projectName, columnPos, itemPos,this);

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
        Listeners();
    }

    private void Listeners(){
        Context context = this;


        columnSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = v.findViewById(android.R.id.content);

                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.bottomdialog_complex_recyclerview, viewGroup, false);
                BottomSheetDialog bottomDialog = new BottomSheetDialog(context);
                    bottomDialog.setTitle("Issue Type");
                    bottomDialog.setTitle("These are the issue types you can choose, based on the workflow of the current issue type.");
                    bottomDialog.getWindow().setBackgroundDrawableResource(R.color.darkGray);
                    bottomDialog.setContentView(dialogView);

                    //for changing the size
                    //View bottomSheet = bottomDialog.findViewById(R.id.design_bottom_sheet);
                    //bottomSheet.getLayoutParams().height = 500;

                bottomDialog.show();

                //TODO change the tag
                recyclerDataArrayList.clear();
                recyclerDataArrayList.add(new RecyclerData("test", 2131165294, "null"));
                recyclerDataArrayList.add(new RecyclerData("test1", 2131165294, "null"));

                RecyclerView BottomDialog = dialogView.findViewById(R.id.dialog_recyclerview);
                RecyclerAdapter adapter = new RecyclerAdapter(recyclerDataArrayList, context);

                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                BottomDialog.setLayoutManager(layoutManager);
                BottomDialog.setAdapter(adapter);
            }
        });

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
