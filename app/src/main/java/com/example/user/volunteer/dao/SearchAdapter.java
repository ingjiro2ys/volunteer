package com.example.user.volunteer.dao;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.user.volunteer.R;
import com.example.user.volunteer.dao.PhotoItemCollectionDao;
import com.example.user.volunteer.dao.PhotoItemDao;

import java.util.List;


public class SearchAdapter extends ArrayAdapter<PhotoItemDao> {

    private Context context;
    private int resource;
    private List<PhotoItemDao> events;

    public SearchAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<PhotoItemDao> events) {
        super(context, resource, events);
        this.context = context;
        this.resource = resource;
        this.events = events;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = LayoutInflater.from(this.context).inflate(resource, parent, false);
        TextView colorName = ((TextView)row.findViewById(R.id.colorName));
        colorName.setText(events.get(position).getEventName());
        //TextView colorValue = ((TextView)row.findViewById(R.id.colorValue));
        //colorValue.setText(events.get(position).getValue());
        return  row;


    }

    private PhotoItemCollectionDao dao;


//    @Override
//    public PhotoItemDao getItem(int position) {
//        return dao.getEvents().get(position);
//
//    }



    @Override
    public long getItemId(int position) {

        return 0;
    }

}