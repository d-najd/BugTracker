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
import com.aatesting.bugtracker.data.RoadmapEpicJsonData;
import com.aatesting.bugtracker.recyclerview.Adapters.RoadmapWeeksAdapter;
import com.aatesting.bugtracker.recyclerview.Adapters.RoadmapEpicsAdapter;
import com.aatesting.bugtracker.recyclerview.RecyclerData;
import com.aatesting.bugtracker.restApi.RoadmapApi;
import com.aatesting.bugtracker.restApi.RoadmapObject;
import com.aatesting.bugtracker.restApi.RoadmapsSingleton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RoadmapFragment extends Fragment {
    private Context mcontext;
    private View root;
    private ArrayList<RecyclerData> weeksRecyclerDataArrayList = new ArrayList<>();;
    private String tag;
    private Calendar calendarCurDate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_roadmap, container, false);
        mcontext = getContext();

        RecyclerView recyclerView = root.findViewById(R.id.epicsRecyclerView);
        tag = recyclerView.getTag().toString();

        ((ProjectsMainActivity)getActivity()).Listeners(1); // for knowing which fragment is selected
        RoadmapApi.setupRoadmaps(7, mcontext, this);

        ViewGroup.LayoutParams scrollViewLength = root.findViewById(R.id.scrollViewLength).getLayoutParams();

        scrollViewLength.width = (int) (Math.round(10 * getResources().getDimension(R.dimen.activity_roadmap_weeks_width)));

        return root;
    }

    private void weeksBarRecycler(){
        weeksRecyclerDataArrayList = new ArrayList<>();

        RecyclerView recyclerView = root.findViewById(R.id.weeksRecyclerView);
        String tag = recyclerView.getTag().toString();

        Date earliestDate = RoadmapEpicJsonData.getEarliestOrLatestDate(true);
        Date latestDate = RoadmapEpicJsonData.getEarliestOrLatestDate(false);

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

    private void newEpicsAdapter(){
        RecyclerView recyclerView = root.findViewById(R.id.epicsRecyclerView);

        Date earliestDate = RoadmapEpicJsonData.getEarliestOrLatestDate(true);

        Calendar calendarEarliestDate = GregorianCalendar.getInstance();
        calendarEarliestDate.setTime(earliestDate);
        calendarEarliestDate.set(Calendar.WEEK_OF_YEAR, calendarEarliestDate.get(Calendar.WEEK_OF_YEAR) - 1);
        /**do not ask how this line works or why it works, but if you remove it the roadmap adapter will
         *think that starting monday-today that the earliest epic is monday.
         **/
        calendarEarliestDate.set(Calendar.DAY_OF_YEAR, calendarEarliestDate.get(Calendar.DAY_OF_YEAR)
                - calendarEarliestDate.get(Calendar.DAY_OF_WEEK) + 2);
        Date weeksStartDate = calendarEarliestDate.getTime();

        RoadmapEpicsAdapter adapter = new RoadmapEpicsAdapter(RoadmapsSingleton.getInstance().getArray(), weeksStartDate, mcontext);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void onResponse(int code){
        switch (code) {
            case 0:
                Log.wtf("ERROR", "failed to get data");
                break;
            case 1:
                newEpicsAdapter();
                weeksBarRecycler();
                break;
            default:
                Log.wtf("ERROR", "current response code from the server isn't configured for, code: " + code);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        //update the database
        if (GlobalValues.fieldModified != -1)
            RoadmapApi.editRoadmap(this);
        else
            updateData();
    }

    //check if there have been updates from other people and update the new data
    //can be called from RoadmapApi.updateServerField or from this function
    public void updateData(){
        RoadmapApi.setupRoadmaps(7, mcontext, this);
    }
}