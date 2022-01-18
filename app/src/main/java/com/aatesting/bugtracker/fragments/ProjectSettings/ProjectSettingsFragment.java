package com.aatesting.bugtracker.fragments.ProjectSettings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.ProjectsMainActivity;
import com.aatesting.bugtracker.modifiedClasses.ModifiedFragment;

public class ProjectSettingsFragment extends ModifiedFragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);


        ((ProjectsMainActivity)requireActivity()).thisFragment = this;
        ((ProjectsMainActivity)requireActivity()).listeners(root, 2, getParentFragmentManager());

        Message.message(requireContext(), "finish the get authorities and get users in the project");

        listeners(root);

        return root;
    }

    private void listeners(View root){
        TextView curAuthoritiesText = root.findViewById(R.id.curAuthoritiesText);
        TextView seeUsersText = root.findViewById(R.id.seeUsersText);
        TextView manageUsersText = root.findViewById(R.id.manageUsersText);

        FragmentManager fragmentManager = getParentFragmentManager();

        curAuthoritiesText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                ProjectSettings_authFragment projectSettings_authFragment = new ProjectSettings_authFragment();
                projectSettings_authFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.navHostFragment, projectSettings_authFragment);
                fragmentTransaction.disallowAddToBackStack();
                fragmentTransaction.commit();
            }
        });

        manageUsersText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                ProjectSettings_manageUsersFragment dashboardFragment = new ProjectSettings_manageUsersFragment();
                dashboardFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.navHostFragment, dashboardFragment);
                fragmentTransaction.disallowAddToBackStack();
                fragmentTransaction.commit();
            }
        });
    }
}
