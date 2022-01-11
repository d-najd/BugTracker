package com.aatesting.bugtracker.data;

import android.content.Context;
import android.util.Log;

import com.aatesting.bugtracker.GlobalValues;
import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.restApi.ApiJSONObject;
import com.aatesting.bugtracker.restApi.ApiSingleton;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecentlyViewedProjectsData {
    /*this can be used for other data that isn't temp data but isn't settings as well
     */

    private static final String FILE_NAME = "RecentlyViewed";
    public static final String SEPARATOR = "::";
    public static final int MAX_RECENT_PROJECTS = 5;


    //add the project to the list when its opened
            /*
        the way that this works is for ex if there are 3 projects opened and len is 3 already
        pro4::pro3::pro5 and the new one is1 then it should move all projects to the right and
        delete the last, pro1::pro4::pro3 if there arent it should add a new element
        ex: pro1::pro5 to pro1::pro5::pro2
         */

    public static void projectOpened(Context context, String projectId){
        BufferedWriter writer = null;

        File f = new File(context.getFilesDir() + File.separator + "ProjectData", FILE_NAME + ".txt");
        String dataOld = getData(context);
        StringBuilder builder = new StringBuilder();
        ArrayList<String> parts;

        if (dataOld != null)
            parts = new ArrayList<>(Arrays.asList(dataOld.split(SEPARATOR)));
        else
            parts = new ArrayList<>();
        //check if the project hasn't been added to the recentlyViewed list
        if (parts.contains(projectId) && parts.size() != MAX_RECENT_PROJECTS) {
            builder.append(projectId).append(SEPARATOR);
            for (String part : parts) {
                if (!part.equals(projectId))
                    builder.append(part).append(SEPARATOR);
            }
        }

        //add the project to the list
        if (parts.size() >= MAX_RECENT_PROJECTS && !parts.contains(projectId)) {
            builder.append(projectId).append(SEPARATOR);
            for (int i = 0; i < MAX_RECENT_PROJECTS - 1; i++) {
                builder.append(parts.get(i)).append(SEPARATOR);
            }
        } else if (!parts.contains(projectId)) {
            builder.append(projectId).append(SEPARATOR);
            for (int i = 0; i < parts.size(); i++) {
                builder.append(parts.get(i)).append(SEPARATOR);
            }
        }

        String data = builder.toString();

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

    public static void projectRemoved(Context context, String projectId){
        BufferedWriter writer = null;

        File f = new File(context.getFilesDir() + File.separator + "ProjectData", FILE_NAME + ".txt");
        String dataOld = getData(context);
        StringBuilder builder = new StringBuilder();
        if (dataOld != null) {
            String[] parts = dataOld.split(SEPARATOR);
            for (String part : parts) {
                if (!part.equals(projectId))
                    builder.append(part).append(SEPARATOR);
            }
        }

        String data = builder.toString();

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

    public static String getData(Context context){
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

    public static List<ApiJSONObject> getRecentlyViewedList(Context context){
        String data = getData(context);
        ArrayList<ApiJSONObject> projects = ApiSingleton.getInstance().getArray(GlobalValues.PROJECTS_URL);

        if (projects.isEmpty())
        {
            Log.d("DEBUG", "there are no projects in the server to get RecentlyViewedProject (rvp) this may be because rvp is checked before getting the projects (which should be fixed if that is the case) or there are no projects linked with the current user in the database");
            return projects;
        }
        //raw because it needs to be converted to integer list
        ArrayList<String> partsRaw = new ArrayList<>(Arrays.asList(data.split(SEPARATOR)));
        ArrayList<Integer> parts = new ArrayList<>();
        if (partsRaw.get(0).equals(""))
            partsRaw.clear();
        for(String part : partsRaw)
            parts.add(Integer.valueOf(part));

        ArrayList<ApiJSONObject> returnList = new ArrayList<>();

        //sorting the return list this way so it returns the data in the correct order
        for (Integer part : parts){
            for (ApiJSONObject project : projects){
                if (part == project.getId())
                    returnList.add(project);
            }
        }

        return returnList;
    }

    /*


    public static void ProjectOpened(Context context, String projectName) {
        BufferedWriter writer = null;

        File f = new File(context.getFilesDir() + File.separator + "ProjectData", FILE_NAME + ".txt");
        String dataOld = GetData(context);
        StringBuilder builder = new StringBuilder();
        if (dataOld == null)
            return;

        ArrayList<String> parts = new ArrayList<>(Arrays.asList(dataOld.split(SEPARATOR)));
        //check if the project hasn't been added to the recentlyViewed list
        if (parts.contains(projectName) && parts.size() != MAX_RECENT_PROJECTS) {
            builder.append(projectName).append(SEPARATOR);
            for (String part : parts) {
                if (!part.equals(projectName))
                    builder.append(part).append(SEPARATOR);
            }
        }

        //add the project to the list
        if (parts.size() >= MAX_RECENT_PROJECTS) {
            builder.append(projectName).append(SEPARATOR);
            for (int i = 0; i < MAX_RECENT_PROJECTS - 1; i++) {
                builder.append(parts.get(i)).append(SEPARATOR);
            }
        } else if (!parts.contains(projectName)) {
            builder.append(projectName).append(SEPARATOR);
            for (int i = 0; i < parts.size(); i++) {
                builder.append(parts.get(i)).append(SEPARATOR);
            }
        }

        String data = builder.toString();

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


    public static void ProjectRemoved(Context context, String projectName){
        BufferedWriter writer = null;

        File f = new File(context.getFilesDir() + File.separator + "ProjectData", FILE_NAME + ".txt");
        String dataOld = GetData(context);
        StringBuilder builder = new StringBuilder();
        if (dataOld != null) {
            String[] parts = dataOld.split(SEPARATOR);
            for (String part : parts) {
                if (!part.equals(projectName))
                    builder.append(part).append(SEPARATOR);
            }
        }

        String data = builder.toString();

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


     */

    public static void MakeFolders(Context context){
        //TODO add function which checks if permissions for writing data are allowed, same goes for
        // RoadmapEpicData

        File folder = new File(context.getFilesDir() + File.separator + "ProjectData");
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

}
