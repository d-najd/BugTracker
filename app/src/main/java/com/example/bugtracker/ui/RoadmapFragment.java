package com.example.bugtracker.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.R;
import com.example.bugtracker.activities.MainActivity;
import com.example.bugtracker.recyclerview.Adapters.BasicAdapter;
import com.example.bugtracker.recyclerview.Adapters.RoadmapEpicsAdapter;
import com.example.bugtracker.recyclerview.RecyclerData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class RoadmapFragment extends Fragment {

    Context mcontext;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_roadmap, container, false);

        mcontext = getContext();

        ((MainActivity)getActivity()).Listeners(1); // for knowing which fragment is selected
        WeeksBarRecycler();
        EpicsRecycler();

        ViewGroup.LayoutParams scrollViewLength = root.findViewById(R.id.scrollViewLength).getLayoutParams();
        //TODO NOTE the length needs to be changed to something more dynamic
        scrollViewLength.width = (int) (10 * getResources().getDimension(R.dimen.activity_roadmap_weeks_width));

        return root;
    }

    private RecyclerView WeeksBarRecycler(){
        ArrayList<RecyclerData> recyclerDataArrayList = new ArrayList<>();
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        String tag = recyclerView.getTag().toString();

        for (int i = 0; i < 10; i++){
            Calendar c = GregorianCalendar.getInstance();
            c.add(Calendar.WEEK_OF_YEAR, i);
            DateFormat df = new SimpleDateFormat("'w'ww");
            String startDate = df.format(c.getTime());
            recyclerDataArrayList.add(new RecyclerData(startDate, tag));

        }

        BasicAdapter adapter = new BasicAdapter(recyclerDataArrayList, mcontext);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext, RecyclerView.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }


    private void EpicsRecycler(){
        ArrayList<RecyclerData> recyclerDataArrayList = new ArrayList<>();
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView1);
        String tag = recyclerView.getTag().toString();

        //TODO 2 different dates cant be set for some reason
        Calendar calendarStartDate = GregorianCalendar.getInstance(); Calendar calendarEndDate = GregorianCalendar.getInstance();
        calendarStartDate.add(Calendar.DATE, 7);
        calendarEndDate.add(Calendar.DATE, 21);
        recyclerDataArrayList.add(new RecyclerData("Hello World", "00 Nul - 00 Nul", calendarStartDate, calendarEndDate, tag));
        recyclerDataArrayList.add(new RecyclerData("Hello World", "00 Nul - 00 Nul", calendarStartDate, calendarEndDate, tag));

        RoadmapEpicsAdapter adapter = new RoadmapEpicsAdapter(recyclerDataArrayList, mcontext);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}