package com.example.user.volunteer.manager;

import android.content.Context;

import com.example.user.volunteer.dao.PhotoItemCollectionDao;
import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

public class PhotoListManager {

    private static PhotoListManager instance;
    private PhotoItemCollectionDao dao;

    public static PhotoListManager getInstance() {
        if (instance == null)
            instance = new PhotoListManager();
        return instance;
    }

    private Context mContext;


    private PhotoListManager() {
        mContext = Contextor.getInstance().getContext();
    }

    public PhotoItemCollectionDao getDao() {
        return dao;
    }

    public void setDao(PhotoItemCollectionDao dao) {
        this.dao = dao;
    }

    public int getCount(){
        if(dao == null)
            return 0;
        if(dao.getEvents()==null)
            return 0;
        return dao.getEvents().size();
    }
}
