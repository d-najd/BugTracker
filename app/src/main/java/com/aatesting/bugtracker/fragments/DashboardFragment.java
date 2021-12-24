package com.aatesting.bugtracker.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aatesting.bugtracker.GlobalValues;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.ProjectsMainActivity;
import com.aatesting.bugtracker.data.ProjectTableData;
import com.aatesting.bugtracker.modifiedClasses.ModifiedFragment;
import com.aatesting.bugtracker.recyclerview.Adapters.ProjectTableCreateAdapter;
import com.aatesting.bugtracker.recyclerview.RecyclerData;
import com.aatesting.bugtracker.restApi.ApiController;
import com.aatesting.bugtracker.restApi.ApiSingleton;

import java.util.ArrayList;
import java.util.Objects;

public class DashboardFragment extends ModifiedFragment {
    private RecyclerView recyclerView;
    private ArrayList<RecyclerData> recyclerDataArrayList = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<Integer> imgIds = new ArrayList<>();
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private String projectName; //data is passed through intent
    private String tag;
    private boolean resumed; //to prevent creating the recyclerview twice when the activity is started
    private ProjectTableCreateAdapter adapter;
    private String type = "boards";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        ((ProjectsMainActivity)requireActivity()).Listeners(0);
        ((ProjectsMainActivity)requireActivity()).thisFragment = this;

        recyclerView = root.findViewById(R.id.mainRecyclerView);
        tag = recyclerView.getTag().toString();
        projectName = requireActivity().getIntent().getExtras().getString("projectName");

        ProjectTableData.MakeFolders(requireContext());

        ApiController.getAllFields(true, requireContext(), type, this);

        return root;
    }

    private void setupRecycler(){
        recyclerDataArrayList.clear();

        for (int i = 0; i < ApiSingleton.getInstance().getArray(type).size(); i++){
            recyclerDataArrayList.add(new RecyclerData(ApiSingleton.getInstance().getObject(i, type).getTitle(), String.valueOf(i), tag));
        }

        recyclerDataArrayList.add(new RecyclerData(this.getString(R.string.add_column), tag));

        adapter = new ProjectTableCreateAdapter(recyclerDataArrayList, getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.projectMainActivity = (ProjectsMainActivity) getActivity();
        adapter.projectName = projectName;
        adapter.intent = requireActivity().getIntent();
        recyclerView.setRecycledViewPool(viewPool);
    }

    @Override
    public void onResponse(String code) {
        if (getContext() == null) {
            Log.wtf("DEBUG", "the current fragment on the screen and the fragment where the onResponse request is called from are not the same, skipping all fields inside onResponse");
            return;
        }
        if (code.equals(this.getString(R.string.setupData))){
            setupRecycler();
        }
        else if (code.equals(this.getString(R.string.getData))){
            updateData();
        }
        else {
            Log.wtf("ERROR", "onResponse crashed at DashboardFragment with code " + code);
            super.onResponse("Error");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GlobalValues.objectModified != null)
            ApiController.editField(this, null,"tasks");
        if (!resumed) {
            resumed = true;
            return;
        } else
            updateData();
    }

    private void updateData(){
        ApiController.getAllFields(true, requireContext(), type, this);
    }
}