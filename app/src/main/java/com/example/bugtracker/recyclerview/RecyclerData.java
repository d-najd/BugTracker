package com.example.bugtracker.recyclerview;

public class RecyclerData {

    private String title;
    private String description;
    private String tag; // used for differentiating the recyclerViews
    private String id;
    private int imgId;
    private int secondImgId;
    private Boolean favorite = false;
    private Boolean editTextEnable = false;

    public RecyclerData(String title, String description, int imgId, boolean editTextEnable, String tag){
        this.title = title;
        this.description = description;
        this.imgId = imgId;
        this.editTextEnable = editTextEnable;
        this.tag = tag;
    }

    public RecyclerData(String title, String description, int imgId, String tag) {
        this.title = title;
        this.description = description;
        this.imgId = imgId;
        this.tag = tag;
    }

    public RecyclerData(String title, String description, int imgId, String tag, String id) {
        this.title = title;
        this.description = description;
        this.imgId = imgId;
        this.tag = tag;
        this.id = id;
    }

    public RecyclerData(String title, int imgId, String tag){
        this.title = title;
        this.imgId = imgId;
        this.tag = tag;
    }

    public RecyclerData(String title, int imgId, int secondImgId, String tag){
        this.title = title;
        this.imgId = imgId;
        this.tag = tag;
        this.secondImgId = secondImgId;
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

    public int getSecondImgId() {
        return secondImgId;
    }

    public void setSecondImgId(int secondImgId) {
        this.secondImgId = secondImgId;
    }


    public Boolean getFavorite()
    {
        return favorite;
    }

    public void setFavorite(Boolean favorite){
        this.favorite = favorite;
    }

    public String getId ()
    {
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public Boolean getEditTextEnable()
    {
        return editTextEnable;
    }

    public String getTag(){ return tag;}
}
