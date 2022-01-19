package com.aatesting.bugtracker.fragments.ProjectSettings;

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
import com.aatesting.bugtracker.modifiedClasses.ModifiedFragment;
import com.aatesting.bugtracker.recyclerview.Adapters.CreateProjectsAdapter;
import com.aatesting.bugtracker.recyclerview.RecyclerData;
import com.aatesting.bugtracker.restApi.ApiController;
import com.aatesting.bugtracker.restApi.ApiJSONObject;
import com.aatesting.bugtracker.restApi.ApiSingleton;

import java.util.ArrayList;
import java.util.Calendar;

public class ProjectSettings_usersFragment extends ModifiedFragment {
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_project_users, container, false);

        ((ProjectsMainActivity)requireActivity()).thisFragment = this;
        ((ProjectsMainActivity)requireActivity()).listeners(root, 4, getParentFragmentManager());

        ApiController.getFields(true, false, false, requireContext(), GlobalValues.USERS_URL, this);

        return root;
    }

    private void setupRecyclerView() {
        ArrayList<RecyclerData> recyclerDataArrayList = new ArrayList<>();
        RecyclerView recyclerView = root.findViewById(R.id.mainRecyclerView);
        String tag = recyclerView.getTag().toString();

        CreateProjectsAdapter adapter = new CreateProjectsAdapter(recyclerDataArrayList, requireContext());

        ArrayList<ApiJSONObject> dataList = ApiSingleton.getInstance().getArray(GlobalValues.USERS_URL);

        for (ApiJSONObject object : dataList){
            recyclerDataArrayList.add(new RecyclerData(object.getUsername(), R.drawable.ic_account_24dp,  tag));
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResponse(String code) {
        if (code.equals(this.getString(R.string.setupData)))
            setupRecyclerView();
        else {
            Log.wtf("ERROR", "onResponse crashed at " + this.getClass().getSimpleName() + " with code " + code);
            super.onResponse("ERROR");
        }
    }
}
