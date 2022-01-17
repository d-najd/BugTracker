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
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.dialogs.Dialogs;
import com.aatesting.bugtracker.recyclerview.Adapters.MainRecyclerAdapter;
import com.aatesting.bugtracker.recyclerview.RecyclerData;
import com.aatesting.bugtracker.restApi.ApiController;
import com.aatesting.bugtracker.restApi.ApiJSONObject;
import com.aatesting.bugtracker.restApi.ApiSingleton;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class ProjectTableEditTaskActivity extends AppCompatActivity {
    private TextView editDescriptionTxt;
    private TextView topSave;
    private EditText titleMiddle;
    private Button columnSelector;
    public String newData; //the data (string) for the description
    //to get the correct descrition and stuff instead of carrying it for ages.
    private String tag; //there are multiple tags for this activity bc of the bottomdialogs
    private ArrayList<RecyclerData> recyclerDataArrayList = new ArrayList<>();

    private int itemPos;
    private int columnPos;
    private String projectName;
    private String description;
    private String columnName;
    private String itemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectcreate_edittask);

        getData();

        editDescriptionTxt = findViewById(R.id.descriptionTxt);
        topSave = findViewById(R.id.topSave);
        titleMiddle = findViewById(R.id.titleMiddle);
        columnSelector = findViewById(R.id.columnSelector);

        columnSelector.setText(columnName);
        titleMiddle.setText(itemName);

        //setting the description
        if (description == null) {
            editDescriptionTxt.setText(getString(R.string.description));
            editDescriptionTxt.setTextColor(getColor(R.color.white38));
        }
        else {
            editDescriptionTxt.setText(description);
            editDescriptionTxt.setTextColor(getColor(R.color.white60));
        }
        Listeners();
    }

    private void getData() {
        tag = getString(R.string.projectEditTask);

        columnPos = getIntent().getExtras().getInt("columnPos");
        itemPos = getIntent().getExtras().getInt("itemPos");
        
        ApiJSONObject object = ApiSingleton.getInstance().getObject(columnPos, GlobalValues.BOARDS_URL).getTask(itemPos);

        projectName = object.getTitle();
        columnName = ApiSingleton.getInstance().getObject(columnPos, GlobalValues.BOARDS_URL).getTitle();
        itemName = object.getTitle();
        description = object.getDescription();
    }

    private void Listeners(){
        Context context = this;
        ProjectTableEditTaskActivity projectCreateTableEditTask = this;

        TextView issueTypeMainTxt = findViewById(R.id.roleMainTxt);
        ImageButton issueTypeImg = findViewById(R.id.roleImg);
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
                //the task
                ApiJSONObject taskObj = ApiSingleton.getInstance().getObject(columnPos, GlobalValues.BOARDS_URL).getTask(itemPos);
                taskObj.setTitle(s.toString());

                topSave.setVisibility(View.VISIBLE);
                GlobalValues.objectModified = taskObj;
            }
        });

        topSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiController.editField(null, null, GlobalValues.TASKS_URL);
                topSave.setVisibility(View.GONE);
            }
        });

        columnSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = v.findViewById(android.R.id.content);
                ArrayList<String> allColumnTitles = new ArrayList<>();
                ArrayList<Integer> allColumnImages = new ArrayList<>();

                int size = ApiSingleton.getInstance().getArray(GlobalValues.BOARDS_URL).size();

                for (ApiJSONObject object : ApiSingleton.getInstance().getArray(GlobalValues.BOARDS_URL)){
                    allColumnTitles.add(object.getTitle());
                }

                for (int i = 0; i < allColumnTitles.size(); i++){
                    allColumnImages.add(2131165294);
                }
                Pair<MainRecyclerAdapter, BottomSheetDialog> data = Dialogs.BottomDialogCreator(
                        context, v, viewGroup, "Select a transition", null, allColumnTitles, null, allColumnImages, context.getString(R.string.TEDTBottomDialog));
                MainRecyclerAdapter adapter = data.first;
                BottomSheetDialog bottomDialog = data.second;

                adapter.projectTableColumnPos = columnPos;
                adapter.itemPos = itemPos;
                adapter.projectName = projectName;
                adapter.activity = projectCreateTableEditTask;
            }
        });

        editDescriptionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( ProjectTableEditTaskActivity.this, ProjectTableEditDescriptionActivity.class);
                intent.putExtra("oldData", description);
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
                int columnId = ApiSingleton.getInstance().getObject(columnPos, GlobalValues.BOARDS_URL).getId();
                int taskId = ApiSingleton.getInstance().getObject(columnPos, GlobalValues.BOARDS_URL).getTask(itemPos).getId();
                ApiController.removeField(null, projectCreateTableEditTask, null,
                        GlobalValues.TASKS_URL + "/" + taskId);
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

    private void issueTypeOnClick(View v, Context context){
        ViewGroup viewGroup = v.findViewById(android.R.id.content);

        ArrayList<String> allColumnTitles = new ArrayList<>();
        ArrayList<String> allColumnDescriptions = new ArrayList<>();
        ArrayList<Integer> allColumnImages = new ArrayList<>();

        allColumnTitles.clear();
        allColumnImages.clear();
        allColumnDescriptions.clear();

        allColumnTitles.add("Task");
        allColumnImages.add(2131165294);
        allColumnDescriptions.add("A small, distinct piece of work");

        Dialogs.BottomDialogCreator(context, v, viewGroup, "Issue Type", "These are the issue types that you can choose, based on the workflow of the current issue type.",
                allColumnTitles, allColumnDescriptions, allColumnImages, tag); //TEDT = Table EDit Task
        tag = getString(R.string.projectEditTask);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //for getting data back from the second activity
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                newData = data.getStringExtra("newData");

                //updating the description
                if (newData != null) {
                    editDescriptionTxt.setText(newData);

                    ApiJSONObject taskObj = ApiSingleton.getInstance().getObject(columnPos, GlobalValues.BOARDS_URL).getTask(itemPos);
                    taskObj.setDescription(newData);

                    topSave.setVisibility(View.VISIBLE);
                    GlobalValues.objectModified = taskObj;
                }
            }
        }
    }
}
