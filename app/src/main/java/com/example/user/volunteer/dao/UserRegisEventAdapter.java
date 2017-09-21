package com.example.user.volunteer.dao;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.user.volunteer.R;

import java.util.List;

/**
 * Created by User on 21/9/2560.
 */

public class UserRegisEventAdapter extends ArrayAdapter<UserRegisEvent>{

    private Context context;
    private int resource;
    private List<UserRegisEvent> userRegis;

    public UserRegisEventAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<UserRegisEvent> userRegis) {
        super(context, resource, userRegis);
        this.context = context;
        this.resource = resource;
        this.userRegis = userRegis;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = LayoutInflater.from(this.context).inflate(resource, parent, false);
        TextView userRegisName = ((TextView)row.findViewById(R.id.userRegisName));
        userRegisName.setText(userRegis.get(position).getUserFName()+"    "+userRegis.get(position).getUserLname());

        CheckBox checkBox = ((CheckBox)row.findViewById(R.id.checkbox));

        return  row;
    }

    public UserRegisEvent getItem(int position){
        return userRegis.get(position);
    }

}
