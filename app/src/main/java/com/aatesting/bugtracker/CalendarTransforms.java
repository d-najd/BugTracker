package com.aatesting.bugtracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
    @SuppressLint("SimpleDateFormat")
    public static long getMillis(String dateStr, Context mcontext){
        try {
            Calendar calendar = GregorianCalendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat(AppSettings.SERVER_DATE_FORMAT);

            calendar.setTime(df.parse(dateStr));

            return calendar.getTimeInMillis();
        } catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    public static Triple<Integer, Integer, Integer> getAllCalendarData(String dateStr, Context mcontext){
        Calendar calendar = GregorianCalendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(AppSettings.SERVER_DATE_FORMAT);

        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            Message.message(mcontext, "Something went wrong");
            Log.wtf("ERROR", "something went wrong with parsing date");
            e.printStackTrace();
        }

        calendar.setTime(date);
        calendar.get(Calendar.DAY_OF_MONTH);

        return new Triple<>(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
    }
}
