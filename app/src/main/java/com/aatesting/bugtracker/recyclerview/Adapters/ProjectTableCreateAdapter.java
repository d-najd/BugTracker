package com.aatesting.bugtracker.recyclerview.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aatesting.bugtracker.GlobalValues;
import com.aatesting.bugtracker.activities.Projects.ProjectsMainActivity;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.dialogs.Dialogs;
import com.aatesting.bugtracker.recyclerview.CustomSpinnerCreator;
import com.aatesting.bugtracker.recyclerview.RecyclerData;
import com.aatesting.bugtracker.restApi.ApiController;
import com.aatesting.bugtracker.restApi.ApiJSONObject;
import com.aatesting.bugtracker.restApi.ApiSingleton;

import java.util.ArrayList;
import java.util.List;


public class ProjectTableCreateAdapter extends RecyclerView.Adapter<ProjectTableCreateAdapter.RecyclerViewHolder> {
    private ArrayList<RecyclerData> DataArrayList; //is different from recyclerdataArrayList
    private Context mcontext;
    private ArrayList<RecyclerData> recyclerDataArrayList = new ArrayList<>(); //is different from dataArrayList
    private ArrayList<RecyclerViewHolder> holderArrayList = new ArrayList<>();
    private RecyclerViewHolder holder;
    public ProjectsMainActivity projectMainActivity;
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
                .inflate(R.layout.cardlayout_project_table, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        RecyclerData recyclerData = DataArrayList.get(position);
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
            holder.numberOfItems.setText(ApiSingleton.getInstance().getObject(position, GlobalValues.BOARDS_URL).getTasks().size() + "");
        }else{
            NewColumnCreator();
        }
        TableData(recyclerData, position);
    }

    public void RenameTitle(int pos, String newTitle){
        holderArrayList.get(pos).title.setText(newTitle);
        //ProjectTableData.EditData(projectName, pos, 0, newTitle, mcontext);
    }

    private void Listeners(int position){
        ProjectTableCreateAdapter adapter = this;

        holder.addColumnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerViewHolder curholder = holderArrayList.get(position);
                RecyclerData curData = DataArrayList.get(position);

                Dialogs.newItemDialog(mcontext, "Add problem", "ADD",
                        "CANCEL", position, projectMainActivity.thisFragment, GlobalValues.BOARDS_URL);
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
        ApiJSONObject object = ApiSingleton.getInstance().getObject(holderPosition, GlobalValues.BOARDS_URL);

        if (itemText == mcontext.getString(R.string.renameColumn)){
            String description = object.getTitle();
            Dialogs.RenameColumnDialog(mcontext, "Rename Column", description,
                    "CANCEL","RENAME", projectMainActivity.thisFragment, holderPosition, GlobalValues.BOARDS_URL);
        } else if (itemText == mcontext.getString(R.string.moveColumnLeft)){
            ApiController.editField(projectMainActivity.thisFragment, null, "boards/swap/first/" +
                    object.getId() + "/second/" + ApiSingleton.getInstance().getObject(holderPosition - 1, GlobalValues.BOARDS_URL).getId());
        } else if (itemText == mcontext.getString(R.string.moveColumnRight)){
            ApiController.editField(projectMainActivity.thisFragment, null, "boards/swap/first/" +
                    object.getId() + "/second/" + ApiSingleton.getInstance().getObject(holderPosition + 1, GlobalValues.BOARDS_URL).getId());
        } else if (itemText == mcontext.getString(R.string.deleteColumn)){
            ApiController.removeField(null,null, projectMainActivity.thisFragment,"boards/" +
                    ApiSingleton.getInstance().getObject(holderPosition, GlobalValues.BOARDS_URL).getId(), null);
        }
    }

    private void TableData(RecyclerData recyclerData, int position) {
        recyclerDataArrayList.clear();
        //there is an extra column "add column" at the end, he isn't connected with ApiSingleton so
        //there isnt any data for him and just skipping him instead of crashing the app
        if (ApiSingleton.getInstance().getArray(GlobalValues.BOARDS_URL).size() <= position) {
            return;
        }//needs to be converted to Arraylist<RecyclerData> to be used
        ArrayList<ApiJSONObject> rawRecyclerData = ApiSingleton.getInstance().getObject(position, GlobalValues.BOARDS_URL).getTasks();

        for (ApiJSONObject object : rawRecyclerData){
            recyclerDataArrayList.add(new RecyclerData(
                    object.getTitle(),
                    object.getDescription(),
                    tag
            ));
        }

        //for the love of god dont change any of the fields, including the subRecyclerData, I am not sure
        //how or why the fuck this works but it fixes a HUGE problem, just don't
        final ArrayList<RecyclerData> data = new ArrayList<>(recyclerDataArrayList);

        MainRecyclerAdapter adapter = new MainRecyclerAdapter(data, mcontext);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext,
                LinearLayoutManager.VERTICAL, false);

        // at last set adapter to recycler view.
        holderArrayList.get(position).recyclerView.setHasFixedSize(false);

        RecyclerView recyclerView = holderArrayList.get(position).recyclerView;
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        adapter.projectName = projectName;
        adapter.projectTableColumnName = "PLACEHOLDER";
        adapter.projectTableColumnPos = position;
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
                Dialogs.NewColumnDialog(v.getContext(), "Add column", "ADD",
                        "Cancel", projectMainActivity.thisFragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return DataArrayList.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private EditText title;
        private TextView numberOfItems;
        private ImageButton moreVertical;
        private RecyclerView recyclerView;
        private ImageView createImg;
        private TextView createTxt;
        private TextView addColumnTxt;
        private ImageButton addColumnBtn;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            numberOfItems = itemView.findViewById(R.id.numberOfItems);
            moreVertical = itemView.findViewById(R.id.moreVertical);
            recyclerView = itemView.findViewById(R.id.weeksRecyclerView);
            createImg = itemView.findViewById(R.id.createImg);
            createTxt = itemView.findViewById(R.id.createTxt);
            addColumnTxt = itemView.findViewById(R.id.addColumn);
            addColumnBtn = itemView.findViewById(R.id.createColumnBtn);
        }
    }
}
