package com.example.user.volunteer;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.inthecheesefactory.thecheeselibrary.view.BaseCustomViewGroup;
import com.inthecheesefactory.thecheeselibrary.view.state.BundleSavedState;


public class PhotoListItem extends BaseCustomViewGroup { // มันคือ custom view group

    TextView tvName;
    TextView tvDesc;
    TextView tvType;
    ImageView ivImage;

    TextView tv_st;


    TextView eventDate,eventJoin;
    

    public PhotoListItem(Context context) {
        super(context);
        initInflate();
        initInstances();
    }

    public PhotoListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initInflate();
        initInstances();
        initWithAttrs(attrs, 0, 0);
    }

    public PhotoListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, 0);
    }

    @TargetApi(21)
    public PhotoListItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initInflate();
        initInstances();
        initWithAttrs(attrs, defStyleAttr, defStyleRes);
    }

    private void initInflate() {
        inflate(getContext(), R.layout.list_layout, this);
    }

    private void initInstances() {
        // findViewById here
        tvName = (TextView) findViewById(R.id.tvName);
        tvDesc = (TextView) findViewById(R.id.tvDesc);
        tvType = (TextView) findViewById(R.id.tvType);
        ivImage = (ImageView) findViewById(R.id.ivImg);

        eventDate = (TextView) findViewById(R.id.eventDate);
        eventJoin = (TextView) findViewById(R.id.eventJoin);
        tv_st = (TextView) findViewById(R.id.tv_st);
    }

    private void initWithAttrs(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        /*
        TypedArray a = getContext().getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StyleableName,
                defStyleAttr, defStyleRes);

        try {

        } finally {
            a.recycle();
        }
        */
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        BundleSavedState savedState = new BundleSavedState(superState);
        // Save Instance State(s) here to the 'savedState.getBundle()'
        // for example,
        // savedState.getBundle().putString("key", value);

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        BundleSavedState ss = (BundleSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        Bundle bundle = ss.getBundle();
        // Restore State from bundle here
    }

    /*@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec); // width in px
        int height = width * 2/3 ;
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                height, MeasureSpec.EXACTLY);
        // Child View
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Self
        setMeasuredDimension(width,height);
    }*/

    public void setNameText(String text){
        tvName.setText(text);
    }

    public void setDescriptionText(String text) {
        tvDesc.setText(text);
    }

    public void setNameTypeText(String text) {
        tvType.setText(text);
    }

    public void setImageUrl(String url){
        Glide.with(getContext())
                .load(url)
                .placeholder(R.drawable.loading)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivImage);
    }

    public void setStatus(String text){
        tv_st.setText(text);
        if(text.equals("เปิดรับสมัคร")) {
            tv_st.setBackgroundResource(R.drawable.shape_round_tv_status_green);
        }
    }

    public void setStatusColor(String text){
        if(text.equals("เปิดรับสมัคร")) {
            tv_st.setBackgroundResource(R.drawable.shape_round_tv_status_green);
        }else if(text.equals("ปิดรับสมัคร")){
            tv_st.setBackgroundResource(R.drawable.shape_round_tv_status);
        }else if(text.equals("ยังไม่เปิดรับสมัคร")){
            tv_st.setBackgroundResource(R.drawable.shape_round_tv_status_gray);
        }else if(text.equals("อีก3วัน.. ปิดรับสมัคร")){
            tv_st.setBackgroundResource(R.drawable.shape_round_tv_status_yellow);
        }else if(text.equals("อีก2วัน.. ปิดรับสมัคร")){
            tv_st.setBackgroundResource(R.drawable.shape_round_tv_status_yellow);
        }else if(text.equals("อีก1วัน.. ปิดรับสมัคร")){
            tv_st.setBackgroundResource(R.drawable.shape_round_tv_status_yellow);
        }else if(text.equals("เปิดรับสมัครวันสุดท้าย")){
            tv_st.setBackgroundResource(R.drawable.shape_round_tv_status_yellow);
        }
    }


    public void setEventDateText(String text) {
        eventDate.setText(text);
    }

    /*public void setEventJoin(int join) {
        eventJoin.setText(join);
    }*/
}
