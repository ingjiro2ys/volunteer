package com.example.user.volunteer;


import android.app.DatePickerDialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;
import java.util.List;


public class CreateEventActivity extends AppCompatActivity {

    Button btn_startEvent;
    Button btn_endEvent;
    Button btn_openRegis, btn_closeRegis;
    EditText et_eventName;
    EditText et_regisAvailable;
    private GoogleApiClient mGoogleApi;
    EditText et_locationName;
    Button btn_submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        //view
        init();

        //handle calendar button
        showDialogOnButtonClick();

        //connect to Location Service
        mGoogleApi = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(mCallbacks)
                .addOnConnectionFailedListener(mOnFailed)
                .build();

        //handle location name
        forwordGeocoding();



    }

    public void init(){
        et_eventName = (EditText)findViewById(R.id.et_eventName);
        final Spinner spinner_eventType = (Spinner)findViewById(R.id.spinner_eventType);
        et_regisAvailable = (EditText)findViewById(R.id.et_regisAvailable);

        String eventName = et_eventName.getText().toString();

        int regisAvailable = 0; // Integer.parseInt(et_regisAvailable.toString());
        try {
            regisAvailable = Integer.parseInt(et_regisAvailable.getText().toString());
        } catch(NumberFormatException nfe) {}

        spinner_eventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = spinner_eventType.getSelectedItem().toString();
                Toast.makeText(getBaseContext(),selectedItem,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

    //get last location
    private GoogleApiClient.ConnectionCallbacks mCallbacks =
            new GoogleApiClient.ConnectionCallbacks(){

                @Override
                public void onConnected(Bundle bundle) {
                    Location location = null;
                    try{
                        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApi);
                    }catch (SecurityException ex){}
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                }

                @Override
                public void onConnectionSuspended(int i) {

                }

            };

    private GoogleApiClient.OnConnectionFailedListener mOnFailed =
            new GoogleApiClient.OnConnectionFailedListener(){

                @Override
                public void onConnectionFailed(ConnectionResult result) {

                }
            };

    private void forwordGeocoding(){
        et_locationName = (EditText)findViewById(R.id.et_locationName);
        btn_submit = (Button)findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findCoordinate();
            }
        });

    }

    //convert name of location to lat,long
    private void findCoordinate(){
        String place = et_locationName.getText().toString();
        Geocoder geocoder = new Geocoder(getBaseContext());
        try{
            List<Address> addr = geocoder.getFromLocationName(place,1);
            String str = "";
            str += "Latitude: "+addr.get(0).getLatitude()+"\n"
                        +"Longitude: "+addr.get(0).getLongitude();
            Toast.makeText(getBaseContext(),str,Toast.LENGTH_LONG).show();

        }catch (Exception ex){

        }
    }

    @Override
    protected void onStart() {
        if(mGoogleApi != null){
            mGoogleApi.connect();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        if(mGoogleApi != null && mGoogleApi.isConnected()){
            mGoogleApi.disconnect();
        }
        super.onStop();
    }
}
