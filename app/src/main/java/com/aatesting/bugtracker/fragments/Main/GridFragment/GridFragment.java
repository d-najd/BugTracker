package com.aatesting.bugtracker.fragments.Main.GridFragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.Projects.ProjectsMainActivity;
import com.aatesting.bugtracker.fragments.FragmentSettings;
import com.aatesting.bugtracker.fragments.Main.GridFragment.Views.GridFragmentArrowView;
import com.aatesting.bugtracker.fragments.Main.GridFragment.Views.GridFragmentBackgroundView;
import com.aatesting.bugtracker.fragments.Main.GridFragment.Views.GridFragmentBoardView;
import com.aatesting.bugtracker.modifiedClasses.ModifiedFragment;

import org.jetbrains.annotations.NotNull;

public class GridFragment extends ModifiedFragment {
    private View root;
    private GridFragmentBackgroundView gridFragmentBackgroundView;
    public GridFragmentListeners gridFragmentListeners;
    public ViewGroup layout;
    public float dp;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_grid, container, false);
        layout = (ViewGroup) root.findViewById(R.id.container);

        dp = requireContext().getResources().getDisplayMetrics().density;

        ((ProjectsMainActivity)requireActivity()).thisFragment = this;
        ((ProjectsMainActivity)requireActivity()).listeners(root, FragmentSettings.GRID_FRAGMENT_ID, getParentFragmentManager());

        addGrid();

        new GridFragmentBoardView(this, 120, 450);
        new GridFragmentBoardView(this, 210, 90);

        GridFragmentArrowView GridFragmentArrowView = new GridFragmentArrowView(root.getContext(), 90, 90);
        layout.addView(GridFragmentArrowView);

        return root;
    }

    /**
     * adds the grid to the fragment and creates listeners
     */
    private void addGrid(){
        gridFragmentBackgroundView = new GridFragmentBackgroundView(root.getContext());
        gridFragmentBackgroundView.setMinimumWidth(50000);
        gridFragmentBackgroundView.setMinimumHeight(50000);
        layout.addView(gridFragmentBackgroundView);

        gridFragmentListeners = new GridFragmentListeners(root, gridFragmentBackgroundView);

        ImageButton gridColliders = new ImageButton(root.getContext());
        gridColliders.setMinimumWidth(50000);
        gridColliders.setMinimumHeight(50000);
        gridColliders.setBackgroundColor(Color.parseColor("#00000000"));
        gridColliders.setTag("gridColliders");
        gridColliders.setOnDragListener(gridFragmentListeners);
        gridColliders.setPadding(150, 150, 0, 0);
        layout.addView(gridColliders);

    }
}
