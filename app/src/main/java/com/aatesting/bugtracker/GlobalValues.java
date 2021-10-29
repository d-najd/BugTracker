package com.aatesting.bugtracker;

public class GlobalValues {
    public static boolean reloadedActivity; //for knowing whether the current activity is reloaded like the activity with multiple recyclerviews which needs that, in theory this can be used for multiple things, worst case scenario it should reload the activity twice;
    public static int fragmentSelected; //for diferenciating fragments selected in the MainActivity, 0-4 is the order left to right
}
