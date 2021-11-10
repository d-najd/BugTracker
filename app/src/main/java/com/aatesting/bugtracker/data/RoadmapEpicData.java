package com.aatesting.bugtracker.data;

import android.content.Context;
import android.util.Log;

import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.activities.RoadmapEditEpicActivity;
import com.aatesting.bugtracker.recyclerview.RecyclerData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RoadmapEpicData {

    public static final int amountOfPartsInData = 4;
    private static final int amountOfPartsInExtras = 4;
    private static final String separator = "::"; //the type of separator used for saving the data
    private static final String extrasSeparator = ">>"; //why cant I use $$ ?
    /*
        epics are stored in ProjectData/Roadmap/Epics
        format for epics
        title::startDate::dueDate::extras
        extras for epics
        description::dateCreated::taskTitles(the column title)::tasks(the task inside the column)
     */

    public static Date GetEarliestOrLatestDate(String projectName, Context context, boolean getEarliest) {
        String data = GetData(projectName, context);

        //setting the time like its 00:00 so it does not cause problems cuz other dates are stored
        // in that format and if this one is 00:01 it might screw up the calculation and say its
        // the next day or vice versa
        Calendar calendarEarliestDate = GregorianCalendar.getInstance();
        calendarEarliestDate.set(Calendar.MILLISECOND, 0);
        calendarEarliestDate.set(Calendar.SECOND, 0);
        calendarEarliestDate.set(Calendar.MINUTE, 0);
        calendarEarliestDate.set(Calendar.HOUR_OF_DAY, 0);

        Date returnDate = GregorianCalendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat(context.getString(R.string.storageDateFormat));

        if (data == null) {
            Log.wtf("Note", "there arent any epics in data");
            return returnDate;
        }

        String[] parts = data.split("::");
        for (int i = 0; i < parts.length / amountOfPartsInData; i++) {
            String earliestDateStr = parts[1 + (i * amountOfPartsInData)];
            String latestDateStr = parts[2 + (i * amountOfPartsInData)];
            try {
                Date curDataDate;
                long timeDifference;//value is milliseconds
                if (getEarliest) {
                    curDataDate = df.parse(earliestDateStr);
                    timeDifference = returnDate.getTime() - curDataDate.getTime();
                } else {
                    curDataDate = df.parse(latestDateStr);
                    timeDifference = curDataDate.getTime() - returnDate.getTime();
                }
                if (timeDifference <= 0)
                    continue;
                returnDate = curDataDate;

            } catch (ParseException e) {
                Log.wtf("ERROR", "there is problem with getting the epic data");
                e.printStackTrace();
            }
        }
        return returnDate;
    }

    public static String GetSpecificEpicData(String projectName, int id, int fieldId, Context context){
        String data = GetData(projectName, context);
        String[] parts = data.split(separator);

        return parts[(id * amountOfPartsInData) + fieldId];
    }

    public static String GetSpecificExtrasEpicData(String projectName, int id, int fieldId, Context context) {
        String data = GetData(projectName, context);
        String[] parts = data.split(separator);

        String extrasStr = parts[(id * amountOfPartsInData) + amountOfPartsInData - 1];
        String[] extras = extrasStr.split(extrasSeparator); //splitting the extras

        return extras[fieldId];
    }


    public static void SaveNewEpic(String projectName, String title, String description,
                                   String startDate, String endDate, Context context){
        BufferedWriter writer = null;

        File f = new File(context.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "Roadmap", projectName + ".txt");

        if (title.isEmpty()){
            title = "Example";
        }

        Calendar calendarDate = GregorianCalendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(context.getString(R.string.storageDateFormat));

        if (endDate.isEmpty() && startDate.isEmpty()){
            startDate = df.format(calendarDate.getTime());

            calendarDate.add(Calendar.WEEK_OF_YEAR, 2);
            endDate = df.format(calendarDate.getTime());
        } else if (endDate.isEmpty()) {
            try {
                Date date = df.parse(startDate);
                calendarDate.setTime(date);
                calendarDate.add(Calendar.WEEK_OF_YEAR, 2);
                endDate = df.format(calendarDate.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (startDate.isEmpty()){
            startDate = df.format(calendarDate.getTime());
        }

        String extras = description + extrasSeparator + "" + extrasSeparator + "[]" + extrasSeparator + "[]";
        String data = title + separator + startDate + separator + endDate + separator + extras + separator;

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

    public static void EditEpic(String projectName, int id, int fieldId, String newData, Context context){
        BufferedWriter writer = null;
        String data = "";
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
                + File.separator + "Roadmap", projectName + ".txt");

        String[] parts = dataOld.split(separator); //splitting the data

        parts[(id * amountOfPartsInData) + fieldId] = newData;

        for (int i = 0; i < parts.length; i++)
            data += (parts[i] + separator);

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

    public static void EditEpicExtras(String projectName, int id, int fieldId, String newData, Context context){
        BufferedWriter writer = null;
        String data = "";
        String extrasData = "";
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
                + File.separator + "Roadmap", projectName + ".txt");

        String[] parts = dataOld.split(separator); //splitting the data
        String extrasStr = parts[(id * amountOfPartsInData) + amountOfPartsInData - 1];
        String[] extras = extrasStr.split(extrasSeparator); //splitting the extras

        extras[fieldId] = newData;

        for (int i = 0; i < extras.length; i++){
            extrasData += (extras[i]);
            if (i != extras.length - 1)
                extrasData += extrasSeparator;
        }

        parts[(id * amountOfPartsInData) + amountOfPartsInData - 1] = extrasData;

        for (int i = 0; i < parts.length; i++)
            data += (parts[i] + separator);

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

    public static void RemoveFile(String projectName, Context context){
        File file = new File(context.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "Roadmap", projectName + ".txt");

        boolean deleted = file.delete();

        if (!deleted){
            Log.wtf("ERROR", "Something went wrong with removing the file with name "
                    + projectName + " at " + context.getFilesDir().toString() +
                    File.separator + "ProjectData" + File.separator + "ProjectBoard");
        }
    }

    public static void RemoveEpic(String projectName, int id, Context context){
    BufferedWriter writer = null;
    File f = new File(context.getFilesDir() + File.separator + "ProjectData"
            + File.separator + "Roadmap", projectName + ".txt");

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

    public static String GetData(String projectName, Context context){
        String data = null;

        File f = new File(context.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "Roadmap", projectName + ".txt");
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
        File folder = new File(context.getFilesDir() + File.separator + "ProjectData" + File.separator + "Roadmap");

        if (!folder.exists()) {
            folder.mkdirs();
        }
    }


}
