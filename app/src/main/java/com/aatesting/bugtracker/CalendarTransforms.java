package com.aatesting.bugtracker;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import kotlin.Triple;

public class CalendarTransforms {
    //NOTE FOR ALL THE DATA IN HERE MAKE SURE TO USE FORMAT "dd'-'MM'-'yyyy" IN OTHER WORDS
    // STORAGEDATEFORMAT
    public static Date StringToDate(String dateStr, Context mcontext){
        Date date = null;
        SimpleDateFormat fm = new SimpleDateFormat(mcontext.getString(R.string.storageDateFormat));
        try {
            date = fm.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.wtf("ERROR", "something went wrong with parsing the dateStr to date");
        }

        return date;
    }

    public static long getMillis(String dateStr, Context mcontext){
        try {
            String parts[] = dateStr.split("-");

            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]) - 1; //doing this is such a bad idea
            int year = Integer.parseInt(parts[2]);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);

            long milliTime = calendar.getTimeInMillis();

            return milliTime;
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public static Triple<Integer, Integer, Integer> getAllCalendarData(String dateStr, Context mcontext){
        String parts[] = dateStr.split("-");

        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]) - 1; //doing this is such a bad idea
        int year = Integer.parseInt(parts[2]);

        return new Triple<>(day, month, year);
    }
}
