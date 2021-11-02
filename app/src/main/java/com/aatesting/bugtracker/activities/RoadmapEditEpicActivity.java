package com.aatesting.bugtracker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.data.RoadmapCreateEpicData;

public class RoadmapEditEpicActivity extends AppCompatActivity {

    private String projectName;
    private int epicId;
    private Context mcontext;

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
        String title = RoadmapCreateEpicData.GetSpecificEpicData(projectName, epicId, 0, this);
        String startDateStr = RoadmapCreateEpicData.GetSpecificEpicData(projectName, epicId, 1, this);
        String dueDateStr = RoadmapCreateEpicData.GetSpecificEpicData(projectName, epicId, 2, this);

        TextView titleMid = findViewById(R.id.titleMiddle);
        TextView startDateDescriptionTxt = findViewById(R.id.startDateDescriptionTxt);
        TextView dueDateDescriptionTxt = findViewById(R.id.dueDateDescriptionTxt);

        titleMid.setText(title);
        startDateDescriptionTxt.setText(startDateStr);
        dueDateDescriptionTxt.setText(dueDateStr);
    }

    private void Listeners(){
        TextView editDescriptionTxt = findViewById(R.id.descriptionTxt);


        editDescriptionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, ProjectCreateTableEditDescriptionActivity.class);
                //TODO make it so it doesnt pass description if its empty
                intent.putExtra("oldData", editDescriptionTxt.getText().toString());
                startActivityForResult(intent, 1); //for getting data back from the second activity
            }
        });
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
