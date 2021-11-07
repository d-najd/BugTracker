package com.aatesting.bugtracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.MainActivity;

public class AccountFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        ((MainActivity)getActivity()).Listeners(1);

        return root;
    }
}