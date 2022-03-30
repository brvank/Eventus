package com.example.eventus.ApplicationDatabase.Tasks;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.eventus.ApplicationDatabase.Events.Event;

import java.util.List;

@Dao
public interface TasksDao {

    @Query("SELECT * from Tasks where eventId = :eventId")
    public List<Task> getTasks(int eventId);

    @Insert
    public void createTask(Task task);

    @Delete
    public void deleteTask(Task task);

    @Query("DELETE from Tasks")
    public void deleteAllTasks();

    @Update
    public void updateTask(Task task);

    @Query("DELETE from Tasks where eventId = :eventId")
    public void deleteTasksWithEvent(int eventId);
}
