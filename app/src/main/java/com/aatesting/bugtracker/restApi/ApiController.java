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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class ApiController {
    public static Context context;
    public static int userId;
    public static RequestQueue requestQueue;
    /**
     *
     * @param userId needs to be replaced with projectId
     * @param context the context is stored as a static function after so it doesn't have to be passed for other methods
     * @param url the last part of the url where the request is sent "xxx.xxx.xxx:xxxx/{url}
     * @param fragment if fragment is specified setupData response will be sent to the specified fragment
     */

    public static void getAllFields(int userId, @NotNull Context context, @NotNull String url, ModifiedFragment fragment) {
        ApiController.userId = userId;

        if (requestQueue == null || context != ApiController.context) {
            ApiController.context = context;
            requestQueue = Volley.newRequestQueue(context);
        }

        String URL = AppSettings.SERVERIP + "/" + url + "/all/" + userId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                response -> {
                    ApiSingleton.getInstance().reset();
                    dataToSingleton(response, url);

                    fragment.onResponse(fragment.getString(R.string.setupData));
                },
                error -> fragment.onResponse("Error")
        );
        requestQueue.add(jsonArrayRequest);
    }

    /**
     * @param fragment if fragment is specified getData response will be sent to the specified fragment
     * @param url the last part of the url where the request is sent "xxx.xxx.xxx:xxxx/{url}
     * @param activity if activity is passed it will be closed upon creation of the field
     */

    public static void createField(ApiJSONObject object, @NotNull String url, ModifiedFragment fragment, Activity activity) {
        String URL = AppSettings.SERVERIP + "/" + url;

        JSONObject jsonObject = objectToJSON(object, context);

        if (requestQueue == null){
            Message.message(context, "Something went wrong");
            Log.wtf("ERROR", "request queue is null which that you called some other field before calling to get the data from the server");
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                jsonObject,
                response -> {
                    if (activity != null)
                        activity.finish();
                    if (fragment != null) {
                        fragment.onResponse(fragment.getString(R.string.getData));
                    }
                },
                error -> {
                    GlobalValues.objectModified = null;
                    fragment.onResponse("Error");
                }
        );
        requestQueue.add(jsonObjectRequest);
    }


    /**
     * @apiNote modifies specified API object if GlobalValues.objectModified isn't null, if it only sends put request on the selected url
     * @param fragment if fragment is specified getData response will be sent to the specified fragment
     * @param url the last part of the url where the request is sent "xxx.xxx.xxx:xxxx/{url}
     */

    public static void editField(ModifiedFragment fragment, Activity activity, @NotNull String url) {
        String URL = AppSettings.SERVERIP + "/" + url;

        JSONObject jsonObject = null;

        if (GlobalValues.objectModified != null)
            jsonObject = objectToJSON(GlobalValues.objectModified, context);

        if (requestQueue == null){
            Message.message(context, "Something went wrong");
            Log.wtf("ERROR", "request queue is null which that you called some other field before calling to get the data from the server");
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                URL,
                jsonObject,
                response -> {
                    GlobalValues.objectModified = null;
                    if (activity != null)
                        activity.finish();
                    if (fragment != null)
                        fragment.onResponse(fragment.getString(R.string.getData));
                },
                error -> {
                    GlobalValues.objectModified = null;
                    if (activity != null)
                        activity.finish();
                    if (fragment != null)
                        fragment.onResponse("Error");
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    /**
     * @param fragment if fragment is specified getData response will be sent to the specified fragment
     * @param url the last part of the url where the request is sent "xxx.xxx.xxx:xxxx/{url}
     * @param activity if activity is passed it will be closed upon editing of the field
     */
    public static void removeField(Activity activity, ModifiedFragment fragment, @NotNull String url) {
        String URL = AppSettings.SERVERIP + "/" + url;

        if (requestQueue == null){
            Message.message(context, "Something went wrong");
            Log.wtf("ERROR", "request queue is null which that you called some other field before calling to get the data from the server");
        }

        StringRequest request = new StringRequest(
                Request.Method.DELETE,
                URL,
                response -> {
                    if (!response.equals("ok"))
                    {
                        Message.message(context, "Something went wrong");
                        Log.wtf("ERROR", "the response is " + response);
                    }
                    GlobalValues.objectModified = null;
                    if (activity != null)
                        activity.finish();
                    if (fragment != null) {
                        fragment.onResponse(fragment.getString(R.string.getData));
                    }
                },
                error -> {
                    GlobalValues.objectModified = null;

                    if (fragment != null) {
                        fragment.onResponse("Error");
                    }
                    if (activity != null) {
                        activity.finish();
                    }
                    if (fragment == null) {
                        Message.message(context, "Something went wrong");
                        Log.wtf("Error", "something went wrong with removing field server sided");
                    }
                }
        );

        requestQueue.add(request);
    }

    private static void dataToSingleton(JSONArray data, String value) {
        try {
            for (int i = 0; i < data.length(); i++) {
                singletonConstructor(data, value, i);
            }
        } catch (JSONException e){
            Message.defErrMessage(context);
            Log.wtf("ERROR", "Failed to convert JSONArray data to " + data + " and put it to singleton class");
            e.printStackTrace();
        }
        //reorder by position field if it exists in the current singleton

        switch (value){
            case "roadmaps":
            case "boards":
                ApiSingleton.getInstance().reorderByPosition(false);
                break;
            case "tasks":
                ApiSingleton.getInstance().reorderByPosition(true);
                break;
            default:
                Log.wtf("\nWARNING","the reordering order for the value " + value +
                        " is not defined, will reorder items the default way\n" );
                ApiSingleton.getInstance().reorderByPosition(false);
                break;
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
        int position = checkIfIntNull("position", object, context);
        int userId = checkIfIntNull("userId", object, context);
        String title = checkIfStrNull("title", object, context);
        ArrayList<ApiJSONObject> tasks = checkIfListNull("tasks", object, context);

        ApiSingleton.getInstance().addToArray(new ApiJSONObject(
                id,
                position,
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
                int position = checkIfIntNull("position", jsonObject, context);
                String title = checkIfStrNull("title", jsonObject, context);
                String description = checkIfStrNull("description", jsonObject, context);
                String dateCreated = ApiController.checkIfStrNull("dateCreated", jsonObject, context);

                object = new ApiJSONObject(
                        id,
                        position,
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
                    returnList.add(singletonConstructor(jsonArray, getVal, i));
                }
            }
            else
                return null;
        } catch (JSONException e) {
            Message.message(context, "something went wrong");
            Log.wtf("ERROR", "the field " + getVal + " does not exist in the object " + object.toString());
            e.printStackTrace();
        }

        //reorder field because for some reason the server decides to send stuff out of order and android as well
        returnList.sort(Comparator.comparing(ApiJSONObject::getPosition).reversed());

        return returnList;
    }
}