package com.aatesting.bugtracker.restApi;

import java.util.ArrayList;
import java.util.Date;

public class RoadmapObject {
    private Integer fieldId;
    private String title;
    private String description;
    private Date startDate;
    private Date dueDate;
    private Date dateCreated;

    public RoadmapObject(Integer field_id, String title, String description, Date startDate,
                         Date dueDate, Date dateCreated) {
        super();
        this.fieldId = field_id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.dateCreated = dateCreated;
    }


    public Integer getField_id() {
        return fieldId;
    }

    public void setField_id(Integer field_id) {
        this.fieldId = field_id;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}

