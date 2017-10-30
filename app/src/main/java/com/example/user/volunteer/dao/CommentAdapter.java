package com.example.user.volunteer.dao;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.user.volunteer.R;

import java.util.List;

/**
 * Created by User on 27/10/2560.
 */

public class CommentAdapter extends ArrayAdapter<Comment>{

    private Context context;
    private int resource;
    private List<Comment> comments;

    public CommentAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Comment> comments) {
        super(context,  resource, comments);
        this.context = context;
        this.resource = resource;
        this.comments = comments;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = LayoutInflater.from(this.context).inflate(resource, parent, false);
        TextView userName = ((TextView)row.findViewById(R.id.tvUsername));
        userName.setText((comments.get(position).getUserFName())
                +"  "+comments.get(position).getUserLname());
        TextView user = ((TextView)row.findViewById(R.id.tvUser));
        user.setText(comments.get(position).getUserName());
        TextView commentDesc = ((TextView)row.findViewById(R.id.tvCommentdesc));
        commentDesc.setText(comments.get(position).getDescription());

        return  row;
    }

    public Comment getItem(int position){
        return comments.get(position);
    }
}
