package com.aatesting.bugtracker.restApi;

import java.util.ArrayList;

public class ApiSingleton {
    private static ApiSingleton mInstance;
    private ArrayList<ApiJSONObject> list = null;

    public static ApiSingleton getInstance() {
        if (mInstance == null)
            mInstance = new ApiSingleton();

        return mInstance;
    }
    private ApiSingleton() {
        list = new ArrayList<ApiJSONObject>();
    }

    public ArrayList<ApiJSONObject> getArray() {
        return this.list;
    }

    public void addToArray(ApiJSONObject value) {
        list.add(value);
    }

    public void reset() {
        if (list != null)
            list.clear();
    }

    public ApiJSONObject getObject(int pos){
        return list.get(pos);
    }
}
