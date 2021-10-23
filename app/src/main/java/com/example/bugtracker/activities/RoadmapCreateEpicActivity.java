package com.example.bugtracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bugtracker.R;

public class RoadmapCreateEpicActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadmap_addepic);

        Listeners();
    }

    private void Listeners(){
        Button startDateButton = findViewById(R.id.startDateButton);
        Button dueDate = findViewById(R.id.dueDateButton);

        startDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //TODO optimize datetime1 (datesetter) so it only needs to check if smtn is pressed, basicdialogs should be of help and its inside

            //TODO ALSO ITS USED IN OTHER ACTIVITIES SO DON'T FORGET TO MAKE THEM USE THE OPTIMIZED VERSION
            }
        });
    }
}
