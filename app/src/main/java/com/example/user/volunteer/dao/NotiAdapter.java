package com.example.user.volunteer.dao;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.volunteer.R;

import java.util.List;

/**
 * Created by User on 22/9/2560.
 */

public class NotiAdapter extends ArrayAdapter<Noti> {

    private Context context;
    private int resource;
    private List<Noti> noti;
    String eventName;

    public NotiAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Noti> Noti) {
        super(context, resource, Noti);
        this.context = context;
        this.resource = resource;
        this.noti = Noti;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = LayoutInflater.from(this.context).inflate(resource, parent, false);
        TextView tv_noti = ((TextView)row.findViewById(R.id.tv_noti));
        TextView tv_eventID = ((TextView)row.findViewById(R.id.tv_eventID));
        TextView tv_regisID = ((TextView)row.findViewById(R.id.tv_regisID));
        TextView tv_isJoined = ((TextView)row.findViewById(R.id.tv_isJoined));
        TextView tv_clicked = ((TextView)row.findViewById(R.id.tv_clicked));
        ImageView iv_noti = ((ImageView)row.findViewById(R.id.iv_noti));
        eventName=noti.get(position).getEventName();
        tv_noti.setText("ยินดีด้วย ! คุณได้รับคัดเลือกเข้าร่วมกิจกรรม "+eventName);
        tv_eventID.setText(noti.get(position).getEventID()+"");
        tv_regisID.setText(noti.get(position).getRegisID()+"");
        tv_isJoined.setText(noti.get(position).getIsJoined()+"");
        tv_clicked.setText(noti.get(position).getClicked()+"");

        /*Picasso.with(getContext()).load(noti.get(position).getImagePath()).placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(iv_noti,new com.squareup.picasso.Callback(){

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });*/

        Glide.with(context)
                .load(noti.get(position).getImagePath())
                .into(iv_noti);

        if(tv_clicked.getText().toString().equals("1")){
            tv_noti.setTypeface(Typeface.DEFAULT);
        }



        return  row;
    }

    public Noti getItem(int position){
        return noti.get(position);
    }
}
