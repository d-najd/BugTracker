package com.aatesting.bugtracker.fragments;

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

import com.aatesting.bugtracker.data.ProjectTableData;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.MainActivity;
import com.aatesting.bugtracker.data.RecentlyViewedProjectsData;
import com.aatesting.bugtracker.data.RoadmapEpicData;
import com.aatesting.bugtracker.dialogs.Dialogs;
import com.aatesting.bugtracker.recyclerview.Adapters.MainRecyclerAdapter;
import com.aatesting.bugtracker.recyclerview.RecyclerData;
import com.aatesting.bugtracker.data.ProjectsDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class ProjectsFragment extends Fragment {
    private RecyclerView allProjects_RecyclerView, recentlyViewed_RecyclerView,
            starredProjects_RecyclerView;
    private ArrayList<RecyclerData> allProjects_List, recentlyViewed_List, starredProjects_List;
    private ProjectsFragment projectsFragment;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_projects, container, false);

        projectsFragment = this;

        ((MainActivity)getActivity()).Listeners(0);
        ((MainActivity)getActivity()).projectsFragment = this;

        allProjects_List = new ArrayList<>();
        allProjects_RecyclerView = root.findViewById(R.id.allProjectsRecyclerView);

        SetupAdapters();
        return root;
    }

    public void SetupAdapters()
    {
        SetupAllProjects_Adapter();
        //SetupRecentlyViewedAdapter();
    }

    //one of the adapters not all
    private void SetupAllProjects_Adapter(){
        //The listeners are in RecyclerAdapter class
        allProjects_RecyclerView.getAdapter().notifyItemRangeRemoved(0, allProjects_List.size());
        allProjects_List.clear();
        String tag = allProjects_RecyclerView.getTag().toString();
        ProjectsDatabase helper = new ProjectsDatabase(getContext());

        String data = helper.GetData();
        String [] parts = data.split("/");

        for (int i = 0; i < parts.length - 1; i++){
            if (i % 3 == 0) {
                allProjects_List.add(new RecyclerData(parts[i], R.drawable.ic_launcher_background, tag, parts[i + 2]));
                allProjects_RecyclerView.getAdapter().notifyItemInserted(i/3);
            }
        }

        SetAdapter(allProjects_RecyclerView, allProjects_List);

        ItemTouchHelper itemTouchHelper;

        itemTouchHelper = new ItemTouchHelper(ItemMoved);
        itemTouchHelper.attachToRecyclerView(allProjects_RecyclerView);

        itemTouchHelper = new ItemTouchHelper(ItemSwiped);
        itemTouchHelper.attachToRecyclerView(allProjects_RecyclerView);
    }



    private void SetupRecentlyViewedAdapter(){
        allProjects_RecyclerView.getAdapter().notifyItemRangeRemoved(0, allProjects_List.size());
        allProjects_List.clear();
        String tag = allProjects_RecyclerView.getTag().toString();
        ProjectsDatabase helper = new ProjectsDatabase(getContext());

        String data = helper.GetData();
        String [] parts = data.split("/");

        for (int i = 0; i < parts.length - 1; i++){
            if (i % 3 == 0) {
                allProjects_List.add(new RecyclerData(parts[i], R.drawable.ic_launcher_background, tag, parts[i + 2]));
                allProjects_RecyclerView.getAdapter().notifyItemInserted(i/3);
            }
        }

        SetAdapter(allProjects_RecyclerView, allProjects_List);
    }

    private void SetAdapter(RecyclerView recyclerView, ArrayList<RecyclerData> RecyclerDataList) {
        MainRecyclerAdapter allProjects_adapter = new MainRecyclerAdapter(RecyclerDataList, requireContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());

        allProjects_adapter.projectsFragment = this;

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(allProjects_adapter);
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
            Collections.swap(allProjects_List, fromPosition, endPosition);

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
        String tag = allProjects_RecyclerView.getTag().toString();
        ProjectsDatabase helper = new ProjectsDatabase(getContext());

        String data = helper.GetData();
        String [] parts = data.split("/");
        allProjects_List.add(new RecyclerData(parts[parts.length - 3], R.drawable.ic_launcher_background, tag, parts[parts.length - 1]));
        allProjects_RecyclerView.getAdapter().notifyItemInserted((parts.length - 1)/3);
    }

    public void NotifyProjectNotRemoved(){
        //called when you change your mind and cancel the removal of the project
        allProjects_RecyclerView.getAdapter().notifyDataSetChanged();
    }

    public void NotifyProjectViewed(String projectName){
        RecentlyViewedProjectsData.ProjectOpened(getContext(), projectName);
    }

    public void RemoveProject(RecyclerView.ViewHolder viewHolder){
        ProjectsDatabase helper = new ProjectsDatabase(getContext());

        ProjectTableData.RemoveFile(allProjects_List.get
                (viewHolder.getAdapterPosition()).getTitle(), getContext());
        RoadmapEpicData.RemoveFile(allProjects_List.get
                (viewHolder.getAdapterPosition()).getTitle(), getContext());

        helper.Delete(allProjects_List.get(viewHolder.getAdapterPosition()).getId());

        allProjects_List.remove(viewHolder.getAdapterPosition());
        allProjects_RecyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
    }
}