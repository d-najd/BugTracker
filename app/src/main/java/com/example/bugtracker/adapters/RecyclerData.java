package com.example.bugtracker.adapters;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bugtracker.R;

public class RecyclerData {

    private String title;
    private String description;
    private String editText;
    private int imgId;
    private Boolean favorite = false;

    public RecyclerData(String title, String description, String editText, int imgId) {
        this.title = title;
        this.imgId = imgId;
        this.description = description;
        this.editText = editText;
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

    public String getEditText(){
        return editText;
    }

    public void setEditText(String editText) {
        this.editText = editText;
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
}
