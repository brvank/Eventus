package com.example.eventus.Singletons;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.eventus.ApplicationDatabase.Database;
import com.example.eventus.ApplicationDatabase.Events.EventsDao;
import com.example.eventus.ApplicationDatabase.Tasks.TasksDao;

public class DatabaseSingleton extends AppCompatActivity {

    private static DatabaseSingleton instance;
    private Database eventsDatabase;
    private EventsDao eventsDao;
    private TasksDao tasksDao;

    private DatabaseSingleton(){}

    synchronized public static DatabaseSingleton getInstance(){
        if(instance == null){
            instance = new DatabaseSingleton();
        }
        return instance;
    }

    synchronized public Database getDatabaseInstance(Context context){
        if(eventsDatabase == null){
            eventsDatabase = Room.databaseBuilder(context, Database.class, "Events")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return eventsDatabase;
    }

    synchronized public EventsDao getEventsDao(Context context){
        if(eventsDao == null){
            Database database = getDatabaseInstance(context);
            eventsDao = database.eventsDao();
        }
        return eventsDao;
    }

    synchronized public TasksDao getTasksDao(Context context){
        if(tasksDao == null){
            Database database = getDatabaseInstance(context);
            tasksDao = database.tasksDao();
        }
        return tasksDao;
    }
}
