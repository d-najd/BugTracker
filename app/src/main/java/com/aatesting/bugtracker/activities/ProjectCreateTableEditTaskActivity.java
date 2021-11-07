package com.aatesting.bugtracker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aatesting.bugtracker.GlobalValues;
import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.data.ProjectTableData;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.dialogs.Dialogs;
import com.aatesting.bugtracker.recyclerview.Adapters.MainRecyclerAdapter;
import com.aatesting.bugtracker.recyclerview.RecyclerData;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class ProjectCreateTableEditTaskActivity extends AppCompatActivity {
    private TextView editDescriptionTxt;
    private EditText titleMiddle;
    private Button columnSelector;
    public String newData; //the data (string) for the description
    //to get the correct descrition and stuff instead of carrying it for ages.
    private String tag; //there are multiple tags for this activity bc of the bottomdialogs
    private String projectName;
    private String oldDescription;
    private int itemPos;
    private int columnPos;
    private ArrayList<RecyclerData> recyclerDataArrayList = new ArrayList<>();
    //used for the bottomdialogs
    private ArrayList<String> allColumnTitles = new ArrayList<>();
    private ArrayList<Integer> allColumnImages = new ArrayList<>();
    private ArrayList<String> allColumnDescriptions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectcreate_edittask);

        tag = getString(R.string.projectEditTask0);

        projectName = getIntent().getExtras().getString("projectName");
        columnPos = getIntent().getExtras().getInt("columnPos");
        itemPos = getIntent().getExtras().getInt("itemPos");
        String columnName = getIntent().getExtras().getString("columnName");
        String itemName = getIntent().getExtras().getString("itemName");

        editDescriptionTxt = findViewById(R.id.descriptionTxt);
        titleMiddle = findViewById(R.id.titleMiddle);
        columnSelector = findViewById(R.id.columnSelector);

        oldDescription = ProjectTableData.GetItemListData(projectName, columnPos, itemPos, 3,this);

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
        ProjectCreateTableEditTaskActivity projectCreateTableEditTask = this;

        TextView issueTypeMainTxt = findViewById(R.id.issueTypeMainTxt);
        ImageButton issueTypeImg = findViewById(R.id.issueTypeImg);
        ImageButton closeBtn = findViewById(R.id.closeBtn);
        Button deleteBtn = findViewById(R.id.deleteBtn);

        titleMiddle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Message.message(context, "change title in code");
            }
        });

        columnSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = v.findViewById(android.R.id.content);
                allColumnTitles.clear();
                allColumnImages.clear();

                allColumnTitles = ProjectTableData.GetAllItemData(projectName, 0, context);

                for (int i = 0; i < allColumnTitles.size(); i++){
                    allColumnImages.add(2131165294);
                }
                Pair<MainRecyclerAdapter, BottomSheetDialog> data = Dialogs.BottomDialogCreator(
                        context, v, viewGroup, "Select a transition", null, allColumnTitles, null, allColumnImages, tag);
                MainRecyclerAdapter adapter = data.first;
                BottomSheetDialog bottomDialog = data.second;

                adapter.projectTableColumnPos = columnPos;
                adapter.projectCreateEditTask_BottomDialog = bottomDialog;
                adapter.projectCreateTableEditTask = projectCreateTableEditTask;
                adapter.itemPos = itemPos;
                adapter.projectName = projectName;
            }
        });

        editDescriptionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( ProjectCreateTableEditTaskActivity.this, ProjectCreateTableEditDescriptionActivity.class);
                intent.putExtra("oldData", oldDescription);
                startActivityForResult(intent, 1); //for getting data back from the second activity
            }
        });

        issueTypeMainTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                issueTypeOnClick(v, context);
            }
        });

        issueTypeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                issueTypeOnClick(v, context);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjectTableData.RemoveTask(projectName, columnPos, itemPos, context);
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalValues.reloadedActivity = true;
                finish();
            }
        });


    }


    public void UpdateColumn(int columnPos){
        //for when you use the button to change the column.
        this.columnPos = columnPos;
        itemPos = 0;

        String columnName = ProjectTableData.GetColumnData(projectName, columnPos, 0, this);
        columnSelector.setText(columnName);
    }

    private void issueTypeOnClick(View v, Context context){
        ViewGroup viewGroup = v.findViewById(android.R.id.content);

        allColumnTitles.clear();
        allColumnImages.clear();
        allColumnDescriptions.clear();

        allColumnTitles.add("Task");
        allColumnImages.add(2131165294);
        allColumnDescriptions.add("A small, distinct piece of work");

        tag = getString(R.string.projectEditTask1);
        Dialogs.BottomDialogCreator(context, v, viewGroup, "Issue Type", "These are the issue types that you can choose, based on the workflow of the current issue type.",
                allColumnTitles, allColumnDescriptions, allColumnImages, tag);
        tag = getString(R.string.projectEditTask0);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //for getting data back from the second activity
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                newData = data.getStringExtra("newData");
                ProjectTableData.SaveDescription(projectName, newData, columnPos, itemPos, this);
                Message.message(getBaseContext(), newData);

                //updating the description
                if (newData != null) {
                    editDescriptionTxt.setText(newData);
                }
            }
        }
    }
}
