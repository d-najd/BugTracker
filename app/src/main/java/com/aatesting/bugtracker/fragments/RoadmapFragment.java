package com.aatesting.bugtracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aatesting.bugtracker.GlobalValues;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.ProjectsMainActivity;
import com.aatesting.bugtracker.data.RoadmapEpicData;
import com.aatesting.bugtracker.recyclerview.Adapters.RoadmapWeeksAdapter;
import com.aatesting.bugtracker.recyclerview.Adapters.RoadmapEpicsAdapter;
import com.aatesting.bugtracker.recyclerview.RecyclerData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RoadmapFragment extends Fragment {

    private Context mcontext;
    private View root;
    private ArrayList<RecyclerData> epicsRecyclerDataArrayList = new ArrayList<>();;
    private ArrayList<RecyclerData> weeksRecyclerDataArrayList = new ArrayList<>();;
    private String tag;
    private String projectName; //data is passed through intent
    private Calendar calendarCurDate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_roadmap, container, false);
        mcontext = getContext();
        projectName = getActivity().getIntent().getExtras().getString("projectName");

        RecyclerView recyclerView = root.findViewById(R.id.epicsRecyclerView);
        tag = recyclerView.getTag().toString();

        ((ProjectsMainActivity)getActivity()).Listeners(1); // for knowing which fragment is selected
        WeeksBarRecycler();
        EpicsRecycler();

        ViewGroup.LayoutParams scrollViewLength = root.findViewById(R.id.scrollViewLength).getLayoutParams();

        scrollViewLength.width = (int) (Math.round(10 * getResources().getDimension(R.dimen.activity_roadmap_weeks_width)));

        return root;
    }

    private RecyclerView WeeksBarRecycler(){
        weeksRecyclerDataArrayList = new ArrayList<>();

        RecyclerView recyclerView = root.findViewById(R.id.weeksRecyclerView);
        String tag = recyclerView.getTag().toString();

        Date earliestDate = RoadmapEpicData.GetEarliestOrLatestDate(projectName, mcontext, true);
        Date latestDate = RoadmapEpicData.GetEarliestOrLatestDate(projectName, mcontext, false);

        Calendar calendarEarliestDate = GregorianCalendar.getInstance();
        calendarEarliestDate.setTime(earliestDate);
        calendarEarliestDate.set(Calendar.WEEK_OF_YEAR, calendarEarliestDate.get(Calendar.WEEK_OF_YEAR) - 1);
        earliestDate = calendarEarliestDate.getTime();

        calendarCurDate = GregorianCalendar.getInstance(); //calendar that is used for knowing which week is next to be added and which is current
        calendarCurDate.setTime(earliestDate);

        for (int i = 0; i < 10; i++){
            calendarCurDate.add(Calendar.WEEK_OF_YEAR, 1);
            DateFormat df = new SimpleDateFormat("'w'ww");
            String startDate = df.format(calendarCurDate.getTime());
            weeksRecyclerDataArrayList.add(new RecyclerData(startDate, tag));
        }

        AddMoreWeeks(calendarCurDate.getTime(), latestDate, tag);

        RoadmapWeeksAdapter adapter = new RoadmapWeeksAdapter(weeksRecyclerDataArrayList, mcontext);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext, RecyclerView.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        GlobalValues.weeksRoadmapLen = weeksRecyclerDataArrayList.size();

        return recyclerView;
    }

    private void AddMoreWeeks(Date earliestDate, Date latestDate, String tag) {
        //if there are more weeks needed in the weeksRecycler, the minimum is 10 if it passed that
        // then the code below will add as much as needed to the first 10
        //if there are more weeks needed

        long timeDifference;
        timeDifference = latestDate.getTime() - earliestDate.getTime();

        if (timeDifference > 0)
        {
            long weeksDifference = timeDifference / (24 * 60 * 60 * 1000);

            if (weeksDifference % 7 != 0)
                weeksDifference = weeksDifference / 7 + 1;
            else
                weeksDifference = weeksDifference / 7;

            for (int i = 0; i < weeksDifference; i++){
                calendarCurDate.add(Calendar.WEEK_OF_YEAR, 1);
                DateFormat df = new SimpleDateFormat("'w'ww");
                String startDate = df.format(calendarCurDate.getTime());
                weeksRecyclerDataArrayList.add(new RecyclerData(startDate, tag));
            }
        }
    }

    private void EpicsRecycler(){
        epicsRecyclerDataArrayList = new ArrayList<>();;

        RecyclerView recyclerView = root.findViewById(R.id.epicsRecyclerView);
        Date earliestDate = RoadmapEpicData.GetEarliestOrLatestDate(projectName, mcontext, true);

        Calendar calendarEarliestDate = GregorianCalendar.getInstance();
        calendarEarliestDate.setTime(earliestDate);
        calendarEarliestDate.set(Calendar.WEEK_OF_YEAR, calendarEarliestDate.get(Calendar.WEEK_OF_YEAR) - 1);
        //do not ask how this line works or why it works, but if you remove it the roadmap adapter will
        //think that starting monday-today that the earliest epic is monday.
        calendarEarliestDate.set(Calendar.DAY_OF_YEAR, calendarEarliestDate.get(Calendar.DAY_OF_YEAR)
                - calendarEarliestDate.get(Calendar.DAY_OF_WEEK) + 2);
        Date weeksStartDate = calendarEarliestDate.getTime();

        //GetEpicsFromStorage puts the data in recyclerDataArrayList
        GetEpicsFromStorage();
        RoadmapEpicsAdapter adapter = new RoadmapEpicsAdapter(epicsRecyclerDataArrayList, weeksStartDate, projectName, mcontext);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void GetEpicsFromStorage(){
        String data = RoadmapEpicData.GetData(projectName, mcontext);
        int amountOfPartsInData = RoadmapEpicData.AMOUNT_OF_PARTS_IN_DATA;

        SimpleDateFormat df = new SimpleDateFormat(mcontext.getString(R.string.storageDateFormat));

        if (data == null){
            Log.wtf("Note", "there arent any epics in data");
        } else {
            String[] parts = data.split("::");

            for (int i = 0; i < parts.length / amountOfPartsInData; i++){
                String title =  parts[(i * amountOfPartsInData)];
                String startDateStr = parts[1 + (i * amountOfPartsInData)];
                String endDateStr = parts[2 + (i * amountOfPartsInData)];
                try {
                    Date startDate = df.parse(startDateStr);
                    Date dueDate = df.parse(endDateStr);

                    Calendar calendarDueDate = GregorianCalendar.getInstance(), calendarStartDate = GregorianCalendar.getInstance();

                    calendarStartDate.setTime(startDate);
                    calendarDueDate.setTime(dueDate);
                    epicsRecyclerDataArrayList.add(new RecyclerData(title, calendarStartDate, calendarDueDate, tag));
                } catch (ParseException e) {
                    Log.wtf("Error", "there is problem with getting the epic data");
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        WeeksBarRecycler();
        EpicsRecycler();
    }
}