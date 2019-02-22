package com.example.tonywu.multinotes;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class notedetails implements Serializable{


    private int id;
    private String title;
    private String note;
    private String date;


    public notedetails(int id, String title, String note, String date){
        this.id = id;
        this.title = title;
        this.note = note;
        this.date = date;
    }

    public int getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public String getNote(){
        return note;
    }
    public String getDate(){
        return date;
    }
    public void setId(int id){
        this.id = id;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setNote(String note){
        this.note = note;
    }
    public void setDate(String date){
        this.date = date;
    }
}
