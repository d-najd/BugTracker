package com.aatesting.bugtracker.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.data.ProjectTableData;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.MainActivity;
import com.aatesting.bugtracker.data.RoadmapEpicData;
import com.aatesting.bugtracker.dialogs.Dialogs;
import com.aatesting.bugtracker.recyclerview.Adapters.MainRecyclerAdapter;
import com.aatesting.bugtracker.recyclerview.RecyclerData;
import com.aatesting.bugtracker.data.ProjectsDatabase;

import java.util.ArrayList;
import java.util.Collections;

import javax.xml.XMLConstants;

public class ProjectsFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<RecyclerData> recyclerDataArrayList;
    private ProjectsFragment projectsFragment;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_projects, container, false);

        projectsFragment = this;

        ((MainActivity)getActivity()).Listeners(0);
        ((MainActivity)getActivity()).projectsFragment = this;

        recyclerDataArrayList = new ArrayList<>();
        recyclerView = root.findViewById(R.id.recyclerViewFraProjects);

        // added data from arraylist to adapter class.
        MainRecyclerAdapter adapter = new MainRecyclerAdapter(recyclerDataArrayList, requireContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());

        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        RetrieveData();

        ItemTouchHelper itemTouchHelper;

        itemTouchHelper = new ItemTouchHelper(ItemMoved);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        itemTouchHelper = new ItemTouchHelper(ItemSwiped);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return root;
    }

    public void RetrieveData()
    {
        recyclerView.getAdapter().notifyItemRangeRemoved(0, recyclerDataArrayList.size());
        recyclerDataArrayList.clear();
        String tag = recyclerView.getTag().toString();
        ProjectsDatabase helper = new ProjectsDatabase(getContext());

        String data = helper.GetData();
        String [] parts = data.split("/");

        for (int i = 0; i < parts.length - 1; i++){
            if (i % 3 == 0) {
                recyclerDataArrayList.add(new RecyclerData(parts[i], R.drawable.ic_launcher_background, tag, parts[i + 2]));
                recyclerView.getAdapter().notifyItemInserted(i/3);
            }
        }
        //The listeners are in RecyclerAdapter class
    }

    //used for reordering items
    ItemTouchHelper.SimpleCallback ItemMoved = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder
                viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int endPosition = target.getAdapterPosition();
            //TODO update item swap in data, so it does not just swap place visually
            Collections.swap(recyclerDataArrayList, fromPosition, endPosition);

            recyclerView.getAdapter().notifyItemMoved(fromPosition, endPosition);
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Toast.makeText(getContext(), "Swiped", Toast.LENGTH_SHORT).show();
        }
    };



    ItemTouchHelper.SimpleCallback ItemSwiped = new ItemTouchHelper.SimpleCallback( 0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Dialogs.DeleteProjectDialog(getContext(), "Delete this project?",
                    "Deleting the project permanently erases all of its contents. " +
                            "This includes all of the epics, tasks and subtasks", viewHolder,
                    "DELETE", "CANCEL", projectsFragment);
        }
    };

    public void NotifyItemAdded(){
        String tag = recyclerView.getTag().toString();
        ProjectsDatabase helper = new ProjectsDatabase(getContext());

        String data = helper.GetData();
        String [] parts = data.split("/");
        recyclerDataArrayList.add(new RecyclerData(parts[parts.length - 3], R.drawable.ic_launcher_background, tag, parts[parts.length - 1]));
        recyclerView.getAdapter().notifyItemInserted((parts.length - 1)/3);
    }

    public void NotifyProjectNotRemoved(){
        //called when you change your mind and cancel the removal of the project
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public void RemoveProject(RecyclerView.ViewHolder viewHolder){
        ProjectsDatabase helper = new ProjectsDatabase(getContext());

        ProjectTableData.RemoveFile(recyclerDataArrayList.get
                (viewHolder.getAdapterPosition()).getTitle(), getContext());
        RoadmapEpicData.RemoveFile(recyclerDataArrayList.get
                (viewHolder.getAdapterPosition()).getTitle(), getContext());

        helper.Delete(recyclerDataArrayList.get(viewHolder.getAdapterPosition()).getId());

        recyclerDataArrayList.remove(viewHolder.getAdapterPosition());
        recyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
    }
}