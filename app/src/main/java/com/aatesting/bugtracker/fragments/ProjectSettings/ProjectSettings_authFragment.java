package com.aatesting.bugtracker.fragments.ProjectSettings;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aatesting.bugtracker.GlobalValues;
import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.ProjectsMainActivity;
import com.aatesting.bugtracker.data.UserData;
import com.aatesting.bugtracker.modifiedClasses.ModifiedFragment;
import com.aatesting.bugtracker.recyclerview.Adapters.MainRecyclerAdapter;
import com.aatesting.bugtracker.restApi.ApiController;
import com.aatesting.bugtracker.restApi.ApiJSONObject;
import com.aatesting.bugtracker.restApi.ApiSingleton;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProjectSettings_authFragment extends ModifiedFragment {
    public View view;
    public BottomSheetDialog bottomSheetDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_authorities, container, false);

        view = root;

        ((ProjectsMainActivity)requireActivity()).thisFragment = this;
        ((ProjectsMainActivity)requireActivity()).listeners(root, 3, getParentFragmentManager());

        ApiJSONObject jsonObject = new ApiJSONObject(
                UserData.getLastUser(requireContext()).getUsername(),
                GlobalValues.projectOpened
        );

        ApiController.getField(jsonObject, false, false, false,
                requireContext(), GlobalValues.ROLES_URL, this);

        listeners(root);

        return root;
    }

    private void listeners(View root){
        ImageButton roleImg = root.findViewById(R.id.roleImg);
        TextView roleMainTxt = root.findViewById(R.id.roleMainTxt);

        roleImg.setOnClickListener(roleClicked());
        roleMainTxt.setOnClickListener(roleClicked());
    }

    @Override
    public void onResponse(String code) {
        if (code.equals(this.getString(R.string.setupData))) {
            setupData();
        }
        else {
            Log.wtf("ERROR", "onResponse crashed at ProjectsFragment with code " + code);
            super.onResponse("ERROR");
        }

        super.onResponse(code);
    }

    private void setupData(){
        try {
            ArrayList<ApiJSONObject> dataList = ApiSingleton.getInstance().getArray(GlobalValues.ROLES_URL);
            ApiJSONObject data = dataList.get(0);
            Message.message(requireContext(), "dataList size is " + dataList.size());

            setupRoles(data);
        } catch (RuntimeException e){
            Message.defErrMessage(requireContext());
            Log.wtf("ERROR", "Something went wrong while trying to setup data for authFragment ");
            e.printStackTrace();
        }
    }

    private void setupRoles(ApiJSONObject data) throws RuntimeException {
        try{
            CheckBox manageProjectCheckbox = view.findViewById(R.id.manageProjectCheckbox);
            CheckBox manageUsersCheckbox = view.findViewById(R.id.manageUsersCheckbox);
            CheckBox createCheckbox = view.findViewById(R.id.createCheckbox);
            CheckBox editCheckbox = view.findViewById(R.id.editCheckbox);
            CheckBox deleteCheckbox = view.findViewById(R.id.deleteCheckbox);

            manageProjectCheckbox.setChecked(data.getManageProject());
            manageUsersCheckbox.setChecked(data.getManageUsers());
            createCheckbox.setChecked(data.getCreate());
            editCheckbox.setChecked(data.getEdit());
            deleteCheckbox.setChecked(data.getDelete());
        } catch (RuntimeException e){
            throw e;
        }
    }


    @NotNull
    private View.OnClickListener roleClicked() {
        ProjectSettings_authFragment fragment = this;
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair<MainRecyclerAdapter, BottomSheetDialog> pair = ProjectSettings_manageUsersFragment.roleClickedBottomDialog(requireContext(), v, null);

                bottomSheetDialog = pair.second;
                pair.first.projectSettings_authFragment = fragment;
            }
        };
    }
}
