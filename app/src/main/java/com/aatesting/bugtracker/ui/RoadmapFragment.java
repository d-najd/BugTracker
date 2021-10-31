package com.aatesting.bugtracker.ui;

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

import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.data.RoadmapCreateEpicData;
import com.aatesting.bugtracker.activities.MainActivity;
import com.aatesting.bugtracker.recyclerview.Adapters.BasicAdapter;
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

    Context mcontext;
    View root;
    ArrayList<RecyclerData> epicsRecyclerDataArrayList = new ArrayList<>();;
    ArrayList<RecyclerData> weeksRecyclerDataArrayList = new ArrayList<>();;
    String tag;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_roadmap, container, false);
        mcontext = getContext();

        RecyclerView recyclerView = root.findViewById(R.id.epicsRecyclerView);
        tag = recyclerView.getTag().toString();

        ((MainActivity)getActivity()).Listeners(1); // for knowing which fragment is selected
        WeeksBarRecycler();
        EpicsRecycler();

        ViewGroup.LayoutParams scrollViewLength = root.findViewById(R.id.scrollViewLength).getLayoutParams();
        //TODO NOTE the length needs to be changed to something more dynamic
        scrollViewLength.width = (int) (10 * getResources().getDimension(R.dimen.activity_roadmap_weeks_width));

        return root;
    }

    private RecyclerView WeeksBarRecycler(){
        //TODO the element with the biggest and smallest X (horizontal) value can be used to
        // determine how many weeks there should be, if that does not the dates can be used but it
        // will be much slower so it should be avoided

        RecyclerView recyclerView = root.findViewById(R.id.weeksRecyclerView);
        String tag = recyclerView.getTag().toString();

        for (int i = 0; i < 10; i++){
            Calendar c = GregorianCalendar.getInstance();
            c.add(Calendar.WEEK_OF_YEAR, i);
            DateFormat df = new SimpleDateFormat("'w'ww");
            String startDate = df.format(c.getTime());
            weeksRecyclerDataArrayList.add(new RecyclerData(startDate, tag));
        }

        BasicAdapter adapter = new BasicAdapter(weeksRecyclerDataArrayList, mcontext);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext, RecyclerView.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }


    private void EpicsRecycler(){
        RecyclerView recyclerView = root.findViewById(R.id.epicsRecyclerView);

        //GetEpicsFromStorage puts the data in recyclerDataArrayList
        GetEpicsFromStorage();

        RoadmapEpicsAdapter adapter = new RoadmapEpicsAdapter(epicsRecyclerDataArrayList, mcontext);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void GetEpicsFromStorage(){
        String data = RoadmapCreateEpicData.GetData("Testing", mcontext);
        int amountOfPartsInData = RoadmapCreateEpicData.amountOfPartsInData;

        SimpleDateFormat df = new SimpleDateFormat("dd'-'MM'-'yyyy");

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
}