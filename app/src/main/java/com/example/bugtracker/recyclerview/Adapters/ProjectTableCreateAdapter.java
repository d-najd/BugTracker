package com.example.bugtracker.recyclerview.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.ProjectCreateTableData;
import com.example.bugtracker.R;
import com.example.bugtracker.activities.ProjectCreateTable;
import com.example.bugtracker.dialogs.BasicDialogs;
import com.example.bugtracker.recyclerview.CustomSpinnerCreator;
import com.example.bugtracker.recyclerview.RecyclerData;

import java.util.ArrayList;
import java.util.List;


public class ProjectTableCreateAdapter extends RecyclerView.Adapter<ProjectTableCreateAdapter.RecyclerViewHolder> {
    private ArrayList<RecyclerData> DataArrayList;
    private Context mcontext;
    private ArrayList<RecyclerData> recyclerDataArrayList = new ArrayList<>();
    private ArrayList<RecyclerViewHolder> holderArrayList = new ArrayList<>();
    public RecyclerViewHolder holder;
    public ProjectCreateTable projectCreateTableActivity;
    public String projectName;
    private String tag;
    public Intent intent;

    public ProjectTableCreateAdapter(ArrayList<RecyclerData> recyclerDataArrayList, Context mcontext) {
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

    //TODO use singleton to fix the problems with the items when pressing editext? also not sure if
    // it needs to be added in here or in RecycelerAdapter

    //TODO FIXME if there are tooo many colums when you swipe from left to right it will increase the
    // gaps between the items more and more

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        RecyclerData recyclerData = DataArrayList.get(position);
        Log.wtf("recyclerData", recyclerData.toString());
        String layout = recyclerData.getTag();
        this.holder = holder;
        holderArrayList.add(holder);

        tag = mcontext.getString(R.string.projectCreateTask);

        MoreVerticalCustomSpinner(position);

        Listeners(position);

        //to create the correct type of column, in other words if it needs to create the
        //column that adds more columns (last one) or the other one
        if (position != DataArrayList.size() - 1) {
            holder.title.setText(recyclerData.getTitle());
            if (recyclerData.getTitles() != null)
                holder.numberOfItems.setText(recyclerData.getTitles().size() + "");
            else
                holder.numberOfItems.setText("0");
        }else{
            NewColumnCreator();
        }
        TableData(recyclerData, position);
    }

    public void RenameTitle(int pos, String newTitle){
        holderArrayList.get(pos).title.setText(newTitle);
        ProjectCreateTableData.RenameColumn(projectName, pos, newTitle, mcontext);
    }

    private void Listeners(int position){
        holder.addColumnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerDataArrayList.clear();

                RecyclerViewHolder curholder = holderArrayList.get(position);
                RecyclerData curData = DataArrayList.get(position);

                BasicDialogs.NewColumnItemDialog(mcontext, "Add problem", "ADD", "CANCEL", position, projectName, projectCreateTableActivity);

            }
        });
    }

    private void MoreVerticalCustomSpinner(int position){
        List<String> columnSpinnerData = new ArrayList<String>();

        columnSpinnerData.add(mcontext.getString(R.string.renameColumn));

        if (position != 0){
            columnSpinnerData.add(mcontext.getString(R.string.moveColumnLeft));
        }
        if (position != DataArrayList.size() - 2)
        {
            columnSpinnerData.add(mcontext.getString(R.string.moveColumnRight));
        }

        columnSpinnerData.add(mcontext.getString(R.string.deleteColumn));

        ProjectTableCreateAdapter projectTableCreate_recyclerAdapter = this;

        holder.moreVertical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CustomSpinnerCreator(mcontext, columnSpinnerData, position, projectTableCreate_recyclerAdapter, v, -390, 0);
            }
        });
    }

    public void CustomSpinnerItemPressed(String itemText, int holderPosition, int itemPosition){
        if (itemText == mcontext.getString(R.string.renameColumn)){
            String description = ProjectCreateTableData.GetColumnTitle(projectName, holderPosition, mcontext);
            BasicDialogs.RenameColumnDialog(mcontext, "Rename Column", description, "CANCEL","RENAME", holderPosition, projectCreateTableActivity, this);
        } else if (itemText == mcontext.getString(R.string.moveColumnLeft)){
            ProjectCreateTableData.MoveColumnToOtherColumn(projectName, holderPosition, holderPosition - 1, mcontext);
            RefreshActivity();
        } else if (itemText == mcontext.getString(R.string.moveColumnRight)){
            ProjectCreateTableData.MoveColumnToOtherColumn(projectName, holderPosition, holderPosition + 1, mcontext);
            RefreshActivity();
        } else if (itemText == mcontext.getString(R.string.deleteColumn)){
            ProjectCreateTableData.RemoveColumnData(projectName, holderPosition, mcontext);

            //TODO fix this
            RefreshActivity();
        }
    }

    private void TableData(RecyclerData recyclerData, int position) {
        int tableSize = 0;
        recyclerDataArrayList.clear();
        if (recyclerData.getTitles() != null)
            tableSize = recyclerData.getTitles().size();

        if (tableSize != 0) {
            for (int i = 0; i < tableSize; i++) {
                recyclerDataArrayList.add(new RecyclerData(recyclerData.getTitles().get(i),
                        recyclerData.getImgIds().get(i), tag));
            }
        }

        MainRecyclerAdapter adapter = new MainRecyclerAdapter(recyclerDataArrayList, mcontext);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext,
                LinearLayoutManager.VERTICAL, false);

        // at last set adapter to recycler view.
        holder.recyclerView.setHasFixedSize(false);

        RecyclerView recyclerView = holder.recyclerView;
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        adapter.projectName = projectName;
        adapter.projectTableColumnName = holderArrayList.get(position).title.getText().toString();
        adapter.projectTableColumnPos = position;
        //This might break some stuff, need to be on the lookout
    }

    private void NewColumnCreator(){
        holder.numberOfItems.setVisibility(View.GONE);
        holder.recyclerView.setVisibility(View.GONE);
        holder.createTxt.setVisibility(View.GONE);
        holder.createImg.setVisibility(View.GONE);
        holder.moreVertical.setVisibility(View.GONE);
        holder.title.setVisibility(View.GONE);
        holder.addColumnTxt.setVisibility(View.VISIBLE);
        holder.addColumnBtn.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BasicDialogs.NewColumnDialog(mcontext, "Add column", "ADD",
                        "Cancel", projectName, projectCreateTableActivity, intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return DataArrayList.size();
    }

    private void RefreshActivity(){
        projectCreateTableActivity.finish();
        projectCreateTableActivity.overridePendingTransition(0, 0);
        projectCreateTableActivity.startActivity(intent);
        projectCreateTableActivity.overridePendingTransition(0, 0);
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private EditText title;
        private TextView numberOfItems;
        private ImageButton moreVertical;
        private RecyclerView recyclerView;
        private ImageView createImg;
        private TextView createTxt;
        private TextView addColumnTxt;
        private Button addColumnBtn;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            numberOfItems = itemView.findViewById(R.id.numberOfItems);
            moreVertical = itemView.findViewById(R.id.moreVertical);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            createImg = itemView.findViewById(R.id.createImg);
            createTxt = itemView.findViewById(R.id.createTxt);
            addColumnTxt = itemView.findViewById(R.id.addColumn);
            addColumnBtn = itemView.findViewById(R.id.createColumnBtn);
        }
    }
}
