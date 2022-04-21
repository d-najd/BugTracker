package com.aatesting.bugtracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.aatesting.bugtracker.GlobalValues;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.ProjectsMainActivity;
import com.aatesting.bugtracker.modifiedClasses.ModifiedFragment;
import com.aatesting.bugtracker.restApi.ApiController;

public class GridFragment extends ModifiedFragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        ((ProjectsMainActivity)requireActivity()).thisFragment = this;
        ((ProjectsMainActivity)requireActivity()).listeners(root, 0, getParentFragmentManager());

        //recyclerView = root.findViewById(R.id.mainRecyclerView);
        //tag = recyclerView.getTag().toString();
        //projectName = requireActivity().getIntent().getExtras().getString("projectName");

        //ApiController.getFields(true, true, false,  requireContext(), GlobalValues.BOARDS_URL, this);
        return root;
    }
}
