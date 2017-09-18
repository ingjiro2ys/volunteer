package com.example.user.volunteer;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 14/9/2560.
 */

public class AnswerAdapter extends ArrayAdapter<Answer> {

    private Context context;
    private int resource;
    private List<Answer> answers;
    ArrayList<Answer> arrayList= new ArrayList<Answer>();
    int i=0;

    public AnswerAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Answer> answers) {
        super(context, resource, answers);
        this.context = context;
        this.resource = resource;
        this.answers = answers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = LayoutInflater.from(this.context).inflate(resource, parent, false);
        TextView questionName = ((TextView)row.findViewById(R.id.question1));
        questionName.setText(answers.get(position).getQuestionName());

        EditText answer = ((EditText) row.findViewById(R.id.answer1));

        TextView questionId= (TextView)row.findViewById(R.id.tvQuesId);
        questionId.setText(answers.get(position).getQuestionID());
        //answer.setText(answers.get(position).toString());
        /*TextView eventId = (TextView) row.findViewById(R.id.tvEventId);
        eventId.setText(answers.get(position).getEventID());

        TextView questionId = (TextView)row.findViewById(R.id.tvQuesId);
        questionId.setText(answers.get(position).getQuestionID());
        */
        return  row;
    }

    public Answer getItem(int position){
        return answers.get(position);
    }
}
