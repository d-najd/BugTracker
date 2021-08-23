package com.example.bugtracker;

public class GlobalValues {
    public static boolean reloadedActivity; //for knowing whether the current activity is reloaded like the activity with multiple recyclerviews which needs that, in theory this can be used for multiple things, worst case scenario it should reload the activity twice;
}
