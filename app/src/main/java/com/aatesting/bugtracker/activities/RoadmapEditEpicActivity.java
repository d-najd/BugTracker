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

import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.data.RoadmapEpicData;
import com.aatesting.bugtracker.dialogs.Dialogs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RoadmapEditEpicActivity extends AppCompatActivity {
    private RoadmapEditEpicActivity activity = this;
    private String projectName;
    private int epicId;
    private Context mcontext;

    private String startDateStr;
    private String dueDateStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadmap_editepic);
        mcontext = this;

        projectName = getIntent().getExtras().getString("projectName");
        epicId = getIntent().getExtras().getInt("epicId");

        SetupActivityValues();
        Listeners();
    }

    private void SetupActivityValues(){
        String title = RoadmapEpicData.GetSpecificEpicData(projectName, epicId, 0, this);
        String description = RoadmapEpicData.GetSpecificExtrasEpicData(projectName, epicId, 0, this);
        startDateStr = RoadmapEpicData.GetSpecificEpicData(projectName, epicId, 1, this);
        dueDateStr = RoadmapEpicData.GetSpecificEpicData(projectName, epicId, 2, this);

        TextView titleMid = findViewById(R.id.titleMiddle);
        TextView descriptionTxt = findViewById(R.id.descriptionTxt);
        TextView startDateDescriptionTxt = findViewById(R.id.startDateDescriptionTxt);
        TextView dueDateDescriptionTxt = findViewById(R.id.dueDateDescriptionTxt);

        titleMid.setText(title);
        descriptionTxt.setText(description);
        startDateDescriptionTxt.setText(startDateStr);
        dueDateDescriptionTxt.setText(dueDateStr);
    }

    private void Listeners(){
        Button startDateBtn = findViewById(R.id.startDateBtn);
        Button dueDateBtn = findViewById(R.id.dueDateBtn);
        Button deleteBtn = findViewById(R.id.deleteBtn);
        ImageButton closeBtn = findViewById(R.id.closeBtn);
        ImageButton issueTypeImb = findViewById(R.id.issueTypeImb);
        EditText titleMiddle = findViewById(R.id.titleMiddle);
        TextView editDescriptionTxt = findViewById(R.id.descriptionTxt);
        TextView issueTypeMainTxt = findViewById(R.id.issueTypeMainTxt);


        editDescriptionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, ProjectCreateTableEditDescriptionActivity.class);

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
                Dialogs.CalendarDateSetterDialog(mcontext, v, activity, startDateStr, true);
            }
        });

        dueDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.CalendarDateSetterDialog(mcontext, v, activity, dueDateStr, false);
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
                RoadmapEpicData.EditEpic("Testing", epicId, 0, s.toString(),  mcontext);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.DeleteTaskDialog(mcontext, "Delete this issue?", "deleting this " +
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
                allColumnDescriptions.add("A small, distinct piece of work, or not");

                Dialogs.BottomDialogCreator(mcontext, v, viewGroup, "Issue Type",
                        "These are the issue types that you can choose, based on the workflow of the current issue type.",
                        allColumnTitles, allColumnDescriptions, allColumnImages, "exampletag");
            }
        });
    }


    public void UpdateStartDateDescription(Calendar calendar) {
        TextView startDateDescriptionText = findViewById(R.id.startDateDescriptionTxt);

        SimpleDateFormat df = new SimpleDateFormat(mcontext.getString(R.string.storageDateFormat));
        String curDate = df.format(calendar.getTime());
        startDateDescriptionText.setText(curDate);
        startDateDescriptionText.setVisibility(View.VISIBLE);

        RoadmapEpicData.EditEpic("Testing", epicId, 1, curDate, mcontext);
    }

    public void UpdateDueDateDescription(Calendar calendar){
        TextView dueDateDescriptionText = findViewById(R.id.dueDateDescriptionTxt);

        SimpleDateFormat df = new SimpleDateFormat(mcontext.getString(R.string.storageDateFormat));
        String curDate = df.format(calendar.getTime());
        dueDateDescriptionText.setText(curDate);
        dueDateDescriptionText.setVisibility(View.VISIBLE);

        RoadmapEpicData.EditEpic("Testing", epicId, 2, curDate, mcontext);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //for getting data back from the description activity
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String newData = data.getStringExtra("newData");
                Message.message(getBaseContext(), newData);
                TextView editDescriptionTxt = findViewById(R.id.descriptionTxt);

                //updating the description
                if (newData != null) {
                    editDescriptionTxt.setText(newData);
                    RoadmapEpicData.EditEpicExtras("Testing", epicId, 0, newData, mcontext);
                }
            }
        }
    }

    public void DeleteEpic(){
        RoadmapEpicData.RemoveEpic(projectName, epicId, mcontext);
        Message.message(mcontext, "Epic succesfully removed");
        finish();
    }
}
