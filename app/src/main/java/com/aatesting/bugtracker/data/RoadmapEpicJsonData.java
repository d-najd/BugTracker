package com.aatesting.bugtracker.data;

import android.util.Log;

import com.aatesting.bugtracker.AppSettings;
import com.aatesting.bugtracker.GlobalValues;
import com.aatesting.bugtracker.restApi.ApiJSONObject;
import com.aatesting.bugtracker.restApi.ApiSingleton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;



public class RoadmapEpicJsonData {
    /**
     * @return seems to return date which is +1:00h from greenwich so take that into consideration
     */
    private static ApiSingleton apiSingleton = ApiSingleton.getInstance();
    private static final SimpleDateFormat df = new SimpleDateFormat(AppSettings.SERVER_DATE_FORMAT);

    public static Date getEarliestOrLatestDate(boolean getEarliest){
        List<Date> dates = new ArrayList<>(); //for converting JSONObject date to java dates

        if (getEarliest) {
            for (ApiJSONObject apiJsonObject : apiSingleton.getArray(GlobalValues.ROADMAPS_URL)) {
                try {
                    dates.add(df.parse(apiJsonObject.getStartDate()));
                } catch (ParseException e) {
                    Log.wtf("ERROR", "something went wrong with parsing date");
                    e.printStackTrace();
                }
            }
            if (!dates.isEmpty())
                return Collections.min(dates);
            else //return current date
                return GregorianCalendar.getInstance().getTime();

        } else
        {
            for (ApiJSONObject apiJsonObject : apiSingleton.getArray(GlobalValues.ROADMAPS_URL))
                try {
                    dates.add(df.parse(apiJsonObject.getDueDate()));
                }catch (ParseException e) {
                    Log.wtf("ERROR","something went wrong with parsing date");
                    e.printStackTrace();
                }
            if (!dates.isEmpty())
                return Collections.max(dates);
            else //return current date
                return GregorianCalendar.getInstance().getTime();
        }
        /*
        if (getEarliest) {
            for (JSONObject jsonObject : data) {
                try {
                    String dateStr = jsonObject.get("startDate").toString();
                    dates.add(df.parse(dateStr));
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
            Date earliest = Collections.min(dates);
            return earliest;

        } else {
            for (JSONObject jsonObject : data) {
                try {
                    String dateStr = jsonObject.get("dueDate").toString();
                    dates.add(df.parse(dateStr));
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
            Date latest = Collections.max(dates);
            return latest;
        }

         */
    }




    /*
        List<JSONObject> oldRes = data;


        JSONObject temp = oldRes.get(2);
        oldRes.set(2, oldRes.get(1));
        oldRes.set(1, temp);
        Log.wtf("old", oldRes.toString());

        Collections.sort(data, new Comparator<JSONObject>() {

            @Override
            public int compare(JSONObject lhs, JSONObject rhs) {
                Integer lid = 0;
                Integer rid = 0;
                try {
                    lid = lhs.getInt("field_id");
                    rid = rhs.getInt("field_id");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return lid.compareTo(rid);
            }

        });

        Log.wtf("new", oldRes.toString());

         */

}
