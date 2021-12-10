package com.aatesting.bugtracker.restApi;

import org.json.JSONObject;

public class RoadmapObject extends JSONObject {
    private int field_id; //json field id, for the server
    private int userId;
    private String title;
    private String description;
    private String startDate;
    private String dueDate;
    private String dateCreated;

    public RoadmapObject(int field_id, int userId, String title, String description, String startDate,
                         String dueDate, String dateCreated) {
        this.field_id = field_id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.dateCreated = dateCreated;
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

    public int getField_id() {
        return field_id;
    }

    public void setField_id(int field_id) {
        this.field_id = field_id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

