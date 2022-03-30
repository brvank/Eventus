package com.example.eventus.DashBoard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eventus.ApplicationDatabase.Events.Event;
import com.example.eventus.ApplicationDatabase.Events.EventsAdapter;
import com.example.eventus.ApplicationDatabase.Events.EventsDao;
import com.example.eventus.R;
import com.example.eventus.Singletons.DatabaseSingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Dashboard extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    //Declaring the views
    private FloatingActionButton fbAddEvent;
    private Button btnOKNewEvent, btnCancelNewEvent;
    private EditText etDeadlineNewEvent, etTitleNewEvent, etInfoNewEvent;

    //For recycler view
    private RecyclerView rvEvents;
    private RecyclerView.LayoutManager layoutManager;
    public static List<Event> events;
    public static EventsAdapter eventsAdapter;

    //For alert box
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private View alertDialogView;

    //For database
    private DatabaseSingleton databaseSingleton;
    public static EventsDao eventsDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Initializing and setting onclicklisteners on views
        init();
        //RecyclerView stuff
        recyclerViewStuff();
        //new event stuff
        newEventDialog();
        //database stuff
        database();
        //loadEvents
        loadEvents();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fb_add_event_dashboard:
                alertDialog.show();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime());
        etDeadlineNewEvent.setText(selectedDate);
    }

    private void init(){
        //Initializing the views
        rvEvents = findViewById(R.id.rv_events_dashboard);
        fbAddEvent = findViewById(R.id.fb_add_event_dashboard);

        //Setting onclicklisteners on views
        fbAddEvent.setOnClickListener(this);
    }

    private void recyclerViewStuff() {

        //Initializing the recycler view stuffs
        layoutManager = new GridLayoutManager(this, 2);
        rvEvents.setLayoutManager(layoutManager);

    }

    private void newEventDialog() {
        alertDialogBuilder = new AlertDialog.Builder(Dashboard.this);
        alertDialogView = getLayoutInflater().inflate(R.layout.new_event, null);
        alertDialogBuilder.setView(alertDialogView);
        alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        //Instantiating the view
        btnOKNewEvent = alertDialogView.findViewById(R.id.btn_ok_new_event);
        etDeadlineNewEvent = alertDialogView.findViewById(R.id.et_deadline_new_event);
        btnCancelNewEvent = alertDialogView.findViewById(R.id.btn_cancel_new_event);
        etTitleNewEvent = alertDialogView.findViewById(R.id.et_title_new_event);
        etInfoNewEvent = alertDialogView.findViewById(R.id.et_info_new_event);

        //setting on click listeners for the views in dialog box
        btnOKNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();
                alertDialog.dismiss();
            }
        });

        btnCancelNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        etDeadlineNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new com.example.eventus.DateAndTime.DatePicker();
                datePicker.show(getSupportFragmentManager(), null);
            }
        });
    }

    private void database(){
        //getting the database instance
        databaseSingleton = DatabaseSingleton.getInstance();
        //getting the events dao
        eventsDao = databaseSingleton.getEventsDao(getApplicationContext());

        //deleting all previously saved events
        //eventsDao.deleteAllEvent();
    }

    private void addEvent() {

        //Details about the event
        String eventTitle, eventInfo, eventDeadline;
        eventTitle = etTitleNewEvent.getText().toString();
        eventInfo = etInfoNewEvent.getText().toString();
        eventDeadline = etDeadlineNewEvent.getText().toString();

        if(eventTitle.equals("") || eventInfo.equals("") || eventDeadline.equals("")){
            Toast.makeText(Dashboard.this, "Please try again!", Toast.LENGTH_SHORT).show();
        }else{
            //creating an event if every field is filled
            Event event = new Event(eventTitle, eventInfo, eventDeadline);

            Handler handler = new Handler();

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    eventsDao.createEvent(event);
                    Toast.makeText(Dashboard.this, "Event Created", Toast.LENGTH_SHORT).show();
                    refreshEvents();
                }
            };

            handler.post(runnable);
        }
    }

    private void loadEvents(){

        events = eventsDao.getEvents();

        eventsAdapter = new EventsAdapter(events, getApplicationContext());

        rvEvents.setAdapter(eventsAdapter);

    }
    
    public static void refreshEvents(){
        events.clear();

        List<Event> tempEventsList = eventsDao.getEvents();
        for(int i=0;i<tempEventsList.size();i++){
            events.add(tempEventsList.get(i));
        }

        eventsAdapter.notifyDataSetChanged();
    }
}