package com.aatesting.bugtracker.restApi;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.aatesting.bugtracker.AppSettings;
import com.aatesting.bugtracker.GlobalValues;
import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.data.RoadmapEpicJsonData;
import com.aatesting.bugtracker.fragments.RoadmapFragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RoadmapApi {
    private static Context context;
    private static final SimpleDateFormat df = new SimpleDateFormat(AppSettings.SERVER_DATE_FORMAT);
    public static int userId;

    public static void setupRoadmaps(int userId, Context context, RoadmapFragment fragment){
        RoadmapApi.context = context;
        RoadmapApi.userId = userId;

        RequestQueue requestQueue = Volley.newRequestQueue(context); // this might be in different context so setting the value before hand might be a bad idea

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

    public static void createRoadmap(RoadmapObject object){
        String URL = AppSettings.SERVERIP + "/roadmaps/" + userId;

        JSONObject jsonObject = RoadmapEpicJsonData.objectToJSON(object, context);
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Message.message(context, "data successfully saved");
                        GlobalValues.fieldModified = -1;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Message.message(context, "Something went wrong");
                        Log.wtf("ERROR", "something went wrong with creating server roadmap" + error.toString());
                        GlobalValues.fieldModified = -1;
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    public static void editRoadmap(RoadmapFragment fragment){
        //TODO fieldmodified isnt going to work in this case, get the object with singelton and then get its values and pass some of them in the url
        String URL = AppSettings.SERVERIP + "/roadmaps/" + userId + "/" + GlobalValues.fieldModified;

        RoadmapObject object = RoadmapsSingleton.getInstance().getObject(GlobalValues.fieldModified);
        JSONObject jsonObject = RoadmapEpicJsonData.objectToJSON(object, context);

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                URL,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Message.message(context, "data successfully saved");
                        GlobalValues.fieldModified = -1;
                        fragment.updateData();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Message.message(context, "Something went wrong");
                        Log.wtf("ERROR", "something went wrong with updating the server data " + error.toString());
                        GlobalValues.fieldModified = -1;
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    public static void removeRoadmap(int fieldId, Activity activity){
        String URL = AppSettings.SERVERIP + "/roadmaps/" + userId + "/" + RoadmapsSingleton.getInstance().getObject(fieldId).getField_id();

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Message.message(context, "data successfully removed");
                        GlobalValues.fieldModified = -1;
                        activity.finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.toString().equals("false"))
                            return;
                        Message.message(context, "Something went wrong");
                        Log.wtf("ERROR", "something went wrong with updating the server data " + error.toString());
                        GlobalValues.fieldModified = -1;
                        activity.finish();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    //putting the data to singleton class which is located in RoadmapObject-RoadmapsSingleton
    private static void dataToSingleton(JSONArray response){
        RoadmapsSingleton roadmapSingleton = RoadmapsSingleton.getInstance();

        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject object = response.getJSONObject(i);

                //somebody decided it would be funny to return "null" instead of null and now I have to do this

                int userId = checkIfIntNull("userId", object);
                int field_id = checkIfIntNull("field_id", object);
                String title = checkIfStrNull("title", object);
                String description = checkIfStrNull("description", object);
                String startDate = checkIfStrNull("startDate", object);
                String dueDate = checkIfStrNull("dueDate", object);
                String dateCreated = checkIfStrNull("dateCreated", object);

                roadmapSingleton.addToArray(new RoadmapObject(
                        field_id,
                        userId,
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
            if (!object.get(getVal).toString().equals("null"))
                return object.getString(getVal);
            else
                return null;
        } catch (JSONException e) {
            Message.message(context, "something went wrong");
            Log.wtf("ERROR", "the field " + getVal + " in roadmap object does not exist");
            e.printStackTrace();
            return null;
        }
    }

    private static int checkIfIntNull(String getVal, JSONObject object){
        try{
            if (!object.get(getVal).toString().equals("null"))
                return Integer.parseInt(object.get(getVal).toString());
            else
                return -1;
        } catch (NumberFormatException | JSONException e) {
            Message.message(context, "something went wrong");
            Log.wtf("ERROR", "the field " + getVal + " in roadmap object does not exist");
            e.printStackTrace();
            return -1;
        }
    }
}


