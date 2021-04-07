package com.example.bugtracker.recyclerview;

import android.widget.Toast;

import com.example.bugtracker.activities.ProjectCreateActivity;

public class Message {
    public static void message(ProjectCreateActivity context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}