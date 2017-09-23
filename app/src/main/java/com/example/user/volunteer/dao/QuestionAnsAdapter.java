package com.example.user.volunteer.dao;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.example.user.volunteer.R;

/**
 * Created by User on 23/9/2560.
 */

public class QuestionAnsAdapter extends ArrayAdapter<QuestionAns> {

    private Context context;
    private int resource;
    private List<QuestionAns> questionAnses;

    public QuestionAnsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<QuestionAns> questionAnses) {
        super(context, resource, questionAnses);
        this.context = context;
        this.resource = resource;
        this.questionAnses = questionAnses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = LayoutInflater.from(this.context).inflate(resource, parent, false);
        TextView question = ((TextView) row.findViewById(R.id.questionEvent));
        question.setText(questionAnses.get(position).getQuestionName());

        TextView answer = ((TextView)row.findViewById(R.id.userAnswer));
        answer.setText(questionAnses.get(position).getAnswerDes());

        return  row;
    }

    public QuestionAns getItem(int position){
        return questionAnses.get(position);
    }
}
