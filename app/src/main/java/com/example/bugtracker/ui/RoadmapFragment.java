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
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mcontext = getContext();

        Listeners();
        WeeksBarRecycler();
        EpicsRecycler();


        return root;
    }

    private void WeeksBarRecycler(){
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
    }

    private void EpicsRecycler(){
        ArrayList<RecyclerData> recyclerDataArrayList = new ArrayList<>();
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView1);
        String tag = recyclerView.getTag().toString();

        recyclerDataArrayList.add(new RecyclerData("Hello World", "00 Nul - 00 Nul", tag));
        recyclerDataArrayList.add(new RecyclerData("Hello World", "00 Nul - 00 Nul", tag));

        RoadmapEpicsAdapter adapter = new RoadmapEpicsAdapter(recyclerDataArrayList, mcontext);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void Listeners(){
        ((MainActivity)getActivity()).Listeners(1);
    }
}