package com.aatesting.bugtracker.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.dialogs.BasicDialogs;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RoadmapCreateEpicActivity extends AppCompatActivity {
    RoadmapCreateEpicActivity activity = this;
    Context mcontext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadmap_addepic);

        Listeners();
    }

    private void Listeners(){
        Button startDateButton = findViewById(R.id.startDateButton);
        Button dueDateButton = findViewById(R.id.dueDateButton);

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
    }

    public void UpdateStartDateDescription(Calendar calendar) {
        TextView startDateDescriptionText = findViewById(R.id.startDateDescriptionText);

        SimpleDateFormat df = new SimpleDateFormat("yyyy'-'MM'-'dd");
        String curDate = df.format(calendar.getTime());
        startDateDescriptionText.setText(curDate);
        startDateDescriptionText.setVisibility(View.VISIBLE);
    }

    public void UpdateDueDateDescription(Calendar calendar){
        TextView dueDateDescriptionText = findViewById(R.id.dueDateDescriptionText);

        SimpleDateFormat df = new SimpleDateFormat("yyyy'-'MM'-'dd");
        String curDate = df.format(calendar.getTime());
        dueDateDescriptionText.setText(curDate);
        dueDateDescriptionText.setVisibility(View.VISIBLE);
    }
}
