package com.aatesting.bugtracker.restApi;

import android.util.Log;

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

    public ArrayList<ApiJSONObject> getArray(String type) {
        ArrayList<ApiJSONObject> list = findProjectByType(type);
        return list;
    }

    public void addToArray(ApiJSONObject value, String type){
        ArrayList<ApiJSONObject> list = findProjectByType(type);
        list.add(value);
    }


    public void reset(String type) {
        ArrayList<ApiJSONObject> list = findProjectByType(type);
        if (list != null)
            list.clear();
    }

    public ApiJSONObject getObject(int pos, String type){
        ArrayList<ApiJSONObject> list = findProjectByType(type);
        if (!list.isEmpty())
            return list.get(pos);
        else
            return null;
    }

    public void reorderByPosition(boolean reverse, String type){
        ArrayList<ApiJSONObject> list = findProjectByType(type);

        if (!list.isEmpty())
            if (ApiSingleton.getInstance().getObject(0, type).getPosition() != -1 && reverse)
                ApiSingleton.getInstance().getArray(type).sort(Comparator.comparing(ApiJSONObject::getPosition).reversed());
            else
                ApiSingleton.getInstance().getArray(type).sort(Comparator.comparing(ApiJSONObject::getPosition));
    }

    private ArrayList<ApiJSONObject> findProjectByType(String type) {
        switch (type){
            case "project":
                return projectList;
            case "boards":
                return boardsList;
            case "roadmaps":
                return roadmapsList;
            default:
                Log.wtf("ERROR", "use specific type for each of the singleton lists, returning default value");
                return defaultList;
        }
    }
}
