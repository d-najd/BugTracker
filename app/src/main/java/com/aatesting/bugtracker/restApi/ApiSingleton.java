package com.aatesting.bugtracker.restApi;

import android.util.Log;

import com.aatesting.bugtracker.GlobalValues;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;

public class ApiSingleton {
    private static ApiSingleton mInstance;
    private ArrayList<ApiJSONObject> defaultList = null;
    private ArrayList<ApiJSONObject> projectList = null;
    private ArrayList<ApiJSONObject> boardsList = null;
    private ArrayList<ApiJSONObject> roadmapsList = null;

    public static ApiSingleton getInstance() {
        if (mInstance == null)
            mInstance = new ApiSingleton();

        return mInstance;
    }
    private ApiSingleton() {
        defaultList = new ArrayList<>();
        projectList = new ArrayList<>();
        boardsList = new ArrayList<>();
        roadmapsList = new ArrayList<>();
    }

    public ArrayList<ApiJSONObject> getArray(@NotNull String type) {
        ArrayList<ApiJSONObject> list = findProjectByType(type);
        return list;
    }

    public void addToArray(@NotNull ApiJSONObject value, @NotNull String type){
        ArrayList<ApiJSONObject> list = findProjectByType(type);
        list.add(value);
    }


    public void reset(@NotNull String type) {
        ArrayList<ApiJSONObject> list = findProjectByType(type);
        if (list != null)
            list.clear();
    }

    public ApiJSONObject getObject(int pos, @NotNull String type){
        ArrayList<ApiJSONObject> list = findProjectByType(type);
        if (!list.isEmpty())
            return list.get(pos);
        else
            return null;
    }

    public void reorderByPosition(boolean reverse, @NotNull String type){
        ArrayList<ApiJSONObject> list = findProjectByType(type);

        if (!list.isEmpty())
            if (ApiSingleton.getInstance().getObject(0, type).getPosition() != -1 && reverse)
                ApiSingleton.getInstance().getArray(type).sort(Comparator.comparing(ApiJSONObject::getPosition).reversed());
            else
                ApiSingleton.getInstance().getArray(type).sort(Comparator.comparing(ApiJSONObject::getPosition));
    }

    private ArrayList<ApiJSONObject> findProjectByType(@NotNull String type) {
        if (type == null) {
            Log.wtf("ERROR", "WHAT THE ACTUAL FUCK, THE TYPE IS WITH TAG @NOTNULL WTF");
            return null;
        }

        switch (type){
            case GlobalValues.PROJECTS_URL:
                return projectList;
            case GlobalValues.BOARDS_URL:
                return boardsList;
            case GlobalValues.ROADMAPS_URL:
                return roadmapsList;
            default:
                Log.wtf("ERROR", "use specific type for each of the singleton lists, returning default value");
                return defaultList;
        }
    }
}
