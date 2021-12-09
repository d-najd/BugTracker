package com.aatesting.bugtracker.restApi;

import android.content.Context;
import android.util.Log;

import com.aatesting.bugtracker.AppSettings;
import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.fragments.RoadmapFragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RoadmapApi {
    private static Context rContext;
    private static SimpleDateFormat df = new SimpleDateFormat(AppSettings.SERVER_DATE_FORMAT);

    public static void setupRoadmaps(int userId, Context context, RoadmapFragment fragment){
        rContext = context;

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String URL = AppSettings.SERVERIP + "/roadmaps/all/" + userId;

        // Initialize a new JsonArrayRequest instance
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        RoadmapsSingleton.getInstance().reset();
                        dataToSingleton(response);

                        fragment.onResponse(1);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Message.message(context, "failed to get data from the server");
                        Log.wtf("ERROR", error.toString());
                        fragment.onResponse( 0);
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    //putting the data to singleton class which is located in RoadmapObject-RoadmapsSingleton
    private static void dataToSingleton(JSONArray response){
        RoadmapsSingleton roadmapSingleton = RoadmapsSingleton.getInstance();

        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject object = response.getJSONObject(i);

                //somebody decided it would be funny to return "null" instead of null and now I have to do this
                String title = checkIfStrNull("title", object);
                String description = checkIfStrNull("description", object);
                Date startDate = checkIfDateNull("startDate", object);
                Date dueDate = checkIfDateNull("dueDate", object);
                Date dateCreated = checkIfDateNull("dateCreated", object);

                roadmapSingleton.addToArray(new RoadmapObject(
                        i,
                        title,
                        description,
                        startDate,
                        dueDate,
                        dateCreated
                ));

            } catch (JSONException e) {
                Log.wtf("ERROR", "Failed to convert JSONArray to List<JSONObject>");
                e.printStackTrace();
            }
        }
    }

    private static String checkIfStrNull(String getVal, JSONObject object){
        try {
            if (object.get(getVal).toString().equals("null"))
                return null;
            else
                return object.getString(getVal);
        } catch (JSONException e) {
            Message.message(rContext, "something went wrong");
            Log.wtf("ERROR", "the field " + getVal + " in roadmap object does not exist");
            e.printStackTrace();
            return null;
        }
    }

    private static Date checkIfDateNull(String getVal, JSONObject object){
        try{
            if (object.get(getVal).toString().equals("null"))
                    return null;
            else
                return df.parse(object.get(getVal).toString());
        } catch (JSONException | ParseException e) {
            Message.message(rContext, "something went wrong");
            Log.wtf("ERROR", "the field " + getVal + " in roadmap object does not exist");
            e.printStackTrace();
            return null;
        }
    }


    // object.put("title", "helloooo");

    //String test = objectRequest.getBody().toString();

            /* for single object
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.wtf("data is: ", response.toString());
                        fragment.onResponse(response, 1);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Message.message(context, "failed to get data from the server");
                        Log.wtf("ERROR", error.toString());
                        fragment.onResponse(null, 0);
                    }
                }
        );

             */


}


