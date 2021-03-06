package com.aatesting.bugtracker.restApi;

import android.app.Activity;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.aatesting.bugtracker.AppSettings;
import com.aatesting.bugtracker.GlobalValues;
import com.aatesting.bugtracker.Message;
import com.aatesting.bugtracker.R;
import com.aatesting.bugtracker.data.UserData;
import com.aatesting.bugtracker.modifiedClasses.ModifiedFragment;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ApiController {
    public static Context context;
    public static RequestQueue requestQueue;

    /**
     * gets and sets up the project data
     * @param includeProjectId include the current project id
     * @param includeAll include "/all" after the base url after includeProjectId has been checked
     * @param includeGetByUser includes "/getByUser" after include all has been checked
     * @param context the context is stored as a static function after so it doesn't have to be passed for other methods
     * @param url the last part of the url where the request is sent "xxx.xxx.xxx:xxxx/{url}
     * @param fragment if fragment is specified setupData response will be sent to the specified fragment
     */

    public static void getFields(Boolean includeProjectId, Boolean includeAll, Boolean includeGetByUser,
                                 @NotNull Context context, @NotNull String url, ModifiedFragment fragment) {
        if (requestQueue == null || context != ApiController.context) {
            ApiController.context = context;
            requestQueue = Volley.newRequestQueue(context);
        }

        String URL = AppSettings.SERVERIP + "/" + url;

        if (includeAll)
            URL += "/all";
        if (includeProjectId)
            URL += "/" + GlobalValues.projectOpened;
        if (includeGetByUser)
            URL += "/" + GlobalValues.GET_BY_USER;

        //java decided it wants final but this doesn't look like final to me?
        String finalURL = URL;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                response -> {
                    ApiSingleton.getInstance().reset(url); //removes any previous data
                    dataToSingleton(response, url); //works with the data somewhere

                    fragment.onResponse(fragment.getString(R.string.setupData));
                },
                error -> errorHandler(error, finalURL, url)

        ) {
            @Override
            public Map<String, String> getHeaders() {
                return setHeaders(false);
            }
        };
        requestQueue.add(jsonArrayRequest);
    }

    /**
     * used for getting a single json object, does not set up the project data
     * @param object if specified object will be sent along with the request
     * @param context the context is stored as a static function after so it doesn't have to be passed for other methods
     * @param url the last part of the url where the request is sent "xxx.xxx.xxx:xxxx/{url}
     * @param restUrl the rest of the url, like xxx.xxx.xxx/{url}/{restUrl}
     * @param fragment if fragment is specified setupData response will be sent to the specified fragment
     */

    public static void getField(ApiJSONObject object, boolean stringRequest, String restUrl,
                                 @NotNull Context context, @NotNull String url, ModifiedFragment fragment)

    {
        if (requestQueue == null || context != ApiController.context) {
            ApiController.context = context;
            requestQueue = Volley.newRequestQueue(context);
        }

        String URL = AppSettings.SERVERIP + "/" + url;
        if (restUrl != null)
            URL += restUrl;

        //java decided it wants final but this doesn't look like final to me?
        String finalURL = URL;
        if (stringRequest) {
            StringRequest jsonArrayRequest = new StringRequest(
                    Request.Method.GET,
                    finalURL,
                    response -> {
                        if (url.equals(GlobalValues.USERS_URL)) {
                            UserData.saveUser(context, object);
                            fragment.onResponse("gotUser");
                        }
                    },
                    error -> errorHandler(error, finalURL, url)
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    if (!url.equals(GlobalValues.USERS_URL))
                        return setHeaders(false);
                    else {
                        HashMap<String, String> params = new HashMap<>();
                        String creds = String.format("%s:%s", object.getUsername(), object.getPassword());
                        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.NO_WRAP);
                        params.put("Authorization", auth);
                        return params;
                    }
                }
            };
            requestQueue.add(jsonArrayRequest);
        } else {
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    URL,
                    null,
                    response -> {
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(response);

                        ApiSingleton.getInstance().reset(url); //removes any previous data
                        dataToSingleton(jsonArray, url); //works with the data somewhere

                        fragment.onResponse(fragment.getString(R.string.setupData));
                    },
                    error -> errorHandler(error, finalURL, url)) {
                @Override
                public Map<String, String> getHeaders() {
                    return setHeaders(true);
                }
            };
            requestQueue.add(jsObjRequest);
        }
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

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                URL,
                response -> {
                    if (activity != null)
                        activity.finish();
                    if (fragment != null) {
                        fragment.onResponse(fragment.getString(R.string.getData));
                    }
                },
                error -> errorHandler(error, URL, url)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return setHeaders(true);
            }

            @Override
            public byte[] getBody() {
                return jsonObject.toString().getBytes();
            }
        };
        requestQueue.add(stringRequest);
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

        JSONObject finalJsonObject = jsonObject;
        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT,
                URL,
                response -> {
                    GlobalValues.objectModified = null;
                    if (activity != null)
                        activity.finish();
                    if (fragment != null)
                        fragment.onResponse(fragment.getString(R.string.getData));
                },
                error -> errorHandler(error, URL, url)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return setHeaders(true);
            }

            @Override
            public byte[] getBody() {
                if (finalJsonObject == null)
                    return null;
                return finalJsonObject.toString().getBytes();
            }
        };
        requestQueue.add(stringRequest);
    }

    /**
     * @param object if not null will send the object along with the request
     * @param fragment if fragment is specified getData response will be sent to the specified fragment
     * @param url the last part of the url where the request is sent "xxx.xxx.xxx:xxxx/{url}
     * @param restUrl the rest of the url, like xxx.xxx.xxx/{url}/{restUrl}
     * @param activity if activity is passed it will be closed upon editing of the field
     */
    public static void removeField(ApiJSONObject object, Activity activity, ModifiedFragment fragment, @NotNull String url, String restUrl) {
        String URL = AppSettings.SERVERIP + "/" + url;

        if (requestQueue == null){
            Message.message(context, "Something went wrong");
            Log.wtf("ERROR", "request queue is null which that you called some other field before calling to get the data from the server");
        }

        if (restUrl != null)
            URL += restUrl;

        String finalURL = URL;
        StringRequest request = new StringRequest(
                Request.Method.DELETE,
                finalURL,
                response -> {
                    if (!response.equals("ok"))
                    {
                        Message.message(context, "Something went wrong");
                        Log.wtf("ERROR", "failed to remove field using url " + finalURL);
                    }
                    GlobalValues.objectModified = null;
                    if (activity != null)
                        activity.finish();
                    if (fragment != null) {
                        fragment.onResponse(fragment.getString(R.string.getData));
                    }
                },
                error -> errorHandler(error, finalURL, url)
        ) {
            @Override
            public Map<String, String> getHeaders() {
                if (object == null)
                    return setHeaders(false);
                else
                    return setHeaders(true);
            }

            @Override
            public byte[] getBody() {
                if (object != null)
                    return object.toString().getBytes();
                else
                    return getBody();
            }
        };

        requestQueue.add(request);
    }

    /**
     * @return the headers of the request like authentication
     */

    @NotNull
    private static HashMap<String, String> setHeaders(Boolean includeObject) {
        //for sending json data
        HashMap<String, String> params = new HashMap<>();
        if (includeObject)
            params.put("Content-Type", "application/json; charset=utf-8");

        //for authentication
        String userData = UserData.getLastUserRaw(context);

        if (userData == null) {
            Log.wtf("DEBUG", "getting server data without a user");
            return params;
        }

        //String creds = String.format("%s:%s",username,password);
        //String auth = "Basic " + Base64.encodeToString(userData, Base64.NO_WRAP);
        String auth = "Basic " + userData;
        params.put("Authorization", auth);

        return params;
    }

    /**
     * a default error handler for all requests
     * @param url the final url used for the request
     * @param type if there is a special error handling for a request, should always pass the type just in case
     */
    private static void errorHandler(VolleyError error, String url, String type) {
        try {
            switch (error.networkResponse.statusCode) {
                case 400:
                    Message.message(context, "Bad request");
                    Log.wtf("DEBUG", "Bad request error: " + new String(error.networkResponse.data));
                case 401:
                    Message.message(context, "Wrong credentials");
                    Log.wtf("DEBUG", "Entered wrong credentials, error: " + new String(error.networkResponse.data));
                    break;
                case 403:
                    Message.message(context, "Missing authorities for current action");
                    Log.wtf("DEBUG", "Missing authorities for request, error: " + new String(error.networkResponse.data));
                    break;
                case 418:
                    Message.message(context, "The server refuses to brew coffee because it is a teapot");
                    Log.wtf("DEBUG", "server says it is a teapot, error: " + new String(error.networkResponse.data));
                    break;
                case 500:
                    Message.message(context, "Internal server error");
                    Log.wtf("DEBUG", "Internal server error: " + new String(error.networkResponse.data));
                    break;
                case 503:
                    Message.message(context, "Service unavailable");
                    Log.wtf("DEBUG", "Service unavailable, error: " + new String(error.networkResponse.data));
                    break;
                default:
                    Message.defErrMessage(context);
                    Log.wtf("ERROR", "Unhandled server error code, error: " + new String(error.networkResponse.data));
                    error.printStackTrace();
                    break;
            }
        } catch (NullPointerException e){
            Message.message(context, "server seems to be offline");
            Log.wtf("WARNING", "server seems to be offline, failed to get data using url " + url);
        }
    }

    /**
     * works with the data, fields need to be changed here and in singletonConstructor
     * @param data the data array
     */
    private static void dataToSingleton(JSONArray data, String url) {
        try {
            for (int i = 0; i < data.length(); i++) {
                singletonConstructor(data, url, i);
            }
        } catch (JSONException e){
            Message.defErrMessage(context);
            Log.wtf("ERROR", "Failed to convert JSONArray data to " + data + " and put it to singleton class");
            e.printStackTrace();
        }
        //reorder by position field if it exists in the current singleton

        switch (url){
            case GlobalValues.ROADMAPS_URL:
            case GlobalValues.USERS_URL:
            case GlobalValues.BOARDS_URL:
            case GlobalValues.PROJECTS_URL:
            case GlobalValues.ROLES_URL:
                ApiSingleton.getInstance().reorderByPosition(false, url);
                break;
            case GlobalValues.TASKS_URL:
                ApiSingleton.getInstance().reorderByPosition(true, url);
                break;
            default:
                Log.wtf("\nWARNING","the reordering order for the value " + url +
                        " is not defined, will reorder items the default way\n" );
                ApiSingleton.getInstance().reorderByPosition(false, url);
                break;
        }
    }

    /**
     * every type of value is added here so everything is in mostly one place
     * @apiNote also a case needs to be added for each list in ApiSingleton.toArray as well
     * @param response the input array
     * @param type the name of the field
     * @param i the current iteration of the loop (for knowing which object inside the list to get
     * @return returns ApiJsonObject if specific case has been added if not returns null
     * @throws JSONException
     */
    private static ApiJSONObject singletonConstructor(JSONArray response, String type, int i) throws JSONException {
        switch (type) {
            case GlobalValues.USERS_URL:
                addSingletonUsers(response, i, type);
                break;
            case GlobalValues.ROLES_URL:
                addSingletonRoles(response, i, type);
                break;
            case GlobalValues.PROJECTS_URL:
                addSingletonProjects(response, i, type);
                break;
            case GlobalValues.BOARDS_URL:
                addSingletonBoards(response, i, type);
                break;
            case GlobalValues.ROADMAPS_URL:
                addSingletonRoadmaps(response, i, type);
                break;
            case GlobalValues.TASKS_URL:
                return JSONToObject(response.getJSONObject(i), GlobalValues.TASKS_URL, context);
            default:
                Message.message(context, "Something went wrong");
                Log.wtf("ERROR", "current url isn't defined for adding to singletonConstructor in ApiController");
                break;
        }

        return null;
    }

    private static void addSingletonUsers(JSONArray response, int i, String type) throws JSONException{
        JSONObject object = response.getJSONObject(i);
        String username = checkIfStrNull("username", object, context);

        ApiSingleton.getInstance().addToArray(new ApiJSONObject(
                username
        ), type);
    }

    private static void addSingletonRoles(JSONArray response, int i, String type) throws JSONException{
        JSONObject object = response.getJSONObject(i);

        Boolean manageProject = checkIfBooleanNull("manageProject", object, context);
        Boolean manageUsers = checkIfBooleanNull("manageUsers", object, context);
        Boolean create = checkIfBooleanNull("create", object, context);
        Boolean edit = checkIfBooleanNull("edit", object, context);
        Boolean delete = checkIfBooleanNull("delete", object, context);

        ApiJSONObject apiJSONObject = JSONToObject(object, type, context);
        ApiSingleton.getInstance().addToArray(new ApiJSONObject(
                apiJSONObject,
                manageProject,
                manageUsers,
                create,
                edit,
                delete
        ), type);
    }

    private static void addSingletonProjects(JSONArray response, int i, String type) throws JSONException{
        JSONObject object = response.getJSONObject(i);

        int id = ApiController.checkIfIntNull("id", object, context);
        String title = ApiController.checkIfStrNull("title", object, context);

        ApiSingleton.getInstance().addToArray(new ApiJSONObject(
                id,
                title
        ), type);
    }

    private static void addSingletonRoadmaps(JSONArray response, int i, String type) throws JSONException {
        JSONObject object = response.getJSONObject(i);

        int id = ApiController.checkIfIntNull("id", object, context);
        int projectId = ApiController.checkIfIntNull("projectId", object, context);
        String title = ApiController.checkIfStrNull("title", object, context);
        String description = ApiController.checkIfStrNull("description", object, context);
        String startDate = ApiController.checkIfStrNull("startDate", object, context);
        String dueDate = ApiController.checkIfStrNull("dueDate", object, context);
        String dateCreated = ApiController.checkIfStrNull("dateCreated", object, context);

        ApiSingleton.getInstance().addToArray(new ApiJSONObject(
                id,
                projectId,
                title,
                description,
                startDate,
                dueDate,
                dateCreated
        ), type);
    }

    private static void addSingletonBoards(JSONArray response, int i, String type) throws JSONException {
        JSONObject object = response.getJSONObject(i);

        int id = checkIfIntNull("id", object, context);
        int projectId = ApiController.checkIfIntNull("projectId", object, context);
        int position = checkIfIntNull("position", object, context);
        String title = checkIfStrNull("title", object, context);
        ArrayList<ApiJSONObject> tasks = checkIfListNull(GlobalValues.TASKS_URL, object, context);

        ApiSingleton.getInstance().addToArray(new ApiJSONObject(
                id,
                projectId,
                position,
                title,
                tasks
        ), type);
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

    private static ApiJSONObject JSONToObject(JSONObject jsonObject, String type, Context context) {
        ApiJSONObject object = null;
        switch (type)
        {
            case GlobalValues.TASKS_URL:
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
            case GlobalValues.ROLES_URL:
                JSONObject identity;
                try {
                    identity = (JSONObject) jsonObject.get("rolesIdentity");
                } catch (JSONException e){
                    Log.wtf("ERROR", "failed to get rolesIdentity while getting it from roles, located in JSONToObject in ApiController");
                    e.printStackTrace();
                    return null;
                }

                String username = ApiController.checkIfStrNull("username", identity, context);
                int projectId = ApiController.checkIfIntNull("projectId", identity, context);

                object = new ApiJSONObject(
                        username,
                        projectId);
                break;
            default:
                Log.wtf("ERROR", "unable to convert the type " + type + " to java object because the case for that object does not exist" );
                Message.message(context, "Something went wrong");
        }
        return object;
    }

    private static String checkIfStrNull(@NotNull String getVal, JSONObject object, Context context) {
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

    private static int checkIfIntNull(@NotNull String getVal, JSONObject object, Context context) {
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

    private static Boolean checkIfBooleanNull(@NotNull String getVal, JSONObject object, Context context) {
        try {
                return object.getBoolean(getVal);
        } catch (NumberFormatException | JSONException e) {
            Message.message(context, "something went wrong");
            Log.wtf("ERROR", "the field " + getVal + " does not exist in the object " + object.toString());
            e.printStackTrace();
            return null;
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