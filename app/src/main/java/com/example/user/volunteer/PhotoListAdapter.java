package com.example.user.volunteer;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.user.volunteer.dao.PhotoItemCollectionDao;
import com.example.user.volunteer.dao.PhotoItemDao;
import com.example.user.volunteer.manager.PhotoListManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User on 8/4/2560.
 */

public class PhotoListAdapter extends BaseAdapter {

    private PhotoItemCollectionDao dao;

    public void setDao(PhotoItemCollectionDao dao) {
        this.dao = dao;
    }

    @Override
    public int getCount() {
        if(dao== null)
            return 0;
        if(dao.getEvents() == null)
            return 0;
        return dao.getEvents().size();
    }

    @Override
    public Object getItem(int position) {
        return dao.getEvents().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PhotoListItem item;
        if(convertView != null)
            item = (PhotoListItem) convertView;
        else
            item = new PhotoListItem(parent.getContext());

        PhotoItemDao dao = (PhotoItemDao) getItem(position);
        item.setNameText(dao.getEventName());
        item.setDescriptionText(dao.getEventDes1());
        item.setImageUrl(dao.getImagePath());
        item.setNameTypeText(dao.getEventTypeName());

        //TODO: add
        Date today = new Date();

        if(dao.getStartRegis().after(today)){
            item.setStatus("ยังไม่เปิดรับสมัคร");
            item.setStatusColor("ยังไม่เปิดรับสมัคร");
        }else if((dao.getStartRegis().equals(today) || dao.getStartRegis().before(today))&& (dao.getEndRegis().after(today) || dao.getEndRegis().equals(today))){
            item.setStatus("เปิดรับสมัคร");
            item.setStatusColor("เปิดรับสมัคร");
        }else if(dao.getEndRegis().before(today)){
            item.setStatus("ปิดรับสมัคร");
            item.setStatusColor("ปิดรับสมัคร");
        }

        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        String st = fm.format(dao.getStartDate());
        String end = fm.format(dao.getEndDate());
        item.setEventDateText(st+" - "+end);
        //item.setEventJoin(dao.getJoinedAmount());


        return item;
    }


}
