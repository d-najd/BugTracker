package com.example.bugtracker.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.R;

import java.util.ArrayList;
import java.util.List;

public class ProjectTableCreate_RecyclerAdapter extends RecyclerView.Adapter<ProjectTableCreate_RecyclerAdapter.RecyclerViewHolder> {
    private ArrayList<RecyclerData> DataArrayList;
    private Context mcontext;
    private RecyclerViewHolder holder;
    List<RecyclerViewHolder> holderArrayList = new ArrayList<>();
    private ArrayList<RecyclerData> recyclerDataArrayList = new ArrayList<>();

    public ProjectTableCreate_RecyclerAdapter(ArrayList<RecyclerData> recyclerDataArrayList, Context mcontext) {
        this.DataArrayList = recyclerDataArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout_project_table, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        RecyclerData recyclerData = DataArrayList.get(position);
        String layout = recyclerData.getTag();
        this.holder = holder;
        holderArrayList.add(holder);

        holder.title.setText(recyclerData.getTitle());
        holder.numberOfItems.setText(recyclerData.getTitles().size());
        TableData(recyclerData, holder);

    }

    private void TableData(RecyclerData recyclerData, RecyclerViewHolder holder) {
        int tableSize = recyclerData.getTitles().size();

        Message.message(mcontext, "table size is " + tableSize);

        for (int i = 0; i < tableSize; i++){
            recyclerDataArrayList.add(new RecyclerData(recyclerData.getTitles().get(i),
                    recyclerData.getImgIds().get(i),  recyclerData.getTag()));

        }

        RecyclerAdapter adapter = new RecyclerAdapter(recyclerDataArrayList, mcontext);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext, LinearLayoutManager.VERTICAL, false);

        // at last set adapter to recycler view.
        holder.recyclerView.setHasFixedSize(false);

        RecyclerView recyclerView = holder.recyclerView;
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


    }


    @Override
    public int getItemCount() {
        return DataArrayList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView numberOfItems;
        private ImageButton moreVertical;
        private RecyclerView recyclerView;
        private ImageView backgroundofCreate;
        private ImageView createImg;
        private TextView createTxt;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            numberOfItems = itemView.findViewById(R.id.number_of_items);
            moreVertical = itemView.findViewById(R.id.more_vertical);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            backgroundofCreate = itemView.findViewById(R.id.background_of_create);
            createImg = itemView.findViewById(R.id.create_Img);
            createTxt = itemView.findViewById(R.id.create_Txt);
        }
    }
}