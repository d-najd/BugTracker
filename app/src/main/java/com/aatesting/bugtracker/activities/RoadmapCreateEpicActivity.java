package com.aatesting.bugtracker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
        Button startDateBtn = findViewById(R.id.startDateBtn);
        Button dueDateBtn = findViewById(R.id.dueDateBtn);
        TextView createTxt = findViewById(R.id.createTxt);
        TextView startDateDescriptionTxt = findViewById(R.id.startDateDescriptionTxt);
        TextView dueDateDescriptionTxt = findViewById(R.id.dueDateDescriptionTxt);
        TextView editDescriptionTxt = findViewById(R.id.descriptionTxt);
        EditText titleEdt = findViewById(R.id.titleEdt);
        ImageButton closeBtn = findViewById(R.id.closeBtn);

        editDescriptionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, ProjectCreateTableEditDescriptionActivity.class);
                //TODO make it so it doesnt pass description if its empty
                intent.putExtra("oldData", editDescriptionTxt.getText().toString());
                startActivityForResult(intent, 1); //for getting data back from the second activity
            }
        });


        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.CalendarDateSetterDialog(mcontext, v, activity, startDateDescriptionTxt.getText().toString(),true);
            }
        });

        dueDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.CalendarDateSetterDialog(mcontext, v, activity, dueDateDescriptionTxt.getText().toString(), false);
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

                RoadmapEpicData.MakeFolders(mcontext);
                RoadmapEpicData.SaveNewEpic("Testing", title, description, startDate,
                        dueDate, mcontext);
                finish();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void UpdateStartDateDescription(Calendar calendar) {
        TextView startDateDescriptionText = findViewById(R.id.startDateDescriptionTxt);


        //TODO this might be broken
        SimpleDateFormat df = new SimpleDateFormat(mcontext.getString(R.string.storageDateFormat));
        String curDate = df.format(calendar.getTime());
        startDateDescriptionText.setText(curDate);
        startDateDescriptionText.setVisibility(View.VISIBLE);
    }

    public void UpdateDueDateDescription(Calendar calendar){
        TextView dueDateDescriptionText = findViewById(R.id.dueDateDescriptionTxt);

        SimpleDateFormat df = new SimpleDateFormat(mcontext.getString(R.string.storageDateFormat));
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
                if (newData != null) {
                    editDescriptionTxt.setText(newData);
                }
            }
        }
    }
}
