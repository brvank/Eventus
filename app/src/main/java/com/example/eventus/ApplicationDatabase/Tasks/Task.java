package com.example.eventus.ApplicationDatabase.Tasks;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Tasks")
public class Task {

    @PrimaryKey(autoGenerate = true)
    int id;
    int eventId;

    private String title;

    private String info;

    private String deadline;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId(){
        return eventId;
    }

    public void setEventId(int eventId){
        this.eventId = eventId;
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

    public Task(String title, String info, String deadline, int eventId){
        this.title = title;
        this.info = info;
        this.deadline = deadline;
        this.eventId = eventId;
    }
}
