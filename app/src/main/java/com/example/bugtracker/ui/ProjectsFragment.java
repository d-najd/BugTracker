package com.example.bugtracker.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.R;
import com.example.bugtracker.adapters.ProjectsRecyclerAdapter;
import com.example.bugtracker.adapters.ProjectsRecyclerData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ProjectsFragment extends Fragment {
    RecyclerView recyclerView;
    private ArrayList<ProjectsRecyclerData> recyclerDataArrayList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_projects, container, false);

        recyclerDataArrayList = new ArrayList<>();

        recyclerDataArrayList.add(new ProjectsRecyclerData("Add item", "Description", R.drawable.ic_launcher_background));
        recyclerDataArrayList.add(new ProjectsRecyclerData("Add item", "Description", R.drawable.ic_launcher_background));
        recyclerDataArrayList.add(new ProjectsRecyclerData("Add item", "Description", R.drawable.ic_launcher_background));

        recyclerView = root.findViewById(R.id.recyclerView_Act_checklist);

        // added data from arraylist to adapter class.
        ProjectsRecyclerAdapter adapter = new ProjectsRecyclerAdapter(recyclerDataArrayList, requireContext());

        // setting grid layout manager to implement grid view.
        // in this method '1' represents number of columns to be displayed in grid view.
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 1);

        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return root;
    }

    //used for reordering items
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder
                viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int endPosition = target.getAdapterPosition();

            Collections.swap(recyclerDataArrayList, fromPosition, endPosition);

            recyclerView.getAdapter().notifyItemMoved(fromPosition, endPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        }
    };
}