package com.example.bugtracker.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.Message;
import com.example.bugtracker.R;
import com.example.bugtracker.databases.ProjectsDatabase;
import com.example.bugtracker.recyclerview.Adapters.CreateProjectsAdapter;
import com.example.bugtracker.recyclerview.Adapters.MainRecyclerAdapter;
import com.example.bugtracker.recyclerview.Adapters.RoadmapAdapter;
import com.example.bugtracker.recyclerview.RecyclerData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RoadmapFragment extends Fragment {

    Context mcontext;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mcontext = getContext();

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

        RoadmapAdapter adapter = new RoadmapAdapter(recyclerDataArrayList, mcontext);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext, RecyclerView.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return root;
    }
}