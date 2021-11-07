package com.aatesting.bugtracker.recyclerview.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.activities.ProjectsMainActivity;
import com.aatesting.bugtracker.data.ProjectTableData;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.ProjectCreateTableEditTaskActivity;
import com.aatesting.bugtracker.recyclerview.RecyclerData;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.RecyclerViewHolder>{

    public ArrayList<RecyclerData> recyclerDataArrayList;
    public Context mcontext;
    public RecyclerViewHolder holder;
    public RecyclerData recyclerData;
    List<RecyclerViewHolder> holderArrayList = new ArrayList<RecyclerViewHolder>();

    //region ProjectEditTask
    public String projectName;
    public int projectTableColumnPos; //in which column the item got pressed
    public int itemPos;
    public String projectTableColumnName;
    public BottomSheetDialog projectCreateEditTask_BottomDialog;
    public ProjectCreateTableEditTaskActivity projectCreateTableEditTask;
    //endregion

    public MainRecyclerAdapter(ArrayList<RecyclerData> recyclerDataArrayList, Context mcontext) {
        this.recyclerDataArrayList = recyclerDataArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardlayout_checklist, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        recyclerData = recyclerDataArrayList.get(position);

        String layout = recyclerData.getTag();
        this.holder = holder;
        layout = layout;

        holderArrayList.add(holder);
        if (holderArrayList.size() > recyclerDataArrayList.size()){
            holderArrayList.clear();
            holderArrayList.add(holder);
        }
        holder.itemView.setVisibility(View.VISIBLE);


        Listeners(position);

        holder.title.setText(recyclerData.getTitle());

        if (holder.mainBtn != null)
            holder.mainBtn.setImageResource(recyclerData.getImgId());
        if (recyclerData.getEditTextEnable()) {
            holder.editText.setVisibility(View.VISIBLE);
            holder.editText.setHint(recyclerData.getDescription());
            holder.description.setVisibility(View.GONE);
            if (recyclerData.getTitle() == null) {
                holder.title.setVisibility(View.GONE);
                holder.editText.setTextSize(14);

                //for removing the margins and making it more centered
                ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) holder.editText.getLayoutParams();
                p.setMargins(0, 10, 0, 10);
                holder.editText.requestLayout();

                if (recyclerData.getDescription() != null)
                    holder.editText.setHint(recyclerData.getDescription());
            }
        } else if (recyclerData.getDescription() != null) {
            holder.description.setText(recyclerData.getDescription());
        } else
            holder.description.setVisibility(View.GONE);

        if (layout.equals(mcontext.getString(R.string.titleProjects))) {
            holder.secondaryBtn.setVisibility(View.VISIBLE);
        }

        if (recyclerData.getSecondImgId() != 0) {
            holder.secondaryBtn.setImageResource(recyclerData.getSecondImgId());
            holder.secondaryBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return recyclerDataArrayList.size();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void Listeners(int position){
        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                recyclerData.setDescription(s + "");
                recyclerDataArrayList.get(position).setDescription(s + "");
            }
        });

        if (recyclerData.getTag().equals(mcontext.getString(R.string.titleProjects))) {
            holder.secondaryBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean favorite = recyclerData.getFavorite();
                    if (favorite) {
                        recyclerData.setFavorite(false);
                        holderArrayList.get(position).secondaryBtn.setImageResource(R.drawable.ic_empty_star_24dp);
                    } else {
                        recyclerData.setFavorite(true);
                        holderArrayList.get(position).secondaryBtn.setImageResource(R.drawable.ic_star_24dp);
                    }
                }
            });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, ProjectsMainActivity.class);
                    int test = position;

                    //TODO fixme there is a bug when you create new item it doesnt get added to the list or smtn and you cant open it without the app crashing
                    // but it gets fixed if activity is refreshed.
                    intent.putExtra("projectName", holderArrayList.get(position).title.getText().toString());
                    mcontext.startActivity(intent);
                }
            });
            holder.mainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, ProjectsMainActivity.class);
                    int test = position;
                    intent.putExtra("projectName", holderArrayList.get(position).title.getText().toString());
                    mcontext.startActivity(intent);
                }
            });
        }

        else if (recyclerData.getTag().equals(R.string.createTask)){

            if (position == 0){ //TODO this should be changed with the way of updatedatetime because its more reliable
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SelectBetweenTaskEpic();
                        Toast.makeText(mcontext, "test", Toast.LENGTH_SHORT).show();
                    }
                });
                /* TODO CHECK IF THIS NEEDS TO STAY HERE
                holder.mainBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SelectBetweenTaskEpic();
                        Toast.makeText(mcontext, "test", Toast.LENGTH_SHORT).show();

                    }
                });

                 */
            }
        }

        else if (recyclerData.getTag().equals(mcontext.getString(R.string.projectCreateTask))) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, ProjectCreateTableEditTaskActivity.class);
                    intent.putExtra("projectName", projectName);
                    intent.putExtra("columnName", projectTableColumnName);
                    intent.putExtra("columnPos", projectTableColumnPos);
                    intent.putExtra("itemName", holderArrayList.get(position).title.getText().toString());
                    intent.putExtra("itemPos", position);
                    mcontext.startActivity(intent);
                }
            });
        }

        else if (recyclerData.getTag().equals(mcontext.getString(R.string.projectEditTask0))){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProjectTableData.MoveItemToOtherColumn(projectName, position,
                            projectTableColumnPos, itemPos, mcontext);
                    try
                    {
                        projectCreateEditTask_BottomDialog.dismiss();
                        projectCreateTableEditTask.UpdateColumn(position);
                    }
                    catch (Exception e)
                    {
                        Log.wtf("ERROR", "bottomdialog doesnt exist even tho its pressed or projectCreateTableEditTask is null, the exception is " + e);
                        Message.message(mcontext,"Something went wrong");
                    }
                }
            });
            holder.mainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProjectTableData.MoveItemToOtherColumn(projectName, position,
                            projectTableColumnPos, itemPos, mcontext);
                    try
                    {
                        projectCreateEditTask_BottomDialog.dismiss();
                        projectCreateTableEditTask.UpdateColumn(position);
                    }
                    catch (Exception e)
                    {
                        Log.wtf("ERROR", "bottomdialog doesnt exist even tho its pressed or projectCreateTableEditTask is null, the exception is " + e);
                        Message.message(mcontext,"Something went wrong");
                    }
                }
            });
        } else {
            Log.wtf("Debug", "there is no activity for the current tag, there might be something wrong");
        }

        /* TODO this seems like a dumb way to do it, dont you think?
        if (holder.title.getText().equals(mcontext.getString(R.string.newTask))) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mcontext, CreateTaskActivity.class);
                    mcontext.startActivity(intent);
                }
            });
            holder.mainBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mcontext, CreateTaskActivity.class);
                        mcontext.startActivity(intent);
                    }
                });
            }

         */


        }

    //specialpass is to skip checking and if you are completly sure that it will work and no way to fail
    //also special pass wont hidebuttons bc both will be hidden once they are enabled by default
    //thus fucking everything up so they have to be manualy dissabled outside of here

    private void SelectBetweenTaskEpic(){
        String [] test = new String[2];
        test[0] = "Text";

        test[1] = "Checklist";
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle("Add")
                .setItems(test, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            Toast.makeText(mcontext, "Need to add Activity for this", Toast.LENGTH_SHORT).show();
                        }
                        else if (which == 1){
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // View Holder Class to handle Recycler View.
    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;
        public EditText editText;
        public ImageButton mainBtn;
        public ImageButton secondaryBtn;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.adapterMainTxt);
            description = itemView.findViewById(R.id.adapterSecondaryTxt);
            editText = itemView.findViewById(R.id.adapterEditTxt);
            mainBtn = itemView.findViewById(R.id.adapterMainBtn);
            secondaryBtn = itemView.findViewById(R.id.adapterFavoriteBtn);
        }
    }
}