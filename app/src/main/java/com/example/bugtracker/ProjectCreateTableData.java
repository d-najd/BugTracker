package com.example.bugtracker;

import android.content.Context;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;



public class ProjectCreateTableData {

    public static final int amountOfPartsInData = 4;
    private static final String separator = "::"; //the type of separator used for saving the data

    //region Storage

    public static String GetData(String projectName, Context context){
        String data = null;

        File f = new File( context.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "ProjectBoard", projectName + ".txt");

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

    public static String GetDescription(String projectName, int columnPos, int itemPos, Context context){
        String data = null;
        String descriptions = null;

        File f = new File(context.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "ProjectBoard", projectName + ".txt");

        try {
            FileInputStream fileInputStream = new FileInputStream(f);
            int read = -1;
            StringBuffer buffer = new StringBuffer();
            while((read = fileInputStream.read())!= -1){
                buffer.append((char)read);
            }
            data = buffer.toString();
            String[] parts = data.split(separator);

            descriptions = parts[(columnPos * amountOfPartsInData) + 3].substring(0, parts[(columnPos * amountOfPartsInData) + 3].length() - 1);

            parts = descriptions.split(", ");

            data = parts[itemPos];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    //for creating new column with empty data mostly but can work for other stuff
    public static void SaveNewColumn(ArrayList<String> titles, ArrayList<Integer> imgIds,
                                     ArrayList<String> descriptions, String title, String projectName, Context context){
        BufferedWriter writer = null;
        int id = 0;

        File f = new File(context.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "ProjectBoard", projectName + ".txt");

        String dataOld = GetData(projectName, context);
        if (dataOld != null) {
            String[] parts = dataOld.split(separator);
            id = parts.length / amountOfPartsInData;
        }

        String data = title + separator + titles.toString() + separator + imgIds + separator + descriptions + separator;

        try {
            writer = new BufferedWriter(new FileWriter(f, true));
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


    public static void SaveNewItem(String newItemTitle, String projectName,
                                   int id, boolean keepPreviousItems, Context context){
        BufferedWriter writer = null;
        String dataOld = GetData(projectName, context);
        String data = "";

        if (dataOld == null){
            Log.wtf("the data seems to be null", "Stop the activity");
            Message.message(context, "The data seems to be null, stop the activity");
        }

        String[] parts = dataOld.split(separator);


        File f = new File(context.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "ProjectBoard", projectName + ".txt");

        //old title without the ] at the end
        String title = parts[(id * amountOfPartsInData) + 1].substring(0, parts[(id * amountOfPartsInData) + 1].length() - 1);
        String imgId = parts[(id * amountOfPartsInData) + 2].substring(0, parts[(id * amountOfPartsInData) + 2].length() - 1);
        String description = parts[(id * amountOfPartsInData) + 3].substring(0, parts[(id * amountOfPartsInData) + 3].length() - 1);

        //adding the new data
        // also this checks if the length of the string is 1 because if the list is empty it will be left with [
        // and if we add the ", " it will end up like [, data] which will cause some trouble down
        // the road
        if (title.length() != 1) {
            title += (", " + newItemTitle + "]");
            imgId += (", " + "2131165294" + "]"); // the number is for a drawable, can change later
            description += (", " + "]");
        } else {
            title += (newItemTitle + "]");
            imgId += ("2131165294" + "]");
            description += ("]");
        }

        parts[(id * amountOfPartsInData) + 1] = title;
        parts[(id * amountOfPartsInData) + 2] = imgId;
        parts[(id * amountOfPartsInData) + 3] = description;

        if (keepPreviousItems) {
            for (int i = 0; i < parts.length; i++) {
                data += (parts[i] + separator);
            }
        } else
        {
            Log.wtf("", "add a case where it wont keep the previous items");
            Message.message(context, "add a case where it wont keep the previous items");
        }
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

    public static void SaveDescription(String projectName, String newData, int columnPos, int itemPos, Context context){
        //TODO FIXME
        BufferedWriter writer = null;
        String data = null;
        String descriptions = null;
        String parts1data = null;
        String dataOld = GetData(projectName, context);

        if (dataOld == null){
            Log.wtf("the data seems to be null", "Stop the activity");
            Message.message(context, "The data seems to be null, stop the activity");
        }

        File f = new File(context.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "ProjectBoard", projectName + ".txt");

        try {
            FileInputStream fileInputStream = new FileInputStream(f);
            int read = -1;
            StringBuffer buffer = new StringBuffer();
            while((read = fileInputStream.read())!= -1){
                buffer.append((char)read);
            }
            data = buffer.toString();
            String[] parts = data.split(separator);

            descriptions = parts[(columnPos * amountOfPartsInData) + 3].substring(1, parts[(columnPos * amountOfPartsInData) + 3].length() - 1);

            String[] parts1 = descriptions.split(", ");
            parts1[itemPos] = newData;

            for (int i = 0; i < parts1.length; i++) {
                parts1data += (parts1[i] + ", ");
            }

            parts[(columnPos * amountOfPartsInData) + 3] = parts1data;

            data = null;

            for (int i = 0; i < parts.length; i++) {
                data += (parts[i] + separator);
            }

            data = data;
            //writer = new BufferedWriter(new FileWriter(f, false));
            //writer.write(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void RemoveFile(String projectName, Context context){
        File file = new File(context.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "ProjectBoard", projectName + ".txt");

        boolean deleted = file.delete();

        if (!deleted){
            Log.wtf("ERROR", "Something went wrong with removing the file with name "
                    + projectName + " at " + context.getFilesDir().toString() +
                    File.separator.toString() + "ProjectData" + File.separator.toString() + "ProjectBoard");
            Message.message(context, "Something went wrong with removing the file with name "
                    + projectName + " at " + context.getFilesDir().toString() +
                    File.separator.toString() + "ProjectData" + File.separator.toString() + "ProjectBoard");
        }
    }

    //remove selected column
    public static void RemoveColumnData(int id, String projectName, Context context){
        BufferedWriter writer = null;
        File f = new File(context.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "ProjectBoard", projectName + ".txt");

        String data = GetData(projectName, context);
        String[] parts = data.split(separator);

        //this part is for splitting, ex if the id is 1, the startingStr will get data from
        //0 up until 1 and stop, and endstr will get data from 2 and on

        String endStr = "";
        String startStr = "";

        if (id != 0){
            for (int i = 0; i < id * amountOfPartsInData; i++){
                startStr += parts[i] + separator;
            }
        }

        for (int i = (id + 1) * amountOfPartsInData; i < parts.length; i++){
            endStr += parts[i] + separator;
        }

        data = startStr + endStr;

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


    public static void MakeFolders(Context context){
        //makes folders where the data is stored
        //TODO add function which checks if premissions for writing data are allowed

        File folder = new File(context.getFilesDir(), "ProjectData");

        if (!folder.exists()) {
            folder.mkdirs();
        }

        folder = new File(context.getFilesDir() + File.separator + "ProjectData",
                "ProjectBoard");
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}
