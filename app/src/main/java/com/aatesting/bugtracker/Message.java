package com.aatesting.bugtracker;

import android.content.Context;
import android.widget.Toast;

public class Message {
    public static void message(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void defErrMessage(Context context){
        Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
    }
}