package com.example.bugtracker.recyclerview;

public class RecyclerData {

    private String title;
    private String description;
    private int imgId;
    private int layout; // which recyclerview we are using
    private Boolean favorite = false;
    private Boolean editTextEnable = false;

    public RecyclerData(String title, String description, int imgId, boolean editTextEnable, int layout){
        this.title = title;
        this.imgId = imgId;
        this.description = description;
        this.editTextEnable = editTextEnable;
        this.layout = layout;
    }

    public RecyclerData(String title, String description, int imgId, int layout) {
        this.title = title;
        this.imgId = imgId;
        this.description = description;
        this.layout = layout;
    }

    public RecyclerData(String title, int imgId, int layout){
        this.title = title;
        this.imgId = imgId;
        this.layout = layout;
    }

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

    public Boolean getEditTextEnable()
    {
        return editTextEnable;
    }

    public int getLayout(){ return layout;}
}
