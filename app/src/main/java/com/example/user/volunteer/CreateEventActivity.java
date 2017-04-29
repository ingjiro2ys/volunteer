package com.example.user.volunteer;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;


public class CreateEventActivity extends AppCompatActivity {

    Button btn_startEvent;
    Button btn_endEvent;
    Button btn_openRegis, btn_closeRegis;
    EditText et_eventName;
    Spinner spinner_eventType;
    EditText et_regisAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        //view
        init();

        //handle calendar button
        showDialogOnButtonClick();

    }

    public void init(){
        et_eventName = (EditText)findViewById(R.id.et_eventName);
        spinner_eventType = (Spinner)findViewById(R.id.spinner_eventType);
        et_regisAvailable = (EditText)findViewById(R.id.et_regisAvailable);

        String eventName = et_eventName.toString();


        int regisAvailable = Integer.parseInt(et_regisAvailable.toString());
        //insert db code

        spinner_eventType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = spinner_eventType.getSelectedItem().toString();
                //insert DB code
            }
        });
    }

    private void createDatePickerDialog_start(){
        Calendar calendar = Calendar.getInstance();
        int start_year = calendar.get(Calendar.YEAR);
        final int start_month = calendar.get(Calendar.MONTH);
        int start_day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(CreateEventActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                onDatePickerDialogSet_start(year,month,dayOfMonth);
            }
        },start_year,start_month,start_day);
        dialog.show();
    }

    private void createDatePickerDialog_end(){
        Calendar calendar = Calendar.getInstance();
        int end_year = calendar.get(Calendar.YEAR);
        final int end_month = calendar.get(Calendar.MONTH);
        int end_day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(CreateEventActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                onDatePickerDialogSet_end(year,month,dayOfMonth);
            }
        },end_year,end_month,end_day);
        dialog.show();
    }

    private void createDatePickerDialog_open(){
        Calendar calendar = Calendar.getInstance();
        int open_year = calendar.get(Calendar.YEAR);
        final int open_month = calendar.get(Calendar.MONTH);
        int open_day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(CreateEventActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                onDatePickerDialogSet_open(year,month,dayOfMonth);
            }
        },open_year,open_month,open_day);
        dialog.show();
    }

    private void createDatePickerDialog_close(){
        Calendar calendar = Calendar.getInstance();
        int close_year = calendar.get(Calendar.YEAR);
        final int close_month = calendar.get(Calendar.MONTH);
        int close_day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(CreateEventActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                onDatePickerDialogSet_close(year,month,dayOfMonth);
            }
        },close_year,close_month,close_day);
        dialog.show();
    }

    private void onDatePickerDialogSet_start(int year, int month, int day){
        String str = day+"/"+(month+1)+"/"+year;
        btn_startEvent.setText(str);
        Toast.makeText(getBaseContext(),str,Toast.LENGTH_SHORT).show();
    }

    private void onDatePickerDialogSet_end(int year, int month, int day){
        String str = day+"/"+(month+1)+"/"+year;
        btn_endEvent.setText(str);
        Toast.makeText(getBaseContext(),str,Toast.LENGTH_SHORT).show();
    }

    private void onDatePickerDialogSet_open(int year, int month, int day){
        String str = day+"/"+(month+1)+"/"+year;
        btn_openRegis.setText(str);
        Toast.makeText(getBaseContext(),str,Toast.LENGTH_SHORT).show();
    }

    private void onDatePickerDialogSet_close(int year, int month, int day){
        String str = day+"/"+(month+1)+"/"+year;
        btn_closeRegis.setText(str);
        Toast.makeText(getBaseContext(),str,Toast.LENGTH_SHORT).show();
    }

    public void showDialogOnButtonClick(){
        btn_startEvent = (Button)findViewById(R.id.btn_startEvent);
        btn_startEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDatePickerDialog_start();
            }
        });

        btn_endEvent = (Button)findViewById(R.id.btn_endEvent);
        btn_endEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDatePickerDialog_end();
            }
        });

        btn_openRegis = (Button)findViewById(R.id.btn_openRegis);
        btn_openRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDatePickerDialog_open();
            }
        });

        btn_closeRegis = (Button)findViewById(R.id.btn_closeRegis);
        btn_closeRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDatePickerDialog_close();
            }
        });
    }

}
