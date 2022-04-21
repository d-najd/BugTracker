package com.aatesting.bugtracker.fragments.Main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.Projects.ProjectsMainActivity;
import com.aatesting.bugtracker.fragments.FragmentSettings;
import com.aatesting.bugtracker.modifiedClasses.ModifiedFragment;

public class GridFragment extends ModifiedFragment {

        /*
            TODO add drawing of the line thingys from code, this might be useful
                https://stackoverflow.com/questions/3616676/how-to-draw-a-line-in-android
                https://stackoverflow.com/questions/5176441/drawable-image-on-a-canvas
                https://www.raywenderlich.com/142-android-custom-view-tutorial
                if nothing works out 1d recyclerview can be used with multiple vertical elements
                instead of an grid with a view element, similar to how its done in roadmap
         */


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_grid, container, false);

        ((ProjectsMainActivity)requireActivity()).thisFragment = this;
        ((ProjectsMainActivity)requireActivity()).listeners(root, FragmentSettings.GRID_FRAGMENT_ID, getParentFragmentManager());

        //this is a test

        //recyclerView = root.findViewById(R.id.mainRecyclerView);
        //tag = recyclerView.getTag().toString();
        //projectName = requireActivity().getIntent().getExtras().getString("projectName");

        //ApiController.getFields(true, true, false,  requireContext(), GlobalValues.BOARDS_URL, this);
        return root;
    }
}
