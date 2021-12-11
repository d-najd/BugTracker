package com.aatesting.bugtracker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aatesting.bugtracker.AppSettings;
import com.aatesting.bugtracker.GlobalValues;
import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.dialogs.Dialogs;
import com.aatesting.bugtracker.restApi.ApiController;
import com.aatesting.bugtracker.restApi.ApiJSONObject;
import com.aatesting.bugtracker.restApi.ApiSingleton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RoadmapEditEpicActivity extends AppCompatActivity {
    private RoadmapEditEpicActivity activity = this;
    private String projectName;
    private int epicId;
    private Context context;

    private ApiJSONObject apiJsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadmap_editepic);
        context = this;

        epicId = getIntent().getExtras().getInt("epicId");
        apiJsonObject = ApiSingleton.getInstance().getObject(epicId);
        projectName = getIntent().getExtras().getString("projectName");

        SetupActivityValues();
        Listeners();
    }

    private void SetupActivityValues(){
        String title = apiJsonObject.getTitle();
        String description = apiJsonObject.getDescription();

        TextView titleMid = findViewById(R.id.titleMiddle);
        TextView descriptionTxt = findViewById(R.id.descriptionTxt);
        TextView startDateDescriptionTxt = findViewById(R.id.startDateDescriptionTxt);
        TextView dueDateDescriptionTxt = findViewById(R.id.dueDateDescriptionTxt);

        titleMid.setText(title);
        descriptionTxt.setText(description);
        startDateDescriptionTxt.setText(apiJsonObject.getStartDate());
        dueDateDescriptionTxt.setText(apiJsonObject.getDueDate());
    }

    private void Listeners(){
        Button startDateBtn = findViewById(R.id.startDateBtn);
        Button dueDateBtn = findViewById(R.id.dueDateBtn);
        Button deleteBtn = findViewById(R.id.deleteBtn);
        ImageButton closeBtn = findViewById(R.id.closeBtn);
        EditText titleMiddle = findViewById(R.id.titleMiddle);
        TextView editDescriptionTxt = findViewById(R.id.descriptionTxt);
        TextView issueTypeMainTxt = findViewById(R.id.issueTypeMainTxt);

        TextView startDateDescTxt = findViewById(R.id.startDateDescriptionTxt);
        TextView dueDateDescTxt = findViewById(R.id.dueDateDescriptionTxt);

        editDescriptionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProjectTableEditDescriptionActivity.class);

                intent.putExtra("oldData", editDescriptionTxt.getText().toString());
                startActivityForResult(intent, 1); //for getting data back from the second activity
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.CalendarDateSetterDialog(context, v, activity, startDateDescTxt.getText().toString(), true);
            }
        });

        dueDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.CalendarDateSetterDialog(context, v, activity, dueDateDescTxt.getText().toString(), false);
            }
        });

        titleMiddle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ApiSingleton.getInstance().getObject(epicId).setTitle(s.toString());
                GlobalValues.fieldModified = epicId;
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.DeleteTaskDialog(context, "Delete this issue?", "deleting this " +
                        "issue permanently erases the issue, including all subtasks", "CANCEL", "DELETE", activity);
            }
        });

        issueTypeMainTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = v.findViewById(android.R.id.content);

                ArrayList<String> allColumnTitles = new ArrayList<>();
                ArrayList<String> allColumnDescriptions = new ArrayList<>();
                ArrayList<Integer> allColumnImages = new ArrayList<>();

                allColumnTitles.add("Epic");
                allColumnImages.add(2131165294);
                allColumnDescriptions.add("A big, complex set of problems");

                allColumnTitles.add("Hybrid epic");
                allColumnImages.add(2131165294);
                allColumnDescriptions.add("An epic which acts like a column in the board of the project");

                allColumnTitles.add("Task");
                allColumnImages.add(2131165294);
                allColumnDescriptions.add("A small, distinct piece of work");

                Dialogs.BottomDialogCreator(context, v, viewGroup, "Issue Type",
                        "These are the issue types that you can choose, based on the workflow of the current issue type.",
                        allColumnTitles, allColumnDescriptions, allColumnImages, "exampletag");
            }
        });
    }

    public void UpdateStartDateDescription(Calendar calendar) {
        TextView startDateDescriptionText = findViewById(R.id.startDateDescriptionTxt);

        SimpleDateFormat df = new SimpleDateFormat(AppSettings.SERVER_DATE_FORMAT);
        String curDate = df.format(calendar.getTime());
        startDateDescriptionText.setText(curDate);
        startDateDescriptionText.setVisibility(View.VISIBLE);

        ApiSingleton.getInstance().getObject(epicId).setStartDate(curDate);
        GlobalValues.fieldModified = epicId;
    }

    public void UpdateDueDateDescription(Calendar calendar){
        TextView dueDateDescriptionText = findViewById(R.id.dueDateDescriptionTxt);

        SimpleDateFormat df = new SimpleDateFormat(AppSettings.SERVER_DATE_FORMAT);
        String curDate = df.format(calendar.getTime());
        dueDateDescriptionText.setText(curDate);
        dueDateDescriptionText.setVisibility(View.VISIBLE);

        ApiSingleton.getInstance().getObject(epicId).setDueDate(curDate);
        GlobalValues.fieldModified = epicId;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String newData = data.getStringExtra("newData");
                TextView editDescriptionTxt = findViewById(R.id.descriptionTxt);

                //updating the description
                if (newData != null) {
                    editDescriptionTxt.setText(newData);

                    apiJsonObject.setDescription(newData);
                    GlobalValues.fieldModified = epicId;
                }
            }
        }
    }

    public void DeleteEpic(){
        ApiController.removeRoadmap(epicId, activity, "roadmaps");
        Message.message(context, "Epic removed successfully");
    }
}
