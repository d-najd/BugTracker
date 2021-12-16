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

import java.util.ArrayList;

public class ApiController {
    public static Context context;
    public static int userId;

    public static void getAllFields(int userId, Context context, String url, ModifiedFragment fragment) {
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
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        fragment.onResponse("Error");
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    /**
     * @param fragment if fragment is specified getData response will be sent to the specified fragment
     * @param url the last part of the url where the post request is created "xxx.xxx.xxx:xxxx/[url]
     * @param activity if activity is passed it will be closed upon creation of the field
     */

    public static void createField(ApiJSONObject object, String url, ModifiedFragment fragment, Activity activity) {
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
                        if (fragment != null) {
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

    public static void editField(ModifiedFragment fragment, String url) {
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

    /**
     * @param fragment if fragment is specified getData response will be sent to the specified fragment
     * @param url the last part of the url where the post request is created "xxx.xxx.xxx:xxxx/[url]
     * @param activity if activity is passed it will be closed upon creation of the field
     */
    public static void removeField(Activity activity, ModifiedFragment fragment, String url) {
        String URL = AppSettings.SERVERIP + "/" + url;

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
                        if (fragment != null) {
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

                        if (fragment != null) {
                            fragment.onResponse("Error");
                        }
                        if (activity != null) {
                            activity.finish();
                            if (fragment == null) {
                                Message.message(context, "Something went wrong");
                                Log.wtf("Error", "something went wrong with removing field server sided");
                            }
                        }
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }


    private static void dataToSingleton(JSONArray data, String value) {
        for (int i = 0; i < data.length(); i++) {
            try {
                singletonConstructor(data, value, i);
            } catch (JSONException e) {
                Log.wtf("ERROR", "Failed to convert JSONArray to List<JSONObject>");
                e.printStackTrace();
            }
        }
    }

    /**
     * every type of value is added here so everything is in 1 place
     * @param response the input array
     * @param type the name of the field
     * @param i the current iteration of the loop (for knowing which object inside the list to get
     * @return returns ApiJsonObject if specific case has been added if not returns null
     * @throws JSONException
     */
    private static ApiJSONObject singletonConstructor(JSONArray response, String type, int i) throws JSONException {
        switch (type) {
            case "boards":
                addSingletonBoards(response, i);
                break;
            case "roadmaps":
                addSingletonRoadmaps(response, i);
                break;
            case "tasks":
                return JSONToObject(response.getJSONObject(i), "tasks", context);
            default:
                Message.message(context, "Something went wrong");
                Log.wtf("ERROR", "current url isn't defined for adding to singleton");
                break;
        }
        return null;
    }

    private static void addSingletonRoadmaps(JSONArray response, int i) throws JSONException {
        JSONObject object = response.getJSONObject(i);

        int userId = ApiController.checkIfIntNull("userId", object, context);
        int id = ApiController.checkIfIntNull("id", object, context);
        String title = ApiController.checkIfStrNull("title", object, context);
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
    }

    private static void addSingletonBoards(JSONArray response, int i) throws JSONException {
        JSONObject object = response.getJSONObject(i);

        int id = checkIfIntNull("id", object, context);
        int userId = checkIfIntNull("userId", object, context);
        String title = checkIfStrNull("title", object, context);
        ArrayList<ApiJSONObject> tasks = checkIfListNull("tasks", object, context);

        ApiSingleton.getInstance().addToArray(new ApiJSONObject(
                id,
                userId,
                title,
                tasks
        ));
    }

    private static JSONObject objectToJSON(ApiJSONObject object, Context context) {
        JSONObject mJSONObject = null;
        String jsonInString = new Gson().toJson(object);
        try {
            mJSONObject = new JSONObject(jsonInString);
        } catch (JSONException e) {
            Message.message(context, "Something went wrong");
            Log.wtf("ERROR", "error with converting java object to json object");
            e.printStackTrace();
        }
        return mJSONObject;
    }

    private static ApiJSONObject JSONToObject(JSONObject jsonObject, String type, Context context){
        ApiJSONObject object = null;
        switch (type)
        {
            case "tasks":
                int id = checkIfIntNull("id", jsonObject, context);
                String title = checkIfStrNull("title", jsonObject, context);
                String description = checkIfStrNull("description", jsonObject, context);
                String dateCreated = ApiController.checkIfStrNull("dateCreated", jsonObject, context);

                object = new ApiJSONObject(
                        id,
                        title,
                        description,
                        dateCreated);
                break;
            default:
                Log.wtf("ERROR", "unable to convert the type " + type + " to java object because the case for that object does not exist" );
                Message.message(context, "Something went wrong");
        }
        return object;
    }

    private static String checkIfStrNull(String getVal, JSONObject object, Context context) {
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

    private static int checkIfIntNull(String getVal, JSONObject object, Context context) {
        try {
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

    private static ArrayList<ApiJSONObject> checkIfListNull(String getVal, JSONObject object, Context context) {
        JSONArray jsonArray;
        ArrayList<ApiJSONObject> returnList = new ArrayList<>();
        try {
            if (!object.get(getVal).toString().equals("null") && !object.get(getVal).toString().equals("[]")) {
                jsonArray = object.getJSONArray(getVal);
                for (int i = 0; i < jsonArray.length(); i++) {
                    returnList.add(singletonConstructor(jsonArray, "tasks", i));
                }
            }
            else
                return null;
        } catch (JSONException e) {
            Message.message(context, "something went wrong");
            Log.wtf("ERROR", "the field " + getVal + " does not exist in the object " + object.toString());
            e.printStackTrace();
        }
        return returnList;
    }
}
