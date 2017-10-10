package com.example.user.volunteer.dao;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.volunteer.R;
import com.example.user.volunteer.ShowUserRegisActivity;

import java.util.List;

/**
 * Created by User on 22/9/2560.
 */

public class UserRegisAdapter extends ArrayAdapter<UserRegis> {

    private Context context;
    private int resource;
    private List<UserRegis> userRegises;

    public UserRegisAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<UserRegis> userRegises) {
        super(context, resource, userRegises);
        this.context = context;
        this.resource = resource;
        this.userRegises = userRegises;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = LayoutInflater.from(this.context).inflate(resource, parent, false);
        TextView userRegisName = ((TextView)row.findViewById(R.id.userRegisName));
        userRegisName.setText(userRegises.get(position).getUserFName()+"     "+userRegises.get(position).getUserLName());

        CheckBox checkBox = ((CheckBox)row.findViewById(R.id.checkboxName));
        String join = "1";
        if(userRegises.get(position).getIsJoined().equals(join)){
            checkBox.setChecked(true);
        }

        return  row;
    }

    public UserRegis getItem(int position){
        return userRegises.get(position);
    }
}
