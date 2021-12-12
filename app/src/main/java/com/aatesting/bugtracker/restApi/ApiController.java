package com.aatesting.bugtracker.restApi;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.aatesting.bugtracker.AppSettings;
import com.aatesting.bugtracker.GlobalValues;
import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.modifiedClasses.ModifiedFragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiController {
    public static Context context;
    public static int userId;

    public static void getAllFields(int userId, Context context, String url, ModifiedFragment fragment){
        ApiController.context = context;
        ApiController.userId = userId;

        RequestQueue requestQueue = Volley.newRequestQueue(context); // this might be in different context so setting the value before hand might be a bad idea

        String URL = AppSettings.SERVERIP + "/" + url + "/all/" + userId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ApiSingleton.getInstance().reset();
                        dataToSingleton(response, url);

                        fragment.onResponse(fragment.getString(R.string.setupData));
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        fragment.onResponse("Error");
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    /**
     * @param activity if activity is passed it will be closed upon creation of the field
     */

    public static void createField(ApiJSONObject object, String url, ModifiedFragment fragment, Activity activity){
        String URL = AppSettings.SERVERIP + "/" + url;

        JSONObject jsonObject = objectToJSON(object, context);
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Message.message(context, "data successfully saved");
                        if (activity != null)
                            activity.finish();
                        if (fragment != null){
                            fragment.onResponse(fragment.getString(R.string.getData));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        GlobalValues.fieldModified = -1;
                        fragment.onResponse("Error");
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    public static void editField(ModifiedFragment fragment, String url){
        String URL = AppSettings.SERVERIP + "/" + url;

        ApiJSONObject object = ApiSingleton.getInstance().getObject(GlobalValues.fieldModified);
        JSONObject jsonObject = objectToJSON(object, context);

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
                        fragment.onResponse(fragment.getString(R.string.getData));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        GlobalValues.fieldModified = -1;
                        fragment.onResponse("Error");
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    public static void removeField(int fieldId, Activity activity, ModifiedFragment fragment, String url){
        String URL = AppSettings.SERVERIP + "/" + url + "/" + ApiSingleton.getInstance().getObject(fieldId).getId();

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
                        if (activity != null)
                            activity.finish();
                        if (fragment != null){
                            fragment.onResponse(fragment.getString(R.string.getData));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.toString().equals("false"))
                            return;
                        GlobalValues.fieldModified = -1;

                        if (fragment != null)
                        {
                            fragment.onResponse("Error");
                        }
                        if (activity != null) {
                            activity.finish();
                            if (fragment == null){
                                Message.message(context, "Something went wrong");
                                Log.wtf("Error", "something went wrong with removing field server sided");
                            }
                        }
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }


    private static void dataToSingleton(JSONArray response, String value){

        for (int i = 0; i < response.length(); i++) {
            try {
                addValueToSingleton(response, value, i);
            } catch (JSONException e) {
                Log.wtf("ERROR", "Failed to convert JSONArray to List<JSONObject>");
                e.printStackTrace();
            }
        }
    }

    /**
     * adds each object to singleton instance
     * @param value is for differenciating which singleton to use
     */
    private static void addValueToSingleton(JSONArray response, String value, int i) throws JSONException{
        switch (value) {
            case "boards":
                JSONObject object = response.getJSONObject(i);

                int id = ApiController.checkIfIntNull("id", object, context);
                int userId = ApiController.checkIfIntNull("userId", object, context);
                String title = ApiController.checkIfStrNull("title", object, context);

                ApiSingleton.getInstance().addToArray(new ApiJSONObject(
                        id,
                        userId,
                        title
                ));
                break;
            case "roadmaps":
                object = response.getJSONObject(i);

                userId = ApiController.checkIfIntNull("userId", object, context);
                id = ApiController.checkIfIntNull("id", object, context);
                title = ApiController.checkIfStrNull("title", object, context);
                String description = ApiController.checkIfStrNull("description", object, context);
                String startDate = ApiController.checkIfStrNull("startDate", object, context);
                String dueDate = ApiController.checkIfStrNull("dueDate", object, context);
                String dateCreated = ApiController.checkIfStrNull("dateCreated", object, context);

                ApiSingleton.getInstance().addToArray(new ApiJSONObject(
                        id,
                        userId,
                        title,
                        description,
                        startDate,
                        dueDate,
                        dateCreated
                ));
                break;
            default:
                Message.message(context, "Something went wrong");
                Log.wtf("ERROR", "current url isn't defined for adding to singleton");
                break;
        }
    }

    private static JSONObject objectToJSON(ApiJSONObject object, Context context){
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

    private static String checkIfStrNull(String getVal, JSONObject object, Context context){
        try {
            if (!object.get(getVal).toString().equals("null"))
                return object.getString(getVal);
            else
                return null;
        } catch (JSONException e) {
            Message.message(context, "something went wrong");
            Log.wtf("ERROR", "the field " + getVal + " does not exist in the object " + object.toString());
            e.printStackTrace();
            return null;
        }
    }

    private static int checkIfIntNull(String getVal, JSONObject object, Context context){
        try{
            if (!object.get(getVal).toString().equals("null"))
                return Integer.parseInt(object.get(getVal).toString());
            else
                return -1;
        } catch (NumberFormatException | JSONException e) {
            Message.message(context, "something went wrong");
            Log.wtf("ERROR", "the field " + getVal + " does not exist in the object " + object.toString());
            e.printStackTrace();
            return -1;
        }
    }
}
