package com.example.bugtracker.adapters;

public class ProjectsRecyclerData {

    private String title;
    private String description;
    private int imgId;
    private Boolean favorite = false;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public Boolean getFavorite()
    {
        return favorite;
    }

    public void setFavorite(Boolean favorite){
        this.favorite = favorite;
    }

    public ProjectsRecyclerData(String title, String description, int imgId) {
        this.title = title;
        this.imgId = imgId;
        this.description = description;
    }
}
