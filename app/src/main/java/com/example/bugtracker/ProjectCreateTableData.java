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

    public static ArrayList<String> GetAllColumns(String projectName, Context context){
        String data = null;
        ArrayList<String> allColumns = new ArrayList<>();

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

            for (int i = 0; i < parts.length / amountOfPartsInData; i++)
                allColumns.add(parts[i * amountOfPartsInData]);

            return allColumns;
        } catch (Exception e) {
            e.printStackTrace();
            Message.message(context, "Error, something went wrong with sending the old descriptionData back");
            Log.wtf("Error", "something went wrong with sending the old descriptionData back");
            return null;
        }
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
            descriptions = parts[(columnPos * amountOfPartsInData) + 3];
            parts = descriptions.split(",");

            //removing the [ and ] from the strings
            parts[0] = parts[0].substring(1);
            parts[parts.length - 1] = parts[parts.length - 1].substring(0, parts[parts.length - 1].length() - 1);

            data = parts[itemPos];

            data = data.trim();

            if (data.length() <= 0 || data.equals("")) //no intelij its not always false
                return null;

            return data;
        } catch (Exception e) {
            e.printStackTrace();
            Message.message(context, "Error, something went wrong with sending the old descriptionData back");
            Log.wtf("Error", "something went wrong with sending the old descriptionData back");
            return null;
        }
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
            return;
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

    public static void SaveDescription(String projectName, String newData, int columnPos, int itemPos, Context context) {
        BufferedWriter writer = null;
        String data = "";
        String descriptions; //the descriptions string before its edited
        String descriptionsString = ""; //the final descriptions data
        String dataOld = GetData(projectName, context);

        if (newData == null) {
            Log.d("Debug", "description data is null, making it empty string");
            newData = "";
        }

        if (dataOld == null) {
            Log.wtf("the data seems to be null", "Stopping the activity");
            Message.message(context, "The data seems to be null, stopping the activity");
            return;
        }

        File f = new File(context.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "ProjectBoard", projectName + ".txt");

        String[] parts = dataOld.split(separator); //splitting the data
        descriptions = parts[(columnPos * amountOfPartsInData) + 3];
        String[] descriptionParts = descriptions.split(","); // a list of all the parts
        descriptionParts[itemPos] = newData;

        //removing unnecesary spaces or the description wont look soo good and it may cause problems with the arrays
        for (int i = 0; i < descriptionParts.length; i++)
            descriptionParts[i] = descriptionParts[i].trim();

        //formatting the data, transforming it from list to string so it can be replaced it later
        if (itemPos == 0)
            descriptionsString += "[";
        for (int i = 0; i < descriptionParts.length; i++) {
            if (i != descriptionParts.length - 1)
                descriptionsString += (descriptionParts[i] + ", ");
            else
                descriptionsString += descriptionParts[i];
        }
        if (itemPos == descriptionParts.length - 1)
            descriptionsString += "]";

        //remaking the data string
        parts[(columnPos * amountOfPartsInData) + 3] = descriptionsString;
        for (int i = 0; i < parts.length; i++) {
            data += (parts[i] + separator);
        }

        try {
            writer = new BufferedWriter(new FileWriter(f, false));
            writer.write(data);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public static void MoveItemToOtherColumn(String projectName, int newColumn, int oldColumn, int itemPos, Context context){
        BufferedWriter writer = null;
        String data = "";
        String descriptions; //the descriptions string before its edited
        String dataRaw = ""; //the final descriptions data
        String dataOld = GetData(projectName, context);

        String items = ""; //refers to the items inside the titles for example, [title1, title2]

        if (dataOld == null) {
            Log.wtf("the data seems to be null", "Stopping the activity");
            Message.message(context, "The data seems to be null, stopping the activity");
            return;
        }

        File f = new File(context.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "ProjectBoard", projectName + ".txt");

        String[] parts = dataOld.split(separator); //splitting the data

        for (int i = 1; i < amountOfPartsInData; i++){
            //getting the data title for example
            items = parts[(oldColumn * amountOfPartsInData) + i];
            String[] itemParts = items.split(","); //refers to the parts of the items for example, out of [title1, title2] just title1 or title2
            String newData = itemParts[itemPos];

            //TODO add removing and adding of item
            //removing the item ex [title1, title2] and tt

            String newColumnItems = parts[(newColumn * amountOfPartsInData) + i];
            String[] newItemParts = newColumnItems.split(",");

            //removing unnecesary spaces or the description wont look soo good and it may cause problems with the arrays
            for (int b = 0; b < newItemParts.length; b++)
                newItemParts[b] = newItemParts[b].trim();

            //formatting the data, transforming it from list to string so it can be replaced it later
            int newItemPos = (newColumn * amountOfPartsInData) + i - 1;
            //TODO the adding has to be here and the line above is useless, use itempos instead

            if (itemPos == 0) {
                dataRaw += "[";
                dataRaw += newData;
                dataRaw += ", ";
            }
            for (int b = 0; b < newItemParts.length; b++) {
                if (b != newItemParts.length - 1)
                    dataRaw += (newItemParts[b] + ", ");
                else
                    dataRaw += newItemParts[b];
            }
            if (itemPos == newItemParts.length - 1)
                dataRaw += "]";

            //remaking the data string
            parts[(newColumn * amountOfPartsInData) + i] = dataRaw;

            String testdata = "";

            for (int b = 0; b < parts.length; b++) {
                testdata += (parts[b] + separator);
            }


            testdata = testdata;

        }


        /*
        descriptions = parts[(oldColumn * amountOfPartsInData) + 3];
        String[] descriptionParts = descriptions.split(","); // a list of all the parts
        descriptionParts[itemPos] = newData;

        //removing unnecesary spaces or the description wont look soo good and it may cause problems with the arrays
        for (int i = 0; i < descriptionParts.length; i++)
            descriptionParts[i] = descriptionParts[i].trim();

        //formatting the data, transforming it from list to string so it can be replaced it later
        if (itemPos == 0)
            descriptionsString += "[";
        for (int i = 0; i < descriptionParts.length; i++) {
            if (i != descriptionParts.length - 1)
                descriptionsString += (descriptionParts[i] + ", ");
            else
                descriptionsString += descriptionParts[i];
        }
        if (itemPos == descriptionParts.length - 1)
            descriptionsString += "]";

        //remaking the data string
        parts[(columnPos * amountOfPartsInData) + 3] = descriptionsString;
        for (int i = 0; i < parts.length; i++) {
            data += (parts[i] + separator);
        }

         */

        /*
        try {
            writer = new BufferedWriter(new FileWriter(f, false));
            writer.write(data);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

         */

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
