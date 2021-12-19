package com.aatesting.bugtracker.restApi;

import java.util.ArrayList;
import java.util.Comparator;

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
        if (!list.isEmpty())
            return list.get(pos);
        else
            return null;
    }

    public void reorderByPosition(boolean reverse){
        if (!list.isEmpty())
            if (ApiSingleton.getInstance().getObject(0).getPosition() != -1 && reverse)
                ApiSingleton.getInstance().getArray().sort(Comparator.comparing(ApiJSONObject::getPosition).reversed());
            else
                ApiSingleton.getInstance().getArray().sort(Comparator.comparing(ApiJSONObject::getPosition));
    }
}
