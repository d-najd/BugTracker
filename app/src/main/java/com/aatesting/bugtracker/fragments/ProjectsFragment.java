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
import com.aatesting.bugtracker.restApi.ApiController;
import com.aatesting.bugtracker.restApi.ApiSingleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ProjectsFragment extends ModifiedFragment {
    private RecyclerView mainProjectsRecyclerView, recentlyViewed_RecyclerView;
    private ArrayList<RecyclerData> mainProjectsList = new ArrayList<>(), recentlyViewed_List;
    private ProjectsFragment projectsFragment;
    private View root;

    //url of the thing you want to get from the server
    private String type = "project";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_projects, container, false);

        projectsFragment = this;

        ((MainActivity)requireActivity()).Listeners(0);
        ((MainActivity)requireActivity()).projectsFragment = this;

        SetupAdapters();
        return root;
    }


    public void SetupAdapters()
    {
        ApiController.getAllFields(false,requireContext(), type, projectsFragment);
        //SetupRecentlyViewedAdapter();
    }

    private void SetupRecentlyViewedAdapter() {
        RecentlyViewedProjectsData.MakeFolders(requireContext());

        recentlyViewed_List = new ArrayList<>();
        recentlyViewed_RecyclerView = root.findViewById(R.id.recentlyViewedRecyclerView);

        //NOTE The listeners are in the adapter class
        SetAdapter(recentlyViewed_RecyclerView, recentlyViewed_List);

        recentlyViewed_RecyclerView.getAdapter().notifyItemRangeRemoved(0, recentlyViewed_List.size());
        recentlyViewed_List.clear();
        String tag = recentlyViewed_RecyclerView.getTag().toString();

        String viewedData = RecentlyViewedProjectsData.GetData(requireContext());
        String SEPARATOR = RecentlyViewedProjectsData.SEPARATOR;

        if (viewedData == null)
            return;
        String[] viewedParts = viewedData.split(SEPARATOR);

        ProjectsDatabase helper = new ProjectsDatabase(requireContext());

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
    private void setupMainProjectsAdapter(){
        //NOTE The listeners are in the adapter class
        //getting data
        mainProjectsList.clear();
        mainProjectsRecyclerView = root.findViewById(R.id.allProjectsRecyclerView);

        String tag = mainProjectsRecyclerView.getTag().toString();
        for (int i = 0; i < ApiSingleton.getInstance().getArray(type).size(); i++){
            mainProjectsList.add(new RecyclerData(ApiSingleton.getInstance().getObject(i, type).getTitle(), tag));
        }

        //setting adapter
        SetAdapter(mainProjectsRecyclerView, mainProjectsList);



        ItemTouchHelper itemTouchHelper;

        //itemTouchHelper = new ItemTouchHelper(ItemMoved);
        //itemTouchHelper.attachToRecyclerView(mainProjectsRecyclerView);

        itemTouchHelper = new ItemTouchHelper(ItemSwiped);
        itemTouchHelper.attachToRecyclerView(mainProjectsRecyclerView);
    }

    private void SetAdapter(RecyclerView recyclerView, ArrayList<RecyclerData> recyclerData) {
        MainRecyclerAdapter mainProjectsAdapter = new MainRecyclerAdapter(recyclerData, requireContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());

        mainProjectsAdapter.fragment = this;

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mainProjectsAdapter);
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
            Collections.swap(mainProjectsList, fromPosition, endPosition);

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

    public void notifyItemAdded(){
        String tag = mainProjectsRecyclerView.getTag().toString();
        ProjectsDatabase helper = new ProjectsDatabase(getContext());

        String data = helper.GetData();
        String [] parts = data.split("/");

        mainProjectsList.add(new RecyclerData(parts[parts.length - 3], R.drawable.ic_launcher_background, tag, parts[parts.length - 1]));
        mainProjectsRecyclerView.getAdapter().notifyItemInserted((parts.length - 1)/3);
    }

    public void notifyProjectNotRemoved(){
        Objects.requireNonNull(mainProjectsRecyclerView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onResponse(String code, String data) {
        if (code.equals("NotifyProjectViewed")){
            notifyProjectViewed(data);
        }
        else {
            super.onResponse("ERROR");
        }
    }

    @Override
    public void onResponse(String code) {
        if (code.equals(this.getString(R.string.setupData))){
            setupMainProjectsAdapter();
        } else if (code.equals(this.getString(R.string.getData))){
            updateData();
        } else if (code.equals("notifyProjectNotRemoved")){
            Message.message(getContext(),"add method");
            //notifyProjectNotRemoved();
        } else if (code.equals("removeProject")){
            Message.message(getContext(),"add method");
            //RemoveProject();
        }

        else {
            Log.wtf("ERROR", "onResponse crashed at ProjectsFragment with code " + code);
            super.onResponse("ERROR");
        }
    }

    private void updateData(){
        ApiController.getAllFields(false,requireContext(), type, this);
    }

    public void notifyProjectViewed(String projectName) {
        RecentlyViewedProjectsData.ProjectOpened(requireContext(), projectName);
        //SetupRecentlyViewedAdapter();
    }

    public void RemoveProject(){
        ApiController.removeField(null, this, "");
    }

    public void RemoveProject(RecyclerView.ViewHolder viewHolder){
        ProjectsDatabase helper = new ProjectsDatabase(getContext());

        ProjectTableData.RemoveFile(mainProjectsList.get
                (viewHolder.getAdapterPosition()).getTitle(), requireContext());
        //RoadmapEpicData.RemoveFile(allProjects_List.get
        //        (viewHolder.getAdapterPosition()).getTitle(), getContext());
        RecentlyViewedProjectsData.ProjectRemoved(requireContext(), mainProjectsList.get
                (viewHolder.getAdapterPosition()).getTitle());

        helper.Delete(mainProjectsList.get(viewHolder.getAdapterPosition()).getId());

        mainProjectsList.remove(viewHolder.getAdapterPosition());
        mainProjectsRecyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());

        //refreshing the data for recentlyViewedAdapter
        //SetupRecentlyViewedAdapter();
    }
}