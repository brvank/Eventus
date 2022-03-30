package com.example.eventus.ApplicationDatabase.Events;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

//step 2: create the dao of the table then move on to create an abstract method in the database
@Dao
public interface EventsDao {

    @Query("SELECT * from Events")
    public List<Event> getEvents();

    @Insert
    public void createEvent(Event event);

    @Delete
    public void deleteEvent(Event event);

    @Query("DELETE from Events")
    public void deleteAllEvent();

    @Update
    public void updateEvent(Event event);

}
