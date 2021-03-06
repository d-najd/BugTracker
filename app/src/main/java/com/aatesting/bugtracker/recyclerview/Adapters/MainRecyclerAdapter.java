package com.aatesting.bugtracker.recyclerview.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.aatesting.bugtracker.GlobalValues;
import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.activities.Projects.ProjectsMainActivity;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.Projects.ProjectTableEditTaskActivity;
import com.aatesting.bugtracker.data.UserData;
import com.aatesting.bugtracker.fragments.ProjectSettings.SubFragments.ProjectSettings_authFragment;
import com.aatesting.bugtracker.fragments.ProjectSettings.SubFragments.ProjectSettings_manageUsersFragment;
import com.aatesting.bugtracker.fragments.ProjectSettings.SubFragments.ProjectSettings_usersFragment;
import com.aatesting.bugtracker.fragments.Main.ProjectsFragment;
import com.aatesting.bugtracker.modifiedClasses.ModifiedFragment;
import com.aatesting.bugtracker.recyclerview.RecyclerData;
import com.aatesting.bugtracker.restApi.ApiController;
import com.aatesting.bugtracker.restApi.ApiJSONObject;
import com.aatesting.bugtracker.restApi.ApiSingleton;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.RecyclerViewHolder>{

    public ArrayList<RecyclerData> recyclerDataArrayList;
    public Context mcontext;
    public RecyclerViewHolder holder;
    public RecyclerData recyclerData;
    public List<RecyclerViewHolder> holderArrayList = new ArrayList<RecyclerViewHolder>();

    //region ProjectEditTask
    public String projectName;
    public int projectTableColumnPos; //in which column the item got pressed
    public int itemPos;
    public String projectTableColumnName;
    public ModifiedFragment fragment;
    public Activity activity;

    public BottomSheetDialog bottomDialog;
    public ProjectSettings_manageUsersFragment projectSettingsAddUserFragment;
    public ProjectSettings_authFragment projectSettings_authFragment;
    public ProjectSettings_usersFragment projectSettings_usersFragment;

    //endregion

    public MainRecyclerAdapter(ArrayList<RecyclerData> recyclerDataArrayList, Context mcontext) {
        this.recyclerDataArrayList = recyclerDataArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardlayout_checklist, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        recyclerData = recyclerDataArrayList.get(position);

        String layout = recyclerData.getTag();
        this.holder = holder;
        if (layout == null)
            layout = "DEFINELAYOUT";

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
                holder.editText.requestLayout();

                if (recyclerData.getDescription() != null)
                    holder.editText.setHint(recyclerData.getDescription());
            }
        } else if (recyclerData.getDescription() != null && !layout.equals(mcontext.getString(R.string.projectCreateTask))) {
            holder.description.setText(recyclerData.getDescription());
        } else
            holder.description.setVisibility(View.GONE);

        if (layout != null && layout.equals(mcontext.getString(R.string.titleProjects))) {
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

        if (recyclerData.getTag() == null)
            return;

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
                    ifTagProjects(position);
                }
            });
            holder.mainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ifTagProjects(position);
                }
            });
        }
        else if (recyclerData.getTag().equals(mcontext.getString(R.string.projectUsers))){
            holder.itemView.setOnClickListener(ifTagProjectsUsers(position));
            holder.mainBtn.setOnClickListener(ifTagProjectsUsers(position));
        }

        else if (recyclerData.getTag().equals(mcontext.getString(R.string.projectCreateTask))) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ifTagProjectCreateTask(position);
                }
            });
            holder.mainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ifTagProjectCreateTask(position);
                }
            });
        }

        else if (recyclerData.getTag().equals(mcontext.getString(R.string.TEDTBottomDialog))){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = position;
                    int columnpos = projectTableColumnPos;
                    int itempos = itemPos;

                    ifTagTEDTBottomDialog(position, GlobalValues.BOARDS_URL);
                }
            });
            holder.mainBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ifTagTEDTBottomDialog(position, GlobalValues.BOARDS_URL);
                }
            });
        }
        else if (recyclerData.getTag().equals(ProjectSettings_manageUsersFragment.tag)){
            holder.mainBtn.setOnClickListener(ifTagProjectSettingsBottomDialog(position));
            holder.itemView.setOnClickListener(ifTagProjectSettingsBottomDialog(position));
        }
        else {
            Log.wtf("\nWARRNING", "there is no function for the current tag: " + recyclerData.getTag() + ", there might be something wrong\n");
        }
    }

    //the fragment in projectSettings
    private View.OnClickListener ifTagProjectsUsers(Integer position){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = projectSettings_usersFragment.getParentFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("setupCode", "custom_user");
                bundle.putString("username", holderArrayList.get(position).title.getText().toString());
                ProjectSettings_authFragment fragment = new ProjectSettings_authFragment();
                fragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.navHostFragment, fragment);
                fragmentTransaction.disallowAddToBackStack();
                fragmentTransaction.commit();
            }
        };
    }

    private View.OnClickListener ifTagProjectSettingsBottomDialog(Integer position){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = holderArrayList.get(position).title.getText().toString().toLowerCase();
                View pjsView;
                if (projectSettingsAddUserFragment != null)
                    pjsView = projectSettingsAddUserFragment.view;
                else if (projectSettings_authFragment != null)
                    pjsView = projectSettings_authFragment.view;
                else{
                    Message.defErrMessage(mcontext);
                    Log.wtf("ERROR", "there is a dialog with roles but it isnt in authFragment or manageUsersFragment in projectSettings");
                    return;
                }

                if (title.equals("admin"))
                    UserData.setRolesCheckboxes(pjsView, true, true, true, true, true);
                else if (title.equals("manager"))
                    UserData.setRolesCheckboxes(pjsView,false, true, true, true ,true);
                else if (title.equals("developer"))
                    UserData.setRolesCheckboxes(pjsView,false, false, true, true ,true);
                else if (title.equals("user"))
                    UserData.setRolesCheckboxes(pjsView,false, false, true, true, false);
                else if (title.equals("tester"))
                    UserData.setRolesCheckboxes(pjsView,false, false, false, false, false);
                else{
                    Message.defErrMessage(v.getContext());
                    Log.wtf("ERROR", "invalid field selected in the ProjectSettingsBottomDialog, selected field name: " + title);
                    return;
                }
                try {
                    TextView psfTextView = pjsView.findViewById(R.id.roleMainTxt);
                    psfTextView.setText(holderArrayList.get(position).title.getText().toString());
                } catch (NullPointerException e){
                    Message.defErrMessage(v.getContext());
                    e.printStackTrace();
                }

                if (projectSettingsAddUserFragment != null)
                    projectSettingsAddUserFragment.bottomSheetDialog.dismiss();
                else if (projectSettings_authFragment != null)
                    projectSettings_authFragment.bottomSheetDialog.dismiss();
                else{
                    Message.defErrMessage(mcontext);
                    Log.wtf("WTF", "there is a dialog with roles but it isnt in authFragment or manageUsersFragment in projectSettings, oh and this is the second one and it should be IMPOSSIBLE TO FUCKING REACH WTF?");
                    return;
                }

            }
        };
    }

    private void ifTagTEDTBottomDialog(int position, @NotNull String type) {
        //prevents window leak
        if (bottomDialog != null)
            bottomDialog.cancel();

        int taskid = ApiSingleton.getInstance().getObject(projectTableColumnPos, type).getTask(itemPos).getId();
        int columnid = ApiSingleton.getInstance().getObject(position, type).getId();

        ApiController.editField(fragment, activity,
                GlobalValues.BTJ_URL + "/" + GlobalValues.TASKS_URL + "/" + ApiSingleton
                        .getInstance().getObject(projectTableColumnPos, type).getTask(itemPos).getId()
                + "/endboard/" + ApiSingleton.getInstance().getObject(position, type).getId()
                + "/newpos/" + ApiSingleton.getInstance().getObject(position, type).getTasks().size());
    }

    private void ifTagProjectCreateTask(int position) {
        Intent intent = new Intent(mcontext, ProjectTableEditTaskActivity.class);
        intent.putExtra("columnPos", projectTableColumnPos);
        intent.putExtra("itemPos", position);
        mcontext.startActivity(intent);
    }

    private void ifTagProjects(int position) {
        Intent intent = new Intent(mcontext, ProjectsMainActivity.class);

        String projectName = holderArrayList.get(position).title.getText().toString();
        intent.putExtra("projectName", projectName);

        ArrayList<ApiJSONObject> projectsList = ApiSingleton.getInstance().getArray(GlobalValues.PROJECTS_URL);
        for (int i = 0; i < projectsList.size(); i++){
            if (projectsList.get(i).getTitle().equals(projectName))
            {
                GlobalValues.projectOpened = ApiSingleton.getInstance().getObject(i, GlobalValues.PROJECTS_URL).getId();

                fragment.onResponse("NotifyProjectViewed", String.valueOf(GlobalValues.projectOpened));
                mcontext.startActivity(intent);
                return;
            }
        }

        Message.defErrMessage(mcontext);
        Log.wtf("ERROR", "while getting a project in " + ProjectsFragment.class.getSimpleName() +
                " got title which isn't in the list of projects in the phone, pressed item position is: " + position + ", project name is:" + projectName);
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