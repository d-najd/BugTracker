package com.aatesting.bugtracker.recyclerview.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.recyclerview.RecyclerData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RoadmapWeeksAdapter extends RecyclerView.Adapter<RoadmapWeeksAdapter.RecyclerViewHolder> {
    public ArrayList<RecyclerData> recyclerDataArrayList;
    public Context mcontext;
    public RecyclerViewHolder holder;
    public RecyclerData recyclerData;

    public RoadmapWeeksAdapter(ArrayList<RecyclerData> recyclerDataArrayList, Context mcontext) {
        this.recyclerDataArrayList = recyclerDataArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardlayout_roadmap_weeks, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerViewHolder holder, int position) {
        recyclerData = recyclerDataArrayList.get(position);
        this.holder = holder;

        holder.title.setText(recyclerData.getTitle());
    }

    @Override
    public int getItemCount() {
        return recyclerDataArrayList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.adapterMainTxt);
        }
    }
}
