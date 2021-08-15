package com.example.bugtracker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.Message;
import com.example.bugtracker.ProjectCreateTableData;
import com.example.bugtracker.R;
import com.example.bugtracker.recyclerview.Adapters.RecyclerAdapter;
import com.example.bugtracker.recyclerview.RecyclerData;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class ProjectCreateTableEditTask extends AppCompatActivity {
    private TextView editDescriptionTxt;
    private TextView titleMiddle;
    private Button columnSelector;
    public String newData; //the data (string) for the description
    //to get the correct descrition and stuff instead of carrying it for ages.
    private String tag;
    private String projectName;
    private String oldDescription;
    private int itemPos;
    private int columnPos;
    private ArrayList<RecyclerData> recyclerDataArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_create_edit_task);

        tag = getString(R.string.projectEditTask);

        projectName = getIntent().getExtras().getString("projectName");
        columnPos = getIntent().getExtras().getInt("columnPos");
        itemPos = getIntent().getExtras().getInt("itemPos");
        String columnName = getIntent().getExtras().getString("columnName");
        String itemName = getIntent().getExtras().getString("itemName");

        editDescriptionTxt = findViewById(R.id.descriptionTxt);
        titleMiddle = findViewById(R.id.titleMiddle);
        columnSelector = findViewById(R.id.columnSelector);

        oldDescription = ProjectCreateTableData.GetDescription(projectName, columnPos, itemPos,this);

        columnSelector.setText(columnName);
        titleMiddle.setText(itemName);

        //setting the description
        if (oldDescription == null) {
            editDescriptionTxt.setText(getString(R.string.description));
            editDescriptionTxt.setTextColor(getColor(R.color.white38));
        }
        else {
            editDescriptionTxt.setText(oldDescription);
            editDescriptionTxt.setTextColor(getColor(R.color.white60));
        }
        Listeners();
    }

    private void Listeners(){
        Context context = this;
        ProjectCreateTableEditTask projectCreateTableEditTask = this;

        columnSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = v.findViewById(android.R.id.content);
                ArrayList<String> allColumnTitles = new ArrayList<>();

                View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.bottomdialog_complex_recyclerview, viewGroup, false);
                BottomSheetDialog bottomDialog = new BottomSheetDialog(context);
                    //bottomDialog.setTitle("Issue Type");
                    //bottomDialog.setTitle("These are the issue types you can choose, based on the workflow of the current issue type.");
                    bottomDialog.getWindow().setBackgroundDrawableResource(R.color.darkGray);
                    bottomDialog.setContentView(dialogView);

                    //for changing the size
                    //View bottomSheet = bottomDialog.findViewById(R.id.design_bottom_sheet);
                    //bottomSheet.getLayoutParams().height = 500;

                bottomDialog.show();

                recyclerDataArrayList.clear();

                allColumnTitles = ProjectCreateTableData.GetAllColumns(projectName, context);

                for (int i = 0; i < allColumnTitles.size(); i++)
                    recyclerDataArrayList.add(new RecyclerData(allColumnTitles.get(i), 2131165294, tag));

                RecyclerView bottomDialogRecyclerView = dialogView.findViewById(R.id.BtmDialogRecyclerview);
                TextView title = dialogView.findViewById(R.id.BtmDialogTitle);

                title.setVisibility(View.VISIBLE);
                title.setText("Select a transition");
                RecyclerAdapter adapter = new RecyclerAdapter(recyclerDataArrayList, context);

                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                bottomDialogRecyclerView.setLayoutManager(layoutManager);

                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(bottomDialog.getContext(),
                        layoutManager.getOrientation());
                dividerItemDecoration.setDrawable(context.getDrawable(R.drawable.shape_seperator));
                bottomDialogRecyclerView.addItemDecoration(dividerItemDecoration);

                bottomDialogRecyclerView.setAdapter(adapter);
                adapter.projectTableColumnPos = columnPos;
                adapter.projectCreateEditTask_BottomDialog = bottomDialog;
                adapter.projectCreateTableEditTask = projectCreateTableEditTask;
                adapter.itemPos = itemPos;
                adapter.projectName = projectName;
            }
        });

        editDescriptionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( ProjectCreateTableEditTask.this, ProjectCreateTableEditDescription.class);
                intent.putExtra("oldData", oldDescription);
                startActivityForResult(intent, 1); //for getting data back from the second activity
            }
        });
    }

    public void UpdateColumn(int columnPos){
        //for when you use the button to change the column.
        this.columnPos = columnPos;
        itemPos = 0;

        String columnName = ProjectCreateTableData.GetColumn(projectName, columnPos, this);
        columnSelector.setText(columnName);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //for getting data back from the second activity
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                newData = data.getStringExtra("newData");
                ProjectCreateTableData.SaveDescription(projectName, newData, columnPos, itemPos, this);
                Message.message(getBaseContext(), newData);

                //updating the description
                if (newData == null) {
                    editDescriptionTxt.setText(getString(R.string.description));
                    editDescriptionTxt.setTextColor(getColor(R.color.white38));
                }
                else {
                    editDescriptionTxt.setText(newData);
                    editDescriptionTxt.setTextColor(getColor(R.color.white60));
                }
            }
        }
    }
}
