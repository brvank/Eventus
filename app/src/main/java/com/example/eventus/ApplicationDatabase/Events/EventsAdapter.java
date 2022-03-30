package com.example.eventus.ApplicationDatabase.Events;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventus.DashBoard.Dashboard;
import com.example.eventus.EventAndTaskBoard.EventBoard;
import com.example.eventus.R;

import java.util.ArrayList;
import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {


    private List<Event> events;
    private Context context;

    public EventsAdapter(List<Event> events, Context context){
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.event_box,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsAdapter.ViewHolder holder, int position) {
        holder.tvEventTitle.setText(events.get(position).getTitle());
        holder.tvDeadline.setText((CharSequence) events.get(position).getDeadline());
        int eventId = events.get(position).getId();
        String eventDeadline = events.get(position).getDeadline();
        holder.llEventBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventBoard.class);
                intent.putExtra("eventId", eventId);
                intent.putExtra("eventDeadline", eventDeadline);
                context.startActivity(intent);
            }
        });

        int eventIndex = position;
        holder.ivEventDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvents(events.get(eventIndex));
            }
        });

    }

    private void deleteEvents(Event event){
        Dashboard.eventsDao.deleteEvent(event);

        Dashboard.refreshEvents();
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvEventTitle, tvDeadline;
        public LinearLayout llEventBox;
        public ImageView ivEventEdit, ivEventDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEventTitle = itemView.findViewById(R.id.tv_event_title);
            tvDeadline = itemView.findViewById(R.id.tv_deadline);
            llEventBox = itemView.findViewById(R.id.ll_event_box);
            ivEventDelete = itemView.findViewById(R.id.iv_event_delete);
            ivEventEdit = itemView.findViewById(R.id.iv_event_edit);
        }
    }
}
