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
import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.ProjectsMainActivity;
import com.aatesting.bugtracker.data.ProjectTableData;
import com.aatesting.bugtracker.modifiedClasses.ModifiedFragment;
import com.aatesting.bugtracker.recyclerview.Adapters.ProjectTableCreateAdapter;
import com.aatesting.bugtracker.recyclerview.RecyclerData;
import com.aatesting.bugtracker.restApi.ApiController;
import com.aatesting.bugtracker.restApi.ApiJSONObject;
import com.aatesting.bugtracker.restApi.ApiSingleton;

import java.util.ArrayList;

public class DashboardFragment extends ModifiedFragment {
    private RecyclerView recyclerView;
    private ArrayList<RecyclerData> recyclerDataArrayList = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<Integer> imgIds = new ArrayList<>();
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private String projectName; //data is passed through intent
    private String tag;
    private boolean resumed; //to prevent creating the recyclerview twice when the activity is started


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        ((ProjectsMainActivity)getActivity()).Listeners(0);
        ((ProjectsMainActivity)getActivity()).thisFragment = this;

        recyclerView = root.findViewById(R.id.mainRecyclerView);
        tag = recyclerView.getTag().toString();
        projectName = getActivity().getIntent().getExtras().getString("projectName");

        ProjectTableData.MakeFolders(getContext());

        ApiController.getAllFields(7, getContext(), "boards", this);

        return root;
    }

    private void setupRecycler(){
        recyclerDataArrayList.clear();

        ArrayList<ApiJSONObject> test =  ApiSingleton.getInstance().getArray();

        for (int i = 0; i < ApiSingleton.getInstance().getArray().size(); i++){
            recyclerDataArrayList.add(new RecyclerData(ApiSingleton.getInstance().getObject(i).getTitle(), null, null, String.valueOf(i), tag));
        }

        recyclerDataArrayList.add(new RecyclerData(this.getString(R.string.add_column), tag));

        ProjectTableCreateAdapter adapter = new ProjectTableCreateAdapter(recyclerDataArrayList, getContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.ProjectMainActivity = (ProjectsMainActivity) getActivity();
        adapter.projectName = projectName;
        adapter.intent = getActivity().getIntent();
        recyclerView.setRecycledViewPool(viewPool);
    }

    @Override
    public void onResponse(String code) {
        if (code.equals(this.getString(R.string.setupData))){
            setupRecycler();
        }
        else if (code.equals(this.getString(R.string.getData))){
            updateData();
        }
        else
            super.onResponse("Error");
    }

    @Override
    public void onResume() {
        super.onResume();

        //if (GlobalValues.fieldModified != -1)
         //   ApiController.editField(this, "boards");
        if (!resumed) {
            resumed = true;
            return;
        } else
            updateData();


    }

    private void updateData(){
        ApiController.getAllFields(7, getContext(), "boards", this);
    }
}