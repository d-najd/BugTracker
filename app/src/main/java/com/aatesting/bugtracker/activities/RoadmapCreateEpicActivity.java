package com.aatesting.bugtracker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.data.ProjectCreateTableData;
import com.aatesting.bugtracker.data.RoadmapCreateEpicData;
import com.aatesting.bugtracker.dialogs.BasicDialogs;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RoadmapCreateEpicActivity extends AppCompatActivity {
    RoadmapCreateEpicActivity activity = this;
    Context mcontext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadmap_createepic);

        Listeners();
    }

    private void Listeners(){
        Button startDateButton = findViewById(R.id.startDateButton);
        Button dueDateButton = findViewById(R.id.dueDateButton);
        TextView createTxt = findViewById(R.id.createTxt);
        TextView startDateDescriptionTxt = findViewById(R.id.startDateDescriptionTxt);
        TextView dueDateDescriptionTxt = findViewById(R.id.dueDateDescriptionTxt);
        TextView editDescriptionTxt = findViewById(R.id.descriptionTxt);
        EditText titleEdt = findViewById(R.id.titleEdt);


        editDescriptionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, ProjectCreateTableEditDescriptionActivity.class);
                //TODO make it so it doesnt pass description if its empty
                intent.putExtra("oldData", editDescriptionTxt.getText().toString());
                startActivityForResult(intent, 1); //for getting data back from the second activity
            }
        });


        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BasicDialogs.CalendarDateSetterDialog(mcontext, v, activity, true);
            }
        });

        dueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BasicDialogs.CalendarDateSetterDialog(mcontext, v, activity, false);
            }
        });

        createTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO add checker to check if endDatetime is bigger than startDatetime
                String title = titleEdt.getText().toString();
                String description = editDescriptionTxt.getText().toString();
                String startDate = startDateDescriptionTxt.getText().toString();
                String dueDate = dueDateDescriptionTxt.getText().toString();

                RoadmapCreateEpicData.MakeFolders(mcontext);
                RoadmapCreateEpicData.SaveNewEpic("Testing", title, description, startDate,
                        dueDate, mcontext);
                finish();
            }
        });
    }

    public void UpdateStartDateDescription(Calendar calendar) {
        TextView startDateDescriptionText = findViewById(R.id.startDateDescriptionTxt);

        SimpleDateFormat df = new SimpleDateFormat("dd'-'MM'-'yyyy");
        String curDate = df.format(calendar.getTime());
        startDateDescriptionText.setText(curDate);
        startDateDescriptionText.setVisibility(View.VISIBLE);
    }

    public void UpdateDueDateDescription(Calendar calendar){
        TextView dueDateDescriptionText = findViewById(R.id.dueDateDescriptionTxt);

        SimpleDateFormat df = new SimpleDateFormat("dd'-'MM'-'yyyy");
        String curDate = df.format(calendar.getTime());
        dueDateDescriptionText.setText(curDate);
        dueDateDescriptionText.setVisibility(View.VISIBLE);
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
