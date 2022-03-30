package com.example.eventus.ApplicationDatabase;

import androidx.room.RoomDatabase;

import com.example.eventus.ApplicationDatabase.Events.Event;
import com.example.eventus.ApplicationDatabase.Events.EventsDao;
import com.example.eventus.ApplicationDatabase.Tasks.Task;
import com.example.eventus.ApplicationDatabase.Tasks.TasksDao;

//step 3: create the database as an abstract class
@androidx.room.Database(entities = {Event.class, Task.class}, version = 4)
public abstract class Database extends RoomDatabase {

    //for the events part
    public abstract EventsDao eventsDao();

    //for the tasks part
    public abstract TasksDao tasksDao();
}
