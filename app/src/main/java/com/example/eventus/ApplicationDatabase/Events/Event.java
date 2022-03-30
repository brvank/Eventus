package com.example.eventus.ApplicationDatabase.Events;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

//step 1: create the object then move on to creating the dao of the table
@Entity(tableName = "Events")
public class Event {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private String info;

    private String deadline;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

//    @Ignore
//    public Event(){}

    public Event(String title, String info, String deadline){
        this.title = title;
        this.info = info;
        this.deadline = deadline;
    }
}
