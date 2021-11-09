package com.aatesting.bugtracker;

public class GlobalValues {
    public static boolean reloadedActivity; //for knowing whether the current activity is reloaded like the activity with multiple recyclerviews which needs that, in theory this can be used for multiple things, worst case scenario it should reload the activity twice;
    public static int weeksRoadmapLen; //for knowing many weeks are in weeksRecyclerAdapter
    public static boolean mainActivityReloaded; //to know when the main activity needs to be refreshed
}
