package com.example.bugtracker.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bugtracker.MainActivity;
import com.example.bugtracker.R;
import com.example.bugtracker.recyclerview.Message;
import com.example.bugtracker.recyclerview.myDbAdapter;

public class DashboardFragment extends Fragment {
    //EditText Name, Pass, updateold, updatenew, delete;
    //myDbAdapter helper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        return root;
    }
}