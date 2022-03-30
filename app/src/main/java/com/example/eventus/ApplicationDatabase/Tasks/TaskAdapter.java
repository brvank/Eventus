package com.example.eventus.ApplicationDatabase.Tasks;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventus.ApplicationDatabase.Events.EventsAdapter;
import com.example.eventus.EventAndTaskBoard.EventBoard;
import com.example.eventus.R;
import com.example.eventus.Singletons.DatabaseSingleton;

import java.util.List;
import java.util.logging.LogRecord;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.viewHolder> {

    //For database
    private DatabaseSingleton databaseSingleton;
    private TasksDao tasksDao;

    Context context;

    List<Task> tasks;

    public TaskAdapter(List<Task> tasks, Context context){
        this.tasks = tasks;
        this.context = context;

        //database
        database();
    }

    private void database(){
        //getting the database instance
        databaseSingleton = DatabaseSingleton.getInstance();
        //getting the events dao
        tasksDao = databaseSingleton.getTasksDao(context);

        //deleting all previously saved events
        //eventsDao.deleteAllEvent();
    }

    @NonNull
    @Override
    public TaskAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.task_box,parent,false);
        return new TaskAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.viewHolder holder, int position) {
        holder.tvTaskTitle.setText(tasks.get(position).getTitle());
        holder.tvTaskDetails.setText(tasks.get(position).getInfo());
        holder.tvTaskDeadline.setText(tasks.get(position).getDeadline());

        int tasksIndex = position;
        holder.ivTaskDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: to add the refresh function to update the recycler view
                deleteTasks(tasks.get(tasksIndex));
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void deleteTasks(Task task) {
        Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                tasksDao.deleteTask(task);
                //TODO: make any other efficient way to do this
                EventBoard.refreshEvents();
            }
        });
    }

    public static class viewHolder extends RecyclerView.ViewHolder{
        TextView tvTaskTitle, tvTaskDeadline, tvTaskDetails;
        ImageView ivTaskEdit, ivTaskDelete;

        public viewHolder(@NonNull View itemview){
            super(itemview);
            tvTaskTitle = itemview.findViewById(R.id.tv_task_title);
            tvTaskDetails = itemview.findViewById(R.id.tv_task_details);
            tvTaskDeadline = itemview.findViewById(R.id.tv_task_deadline);
            ivTaskEdit = itemview.findViewById(R.id.iv_task_edit);
            ivTaskDelete = itemview.findViewById(R.id.iv_task_delete);
        }
    }
}
