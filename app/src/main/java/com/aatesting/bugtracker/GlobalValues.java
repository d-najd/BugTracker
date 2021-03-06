package com.aatesting.bugtracker;

import com.aatesting.bugtracker.restApi.ApiJSONObject;

public class GlobalValues {
    //for knowing whether the current activity is reloaded like the activity with multiple
    // recyclerviews which needs that, in theory this can be used for multiple things, worst case
    // scenario it should reload the activity twice;
    public static boolean reloadedActivity;

    public static int weeksRoadmapLen; //for knowing many weeks are in weeksRecyclerAdapter
    public static boolean mainActivityReloaded; //to know when the main activity needs to be refreshed

    //for knowing which field of the data is modified and sending the data to the server not updating
    //the server imidiately so it doesn't overload, default value is -1 which means no field modified
    //-2 means the data needs refreshing but there is no data changed
    public static ApiJSONObject objectModified;

    //the current project opened
    public static int projectOpened;

    public static final String GET_BY_USER = "getByUser";
    public static final String BOARDS_URL = "boards";
    public static final String ROADMAPS_URL = "roadmaps";
    public static final String PROJECTS_URL = "project";
    public static final String TASKS_URL = "tasks";
    public static final String BTJ_URL = "btj";
    public static final String USERS_URL = "users";
    public static final String ROLES_URL = "roles";
}
