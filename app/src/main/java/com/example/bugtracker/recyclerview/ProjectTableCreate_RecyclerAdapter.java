package com.example.bugtracker.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.R;
import com.example.bugtracker.activities.ProjectCreateTable;
import com.example.bugtracker.dialogs.BasicDialog;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProjectTableCreate_RecyclerAdapter extends RecyclerView.Adapter<ProjectTableCreate_RecyclerAdapter.RecyclerViewHolder> {
    private ArrayList<RecyclerData> DataArrayList;
    private Context mcontext;
    private RecyclerViewHolder holder;
    private List<RecyclerViewHolder> holderArrayList = new ArrayList<>();
    private ArrayList<RecyclerData> recyclerDataArrayList = new ArrayList<>();

    private String newColumnName = null;

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

        //TODO find out why they are on top of each other (the columns) and a way to properly place them
        if (recyclerData.getTitles() != null && !mcontext.getString(R.string.add_column).equals(recyclerData.getTitle())) {
            holder.numberOfItems.setText(recyclerData.getTitles().size() + "");
        }else{
            AddColumn(holder);
        }
        TableData(recyclerData, holder);
    }

    private void TableData(RecyclerData recyclerData, RecyclerViewHolder holder) {
        int tableSize = 0;
        if (recyclerData.getTitles() != null && !mcontext.getString(R.string.add_column).equals(recyclerData.getTitle()))
            tableSize = recyclerData.getTitles().size();

        recyclerDataArrayList.clear();
        if (tableSize != 0) {
            for (int i = 0; i < tableSize; i++) {
                recyclerDataArrayList.add(new RecyclerData(recyclerData.getTitles().get(i),
                        recyclerData.getImgIds().get(i), recyclerData.getTag()));
            }
        }

        RecyclerAdapter adapter = new RecyclerAdapter(recyclerDataArrayList, mcontext);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext,
                LinearLayoutManager.VERTICAL, false);

        // at last set adapter to recycler view.
        holder.recyclerView.setHasFixedSize(false);

        RecyclerView recyclerView = holder.recyclerView;
        recyclerView.setLayoutManager(layoutManager);

        //this is the seperator, the thing between the items the empty space
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(mcontext.getDrawable(R.drawable.shape_seperator));
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(adapter);
    }

    private void AddColumn(RecyclerViewHolder holder){
        holder.numberOfItems.setVisibility(View.GONE);
        holder.recyclerView.setVisibility(View.GONE);
        holder.createTxt.setVisibility(View.GONE);
        holder.createImg.setVisibility(View.GONE);
        holder.moreVertical.setVisibility(View.GONE);
        holder.title.setTextColor(mcontext.getColor(R.color.blue));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextDialog(v, mcontext, "Add column", "ADD", "CANCEL");
            }
        });
    }

    private void editTextDialog(View v, Context mcontext, String title, String positiveButtonTxt, String negativeButtonTxt){
        //Sadly I cant find a way to return data from a void (the postive button), anyway the dialog
        //is available in basicDialog if I need it
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        ViewGroup viewGroup = v.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(mcontext).inflate(R.layout.dialog_edit_text, viewGroup, false);

        EditText editText = dialogView.findViewById(R.id.editText);
        builder.setView(dialogView)
                .setTitle(title)
                .setNegativeButton(negativeButtonTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(positiveButtonTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /* TODO find a way to save the data
                        newColumnName = editText.getText().toString();
                        ArrayList<String> titles = new ArrayList<>();
                        ArrayList<Integer> imgs = new ArrayList<>();
                        titles.add("TEST");
                        imgs.add(R.drawable.ic_launcher_foreground);

                        ProjectCreateTable projectCreateTable = new ProjectCreateTable();
                        projectCreateTable.SaveData(titles, imgs, editText.getText().toString(), mcontext);
                         */

                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.dark_gray);

        alertDialog.show();
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
        private ImageView createImg;
        private TextView createTxt;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            numberOfItems = itemView.findViewById(R.id.number_of_items);
            moreVertical = itemView.findViewById(R.id.more_vertical);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            createImg = itemView.findViewById(R.id.create_Img);
            createTxt = itemView.findViewById(R.id.create_Txt);
        }
    }
}
