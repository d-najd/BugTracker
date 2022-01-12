package com.aatesting.bugtracker.data;

import android.content.Context;
import android.util.Log;

import com.aatesting.bugtracker.Message;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ProjectTableData {

    public static final int AMOUNT_OF_PARTS_IN_DATA = 4;
    private static final String SEPARATOR = "::"; //the type of separator used for saving the data
    /* NOTE THIS SCRIPT IS DISABLED
     *//*

    tasks will be stored in ProjectData/ProjectBoard/Tasks
    current format
    titles::taskstitles::tasksimgsids::tasksdescriptions
    planned
    title::taskstitles::subTaskIdList::extras
    extras for task
    taskdescriptions, parentids, dateCreatedList

    subtasts will be stored in ProjectData/ProjectBoard/Subtasks
    format for subtasks
    id::extras
    extras for subtask
    title, description, creationdate

     */

    //region getters

    public static ArrayList<String> GetAllItemData(String projectName, int fieldId, Context context){
        //gets specific part of all items, for example get all item titles
        ArrayList<String> returnData = new ArrayList<>();

        String data = GetData(projectName, context);
        String[] parts = data.split(SEPARATOR);

        for (int i = 0; i < parts.length / AMOUNT_OF_PARTS_IN_DATA; i++)
            returnData.add(parts[i * AMOUNT_OF_PARTS_IN_DATA] + fieldId);

        return returnData;
    }

    public static String GetColumnData(String projectName, int columnPos, int fieldId, Context context){
        String data = GetData(projectName, context);
        String[] parts = data.split(SEPARATOR);
        String returnData = parts[(columnPos * AMOUNT_OF_PARTS_IN_DATA) + fieldId];

        return returnData;
    }

    public static String GetItemListData(String projectName, int columnPos, int itemPos, int fieldId, Context context){
        String listData = null;

        String data = GetData(projectName, context);
        String[] parts = data.split(SEPARATOR);
        listData = parts[(columnPos * AMOUNT_OF_PARTS_IN_DATA) + fieldId];
        parts = listData.split(",");

        //removing the [ and ] from the strings
        parts[0] = parts[0].substring(1);
        parts[parts.length - 1] = parts[parts.length - 1].substring(0, parts[parts.length - 1].length() - 1);

        data = parts[itemPos];

        data = data.trim();

        if (data.length() <= 0 || data.equals("")) //no intelij its not always false
            return null;

        return data;
    }

    //endregion

    //region setters

    public static void CreatingNewProject(String projectName, Context context){
        ArrayList<String> titlesEmptyArr = new ArrayList<>();
        ArrayList<String> descriptionsEmptyArr = new ArrayList<>();
        ArrayList<Integer> imgsEmptyArr = new ArrayList<>();

        //SaveNewColumn(projectName, "TO DO", titlesEmptyArr, imgsEmptyArr, descriptionsEmptyArr, context);
        //SaveNewColumn(projectName, "IN PROGRESS", titlesEmptyArr, imgsEmptyArr, descriptionsEmptyArr, context);
       // SaveNewColumn(projectName, "DONE", titlesEmptyArr, imgsEmptyArr, descriptionsEmptyArr, context);
    }

    //for creating new column with empty data mostly but can work for other stuff
    public static void SaveNewColumn(String projectName, String title, ArrayList<String> titles, ArrayList<Integer> imgIds,
                                     ArrayList<String> descriptions, Context context){
        BufferedWriter writer = null;
        int id = 0;

        File f = new File(context.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "ProjectBoard", projectName + ".txt");

        String dataOld = GetData(projectName, context);
        if (dataOld != null) {
            String[] parts = dataOld.split(SEPARATOR);
            id = parts.length / AMOUNT_OF_PARTS_IN_DATA;
        }

        String data = title + SEPARATOR + titles.toString() + SEPARATOR + imgIds + SEPARATOR + descriptions + SEPARATOR;

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

        String[] parts = dataOld.split(SEPARATOR);

        File f = new File(context.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "ProjectBoard", projectName + ".txt");

        //old title without the ] at the end
        String title = parts[(id * AMOUNT_OF_PARTS_IN_DATA) + 1].substring(0, parts[(id * AMOUNT_OF_PARTS_IN_DATA) + 1].length() - 1);
        String imgId = parts[(id * AMOUNT_OF_PARTS_IN_DATA) + 2].substring(0, parts[(id * AMOUNT_OF_PARTS_IN_DATA) + 2].length() - 1);
        String description = parts[(id * AMOUNT_OF_PARTS_IN_DATA) + 3].substring(0, parts[(id * AMOUNT_OF_PARTS_IN_DATA) + 3].length() - 1);

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

        parts[(id * AMOUNT_OF_PARTS_IN_DATA) + 1] = title;
        parts[(id * AMOUNT_OF_PARTS_IN_DATA) + 2] = imgId;
        parts[(id * AMOUNT_OF_PARTS_IN_DATA) + 3] = description;

        if (keepPreviousItems) {
            for (int i = 0; i < parts.length; i++) {
                data += (parts[i] + SEPARATOR);
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

    public static void EditListData(String projectName, String newData, int columnPos, int itemPos, int fieldId, Context context) {
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
            Log.wtf("ERROR", "the data seems to be null, Stopping the activity");
            Message.message(context, "Something went wrong");
            return;
        }

        File f = new File(context.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "ProjectBoard", projectName + ".txt");

        String[] parts = dataOld.split(SEPARATOR); //splitting the data
        descriptions = parts[(columnPos * AMOUNT_OF_PARTS_IN_DATA) + fieldId];
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
        parts[(columnPos * AMOUNT_OF_PARTS_IN_DATA) + fieldId] = descriptionsString;
        for (int i = 0; i < parts.length; i++) {
            data += (parts[i] + SEPARATOR);
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

    //endregion

    //region removers

    public static void RemoveFile(String projectName, Context context){
        File file = new File(context.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "ProjectBoard", projectName + ".txt");

        boolean deleted = file.delete();

        if (!deleted){
            Log.wtf("ERROR", "Something went wrong with removing the file with name "
                    + projectName + " at " + context.getFilesDir().toString() +
                    File.separator + "ProjectData" + File.separator + "ProjectBoard");
        }
    }

    //remove selected column
    public static void RemoveColumn(String projectName, int id, Context context){
        BufferedWriter writer = null;
        File f = new File(context.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "ProjectBoard", projectName + ".txt");

        String data = GetData(projectName, context);
        String[] parts = data.split(SEPARATOR);

        //this part is for splitting, ex if the id is 1, the startingStr will get data from
        //0 up until 1 and stop, and endstr will get data from 2 and on

        String endStr = "";
        String startStr = "";

        if (id != 0){
            for (int i = 0; i < id * AMOUNT_OF_PARTS_IN_DATA; i++){
                startStr += parts[i] + SEPARATOR;
            }
        }

        for (int i = (id + 1) * AMOUNT_OF_PARTS_IN_DATA; i < parts.length; i++){
            endStr += parts[i] + SEPARATOR;
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

    //for removing a singular task
    public static void RemoveTask(String projectName, int id, int fieldId, Context context){
        BufferedWriter writer = null;
        String data = "";
        String descriptions; //the descriptions string before its edited
        String dataOld = GetData(projectName, context);

        if (dataOld == null) {
            Log.wtf("ERROR", "the data seems to be null, Stopping the activity oh and THIS SHOULDNT BE POSSIBLE");
            Message.message(context, "Something went wrong");
            return;
        }

        File f = new File(context.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "ProjectBoard", projectName + ".txt");

        List<String> parts = Arrays.asList(dataOld.split(SEPARATOR));
        String tempData;

        StringBuilder dataRaw = null;
        for (int i = 1; i < AMOUNT_OF_PARTS_IN_DATA; i++) {
            //getting the left and right side and combining them
            tempData = "";
            ArrayList<String> itemParts = new ArrayList<String>(Arrays.asList
                    (parts.get((AMOUNT_OF_PARTS_IN_DATA * id) + i).split(",")));
            if (fieldId != 0) {
                for (int curField = 0; curField < itemParts.subList(0, fieldId).size(); curField++){
                    if (curField != itemParts.size() - 2)
                        tempData += itemParts.get(curField).trim() + ", ";
                    else
                        tempData += itemParts.get(curField).trim();
                }
            }
            else
                tempData += "[";
            if (fieldId != itemParts.size() - 1) {
                for (int curField = fieldId + 1; curField < itemParts.size(); curField++) {
                    if (curField != itemParts.size() - 1)
                        tempData += itemParts.get(curField).trim() + ", ";
                    else
                        tempData += itemParts.get(curField).trim();
                }
            }
            else
                tempData += "]";
            parts.set((AMOUNT_OF_PARTS_IN_DATA * id) + i, tempData);
        }

        for (int i = 0; i < parts.size(); i++)
            data += (parts.get(i) + SEPARATOR);

        data = data;

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
    //endregion

    //region modifiers
    public static void EditData(String projectName, int id, int fieldId, String newData, Context context){
        BufferedWriter writer = null;
        String data = "";
        String descriptions; //the descriptions string before its edited
        String dataOld = GetData(projectName, context);

        if (dataOld == null) {
            Log.wtf("ERROR", "the data seems to be null, Stopping the activity oh and THIS SHOULDNT BE POSSIBLE");
            Message.message(context, "Something went wrong");
            return;
        }

        if (newData == null || newData == ""){
            Log.wtf("Debug", "cannot set column title to null value or empty string, quiting");
            return;
        }

        File f = new File(context.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "ProjectBoard", projectName + ".txt");

        String[] parts = dataOld.split(SEPARATOR); //splitting the data

        parts[(id * AMOUNT_OF_PARTS_IN_DATA)] = newData;

        for (int i = 0; i < parts.length; i++)
            data += (parts[i] + SEPARATOR);

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


    //swap columns
    public static void SwapColumns(String projectName, int oldColumnPos, int newColumnPos, Context context){
        BufferedWriter writer = null;
        String data = "";
        String descriptions; //the descriptions string before its edited
        String dataOld = GetData(projectName, context);

        if (dataOld == null) {
            Log.wtf("ERROR", "the data seems to be null, Stopping the activity oh and THIS SHOULDNT BE POSSIBLE");
            Message.message(context, "Something went wrong");
            return;
        }

        if (newColumnPos == oldColumnPos){
            Log.d("Debug", "you cant move the new column in the place of the old column");
            return;
        }

        File f = new File(context.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "ProjectBoard", projectName + ".txt");

        ArrayList<String> parts = new ArrayList<String>(Arrays.asList(dataOld.split(SEPARATOR)));

        ArrayList<String> oldColumnParts = new ArrayList<>();
        ArrayList<String> newColumnParts = new ArrayList<>();


        for (int i = 0; i < AMOUNT_OF_PARTS_IN_DATA; i++){
            oldColumnParts.add(parts.get((oldColumnPos * AMOUNT_OF_PARTS_IN_DATA) + i));
            newColumnParts.add(parts.get((newColumnPos * AMOUNT_OF_PARTS_IN_DATA) + i));
        }

        for (int i = 0; i < AMOUNT_OF_PARTS_IN_DATA; i++){
            parts.set((newColumnPos * AMOUNT_OF_PARTS_IN_DATA) + i, oldColumnParts.get(i));
            parts.set((oldColumnPos * AMOUNT_OF_PARTS_IN_DATA) + i, newColumnParts.get(i));
        }


        for (int i = 0; i < parts.size(); i++)
            data += (parts.get(i) + SEPARATOR);

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

    public static void MoveItemToOtherColumn(String projectName, int newColumn, int oldColumn, int itemPos, Context context){
        BufferedWriter writer = null;
        String data = "";
        String descriptions; //the descriptions string before its edited
        String dataOld = GetData(projectName, context);

        if (dataOld == null) {
            Log.wtf("ERROR", "the data seems to be null, Stopping the activity oh and THIS SHOULDNT BE POSSIBLE");
            Message.message(context, "Something went wrong");
            return;
        }

        if (newColumn == oldColumn){
            Log.d("Debug", "trying to move column 1 to colum 1 basicly replace the item inside the column, skipping this");
            return;
        }

        File f = new File(context.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "ProjectBoard", projectName + ".txt");

        String[] parts = dataOld.split(SEPARATOR); //splitting the data

        for (int i = 1; i < AMOUNT_OF_PARTS_IN_DATA; i++){
            //getting the data title for example
            StringBuilder dataRaw = new StringBuilder(""); //the final descriptions data

            String oldColumnItems = parts[(oldColumn * AMOUNT_OF_PARTS_IN_DATA) + i]; //refers to the items inside the titles for example, [title1, title2]
            String[] oldItemParts = oldColumnItems.split(","); //refers to the parts of the items for example, out of [title1, title2] just title1 or title2

            String newColumnItems = parts[(newColumn * AMOUNT_OF_PARTS_IN_DATA) + i];
            String[] newItemParts = newColumnItems.split(",");

            String newData = oldItemParts[itemPos]; //the data that will be moved

            //removing unnecesary parts and adding formatting the part that needs to be added to the new column
            if (itemPos == 0)
                newData = newData.substring(1);
            if (itemPos == oldItemParts.length - 1)
                newData = newData.substring(0, newData.length() - 1);
            newData = newData.trim();
            newData += ", ";

            //removing unnecesary spaces or the description wont look soo good and it may cause problems with the arrays
            for (int b = 0; b < newItemParts.length; b++)
                newItemParts[b] = newItemParts[b].trim();

            //formatting the data (transforming it to string) and also adding part that needs to be replaced (added in this case)
            for (int b = 0; b < newItemParts.length; b++) {
                if (b != newItemParts.length - 1)
                    dataRaw.append(newItemParts[b] + ", ");
                else
                    dataRaw.append(newItemParts[b]);
                if (b == 0)
                    dataRaw.insert(1, newData);
            }

            //replacing the data (adding the new element to the data)
            parts[(newColumn * AMOUNT_OF_PARTS_IN_DATA) + i] = dataRaw.toString();


            //REMOVING PARTS OF THE DATA
            //removing the item ex [title1, title2] and we remove title1 for example so we will be left with [title2]

            dataRaw = new StringBuilder("");
            oldItemParts[itemPos] = ""; //I have no idea why but this line is needed

            //removing unnecesary spaces
            for (int b = 0; b < oldItemParts.length; b++)
                oldItemParts[b] = oldItemParts[b].trim();

            if (itemPos == 0)
                dataRaw.append("[");
            for (int b = 0; b < oldItemParts.length; b++) {
                if (oldItemParts[b] == "")
                    continue;
                    //checks if addition of ", " is needed, the last part is for making sure that if the last element is changed then the ", " isnt added to second from last element because last is so we dont need the ", "
                else if (b != oldItemParts.length - 1 && (itemPos != oldItemParts.length - 1 ||
                        (itemPos == oldItemParts.length - 1 && b != oldItemParts.length - 2)))
                    dataRaw.append(oldItemParts[b] + ", ");
                else
                    dataRaw.append(oldItemParts[b]);
            }
            if (itemPos == oldItemParts.length - 1)
                dataRaw.append("]");

            parts[(oldColumn * AMOUNT_OF_PARTS_IN_DATA) + i] = dataRaw.toString();
        }

        for (int i = 0; i < parts.length; i++) {
            data += (parts[i] + SEPARATOR);
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

    //endregion

    public static String GetData(String projectName, Context context){
        String data = null;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void MakeFolders(Context context){
        //TODO add function which checks if permissions for writing data are allowed, same goes for
        // RoadmapEpicData

        File folder = new File(context.getFilesDir() + File.separator + "ProjectData" + File.separator + "ProjectBoard");
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}
