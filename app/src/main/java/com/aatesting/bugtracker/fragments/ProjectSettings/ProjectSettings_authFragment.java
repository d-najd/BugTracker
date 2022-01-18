package com.aatesting.bugtracker.fragments.ProjectSettings;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.ProjectsMainActivity;
import com.aatesting.bugtracker.modifiedClasses.ModifiedFragment;
import com.aatesting.bugtracker.recyclerview.Adapters.MainRecyclerAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;

public class ProjectSettings_authFragment extends ModifiedFragment {
    public View view;
    public BottomSheetDialog bottomSheetDialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_authorities, container, false);

        view = root;

        ((ProjectsMainActivity)requireActivity()).thisFragment = this;
        ((ProjectsMainActivity)requireActivity()).listeners(root, 3, getParentFragmentManager());

        Message.message(requireContext(), "get the authorities");

        listeners(root);

        return root;
    }

    private void listeners(View root){
        ImageButton roleImg = root.findViewById(R.id.roleImg);
        TextView roleMainTxt = root.findViewById(R.id.roleMainTxt);

        roleImg.setOnClickListener(roleClicked());
        roleMainTxt.setOnClickListener(roleClicked());
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
