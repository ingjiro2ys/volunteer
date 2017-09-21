package com.example.user.volunteer;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreateEventActivity extends AppCompatActivity {

    Button btn_startEvent;
    Button btn_endEvent;
    Button btn_openRegis, btn_closeRegis;
    EditText et_eventName;
    EditText et_regisAvailable;
    private GoogleApiClient mGoogleApi;
    EditText et_locationName;
    Button btn_submit;
    Double lat;
    Double lng;
    Button btn_chooseImg;
    ImageView imgv_show;
    private static final int PICK_IMAGE =100;
    Uri imageUri;
    Bitmap bitmap;
    EditText et_desc;
    EditText et_telNo;
    // TODO: แก้ path insertEventDetail.php ไป
    private static final String URL = "http://10.4.56.14/insertEventDetail.php";
    String eventName;
    String eventDes1;
    String joinedAmount; //regisAvailable
    String selectedItem;
    String eventTypeID;
    String eventLocationName;
    String startDate=null;
    String endDate=null;
    String startRegis=null;
    String endRegis=null;
    String eventPhone;
    String eventID="null";
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date todayDate = new Date();
    Date sdate;
    Date edate;
    Date odate;
    Date cdate;
    String userID;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        //TODO: add sharePrefer
        SharedPreferences sp = getSharedPreferences("USER", Context.MODE_PRIVATE);
        userID = sp.getString("userID","");

        //view
        init();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //handle calendar button
        showDialogOnButtonClick();

        //connect to Location Service
        /*mGoogleApi = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(mCallbacks)
                .addOnConnectionFailedListener(mOnFailed)
                .build();*/

        //handle location name
        //forwordGeocoding();

        //handle chooseImg click
        chooseImage();

        //submit
        submitClick();

        //Toast.makeText(getBaseContext(),todayDate+"+",Toast.LENGTH_SHORT).show();

    }

    public void submitClick() {
        btn_submit = (Button)findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                eventDes1 = et_desc.getText().toString();
                eventName = et_eventName.getText().toString();
                joinedAmount = et_regisAvailable.getText().toString();
                eventPhone = et_telNo.getText().toString();
                findCoordinate();

                if(saveData()){
                    Intent intent = new Intent(getBaseContext(),CreateQuestionActivity.class);
                    /*intent.putExtra("lat",getLat());
                    intent.putExtra("lng",getLng());*/
                    startActivity(intent);
                }
            }
        });

    }

    public boolean saveData() {
        // Dialog
        final AlertDialog.Builder ad = new AlertDialog.Builder(this);

        //check event name
        if (et_eventName.getText().length() == 0) {
            ad.setMessage("กรุณาระบุชื่อกิจกรรม");
            ad.show();
            et_eventName.requestFocus();
            return false;
        }

        //check date
        if(startDate==null){
            ad.setMessage("กรุณาระบุวันเเริ่มกิจกรรม");
            ad.show();
            //btn_startEvent.requestFocus();
            return false;
        }
        if(endDate==null){
            ad.setMessage("กรุณาระบุวันสิ้นสุดกิจกรรม");
            ad.show();
            //btn_endEvent.requestFocus();
            return false;
        }
        if(startRegis==null){
            ad.setMessage("กรุณาระบุวันเปิดรับสมัคร");
            ad.show();
            //btn_closeRegis.requestFocus();
            return false;
        }
        if(endRegis==null){
            ad.setMessage("กรุณาระบุวันปิดรับสมัคร");
            ad.show();
            //btn_closeRegis.requestFocus();
            return false;
        }

        //check regis Available
        if (et_regisAvailable.getText().length() == 0 || et_regisAvailable.getText().equals("0")) {
            ad.setMessage("กรุณาระบุจำนวนคนที่เปิดรับสมัคร");
            ad.show();
            et_regisAvailable.requestFocus();
            return false;
        }

        //check location
        if (et_locationName.getText().length() == 0) {
            ad.setMessage("กรุณาระบุสถานที่จัดกิจกรรม");
            ad.show();
            et_locationName.requestFocus();
            return false;
        } else if (getLat() == 0 || getLng() == 0) {
            ad.setMessage("กรุณาเปลี่ยนชื่อสถานที่จัดกิจกรรม");
            ad.show();
            et_locationName.requestFocus();
            return false;
        }

        /*//check date
        if(!startDate.isEmpty() && !endDate.isEmpty() ){
            try {
                sdate = formatter.parse(startDate);
                edate = formatter.parse(endDate);
                Toast.makeText(getBaseContext(),sdate+" / "+edate,Toast.LENGTH_LONG).show();
                if(sdate.before(edate))
                {
                    //return true;
                }
                else if(sdate.equals(edate))
                {
                    //return true;//If two dates are equal
                }
                else if(edate.before(sdate))
                {
                    ad.setMessage("กรุณาระบุวันจัดกิจกรรมอีกครั้ง");
                    ad.show();
                    return false; //If start date is after the end date
                }else if(sdate.before(todayDate) || edate.before(todayDate)){
                    ad.setMessage("กรุณาระบุวันจัดกิจกรรมอีกครั้ง");
                    ad.show();
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //check date
        if(!startRegis.isEmpty() && !endRegis.isEmpty() ){
            try {
                odate = formatter.parse(startRegis);
                cdate = formatter.parse(endRegis);
                if(odate.before(cdate))
                {
                    //return true;
                }
                else if(odate.equals(cdate))
                {
                    //return true;//If two dates are equal
                }
                else if(cdate.before(odate))
                {
                    ad.setMessage("กรุณาระบุวันรับสมัครอีกครั้ง");
                    ad.show();
                    return false; //If start date is after the end date
                }else if(odate.before(todayDate) || cdate.before(todayDate)){
                    ad.setMessage("กรุณาระบุวันจัดกิจกรรมอีกครั้ง");
                    ad.show();
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }*/

        //check date
        if(!startDate.isEmpty() && !endDate.isEmpty() &&!startRegis.isEmpty() && !endRegis.isEmpty() ){
            try {
                Date sdate = formatter.parse(startDate);
                Date edate = formatter.parse(endDate);
                Date odate = formatter.parse(startRegis);
                Date cdate = formatter.parse(endRegis);

                if(sdate.before(todayDate)){
                    ad.setMessage("กรุณาระบุวันเริ่มจัดกิจกรรมอีกครั้ง");
                    ad.show();
                    return false;
                }else if(edate.before(todayDate)){
                    ad.setMessage("กรุณาระบุวันสิ้นสุดกิจกรรมอีกครั้ง");
                    ad.show();
                    return false;
                }else if(odate.before(todayDate)){
                    ad.setMessage("กรุณาระบุวันเปิดรับสมัครอีกครั้ง");
                    ad.show();
                    return false;
                }else if(cdate.before(todayDate)){
                    ad.setMessage("กรุณาระบุวันปิดรับสมัครอีกครั้ง");
                    ad.show();
                    return false;
                } else if(sdate.after(todayDate) && edate.after(todayDate) && odate.after(todayDate) && cdate.after(todayDate)){
                    if(sdate.before(edate)){ //วันจบกิจกรรมอยู่หลังวันเริ่ม ถูก
                        if(odate.before(cdate)){ //วันปิดรับสมัครอยู่หลังวันเปิด ถูก
                            if(odate.before(sdate)){ //รับสมัครก่อนทำกิจกรรม ถูก

                            }else if(odate.after(sdate)){
                                ad.setMessage("กรุณาระบุวันเปิดรับสมัครหลังวันเริ่มกิจกรรม"); //รับสมัครหลังวันจัดกิจกรรม ผิด
                                ad.show();
                                return false;
                            }
                        }else if(cdate.before(odate)){ //วันปิดรับสมัครอยู่ก่อนวันเปิดรับสมัคร ผิด
                            ad.setMessage("กรุณาระบุวันปิดรับสมัครหลังวันเปิดรับสมัคร");
                            ad.show();
                            return false;
                        }
                    }else if(edate.before(sdate)) { //วันจบกิจกรรมอยู่ก่อนวันเริ่มกิจกรรม ผืด
                        ad.setMessage("กรุณาระบุวันเริ่มต้นกิจกรรมก่อนวันจบกิจกรรม");
                        ad.show();
                        return false;
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //check event tel
        if(!eventPhone.isEmpty()){
            if(eventPhone.length()<9 || eventPhone.length()>10){
                ad.setMessage("กรุณาระบุหมายเลขโทรศัพท์อีกครั้ง");
                ad.show();
                et_telNo.setText("");
                et_telNo.requestFocus();
                return false;
            }
        }

        //set empty eventDes1 field to null
        if(eventDes1.isEmpty()) {
            eventDes1="";
        }

        //set empty eventPhone field to null
        if(eventPhone.isEmpty()) {
            eventPhone="";
        }




        Toast.makeText(getBaseContext(),"ID: "+eventID+"\neventName:"+eventName+"\neventTypeID:"+eventTypeID+"\nstartDate"+startDate
                +"\nendDate: "+endDate+"\njoinedAmount: "+joinedAmount+"\nstartRegis: "+startRegis+"\nendRegis:"
                +endRegis+"\neventPhone: "+eventPhone+"\neventDes1: "+eventDes1+"\neventLocationName: "+eventLocationName+"\nlat: "+Double.toString(getLat())+"\nlng: "
                +Double.toString(getLng()),Toast.LENGTH_LONG).show();

        if(!eventName.isEmpty()&& !joinedAmount.isEmpty() && !eventLocationName.isEmpty()){
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("onResponse", response);
                    Toast.makeText(getBaseContext(), "done", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("onError", error.toString()+"\n"+error.networkResponse.statusCode
                            +"\n"+error.networkResponse.data+"\n"+error.getMessage());
                    Toast.makeText(getBaseContext(), "error", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    String imageData = imageToString(bitmap);

                    params.put("eventID", eventID);
                    params.put("eventName", eventName);
                    params.put("eventTypeID", eventTypeID);
                    params.put("startDate", startDate);
                    params.put("endDate", endDate);
                    params.put("joinedAmount", joinedAmount);
                    params.put("startRegis", startRegis);
                    params.put("endRegis", endRegis);
                    params.put("eventPhone", eventPhone);
                    params.put("eventLocationName", eventLocationName);
                    params.put("eventDes1", eventDes1);
                    params.put("lat", Double.toString(getLat()));
                    params.put("lng", Double.toString(getLng()));
                    params.put("image",imageData);
                    params.put("userID",userID);


                    return params;
                }
            };
            requestQueue.add(request);
        }

        return true;

    }
    //image manage
    public void chooseImage() {
        imgv_show = (ImageView)findViewById(R.id.imgv_show);
        btn_chooseImg = (Button)findViewById(R.id.btn_chooseImg);
        btn_chooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            //imgv_show.setImageURI(imageUri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imgv_show.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageByte = outputStream.toByteArray();
        String encodeImage = Base64.encodeToString(imageByte, Base64.DEFAULT);
        return encodeImage;
    }

    public void init(){
        et_eventName = (EditText)findViewById(R.id.et_eventName);
        final Spinner spinner_eventType = (Spinner)findViewById(R.id.spinner_eventType);
        et_regisAvailable = (EditText)findViewById(R.id.et_regisAvailable);
        et_desc = (EditText)findViewById(R.id.et_desc);
        et_telNo = (EditText)findViewById(R.id.et_telNo);

        /*eventDes1 = et_desc.getText().toString();
        eventName = et_eventName.getText().toString();
        joinedAmount = et_regisAvailable.getText().toString();*/


        /*joinedAmount = 0; // Integer.parseInt(et_regisAvailable.toString());
        try {
            joinedAmount = Integer.parseInt(et_regisAvailable.getText().toString());
        } catch(NumberFormatException nfe) {}*/

        spinner_eventType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = spinner_eventType.getSelectedItem().toString();
                if(selectedItem.equals("กิจกรรมกับเด็ก")){
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
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
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
        startDate = year+"-"+(month+1)+"-"+day;
        btn_startEvent.setText(str);
        Toast.makeText(getBaseContext(),str,Toast.LENGTH_SHORT).show();
    }

    private void onDatePickerDialogSet_end(int year, int month, int day){
        String str = day+"/"+(month+1)+"/"+year;
        btn_endEvent.setText(str);
        endDate = year+"-"+(month+1)+"-"+day;
        Toast.makeText(getBaseContext(),str,Toast.LENGTH_SHORT).show();
    }

    private void onDatePickerDialogSet_open(int year, int month, int day){
        String str = day+"/"+(month+1)+"/"+year;
        btn_openRegis.setText(str);
        startRegis = year+"-"+(month+1)+"-"+day;
        Toast.makeText(getBaseContext(),str,Toast.LENGTH_SHORT).show();
    }

    private void onDatePickerDialogSet_close(int year, int month, int day){
        String str = day+"/"+(month+1)+"/"+year;
        btn_closeRegis.setText(str);
        endRegis = year+"-"+(month+1)+"-"+day;
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
    /*private GoogleApiClient.ConnectionCallbacks mCallbacks =
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
            };*/

    /*private void forwordGeocoding(){
        et_locationName = (EditText)findViewById(R.id.et_locationName);
        btn_submit = (Button)findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findCoordinate();
                Intent intent = new Intent(getBaseContext(),Main2Activity.class);
                intent.putExtra("lat",getLat());
                intent.putExtra("lng",getLng());
                startActivity(intent);
            }
        });

    }*/

    //convert name of location to lat,long
    private void findCoordinate(){
        et_locationName = (EditText)findViewById(R.id.et_locationName);
        String place = et_locationName.getText().toString();
        eventLocationName = place;
        Geocoder geocoder = new Geocoder(getBaseContext());
        try{
            List<Address> addr = geocoder.getFromLocationName(place,1);
            setLat(addr.get(0).getLatitude());
            setLng(addr.get(0).getLongitude());
            String str = "";
            str += "Latitude: "+addr.get(0).getLatitude()+"\n"
                    +"Longitude: "+addr.get(0).getLongitude();
            //Toast.makeText(getBaseContext(),str,Toast.LENGTH_LONG).show();

        }catch (Exception ex){

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    /*@Override
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
    }*/
}
