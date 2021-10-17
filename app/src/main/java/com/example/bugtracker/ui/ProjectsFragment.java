package com.example.bugtracker.ui;

import android.content.Intent;
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

import com.example.bugtracker.ProjectCreateTableData;
import com.example.bugtracker.R;
import com.example.bugtracker.activities.MainActivity;
import com.example.bugtracker.activities.ProjectCreateActivity;
import com.example.bugtracker.recyclerview.Adapters.MainRecyclerAdapter;
import com.example.bugtracker.recyclerview.RecyclerData;
import com.example.bugtracker.databases.ProjectsDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class ProjectsFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<RecyclerData> recyclerDataArrayList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_projects, container, false);

        Listeners();

        recyclerDataArrayList = new ArrayList<>();
        recyclerView = root.findViewById(R.id.recyclerViewFraProjects);

        // added data from arraylist to adapter class.
        MainRecyclerAdapter adapter = new MainRecyclerAdapter(recyclerDataArrayList, requireContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());

        // at last set adapter to recycler view.
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper;

        itemTouchHelper = new ItemTouchHelper(ItemMoved);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        itemTouchHelper = new ItemTouchHelper(ItemSwiped);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return root;
    }

    private void Listeners(){
        ((MainActivity)getActivity()).Listeners(0);
    }

    @Override
    public void onStart() {
        super.onStart();
        RetrieveData();
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
                recyclerDataArrayList.add(new RecyclerData(parts[i], parts[i + 1], R.drawable.ic_launcher_background, tag, parts[i + 2]));
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
            //TODO after coming back to the activity if the item was movied it will go back to the
            // starting pos when the app was opened
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
            ProjectsDatabase helper = new ProjectsDatabase(getContext());

            ProjectCreateTableData.RemoveFile(recyclerDataArrayList.get
                    (viewHolder.getAdapterPosition()).getTitle(), getContext());
            //TODO FIXME
            helper.Delete(recyclerDataArrayList.get(viewHolder.getAdapterPosition()).getId());

            recyclerDataArrayList.remove(viewHolder.getAdapterPosition());
            recyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
        }
    };
}