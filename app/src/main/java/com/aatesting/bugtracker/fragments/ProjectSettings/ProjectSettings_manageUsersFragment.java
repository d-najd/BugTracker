package com.aatesting.bugtracker.fragments.ProjectSettings;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.aatesting.bugtracker.GlobalValues;
import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.ProjectsMainActivity;
import com.aatesting.bugtracker.dialogs.Dialogs;
import com.aatesting.bugtracker.modifiedClasses.ModifiedFragment;
import com.aatesting.bugtracker.recyclerview.Adapters.MainRecyclerAdapter;
import com.aatesting.bugtracker.restApi.ApiController;
import com.aatesting.bugtracker.restApi.ApiJSONObject;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProjectSettings_manageUsersFragment extends ModifiedFragment {
    private View root;
    private Context context;

    public static final String tag = "ProjectSettings_BottomDialog";
    public View view;
    public BottomSheetDialog bottomSheetDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_manage_users, container, false);
        context = getContext();

        view = root;

        ((ProjectsMainActivity)requireActivity()).thisFragment = this;
        ((ProjectsMainActivity)requireActivity()).listeners(root, 5, getParentFragmentManager());

        listeners(root);

        return root;
    }

    private void listeners(View root){
        EditText usernameEdt = root.findViewById(R.id.usernameEdt);
        ImageButton roleImg = root.findViewById(R.id.roleImg);
        TextView roleMainTxt = root.findViewById(R.id.roleMainTxt);
        AppCompatButton removeButton = root.findViewById(R.id.removeButton);
        Button addButton = root.findViewById(R.id.addButton);

        roleImg.setOnClickListener(roleClicked());
        roleMainTxt.setOnClickListener(roleClicked());

        addButton.setOnClickListener(addUser(usernameEdt, root));
        removeButton.setOnClickListener(removeUser(usernameEdt));
    }

    @Override
    public void onResponse(String code) {
        if (code.equals(this.getString(R.string.getData)))
            Message.message(context, "successfully created user");
        else {
            Log.wtf("ERROR", "onResponse crashed at ProjectsFragment with code " + code);
            super.onResponse("ERROR");
        }
    }

    @NotNull
    private View.OnClickListener removeUser(EditText usernameEdt) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiJSONObject rolesIdentity = new ApiJSONObject(
                        usernameEdt.getText().toString(),
                        GlobalValues.projectOpened
                );

                ApiController.removeField(rolesIdentity, null, null, GlobalValues.ROLES_URL);
            }
        };
    }

    @NotNull
    private View.OnClickListener addUser(EditText usernameEdt, View root){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox manageProjectCheckbox = root.findViewById(R.id.manageProjectCheckbox);
                CheckBox manageUsersCheckbox = root.findViewById(R.id.manageUsersCheckbox);
                CheckBox createCheckbox = root.findViewById(R.id.createCheckbox);
                CheckBox editCheckbox = root.findViewById(R.id.editCheckbox);
                CheckBox deleteCheckbox = root.findViewById(R.id.deleteCheckbox);

                ApiJSONObject rolesIdentity = new ApiJSONObject(
                        usernameEdt.getText().toString(),
                        GlobalValues.projectOpened
                );

                ApiJSONObject roles = new ApiJSONObject(
                        rolesIdentity,
                        manageProjectCheckbox.isChecked(),
                        manageUsersCheckbox.isChecked(),
                        createCheckbox.isChecked(),
                        editCheckbox.isChecked(),
                        deleteCheckbox.isChecked()
                );

                ApiController.createField(roles, GlobalValues.ROLES_URL, null, null);
            }
        };
    }

    @NotNull
    private View.OnClickListener roleClicked() {
        ProjectSettings_manageUsersFragment fragment = this;
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair<MainRecyclerAdapter, BottomSheetDialog> pair = roleClickedBottomDialog(context, v, tag);

                bottomSheetDialog = pair.second;
                pair.first.projectSettingsAddUserFragment = fragment;
            }
        };
    }

    public static Pair<MainRecyclerAdapter, BottomSheetDialog> roleClickedBottomDialog(Context context, View v, String tag){
        ViewGroup viewGroup = v.findViewById(android.R.id.content);

        ArrayList<String> allColumnTitles = new ArrayList<>();
        ArrayList<String> allColumnDescriptions = new ArrayList<>();
        ArrayList<Integer> allColumnImages = new ArrayList<>();

        allColumnTitles.add("Admin");
        allColumnImages.add(R.drawable.ic_account_24dp);
        allColumnDescriptions.add("Has all the abilities except altering of other admins or the owner");

        allColumnTitles.add("Manager");
        allColumnImages.add(R.drawable.ic_account_24dp);
        allColumnDescriptions.add("Has all the abilities except altering other admins, managers or the owner");

        allColumnTitles.add("Developer");
        allColumnImages.add(R.drawable.ic_account_24dp);
        allColumnDescriptions.add("Has ability to create, edit, remove tasks/fields and view project data");

        allColumnTitles.add("User");
        allColumnImages.add(R.drawable.ic_account_24dp);
        allColumnDescriptions.add("Has abilities to create and edit tasks/fields and view project data");

        allColumnTitles.add("Tester");
        allColumnImages.add(R.drawable.ic_account_24dp);
        allColumnDescriptions.add("Has the ability to view project data but not to change any of the contents");

        return Dialogs.BottomDialogCreator(context, v, viewGroup, "Issue Type", "These are the issue types that you can choose, based on the workflow of the current issue type.",
                allColumnTitles, allColumnDescriptions, allColumnImages, tag);
    }
}
