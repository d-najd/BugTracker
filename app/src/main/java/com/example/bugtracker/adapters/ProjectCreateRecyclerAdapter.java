package com.example.bugtracker.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.R;

import java.util.ArrayList;

public class ProjectCreateRecyclerAdapter extends RecyclerView.Adapter<ProjectCreateRecyclerAdapter.RecyclerViewHolder>{

    private ArrayList<RecyclerData> DataArrayList;
    private Context mcontext;

    public ProjectCreateRecyclerAdapter(ArrayList<RecyclerData> recyclerDataArrayList, Context mcontext) {
        this.DataArrayList = recyclerDataArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout_checklist, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Set the data to textview and imageview.
        RecyclerData ProjectCreateRecyclerData = DataArrayList.get(position);
        holder.title.setText(ProjectCreateRecyclerData.getTitle());
        holder.mainBtn.setImageResource(ProjectCreateRecyclerData.getImgId());

        if (ProjectCreateRecyclerData.getEditText() != null && ProjectCreateRecyclerData.getDescription() != null)
        {
            Toast.makeText(mcontext, "Both Editext and Description \nhave text in them", Toast.LENGTH_SHORT).show();
            Log.wtf("Error", "Both Editext and Description \nhave text in them" );
        }
        else if (ProjectCreateRecyclerData.getDescription() != null)
            holder.description.setText(ProjectCreateRecyclerData.getDescription());

        else if (ProjectCreateRecyclerData.getEditText() != null)
            holder.editText.setText(ProjectCreateRecyclerData.getEditText());

        holder.secondaryBtn.setVisibility(View.GONE);

        Listeners(holder, position);
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return DataArrayList.size();
    }

    private void Listeners(RecyclerViewHolder holder, int position){
        RecyclerData ProjectCreateRecyclerData = DataArrayList.get(position);
        /*
        holder.mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext, "Implement dragging with holding this button...", Toast.LENGTH_SHORT).show();
            }
        });
         */
    }


    // View Holder Class to handle Recycler View.
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private EditText editText;
        private ImageButton mainBtn;
        private ImageButton secondaryBtn;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.adapter_main_text);
            description = itemView.findViewById(R.id.adapter_secondary_text);
            editText = itemView.findViewById(R.id.editText);
            mainBtn = itemView.findViewById(R.id.adapter_mainImgBtn);
            secondaryBtn = itemView.findViewById(R.id.adapter_favorite_button);
        }
    }
}
