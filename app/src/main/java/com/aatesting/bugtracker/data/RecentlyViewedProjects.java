package com.aatesting.bugtracker.data;

import android.content.Context;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class RecentlyViewedProjects {
    /*this can be used for other data that isn't temp data but isn't settings as well
     */

    private static final String FILE_NAME = "RecentlyViewed";
    private static final String SEPARATOR = "::";
    private static final int MAX_RECENT_PROJECTS = 5;

    //add the project to the list when its opened
    public static void ProjectOpened(Context context, int projectID){
        /*TODO finish this, for ex if there are 3 projects opened and len is 3 already 4::3::5 and the new one is
           1 then it should move all projects to the right and delete the last, 1::4::3 if there
           arent it should add a new element ex: 1::5 to 1::5::2

         */

        BufferedWriter writer = null;

        File f = new File(context.getFilesDir() + File.separator + "ProjectData", FILE_NAME + ".txt");
        String dataOld = GetData(context);
        String data;
        if (dataOld != null) {
            String[] parts = dataOld.split(SEPARATOR);
            /*
            if (parts.length >= MAX_RECENT_PROJECTS){
                return;
            } else{

            }

             */
        }

        data = dataOld + projectID + SEPARATOR;

        try {
            writer = new BufferedWriter(new FileWriter(f, false));
            writer.write(data);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String GetData(Context context){
        String data = null;

        File f = new File(context.getFilesDir() + File.separator + "ProjectData", FILE_NAME + ".txt");
        try {
            FileInputStream fileInputStream = new FileInputStream(f);
            int read = -1;
            StringBuffer buffer = new StringBuffer();
            while((read = fileInputStream.read())!= -1){
                buffer.append((char)read);
            }
            data = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void MakeFolders(Context context){
        //TODO add function which checks if permissions for writing data are allowed, same goes for
        // RoadmapEpicData

        File folder = new File(context.getFilesDir() + File.separator + "ProjectData");
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}
