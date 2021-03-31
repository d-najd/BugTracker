package com.example.bugtracker.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{

    private ArrayList<RecyclerData> DataArrayList;
    private Context mcontext;

    public RecyclerAdapter(ArrayList<RecyclerData> recyclerDataArrayList, Context mcontext) {
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
        RecyclerData RecyclerData = DataArrayList.get(position);
        holder.title.setText(RecyclerData.getTitle());
        holder.mainBtn.setImageResource(RecyclerData.getImgId());

        Log.wtf("what is going on", "??");

        if (RecyclerData.getEditTextEnable())
        {
            holder.editText.setVisibility(View.VISIBLE);
            holder.editText.setText(RecyclerData.getDescription());
            holder.description.setVisibility(View.GONE);
        } else if (RecyclerData.getDescription() != null){
            holder.description.setText(RecyclerData.getDescription());
        }
        else
            holder.description.setVisibility(View.GONE);

        if (RecyclerData.getLayout() == 0)
            holder.secondaryBtn.setVisibility(View.VISIBLE);

        Listeners(holder, position);
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return DataArrayList.size();
    }

    private void Listeners(RecyclerViewHolder holder, int position){
        RecyclerData recyclerData = DataArrayList.get(position);
        /*
        holder.mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext, "Implement dragging with holding this button...", Toast.LENGTH_SHORT).show();
            }
        });
         */

        holder.secondaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean favorite = recyclerData.getFavorite();

                if (favorite) {
                    recyclerData.setFavorite(false);
                    holder.secondaryBtn.setImageResource(R.drawable.ic_empty_star_24dp);
                    holder.secondaryBtn.setColorFilter(Color.WHITE);
                }
                else {
                    recyclerData.setFavorite(true);
                    holder.secondaryBtn.setImageResource(R.drawable.ic_star_24dp);
                    holder.secondaryBtn.setColorFilter(Color.YELLOW);
                }
            }
        });
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
            editText = itemView.findViewById(R.id.adapter_editText);
            mainBtn = itemView.findViewById(R.id.adapter_mainImgBtn);
            secondaryBtn = itemView.findViewById(R.id.adapter_favorite_button);
        }
    }
}
