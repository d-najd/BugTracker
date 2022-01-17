package com.aatesting.bugtracker.restApi;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;

public class ApiJSONObject extends JSONObject {
    private int id;
    private int position = -1;
    private int userId = -1;
    private int projectId;
    private String title;
    private String description;
    private String startDate;
    private String dueDate;
    private String dateCreated;
    private String username;
    private String password;
    private ArrayList<ApiJSONObject> tasks;
    private ApiJSONObject rolesIdentity;

    private Boolean manageProject;
    private Boolean manageUsers;
    private Boolean create;
    private Boolean edit;
    private Boolean delete;

    //for user
    public ApiJSONObject(String username, String password){
        this.username = username;
        this.password = password;
    }

    //for project
    public ApiJSONObject(int id, String title){
        this.id = id;
        this.title = title;
    }

    //for keeping track of viewed projects
    public ApiJSONObject(int id){
        this.id = id;
    }

    //for board object
    public ApiJSONObject(int id, int projectId, int position, String title, ArrayList<ApiJSONObject> tasks) {
        this.id = id;
        this.projectId = projectId;
        this.position = position;
        this.title = title;
        if (tasks == null)
            this.tasks = new ArrayList<>();
        else
            this.tasks = tasks;
    }

    //for roadmap object
    public ApiJSONObject(int id, int projectId, String title, String description, String startDate,
                         String dueDate, String dateCreated) {
        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.dateCreated = dateCreated;
    }

    //for roles
    public ApiJSONObject(ApiJSONObject rolesIdentity, Boolean manageProject, Boolean manageUsers, Boolean create, Boolean edit, Boolean delete) {
        this.rolesIdentity = rolesIdentity;
        this.manageProject = manageProject;
        this.manageUsers = manageUsers;
        this.create = create;
        this.edit = edit;
        this.delete = delete;
    }

    //for rolesIdentity
    public ApiJSONObject(String username, int projectId){
        this.username = username;
        this.projectId = projectId;
    }

    //for board/tasks
    public ApiJSONObject(int id, int position, String title, String description, String dateCreated){
        this.id = id;
        this.position = position;
        this.title = title;
        this.description = description;
        this.dateCreated = dateCreated;
    }

    public static Comparator<ApiJSONObject> positionComparator
            = new Comparator<ApiJSONObject>() {
        @Override
        public int compare(ApiJSONObject obj1, ApiJSONObject obj2) {

            Integer ob1pos = obj1.getPosition();
            Integer obj2pos = obj2.getPosition();

            //ascending order
            return ob1pos.compareTo(obj2pos);

            //descending order
            //return fruitName2.compareTo(fruitName1);
        }

    };

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ArrayList<ApiJSONObject> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<ApiJSONObject> tasks) {
        this.tasks = tasks;
    }

    public ApiJSONObject getTask(int position){
        return tasks.get(position);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

