package com.aatesting.bugtracker.fragments.ProjectSettings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.Projects.ProjectsMainActivity;
import com.aatesting.bugtracker.fragments.FragmentSettings;
import com.aatesting.bugtracker.fragments.Main.GridFragment;
import com.aatesting.bugtracker.fragments.ProjectSettings.SubFragments.ProjectSettings_authFragment;
import com.aatesting.bugtracker.fragments.ProjectSettings.SubFragments.ProjectSettings_manageUsersFragment;
import com.aatesting.bugtracker.fragments.ProjectSettings.SubFragments.ProjectSettings_usersFragment;
import com.aatesting.bugtracker.modifiedClasses.ModifiedFragment;

public class ProjectSettingsFragment extends ModifiedFragment {
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);


        ((ProjectsMainActivity)requireActivity()).thisFragment = this;
        ((ProjectsMainActivity)requireActivity()).listeners(root, FragmentSettings.PROJECT_SETTINGS_FRAGMENT_ID, getParentFragmentManager());

        listeners(root);

        return root;
    }

    private void listeners(View root){
        TextView curAuthoritiesText = root.findViewById(R.id.curAuthoritiesText);
        TextView seeUsersText = root.findViewById(R.id.seeUsersText);
        TextView manageUsersText = root.findViewById(R.id.manageUsersText);
        TextView gridFragmentText = root.findViewById(R.id.gridFragmentText);

        FragmentManager fragmentManager = getParentFragmentManager();

        curAuthoritiesText.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("setupCode", "original");
            ProjectSettings_authFragment fragment = new ProjectSettings_authFragment();
            fragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.navHostFragment, fragment);
            fragmentTransaction.disallowAddToBackStack();
            fragmentTransaction.commit();
        });

        seeUsersText.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            ProjectSettings_usersFragment fragment = new ProjectSettings_usersFragment();
            fragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.navHostFragment, fragment);
            fragmentTransaction.disallowAddToBackStack();
            fragmentTransaction.commit();
        });

        manageUsersText.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            ProjectSettings_manageUsersFragment fragment = new ProjectSettings_manageUsersFragment();
            fragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.navHostFragment, fragment);
            fragmentTransaction.disallowAddToBackStack();
            fragmentTransaction.commit();
        });


        gridFragmentText.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            GridFragment fragment = new GridFragment();
            fragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.navHostFragment, fragment);
            fragmentTransaction.disallowAddToBackStack();
            fragmentTransaction.commit();
        });
    }
}
