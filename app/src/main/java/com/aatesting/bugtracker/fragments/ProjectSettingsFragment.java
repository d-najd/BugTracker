package com.aatesting.bugtracker.fragments;

import android.content.Context;
import android.os.Bundle;
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
import androidx.recyclerview.widget.RecyclerView;

import com.aatesting.bugtracker.GlobalValues;
import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.ProjectsMainActivity;
import com.aatesting.bugtracker.modifiedClasses.ModifiedFragment;
import com.aatesting.bugtracker.restApi.ApiController;
import com.aatesting.bugtracker.restApi.ApiJSONObject;

import org.jetbrains.annotations.NotNull;

public class ProjectSettingsFragment extends ModifiedFragment {
    private View root;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_roles, container, false);
        context = getContext();

        ((ProjectsMainActivity)requireActivity()).thisFragment = this;
        ((ProjectsMainActivity)requireActivity()).listeners(root, 2, getParentFragmentManager());

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
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message.message(context, "implement method");
            }
        };
    }
}
