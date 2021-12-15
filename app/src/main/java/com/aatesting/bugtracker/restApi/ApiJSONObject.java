package com.aatesting.bugtracker.restApi;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ApiJSONObject extends JSONObject {
    //for some reason the server sends field_id which should be impossible so here we are
    private int id;

    private int userId;
    private String title;
    private String description;
    private String startDate;
    private String dueDate;
    private String dateCreated;
    private ArrayList<ApiJSONObject> tasks;

    //for board object
    public ApiJSONObject(int id, int userId, String title, ArrayList<ApiJSONObject> tasks) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        if (tasks == null)
            this.tasks = new ArrayList<>();
        else
            this.tasks = tasks;
    }

    //for roadmap object
    public ApiJSONObject(int id, int userId, String title, String description, String startDate,
                         String dueDate, String dateCreated) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.dateCreated = dateCreated;
    }

    //for board/tasks
    public ApiJSONObject(int id, String title, String description, String dateCreated){
        this.id = id;
        this.title = title;
        this.description = description;
        this.dateCreated = dateCreated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public ArrayList<ApiJSONObject> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<ApiJSONObject> tasks) {
        this.tasks = tasks;
    }
}
