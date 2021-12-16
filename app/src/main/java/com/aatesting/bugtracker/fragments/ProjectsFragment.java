package com.aatesting.bugtracker.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.data.ProjectTableData;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.MainActivity;
import com.aatesting.bugtracker.data.RecentlyViewedProjectsData;
import com.aatesting.bugtracker.dialogs.Dialogs;
import com.aatesting.bugtracker.modifiedClasses.ModifiedFragment;
import com.aatesting.bugtracker.recyclerview.Adapters.MainRecyclerAdapter;
import com.aatesting.bugtracker.recyclerview.RecyclerData;
import com.aatesting.bugtracker.data.ProjectsDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class ProjectsFragment extends ModifiedFragment {
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

        SetupAdapters();
        return root;
    }


    public void SetupAdapters()
    {
        SetupAllProjects_Adapter();
        SetupRecentlyViewedAdapter();
    }

    private void SetupRecentlyViewedAdapter() {
        RecentlyViewedProjectsData.MakeFolders(getContext());

        recentlyViewed_List = new ArrayList<>();
        recentlyViewed_RecyclerView = root.findViewById(R.id.recentlyViewedRecyclerView);

        //NOTE The listeners are in the adapter class
        SetAdapter(recentlyViewed_RecyclerView, recentlyViewed_List);

        recentlyViewed_RecyclerView.getAdapter().notifyItemRangeRemoved(0, recentlyViewed_List.size());
        recentlyViewed_List.clear();
        String tag = recentlyViewed_RecyclerView.getTag().toString();

        String viewedData = RecentlyViewedProjectsData.GetData(getContext());
        String SEPARATOR = RecentlyViewedProjectsData.SEPARATOR;

        if (viewedData == null)
            return;
        String[] viewedParts = viewedData.split(SEPARATOR);

        ProjectsDatabase helper = new ProjectsDatabase(getContext());

        String dbData = helper.GetData();
        String[] dbParts = dbData.split("/");

        ArrayList<String> idList = new ArrayList<>(); //it seems I have used id while passing data for
        //some reason, it might break something in the longrun so I am getting the id for the project
        //as well

        for (String viewedPart : viewedParts){
            for (int i = 0; i < dbParts.length / 3; i++) {
                if (viewedPart.equals(dbParts[i * 3]))
                    idList.add(dbParts[i * 3] + 2);
            }
        }

        for (int i = 0; i < idList.size(); i++){
            recentlyViewed_List.add(new RecyclerData(viewedParts[i],
                    R.drawable.ic_launcher_background, tag, idList.get(i)));
            recentlyViewed_RecyclerView.getAdapter().notifyItemInserted(i);
        }
    }

    //one of the adapters not all
    private void SetupAllProjects_Adapter(){
        allProjects_List = new ArrayList<>();
        allProjects_RecyclerView = root.findViewById(R.id.allProjectsRecyclerView);

        //NOTE The listeners are in the adapter class
        SetAdapter(allProjects_RecyclerView, allProjects_List);

        allProjects_RecyclerView.getAdapter().notifyItemRangeRemoved(0, allProjects_List.size());
        allProjects_List.clear();
        String tag = allProjects_RecyclerView.getTag().toString();
        ProjectsDatabase helper = new ProjectsDatabase(getContext());

        String data = helper.GetData();
        String[] parts = data.split("/");

        for (int i = 0; i < parts.length / 3; i++) {
            Log.wtf(parts[(i * 3) + 2], parts[(i * 3) + 2]);
            allProjects_List.add(new RecyclerData(parts[i * 3], R.drawable.ic_launcher_background,
                    tag, parts[(i * 3) + 2]));
            allProjects_RecyclerView.getAdapter().notifyItemInserted(i);
        }

        ItemTouchHelper itemTouchHelper;

        itemTouchHelper = new ItemTouchHelper(ItemMoved);
        itemTouchHelper.attachToRecyclerView(allProjects_RecyclerView);

        itemTouchHelper = new ItemTouchHelper(ItemSwiped);
        itemTouchHelper.attachToRecyclerView(allProjects_RecyclerView);
    }

    private void SetAdapter(RecyclerView recyclerView, ArrayList<RecyclerData> RecyclerDataList) {
        MainRecyclerAdapter allProjects_adapter = new MainRecyclerAdapter(RecyclerDataList, requireContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());

        allProjects_adapter.fragment = this;

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

    @Override
    public void onResponse(String code, String data) {
        if (data == null || code == null)
        {
            Log.wtf("ERROR", "code or data is null");
            Message.message(getContext(), "Something went wrong");
        }

        if (code.equals("NotifyProjectViewed")){
            notifyProjectViewed(data);
        }
    }

    public void notifyProjectViewed(String projectName){
        RecentlyViewedProjectsData.ProjectOpened(getContext(), projectName);

        //refreshing the data for recentlyViewedAdapter
        SetupRecentlyViewedAdapter();
    }

    public void RemoveProject(RecyclerView.ViewHolder viewHolder){
        ProjectsDatabase helper = new ProjectsDatabase(getContext());

        ProjectTableData.RemoveFile(allProjects_List.get
                (viewHolder.getAdapterPosition()).getTitle(), getContext());
        //RoadmapEpicData.RemoveFile(allProjects_List.get
        //        (viewHolder.getAdapterPosition()).getTitle(), getContext());
        RecentlyViewedProjectsData.ProjectRemoved(getContext(), allProjects_List.get
                (viewHolder.getAdapterPosition()).getTitle());

        helper.Delete(allProjects_List.get(viewHolder.getAdapterPosition()).getId());

        allProjects_List.remove(viewHolder.getAdapterPosition());
        allProjects_RecyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());

        //refreshing the data for recentlyViewedAdapter
        SetupRecentlyViewedAdapter();
    }
}