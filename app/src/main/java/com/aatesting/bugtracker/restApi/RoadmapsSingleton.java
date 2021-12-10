package com.aatesting.bugtracker.restApi;

import java.util.ArrayList;

public class RoadmapsSingleton {
    // Static variable reference of single_instance
    // of type Singleton
    private static RoadmapsSingleton mInstance;
    private ArrayList<RoadmapObject> list = null;

    public static RoadmapsSingleton getInstance() {
        if (mInstance == null)
            mInstance = new RoadmapsSingleton();

        return mInstance;
    }
    private RoadmapsSingleton() {
        list = new ArrayList<RoadmapObject>();
    }

    // retrieve array from anywhere
    public ArrayList<RoadmapObject> getArray() {
        return this.list;
    }

    //Add element to array
    public void addToArray(RoadmapObject value) {
        list.add(value);
    }

    public void reset() {
        if (list != null)
            list.clear();
    }

    public RoadmapObject getObject(int pos){
        return list.get(pos);
    }
}
