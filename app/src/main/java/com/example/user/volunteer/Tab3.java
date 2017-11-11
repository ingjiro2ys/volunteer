package com.example.user.volunteer;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.volunteer.dao.PhotoItemCollectionDao;
import com.example.user.volunteer.dao.PhotoItemDao;
import com.example.user.volunteer.manager.HttpManager;
import com.example.user.volunteer.manager.PhotoListManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Tab3 extends Fragment {
    private static final String TAG = "Tab3Fragment";

    private Button btnTEST;
    Spinner filterspinner,filterAbility;
    ArrayAdapter<String> adapter;
    InputStream is = null;
    String line = null;
    String result = null;
    String[] data;

    String eventTypeID;
    String selectedItem;
    Button findButton,resetButton;
    ListView findResult;
    PhotoListAdapter findAdapter;
    PhotoListManager photoListManager = PhotoListManager.getInstance();
    SwipeRefreshLayout swipeRefreshLayout;

    View view;
    String userID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab3_fragment,container,false);
//        btnTEST = (Button) view.findViewById(R.id.btnTEST);
//
//        btnTEST.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getActivity(), "TESTING BUTTON CLICK 1",Toast.LENGTH_SHORT).show();
//            }
//        });

        filterspinner = (Spinner)view.findViewById(R.id.filterspinner);


        filterspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = filterspinner.getSelectedItem().toString();
                if(selectedItem.equals("เลือก")){
                    eventTypeID="0";
                }else if(selectedItem.equals("กิจกรรมกับเด็ก")){
                    eventTypeID="1";
                    //Toast.makeText(getBaseContext(),eventTypeID,Toast.LENGTH_SHORT).show();
                }else if (selectedItem.equals("ช่วยเหลือผู้ป่วย/ผู้พิการ/คนชรา")){
                    eventTypeID="2";
                    //Toast.makeText(getBaseContext(),eventTypeID,Toast.LENGTH_SHORT).show();
                }else if (selectedItem.equals("ครูอาสา")){
                    eventTypeID="3";
                    //Toast.makeText(getBaseContext(),eventTypeID,Toast.LENGTH_SHORT).show();
                }else if (selectedItem.equals("ค่ายอนุรักษ์/สิ่งแวดล้อม")){
                    eventTypeID="4";
                    //Toast.makeText(getBaseContext(),eventTypeID,Toast.LENGTH_SHORT).show();
                }else if (selectedItem.equals("ก่อสร้าง/งานช่าง")){
                    eventTypeID="5";
                    //Toast.makeText(getBaseContext(),eventTypeID,Toast.LENGTH_SHORT).show();
                }else if (selectedItem.equals("งานฝีมือ/ดนตรี/ศิลปะ")){
                    eventTypeID="6";
                    //Toast.makeText(getBaseContext(),eventTypeID,Toast.LENGTH_SHORT).show();
                }else if (selectedItem.equals("ช่วยเหลือสัตว์")){
                    eventTypeID="7";
                    //Toast.makeText(getBaseContext(),eventTypeID,Toast.LENGTH_SHORT).show();
                }else if (selectedItem.equals("ศาสนา/ปฏิบัติธรรม")){
                    eventTypeID="8";
                    //Toast.makeText(getBaseContext(),eventTypeID,Toast.LENGTH_SHORT).show();
                }else if (selectedItem.equals("ทำความสะอาด")){
                    eventTypeID="9";
                    //Toast.makeText(getBaseContext(),eventTypeID,Toast.LENGTH_SHORT).show();
                }else if (selectedItem.equals("ไอที/คอมพิวเตอร์")){
                    eventTypeID="10";
                    //Toast.makeText(getBaseContext(),eventTypeID,Toast.LENGTH_SHORT).show();
                }else if (selectedItem.equals("ช่วยเหลือผู้ประสบภัย")){
                    eventTypeID="11";
                    //Toast.makeText(getBaseContext(),eventTypeID,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        showDialogOnButtonClick();





//////////////////////////////////////////////////////////////////////////////////////////
        findButton = (Button)view.findViewById(R.id.findButton);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eventTypeID=="0"&&startDate=="เลือก"){
                    Toast.makeText(getActivity(),"กรุณาเลือกเพื่อคัดกรอง",Toast.LENGTH_SHORT).show();
                }else {
                    initInstance();
                }
            }
        });


        resetButton = (Button)view.findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFilter();
            }
        });




        return view;
    }


    private void resetFilter() {
        filterspinner.setSelection(0);
        startDate = "เลือก";
        startEvent.setText(startDate);
    }


    Button startEvent;


    public void showDialogOnButtonClick(){
        startEvent = (Button)view.findViewById(R.id.startEvent);
        startEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDatePickerDialog_start();
            }
        });

    }




    private void createDatePickerDialog_start(){
        Calendar calendar = Calendar.getInstance();
        int start_year = calendar.get(Calendar.YEAR);
        final int start_month = calendar.get(Calendar.MONTH);
        int start_day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                onDatePickerDialogSet_start(year,month,dayOfMonth);
            }
        },start_year,start_month,start_day);
        dialog.show();
    }

    String str;
    String startDate="เลือก";
    SimpleDateFormat sdf;


    private void onDatePickerDialogSet_start(int year, int month, int day)  {
        String str = day + "/" + (month + 1) + "/" + year;
        startDate = year + "-" + (month + 1) + "-" + day;
        startEvent.setText(startDate);
    }


    private void initInstance() {

        findResult = (ListView)view.findViewById(R.id.findResult);
        findAdapter = new PhotoListAdapter();
        findResult.setAdapter(findAdapter);



        findResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO:ต้องให้ตัว dao รับเป็น Parcelable แล้วส่งไปหาอีก activity นึง
                PhotoItemDao dao = photoListManager.getDao().getEvents().get(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("dao", dao);
                //intent.putExtra("userID", userID);
                startActivity(intent);
                //Toast.makeText(getActivity(), dao.getEventID() + "Done", Toast.LENGTH_LONG).show();
            }
        });

        //////// Refresh data
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
            }
        });
        findResult.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view,
                                 int firstVisibleItem,
                                 int visibleItemCount,
                                 int totalItemCount) {
                swipeRefreshLayout.setEnabled(firstVisibleItem == 0);
            }
        });
        reloadData();
    }

    java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");

    Date d;
    String date;

    private void reloadData() {
        //add
        Map<String,String> map = new HashMap<>();


        if(!eventTypeID.equals("0")&&startDate=="เลือก"){

            map.put("eventTypeID", eventTypeID);

        }else if(!eventTypeID.equals("0")&&startDate!="เลือก"){

            map.put("eventTypeID", eventTypeID);
            map.put("startDate", startDate );

        }else if(eventTypeID.equals("0")&&startDate!="เลือก"){

            map.put("startDate",startDate );

        }

        //add
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance().getService().loadFilterTypeCloseEvent(map);
        call.enqueue(new Callback<PhotoItemCollectionDao>() {
            @Override
            public void onResponse(Call<PhotoItemCollectionDao> call,
                                   Response<PhotoItemCollectionDao> response) {
                swipeRefreshLayout.setRefreshing(false);
                if(response.isSuccessful()){
                    photoListManager.setDao(response.body());
                    findAdapter.setDao(photoListManager.getDao());
                    findAdapter.notifyDataSetChanged();
                    //ลบข้างบน
                    Toast.makeText(getActivity(),"Load Successful",Toast.LENGTH_SHORT).show();
                }else {
                    // Handle
                    try {
                        Toast.makeText(getActivity(),
                                response.errorBody().string(),
                                Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PhotoItemCollectionDao> call,
                                  Throwable t) {
                // Handle Fail
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(),
                        t.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}