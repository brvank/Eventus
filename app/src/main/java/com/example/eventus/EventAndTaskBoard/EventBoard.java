package com.example.eventus.EventAndTaskBoard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eventus.ApplicationDatabase.Database;
import com.example.eventus.ApplicationDatabase.Events.Event;
import com.example.eventus.ApplicationDatabase.Events.EventsAdapter;
import com.example.eventus.ApplicationDatabase.Tasks.Task;
import com.example.eventus.ApplicationDatabase.Tasks.TaskAdapter;
import com.example.eventus.ApplicationDatabase.Tasks.TasksDao;
import com.example.eventus.DashBoard.Dashboard;
import com.example.eventus.R;
import com.example.eventus.Singletons.DatabaseSingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

public class EventBoard extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    //Declaring the views
    private RecyclerView rvTasks;
    private Button btnOKNewTask, btnCancelNewTask;
    private FloatingActionButton fbAddTasks;
    private EditText etDeadlineNewTask, etTitleNewTask, etInfoNewTask;

    //For recycler view
    public static TaskAdapter taskAdapter;
    public static List<Task> tasks;
    private RecyclerView.LayoutManager layoutManager;

    //For alert box
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private View alertDialogView;

    //For database
    private DatabaseSingleton databaseSingleton;
    public static TasksDao tasksDao;

    //For intent
    Intent intent;
    static public int eventId;
    String eventDeadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_board);

        //Getting the data from intent
        intent = getIntent();
        eventId = intent.getIntExtra("eventId", 0);
        eventDeadline = intent.getStringExtra("eventDeadline");

        //Initializing and setting onclicklisteners on views
        init();
        //Recycler view stuff
        recyclerView();
        //new task dialog
        newTaskDialog();
        //database
        database();
        //load tasks
        loadTasks();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fb_add_task_eventboard:
                alertDialog.show();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(mCalendar.getTime());
        etDeadlineNewTask.setText(selectedDate);
    }

    private void init(){

        //initializing the views
        rvTasks = findViewById(R.id.rv_tasks_eventboard);
        fbAddTasks = findViewById(R.id.fb_add_task_eventboard);

        //setting onclicklisteners
        fbAddTasks.setOnClickListener(this);
    }

    private void recyclerView() {

        //Initializing the recycler view stuffs
        layoutManager = new GridLayoutManager(EventBoard.this, 1);
        rvTasks.setLayoutManager(layoutManager);
    }

    private void newTaskDialog(){
        alertDialogBuilder = new AlertDialog.Builder(EventBoard.this);
        alertDialogView = getLayoutInflater().inflate(R.layout.new_event, null);
        alertDialogBuilder.setView(alertDialogView);
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        //Instantiating the view
        btnOKNewTask = alertDialogView.findViewById(R.id.btn_ok_new_event);
        etDeadlineNewTask = alertDialogView.findViewById(R.id.et_deadline_new_event);
        btnCancelNewTask = alertDialogView.findViewById(R.id.btn_cancel_new_event);
        etTitleNewTask = alertDialogView.findViewById(R.id.et_title_new_event);
        etInfoNewTask = alertDialogView.findViewById(R.id.et_info_new_event);

        //setting on click listeners for the views in dialog box
        btnOKNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
                alertDialog.dismiss();
            }
        });

        btnCancelNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        etDeadlineNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new com.example.eventus.DateAndTime.DatePicker();
                //TODO: to set the max date in the date picker dialog
                datePicker.show(getSupportFragmentManager(), null);
            }
        });
    }

    private void database(){
        //getting the database instance
        databaseSingleton = DatabaseSingleton.getInstance();
        //getting the events dao
        tasksDao = databaseSingleton.getTasksDao(getApplicationContext());

        //deleting all previously saved events
        //eventsDao.deleteAllEvent();
    }

    private void addTask() {

        //Details about the event
        String taskTitle, taskInfo, taskDeadline;
        taskTitle = etTitleNewTask.getText().toString();
        taskInfo = etInfoNewTask.getText().toString();
        taskDeadline = etDeadlineNewTask.getText().toString();

        if(taskTitle.equals("") || taskInfo.equals("") || taskDeadline.equals("")){
            Toast.makeText(EventBoard.this, "Please try again!", Toast.LENGTH_SHORT).show();
        }else{
            //creating an event if every field is filled
            Task task = new Task(taskTitle, taskInfo, taskDeadline, eventId);

            Handler handler = new Handler();

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    tasksDao.createTask(task);
                    Toast.makeText(EventBoard.this, "Event Created", Toast.LENGTH_SHORT).show();
                    refreshEvents();
                }
            };

            handler.post(runnable);
        }
    }

    private void loadTasks(){

        tasks = tasksDao.getTasks(eventId);

        taskAdapter = new TaskAdapter(tasks, getApplicationContext());

        rvTasks.setAdapter(taskAdapter);

    }

    public static void refreshEvents(){
        tasks.clear();

        List<Task> tempTasksList = tasksDao.getTasks(eventId);
        for(int i=0;i<tempTasksList.size();i++){
            tasks.add(tempTasksList.get(i));
        }

        taskAdapter.notifyDataSetChanged();
    }

    //TODO: to complete the setMaxDate function in the below class
    //Date picker dialog for the task deadline and start
    public class CustomDatePicker extends DialogFragment{

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

            Calendar mCalendar = Calendar.getInstance();
            int year = mCalendar.get(Calendar.YEAR);
            int month = mCalendar.get(Calendar.MONTH);
            int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);
            //To add a theme for the date picker dialog
            //DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK, (DatePickerDialog.OnDateSetListener) getActivity(), year, month, dayOfMonth);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(), year, month, dayOfMonth);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            //datePickerDialog.getDatePicker().setMaxDate();
            return datePickerDialog;

        }
    }


}