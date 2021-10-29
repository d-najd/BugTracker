package com.aatesting.bugtracker;

import android.content.Context;

import java.io.File;

public class RoadmapCreateEpicData {

    public static void MakeFolders(Context context){
        //makes folders where the data is stored
        //TODO add function which checks if permissions for writing data are allowed

        File folder = new File(context.getFilesDir() + File.separator + "ProjectData/Roadmap");

        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}
