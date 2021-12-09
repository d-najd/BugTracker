package com.aatesting.bugtracker.recyclerview;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RecyclerData {

    private String title;
    private String description;
    private String tag; // used for differentiating the recyclerViews
    private String id;
    private int imgId;
    private int secondImgId;
    private ArrayList<String> titles;
    private ArrayList<Integer> imgIds;
    private Boolean favorite = false;
    private Boolean editTextEnable = false;

    private Calendar calendarStartDate;
    private Calendar calendarEndDate;

    public RecyclerData(String title, String description, int imgId, boolean editTextEnable, String tag){
        this.title = title;
        this.description = description;
        this.imgId = imgId;
        this.editTextEnable = editTextEnable;
        this.tag = tag;
    }


    public RecyclerData(int imgId, String description, boolean editTextEnable, String tag){
        this.imgId = imgId;
        this.editTextEnable = editTextEnable;
        this.description = description;
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

    public RecyclerData(String title, int imgId, String tag, String id) {
        this.title = title;
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

    public RecyclerData(String title, ArrayList<String> titles, ArrayList<Integer> imgIds, String id, String tag)
    {
        this.title = title;
        this.titles = titles;
        this.imgIds = imgIds;
        this.tag = tag;
        this.id = id;
    }

    public RecyclerData(String title, String tag) {
        this.title = title;
        this.tag = tag;
    }

    public RecyclerData(String title, String description, String tag) {
        this.title = title;
        this.description = description;
        this.tag = tag;
    }

    //for epics
    public RecyclerData(String title, Calendar calendarStartDate, Calendar calendarEndDate, String tag) {
        this.title = title;
        this.calendarStartDate = calendarStartDate;
        this.calendarEndDate = calendarEndDate;
        this.tag = tag;
    }

    public RecyclerData (Integer id, String title, String description, Date startDate, Date endDate){

    }

    public ArrayList<String> getTitles(){
        return titles;
    }

    public void setTitles(ArrayList<String> titles){
        this.titles = titles;
    }

    public ArrayList<Integer> getImgIds(){
        return imgIds;
    }

    public void setImgIds (ArrayList<Integer> imgIds){
        this.imgIds = imgIds;
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

    public void setCalendarStartDate(Calendar calendarStartDate) {
        this.calendarStartDate = calendarStartDate;
    }

    public Calendar getCalendarStartDate(){
        return calendarStartDate;
    }

    public void setCalendarEndDate(Calendar calendarEndDate) {
        this.calendarEndDate = calendarEndDate;
    }

    public Calendar getCalendarEndDate(){
        return calendarEndDate;
    }
}
