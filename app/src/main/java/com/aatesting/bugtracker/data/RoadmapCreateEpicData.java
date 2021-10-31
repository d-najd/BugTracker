package com.aatesting.bugtracker.data;

import android.content.Context;
import android.util.Log;

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

public class RoadmapCreateEpicData {

    public static final int amountOfPartsInData = 4;
    private static final String separator = "::"; //the type of separator used for saving the data
    private static final String extrasSeparator = "$$";

    public static void SaveNewEpic(String projectName, String title, String description,
                                   String startDate, String endDate, Context context){
        BufferedWriter writer = null;

        File f = new File(context.getFilesDir() + File.separator + "ProjectData"
                + File.separator + "Roadmap", projectName + ".txt");

        if (title.isEmpty()){
            title = "Example";
        }

        Calendar calendarDate = GregorianCalendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd'-'MM'-'yyyy");

        if (endDate.isEmpty() && startDate.isEmpty()){
            startDate = df.format(calendarDate.getTime());

            calendarDate.add(Calendar.WEEK_OF_YEAR, 1);
            endDate = df.format(calendarDate.getTime());
        } else if (endDate.isEmpty()) {
            try {
                Date date = df.parse(startDate);
                calendarDate.setTime(date);
                calendarDate.add(Calendar.WEEK_OF_YEAR, 1);
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
        //TODO add function which checks if permissions for writing data are allowed

        File folder = new File(context.getFilesDir() + File.separator + "ProjectData" + File.separator + "Roadmap");

        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}
