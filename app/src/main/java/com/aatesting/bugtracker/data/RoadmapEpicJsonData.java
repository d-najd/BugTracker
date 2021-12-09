package com.aatesting.bugtracker.data;

import android.util.Log;

import com.aatesting.bugtracker.AppSettings;
import com.aatesting.bugtracker.restApi.RoadmapObject;
import com.aatesting.bugtracker.restApi.RoadmapsSingleton;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;



public class RoadmapEpicJsonData {
    /**
     * @return seems to return date which is +1:00h from greenwich so take that into consideration
     */
    public static Date GetEarliestOrLatestDate (boolean getEarliest){
        List<Date> dates = new ArrayList<>(); //for converting JSONObject date to java date
        RoadmapsSingleton roadmapSingleton = RoadmapsSingleton.getInstance();

        if (getEarliest) {
            for (RoadmapObject roadmapObject : roadmapSingleton.getArray())
                dates.add(roadmapObject.getStartDate());
            return Collections.min(dates);

        } else
        {
            for (RoadmapObject roadmapObject : roadmapSingleton.getArray())
                dates.add(roadmapObject.getDueDate());
            return Collections.max(dates);
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
