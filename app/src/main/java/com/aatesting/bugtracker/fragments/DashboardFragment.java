package com.aatesting.bugtracker.fragments;

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
import com.aatesting.bugtracker.StringToList;
import com.aatesting.bugtracker.activities.ProjectsMainActivity;
import com.aatesting.bugtracker.data.ProjectTableData;
import com.aatesting.bugtracker.recyclerview.Adapters.ProjectTableCreateAdapter;
import com.aatesting.bugtracker.recyclerview.RecyclerData;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<RecyclerData> recyclerDataArrayList;
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<Integer> imgIds = new ArrayList<>();
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private String projectName; //data is passed through intent
    private String tag;
    private int amountOfPartsInData;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        ((ProjectsMainActivity)getActivity()).Listeners(0);

        recyclerView = root.findViewById(R.id.mainRecyclerView);
        tag = recyclerView.getTag().toString();
        projectName = getActivity().getIntent().getExtras().getString("projectName");

        ProjectTableData.MakeFolders(getContext());

        //titles.add("TEST");
        //titles.add("TEST");
        //imgIds.add(R.drawable.ic_launcher_background);
        //imgIds.add(R.drawable.ic_launcher_foreground);

        //saveData(titles, imgIds, "TO DO", projectName);

        //removeData(2, projectName);

        GetData();

        return root;
    }

    private void GetData(){
        recyclerDataArrayList = new ArrayList<>();

        String data = ProjectTableData.GetData(projectName, getContext());
        amountOfPartsInData = ProjectTableData.AMOUNT_OF_PARTS_IN_DATA;

        //for getting the data nad putting it in arrayList so it can be used by the adapter
        if (data == null){
            Log.wtf("DATA IS EMPTY", "the data is null there is problem");
        } else {
            String[] parts = data.split("::");

            for (int i = 0; i < parts.length / amountOfPartsInData; i++){
                titles = StringToList.StringToList(parts[1 + (i * amountOfPartsInData)], null);
                imgIds = StringToList.StringToList(parts[2 + (i * amountOfPartsInData)], 0);

                recyclerDataArrayList.add(new RecyclerData(parts[i * amountOfPartsInData], titles, imgIds, String.valueOf(i), tag));
            }
        }

        recyclerDataArrayList.add(new RecyclerData(this.getString(R.string.add_column), tag));

        ProjectTableCreateAdapter adapter = new ProjectTableCreateAdapter(recyclerDataArrayList, getContext());

        // setting grid layout manager to implement grid view.
        // in this method '1' represents number of columns to be displayed in grid view.

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);

        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.ProjectMainActivity = (ProjectsMainActivity) getActivity();
        adapter.projectName = projectName;
        adapter.intent = getActivity().getIntent();
        recyclerView.setRecycledViewPool(viewPool);
    }

    @Override
    public void onResume() {
        super.onResume();

        GetData();
    }
}