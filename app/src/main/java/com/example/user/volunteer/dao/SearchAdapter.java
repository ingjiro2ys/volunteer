package com.example.user.volunteer.dao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.IdRes;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.user.volunteer.R;
import com.example.user.volunteer.dao.PhotoItemCollectionDao;
import com.example.user.volunteer.dao.PhotoItemDao;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
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

//        //TODO: add
//        ImageView imageView = ((ImageView)row.findViewById(R.id.ivImg));
//        Bitmap bitmap = null;
//        try {
//            bitmap = BitmapFactory.decodeStream((InputStream)new URL(events.get(position).getImagePath()).getContent());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        imageView.setImageBitmap(bitmap);
//
//        TextView desc = ((TextView)row.findViewById(R.id.tvDesc));
//        desc.setText(events.get(position).getEventDes1());
//
//        // change format
//        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
//        String st = fm.format(events.get(position).getStartDate());
//        String end = fm.format(events.get(position).getEndDate());
//        TextView date = ((TextView)row.findViewById(R.id.eventDate));
//        date.setText(st+ " - "+end );
//
//        TextView eventType = ((TextView)row.findViewById(R.id.tvType));
//        eventType.setText(events.get(position).getEventTypeName());
//
//        TextView status = ((TextView)row.findViewById(R.id.tv_st));
//
//        Date today = new Date();
//
//        if(events.get(position).getStartRegis().after(today)){
//            status.setText("ยังไม่เปิดรับสมัคร");
//        }else if((events.get(position).getStartRegis().equals(today) ||events.get(position).getStartRegis().before(today))&& (events.get(position).getEndRegis().after(today) || events.get(position).getEndRegis().equals(today))){
//            status.setText("เปิดรับสมัคร");
//            if(events.get(position).getEndRegis().getDate()-today.getDate()==3){
//                status.setText("อีก3วัน.. ปิดรับสมัคร");
//            }else if(events.get(position).getEndRegis().getDate()-today.getDate()==2){
//                status.setText("อีก2วัน.. ปิดรับสมัคร");
//            }else if(events.get(position).getEndRegis().getDate()-today.getDate()==1){
//                status.setText("อีก1วัน.. ปิดรับสมัคร");
//            }else if(events.get(position).getEndRegis().equals(today)){
//                status.setText("เปิดรับสมัครวันสุดท้าย");
//            }
//        }else if(events.get(position).getEndRegis().before(today)){
//            status.setText("ปิดรับสมัคร");
//        }

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