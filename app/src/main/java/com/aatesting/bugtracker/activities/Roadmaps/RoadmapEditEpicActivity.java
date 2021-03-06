package com.aatesting.bugtracker.activities.Roadmaps;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.aatesting.bugtracker.activities.Projects.ProjectTableEditDescriptionActivity;
import com.aatesting.bugtracker.dialogs.Dialogs;
import com.aatesting.bugtracker.restApi.ApiController;
import com.aatesting.bugtracker.restApi.ApiJSONObject;
import com.aatesting.bugtracker.restApi.ApiSingleton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RoadmapEditEpicActivity extends AppCompatActivity {
    private final RoadmapEditEpicActivity activity = this;
    private int epicId;
    private Context context;

    private ApiJSONObject apiJsonObject;

    TextView startDateTxt, dueDateTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadmap_editepic);
        context = this;

        try {
            epicId = getIntent().getExtras().getInt("epicId");
            apiJsonObject = ApiSingleton.getInstance().getObject(epicId, GlobalValues.ROADMAPS_URL);
        } catch (Exception e){
            Log.wtf("ERROR", "Failed to get data");
            Message.defErrMessage(this);

            epicId = 1;
        }

        SetupActivityValues();
        Listeners();
    }

    private void SetupActivityValues(){
        String title, description;
        try {
            title = apiJsonObject.getTitle();
            description = apiJsonObject.getDescription();
        } catch (Exception e){
            Log.wtf("ERROR", "Failed to get data");
            Message.defErrMessage(this);

            title = "ERROR";
            description = "ERROR";
        }

        TextView titleMid = findViewById(R.id.titleMiddle);
        TextView descriptionTxt = findViewById(R.id.descriptionTxt);
        startDateTxt = findViewById(R.id.startDateDescriptionTxt);
        dueDateTxt = findViewById(R.id.dueDateDescriptionTxt);

        titleMid.setText(title);
        descriptionTxt.setText(description);
        try {
            startDateTxt.setText(apiJsonObject.getStartDate());
            dueDateTxt.setText(apiJsonObject.getDueDate());
        } catch (Exception e){
            Log.wtf("ERROR", "Failed to get data");
            Message.defErrMessage(this);
            startDateTxt.setText("ERROR");
            dueDateTxt.setText("ERROR");
        }
    }

    private void Listeners(){
        Button startDateBtn = findViewById(R.id.startDateBtn);
        Button dueDateBtn = findViewById(R.id.dueDateBtn);
        Button deleteBtn = findViewById(R.id.deleteBtn);
        ImageButton closeBtn = findViewById(R.id.closeBtn);
        EditText titleMiddle = findViewById(R.id.titleMiddle);
        TextView editDescriptionTxt = findViewById(R.id.descriptionTxt);
        TextView issueTypeMainTxt = findViewById(R.id.roleMainTxt);

        TextView startDateDescTxt = findViewById(R.id.startDateDescriptionTxt);
        TextView dueDateDescTxt = findViewById(R.id.dueDateDescriptionTxt);

        editDescriptionTxt.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProjectTableEditDescriptionActivity.class);

            intent.putExtra("oldData", editDescriptionTxt.getText().toString());
            startActivityForResult(intent, 1); //for getting data back from the second activity
        });

        closeBtn.setOnClickListener(v -> finish());

        startDateBtn.setOnClickListener(v -> Dialogs.CalendarDateSetterDialog(context, v, activity, startDateDescTxt.getText().toString(), true));

        dueDateBtn.setOnClickListener(v -> Dialogs.CalendarDateSetterDialog(context, v, activity, dueDateDescTxt.getText().toString(), false));

        titleMiddle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ApiJSONObject object = ApiSingleton.getInstance().getObject(epicId, GlobalValues.ROADMAPS_URL);

                object.setTitle(s.toString());
                GlobalValues.objectModified = object;
            }
        });

        deleteBtn.setOnClickListener(v -> Dialogs.DeleteTaskDialog(context, "Delete this issue?", "deleting this " +
                "issue permanently erases the issue, including all subtasks", "CANCEL", "DELETE", activity));

        issueTypeMainTxt.setOnClickListener(v -> {
            ViewGroup viewGroup = v.findViewById(android.R.id.content);

            ArrayList<String> allColumnTitles = new ArrayList<>();
            ArrayList<String> allColumnDescriptions = new ArrayList<>();
            ArrayList<Integer> allColumnImages = new ArrayList<>();

            allColumnTitles.add("Epic");
            allColumnImages.add(2131165294);
            allColumnDescriptions.add("A big, complex set of problems");

            allColumnTitles.add("Hybrid epic");
            allColumnImages.add(2131165294);
            allColumnDescriptions.add("An epic which acts like a column");

            allColumnTitles.add("Task");
            allColumnImages.add(2131165294);
            allColumnDescriptions.add("A small, distinct piece of work");

            Dialogs.BottomDialogCreator(context, v, viewGroup, "Issue Type",
                    "These are the issue types that you can choose, based on the workflow of the current issue type.",
                    allColumnTitles, allColumnDescriptions, allColumnImages, "exampletag");
        });
    }

    public void UpdateStartDateDescription(Calendar calendar) {
        TextView startDateDescriptionText = findViewById(R.id.startDateDescriptionTxt);

        SimpleDateFormat df = new SimpleDateFormat(AppSettings.SERVER_DATE_FORMAT);
        String curDate = df.format(calendar.getTime());

        if (forbiddenDates(curDate, dueDateTxt.getText().toString(), context)) return;

        startDateDescriptionText.setText(curDate);
        startDateDescriptionText.setVisibility(View.VISIBLE);

        apiJsonObject.setStartDate(curDate);
        GlobalValues.objectModified = apiJsonObject;
    }

    public void UpdateDueDateDescription(Calendar calendar){
        TextView dueDateDescriptionText = findViewById(R.id.dueDateDescriptionTxt);

        SimpleDateFormat df = new SimpleDateFormat(AppSettings.SERVER_DATE_FORMAT);
        String curDate = df.format(calendar.getTime());

        if (forbiddenDates(startDateTxt.getText().toString(), curDate, context)) return;

        dueDateDescriptionText.setText(curDate);
        dueDateDescriptionText.setVisibility(View.VISIBLE);

        apiJsonObject.setDueDate(curDate);
        GlobalValues.objectModified = apiJsonObject;
    }

    /*
     NOTE forbidden dates atm are dates where the start date is bigger than the due date ex:
                start 2020-01-01 due 2000-01-01 this is forbidden date.
     */

    public static boolean forbiddenDates(String startDateStr, String dueDateStr, Context context) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppSettings.SERVER_DATE_FORMAT);
        try {
            Date dueDate = simpleDateFormat.parse(startDateStr);
            Date startDate = simpleDateFormat.parse(dueDateStr);
            assert dueDate != null;
            assert startDate != null;

            long timeDifference = dueDate.getTime() - startDate.getTime();
            if (timeDifference >= 0){
                Message.message(context, "Please select a due date which is bigger than the start date");
                return true;
            }
        } catch (ParseException e){
            Message.defErrMessage(context);
            Log.wtf("ERROR", "WHAT THE ACTUAL FUCK? DID YOU CHANGE THE DATE FORMAT SOMEWHERE?");
        }
        return false;
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
                    GlobalValues.objectModified = apiJsonObject;
                }
            }
        }
    }

    public void DeleteEpic(){
        ApiController.removeField(null, activity, null, GlobalValues.ROADMAPS_URL + "/" +
                ApiSingleton.getInstance().getObject(epicId, GlobalValues.ROADMAPS_URL).getId(), null);
        Message.message(context, "Epic removed successfully");
    }
}
