package com.aatesting.bugtracker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.aatesting.bugtracker.dialogs.Dialogs;
import com.aatesting.bugtracker.restApi.ApiController;
import com.aatesting.bugtracker.restApi.ApiJSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RoadmapCreateEpicActivity extends AppCompatActivity {
    private final RoadmapCreateEpicActivity activity = this;
    private final Context context = this;
    private String projectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadmap_createepic);

        projectName = getIntent().getExtras().getString("projectName");
        Listeners();
    }

    private void Listeners(){
        Button startDateBtn = findViewById(R.id.startDateBtn);
        Button dueDateBtn = findViewById(R.id.dueDateBtn);
        Button epicTypeBtn = findViewById(R.id.epicTypeBtn);
        TextView titleMid = findViewById(R.id.titleMiddle);
        TextView createTxt = findViewById(R.id.createTxt);
        TextView startDateDescriptionTxt = findViewById(R.id.startDateDescriptionTxt);
        TextView dueDateDescriptionTxt = findViewById(R.id.dueDateDescriptionTxt);
        TextView editDescriptionTxt = findViewById(R.id.descriptionTxt);
        EditText titleEdt = findViewById(R.id.titleEdt);
        ImageButton closeBtn = findViewById(R.id.closeBtn);

        titleMid.setText(projectName);

        String startDateStr = getStartDate(startDateDescriptionTxt, dueDateDescriptionTxt);
        String dueDateStr = getDueDate(startDateStr, dueDateDescriptionTxt);
        startDateDescriptionTxt.setText(startDateStr);
        dueDateDescriptionTxt.setText(dueDateStr);

        epicTypeBtn.setOnClickListener(v -> {
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
                    allColumnTitles, allColumnDescriptions, allColumnImages, null);
        });

        editDescriptionTxt.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProjectTableEditDescriptionActivity.class);
            intent.putExtra("oldData", editDescriptionTxt.getText().toString());
            startActivityForResult(intent, 1); //for getting data back from the second activity
        });

        startDateBtn.setOnClickListener(v -> Dialogs.CalendarDateSetterDialog(context, v, activity, startDateDescriptionTxt.getText().toString(),true));

        dueDateBtn.setOnClickListener(v -> Dialogs.CalendarDateSetterDialog(context, v, activity, dueDateDescriptionTxt.getText().toString(), false));

        createTxt.setOnClickListener(v -> {
            String title = titleEdt.getText().toString();
            String description = editDescriptionTxt.getText().toString();
            String startDateStr1 = getStartDate(startDateDescriptionTxt, dueDateDescriptionTxt);
            String dueDateStr1 = getDueDate(startDateStr1, dueDateDescriptionTxt);

            if (RoadmapEditEpicActivity.forbiddenDates(startDateStr1, dueDateStr1, context)) return;

            ApiJSONObject object = new ApiJSONObject(
                    -1,
                    GlobalValues.projectOpened,
                    title,
                    description,
                    startDateStr1,
                    dueDateStr1,
                    null
            );

            ApiController.createField(object, GlobalValues.ROADMAPS_URL, null, activity);
        });

        closeBtn.setOnClickListener(v -> finish());
    }

    private String getStartDate(TextView startDateDescriptionTxt, TextView dueDateDescriptionTxt) {
        String startDate;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppSettings.SERVER_DATE_FORMAT);
        if (!startDateDescriptionTxt.getText().toString().equals(""))
            startDate = startDateDescriptionTxt.getText().toString();
        else {
            //if it dueDateDescriptionTxt is NOT empty set the startDateDescription 2 weeks before that
            if (!dueDateDescriptionTxt.getText().toString().equals("")){
                try {
                    Date dueDate = simpleDateFormat.parse(dueDateDescriptionTxt.getText().toString());
                    Calendar calendar = GregorianCalendar.getInstance();
                    assert dueDate != null;
                    calendar.setTime(dueDate);
                    calendar.add(Calendar.WEEK_OF_YEAR, -2);
                    startDate = simpleDateFormat.format(calendar.getTime());
                } catch (ParseException e){
                    Message.defErrMessage(context);
                    Log.wtf("ERROR", "failed parsing the start/due date, the parse format is probably changed");
                    e.printStackTrace();
                    startDate = simpleDateFormat.format(GregorianCalendar.getInstance().getTime());
                }
            }
            else {
                startDate = simpleDateFormat.format(GregorianCalendar.getInstance().getTime());
            }
        }
        return startDate;
    }

    private String getDueDate(String startDateStr, TextView dueDateDescriptionTxt) {
        String dueDate;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppSettings.SERVER_DATE_FORMAT);
        if (!dueDateDescriptionTxt.getText().toString().equals(""))
            dueDate = dueDateDescriptionTxt.getText().toString();
        else
        {
            try {
                Date startDate = simpleDateFormat.parse(startDateStr);
                Calendar calendar = GregorianCalendar.getInstance();
                assert startDate != null;
                calendar.setTime(startDate);
                calendar.add(Calendar.WEEK_OF_YEAR, 2);
                dueDate = simpleDateFormat.format(calendar.getTime());
            } catch (ParseException e){
                Message.defErrMessage(context);
                Log.wtf("ERROR", "failed parsing the start/due date, the parse format is probably changed");
                e.printStackTrace();
                dueDate = simpleDateFormat.format(GregorianCalendar.getInstance().getTime());
            }
        }
        return dueDate;
    }

    public void UpdateStartDateDescription(Calendar calendar) {
        TextView startDateDescriptionText = findViewById(R.id.startDateDescriptionTxt);

        SimpleDateFormat df = new SimpleDateFormat(AppSettings.SERVER_DATE_FORMAT);
        String curDate = df.format(calendar.getTime());
        startDateDescriptionText.setText(curDate);
        startDateDescriptionText.setVisibility(View.VISIBLE);
    }

    public void UpdateDueDateDescription(Calendar calendar){
        TextView dueDateDescriptionText = findViewById(R.id.dueDateDescriptionTxt);

        SimpleDateFormat df = new SimpleDateFormat(AppSettings.SERVER_DATE_FORMAT);
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