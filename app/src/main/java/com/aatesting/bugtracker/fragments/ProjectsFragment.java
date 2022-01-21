package com.aatesting.bugtracker.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aatesting.bugtracker.GlobalValues;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.MainActivity;
import com.aatesting.bugtracker.data.RecentlyViewedProjectsData;
import com.aatesting.bugtracker.dialogs.Dialogs;
import com.aatesting.bugtracker.modifiedClasses.ModifiedFragment;
import com.aatesting.bugtracker.recyclerview.Adapters.MainRecyclerAdapter;
import com.aatesting.bugtracker.recyclerview.RecyclerData;
import com.aatesting.bugtracker.data.ProjectsDatabase;
import com.aatesting.bugtracker.restApi.ApiController;
import com.aatesting.bugtracker.restApi.ApiJSONObject;
import com.aatesting.bugtracker.restApi.ApiSingleton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ProjectsFragment extends ModifiedFragment {
    private RecyclerView mainProjectsRecyclerView, recentlyViewed_RecyclerView;
    private ArrayList<RecyclerData> mainProjectsList = new ArrayList<>(), recentlyViewed_List = new ArrayList<>();
    private ProjectsFragment projectsFragment;
    private View root;

    //url of the thing you want to get from the server

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_projects, container, false);

        ((MainActivity)getActivity()).fixBottomMenu();

        projectsFragment = this;
        ((MainActivity)requireActivity()).projectsFragment = this;
        listeners();

        //setting up the main adapter
        ApiController.getFields(false, false, true, requireContext(),
                GlobalValues.PROJECTS_URL, projectsFragment);

        return root;
    }

    private void listeners(){
        View mainBtn = ((MainActivity)getActivity()).mainBtn;
        mainBtn.setVisibility(View.VISIBLE);

        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogs.NewProjectDialog(getContext(), "Create Project",
                        "CREATE", "CANCEL", projectsFragment);
            }
        });
    }


    private void setupRecentlyViewedAdapter() {
        RecentlyViewedProjectsData.MakeFolders(requireContext());

        recentlyViewed_RecyclerView = root.findViewById(R.id.recentlyViewedRecyclerView);
        recentlyViewed_List.clear();

        String tag = recentlyViewed_RecyclerView.getTag().toString();

        List<ApiJSONObject> recViewedProjects = RecentlyViewedProjectsData.getRecentlyViewedList(getContext());

        for (ApiJSONObject project : recViewedProjects) {
            recentlyViewed_List.add(new RecyclerData(project.getTitle(), tag));
        }

        SetAdapter(recentlyViewed_RecyclerView, recentlyViewed_List);
    }

    //one of the adapters not all
    private void setupMainProjectsAdapter(){
        //NOTE The listeners are in the adapter class
        //getting data
        mainProjectsList.clear();
        mainProjectsRecyclerView = root.findViewById(R.id.allProjectsRecyclerView);

        String tag = mainProjectsRecyclerView.getTag().toString();
        for (int i = 0; i < ApiSingleton.getInstance().getArray(GlobalValues.PROJECTS_URL).size(); i++){
            mainProjectsList.add(new RecyclerData(ApiSingleton.getInstance().getObject(i, GlobalValues.PROJECTS_URL).getTitle(), tag));
        }

        //setting adapter
        SetAdapter(mainProjectsRecyclerView, mainProjectsList);

        ItemTouchHelper itemTouchHelper;

        //itemTouchHelper = new ItemTouchHelper(ItemMoved);
        //itemTouchHelper.attachToRecyclerView(mainProjectsRecyclerView);

        itemTouchHelper = new ItemTouchHelper(ItemSwiped);
        itemTouchHelper.attachToRecyclerView(mainProjectsRecyclerView);

        setupRecentlyViewedAdapter();
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
            int pos = viewHolder.getAdapterPosition();
            Objects.requireNonNull(mainProjectsRecyclerView.getAdapter()).notifyItemRemoved(pos);
            Dialogs.DeleteProjectDialog(getContext(), "Delete this project?",
                    "Deleting the project permanently erases all of its contents. " +
                            "This includes all of the epics, tasks and subtasks", pos, GlobalValues.PROJECTS_URL,
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

    @Override
    public void onResponse(String code, String data) {
        if (code.equals("NotifyProjectViewed"))
            notifyProjectViewed(data);
        else if (code.equals("removeProject"))
            removeProject(Integer.parseInt(data));
        else {
            Log.wtf("ERROR", "onResponse crashed at ProjectsFragment with code " + code + " and data " + data);
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
            notifyProjectNotRemoved();
        }

        else {
            Log.wtf("ERROR", "onResponse crashed at ProjectsFragment with code " + code);
            super.onResponse("ERROR");
        }
    }

    private void updateData(){
        ApiController.getFields(false, false,
                true, requireContext(), GlobalValues.PROJECTS_URL, this);
    }

    public void notifyProjectViewed(String projectId) {
        RecentlyViewedProjectsData.projectOpened(requireContext(), projectId);
        setupRecentlyViewedAdapter();
    }

    public void notifyProjectNotRemoved(){
        Objects.requireNonNull(mainProjectsRecyclerView.getAdapter()).notifyDataSetChanged();
    }

    public void removeProject(int field){
        ApiController.removeField(null,null, this, GlobalValues.PROJECTS_URL + "/" + field, null);
    }
}