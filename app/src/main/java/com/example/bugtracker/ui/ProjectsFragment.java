package com.example.bugtracker.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.R;
import com.example.bugtracker.recyclerview.Message;
import com.example.bugtracker.recyclerview.RecyclerAdapter;
import com.example.bugtracker.recyclerview.RecyclerData;
import com.example.bugtracker.recyclerview.myDbAdapter;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;

public class ProjectsFragment extends Fragment {
    RecyclerView recyclerView;
    private ArrayList<RecyclerData> recyclerDataArrayList;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_projects, container, false);

        recyclerDataArrayList = new ArrayList<>();
        recyclerView = root.findViewById(R.id.recyclerView_Fra_Projects);

        retrieveData();
        // added data from arraylist to adapter class.
        RecyclerAdapter adapter = new RecyclerAdapter(recyclerDataArrayList, requireContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());

        //for grid layout just switch this
        //GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 1);
        // setting grid layout manager to implement grid view.
        // in this method '1' represents number of columns to be displayed in grid view.

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

    public void retrieveData()
    {
        String tag = recyclerView.getTag().toString();
        myDbAdapter helper = new myDbAdapter(getContext());

        String data = helper.getData();
        String [] parts = data.split("/");

        for (int i = 0; i < parts.length - 1; i++){
            if (i % 3 == 0)
                recyclerDataArrayList.add(new RecyclerData(parts[i], parts[i + 1], R.drawable.ic_launcher_background, tag, parts[i + 2]));
        }
    }

    //used for reordering items
    ItemTouchHelper.SimpleCallback ItemMoved = new ItemTouchHelper.SimpleCallback(
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
            myDbAdapter helper = new myDbAdapter(getContext());

            //TODO FIXME
            helper.delete(recyclerDataArrayList.get(viewHolder.getAdapterPosition()).getId());

            recyclerDataArrayList.remove(viewHolder.getAdapterPosition());
            recyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
        }
    };
}