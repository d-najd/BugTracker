package com.aatesting.bugtracker.data;

import android.content.Context;
import android.util.Log;

import com.aatesting.bugtracker.AppSettings;
import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.restApi.RoadmapObject;
import com.aatesting.bugtracker.restApi.RoadmapsSingleton;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
    private static RoadmapsSingleton roadmapSingleton = RoadmapsSingleton.getInstance();
    private static final SimpleDateFormat df = new SimpleDateFormat(AppSettings.SERVER_DATE_FORMAT);

    public static Date getEarliestOrLatestDate(boolean getEarliest){
        List<Date> dates = new ArrayList<>(); //for converting JSONObject date to java dates

        if (getEarliest) {
            for (RoadmapObject roadmapObject : roadmapSingleton.getArray()) {
                ArrayList<RoadmapObject> list = roadmapSingleton.getArray();

                try {
                    dates.add(df.parse(roadmapObject.getStartDate()));
                } catch (ParseException e) {
                    Log.wtf("ERROR", "something went wrong with parsing date");
                    e.printStackTrace();
                }
            }
            return Collections.min(dates);

        } else
        {
            for (RoadmapObject roadmapObject : roadmapSingleton.getArray())
                try {
                    dates.add(df.parse(roadmapObject.getDueDate()));
                }catch (ParseException e) {
                    Log.wtf("ERROR","something went wrong with parsing date");
                    e.printStackTrace();
                }
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

    public static JSONObject objectToJSON(RoadmapObject object, Context context){
        JSONObject mJSONObject = null;
        String jsonInString = new Gson().toJson(object);
        try {
            mJSONObject = new JSONObject(jsonInString);
        } catch (JSONException e) {
            e.printStackTrace();
            Message.message(context, "Something went wrong");
            Log.wtf("ERROR", "error with converting java object to json object");
        }
        return mJSONObject;
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
