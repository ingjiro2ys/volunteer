package com.example.user.volunteer;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.user.volunteer.dao.Detail2;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public class Detail2Activity extends AppCompatActivity {

    ImageView ivImg;
    TextView tvName,tvDesc,eventDate,eventJoin,eventPhone,regisDate, eventLocation;
    Button commentBtn,joinEventBth;
    CheckBox favoriteBtn;
    /*String start_date;
    String pattern = "yyyy-MM-dd";
    SimpleDateFormat format = new SimpleDateFormat(pattern);
    Date date;*/

    private MapFragment mMapFragment;
    Double dmlat,lat;
    Double dmlng,lng;

    String eventName,eventLocationName;
    String imagePath;

    // TODO: เอาลิ้งไปแปะหน้า answer แทน
    private static final String URL = "http://10.4.56.14:82/";
    private static final String URLFav = "http://10.4.56.14:82/insertFav.php";
    private static final String URLDelete = "http://10.4.56.14:82/delete.php";


    private static final String URLReport = "http://10.4.56.14:82/insertReportEvent.php";

    String eventID;
    String userID;
    String url;

    Date date;
    String startDate, endDate;
    String startRegis, endRegis;
    String endRegisDate, todayDate, startRegisDate;
    String favCode, joinedAmount;
    //TODO:ADD
    int userOwnerID;

    //add
    String descriptReport = "";

    RequestQueue requestQueue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail2);

        eventID = getIntent().getStringExtra("eventID");
        //Toast.makeText(getBaseContext(),"event id "+eventID, Toast.LENGTH_SHORT).show();
        getDetail();

        // ต้องรับ Parcelable มาจาก Main แล้วแสดง
        //TODO: add sharePrefer
        SharedPreferences sp = getSharedPreferences("USER", Context.MODE_PRIVATE);
        userID = sp.getString("userID","");
        //userID = getIntent().getStringExtra("userID");
        //Toast.makeText(getBaseContext(),userID, Toast.LENGTH_SHORT).show();


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // findViewByid ให้หมดแล้วค่อยมา setText เองแม่งเลย
        ivImg = (ImageView) findViewById(R.id.ivImg);
        tvDesc = (TextView) findViewById(R.id.tvDesc);
        tvName = (TextView) findViewById(R.id.tvName);
        eventDate = (TextView) findViewById(R.id.eventDate);
        eventJoin = (TextView) findViewById(R.id.eventJoin);
        eventPhone = (TextView) findViewById(R.id.eventPhone);
        joinEventBth = (Button) findViewById(R.id.joinEventBth);
        regisDate = (TextView) findViewById(R.id.regisDate);
        eventLocation = (TextView) findViewById(R.id.eventLocation);
        /*start_date = dao.getStartDate().toString();
        Toast.makeText(getBaseContext(),start_date,Toast.LENGTH_LONG).show();
        try {
            date = format.parse(start_date);

        } catch (ParseException e) {
            e.printStackTrace();
        }*/


        //Toast.makeText(DetailActivity.this,dao.getLat()+" , "+dao.getLng(),Toast.LENGTH_LONG).show();

        //Toast.makeText(getBaseContext(),""+lat+"  "+lng+"  "+imagePath, Toast.LENGTH_LONG).show();
        // MAP *******
        FragmentManager fragman = getFragmentManager();
        mMapFragment = (MapFragment)fragman.findFragmentById(R.id.map_frag);
        mMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                dmlat = lat;
                dmlng = lng;
                LatLng pos = new LatLng(dmlat,dmlng);
                MarkerOptions marker = new MarkerOptions()
                        .position(pos)
                        .title(eventLocationName)
                        .snippet("Des");
                googleMap.addMarker(marker);

                CameraPosition cameraPos = CameraPosition.builder()
                        .target(pos)
                        .zoom(16)
                        .tilt(0)
                        .build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPos));
            }
        });

        //TODO: add
        //eventID = dao.getEventID();
        //Toast.makeText(Detail2Activity.this,"event id: "+eventID, Toast.LENGTH_SHORT).show();

        requestQueue = Volley.newRequestQueue(this);
        url = "http://10.4.56.14:82/checkFav.php/?query=SELECT%20*%20FROM%20favoriteEvent%20where%20eventID="+eventID+"%20AND%20userID%20="+userID;
        //jsonReq();
        //postFav();
        favoriteBtn = (CheckBox) findViewById(R.id.favoriteBtn);
        //favoriteBtn.setButtonDrawable(R.drawable.favoritebtn);
        getFav();

        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favoriteBtn.isChecked()){
                    favCode = "1";
                    StringRequest request = new StringRequest(Request.Method.POST, URLFav, new Response.Listener<String>() {
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
                            params.put("eventID", eventID);
                            params.put("userID", userID);
                            params.put("favCode", favCode);


                            return params;
                        }
                    };
                    requestQueue.add(request);
                }else if(!favoriteBtn.isChecked()){
                    favCode = "0";
                    StringRequest request = new StringRequest(Request.Method.POST, URLFav, new Response.Listener<String>() {
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
                            params.put("eventID", (String.valueOf(eventID)));
                            params.put("userID", userID);
                            params.put("favCode", favCode);


                            return params;
                        }
                    };
                    requestQueue.add(request);
                }
            }
        });

        commentBtn = (Button)findViewById(R.id.commentBtn);


        //gestureObject = new GestureDetectorCompat(this , new LearnGesture());
        // learngesture class file


    }

    private void getDetail() {

        Map<String, String> map = new HashMap<>();
        map.put("eventID", eventID);
        //Initialize
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofit.create(Service.class).event(map).enqueue(new Callback<Detail2>() {
            @Override
            public void onResponse(Call<Detail2> call, retrofit2.Response<Detail2> response) {
                Log.d("onResponse", "Called");
                Detail2 detail = response.body();

                // change date format
                SimpleDateFormat fm = new SimpleDateFormat("EEE, d MMM yyyy");
                startDate = fm.format(detail.getStartDate());
                endDate = fm.format(detail.getEndDate());
                endRegisDate = fm.format(detail.getEndRegis());
                startRegisDate = fm.format(detail.getStartRegis());
                startRegis = fm.format(detail.getStartRegis());
                endRegis = fm.format(detail.getEndRegis());
                eventName = detail.getEventName();
                imagePath = detail.getImagePath();
                lat = detail.getLat();
                lng = detail.getLng();
                eventLocationName = detail.getEventLocationName();
                tvName.setText(detail.getEventName());
                tvDesc.setText(detail.getEventDes1()+ " \n ");
                Glide.with(Detail2Activity.this)
                        .load(imagePath)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivImg);
                //eventDate.setText(dao.getStartDate()+" - "+dao.getEndDate());
                eventDate.setText(startDate+" - "+endDate);
                regisDate.setText(startRegis+" - "+endRegis);
                eventJoin.setText(detail.getJoinedAmount()+" คน");
                eventPhone.setText(detail.getEventPhone());
                eventLocation.setText(detail.getEventLocationName());
                //TODO:ADD
                userOwnerID = detail.getUserOwnerID();
                joinedAmount = (String.valueOf(detail.getJoinedAmount()));
                //add





            }

            @Override
            public void onFailure(Call<Detail2> call, Throwable t) {
                Log.d("onFailure", "Called");
                Log.d("onFailure", call.toString());
                Log.d("onFailure", t.getMessage());
                Log.d("onFailure", t.toString());
            }
        });

    }
    interface Service {
        @GET("show_events_detail2.php")
        Call<Detail2> event(@QueryMap Map<String, String> map);
    }

    public void getFav(){
        new MyAsyncTask().execute(url,"parse");
    }

    class MyAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try{
                java.net.URL url = new URL(params[0]);
                HttpURLConnection httpCon = (HttpURLConnection)url.openConnection();
                httpCon.setDoInput(true);
                httpCon.connect();

                InputStream inStream = httpCon.getInputStream();
                Scanner scanner = new Scanner(inStream, "Windows-874");
                response = scanner.useDelimiter("\\A").next();
            }catch (Exception ex){}

            if(params[1].equals("show")){
                return response;

            }else if(params[1].equals("parse")){
                String output = "";
                try{
                    JSONObject jsonResult = new JSONObject(response);
                    JSONArray data = jsonResult.getJSONArray("Fav");
                    for(int p =0; p < data.length(); p++ ){
                        JSONObject productObject = data.optJSONObject(p);
                        output = productObject.optString("chooseFavorite");
                        /*output += "* eventID "+productObject.optString("eventID");
                        output += " - userID "+productObject.optString("userID");
                        output += " - FavCode "+productObject.optString("chooseFavorite")+"\n";*/
                    }
                    //output += "\n";
                } catch (JSONException e) {}
                return output;
            }else{
                return "";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            //Toast.makeText(getBaseContext(),"output: "+s, Toast.LENGTH_SHORT).show();
            //favCode = s;
            if(s.equals("1")){
                favoriteBtn.setChecked(true);
            }else{
                favoriteBtn.setChecked(false);
            }
        }
    }


    MenuItem report;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        MenuItem menuItem = (MenuItem) menu.findItem(R.id.action_share);
        ShareActionProvider shareActionProvider = (ShareActionProvider)
                MenuItemCompat.getActionProvider(menuItem);
        shareActionProvider.setShareIntent(getShareIntent());
        //manage
        MenuItem manage = (MenuItem) menu.findItem(R.id.action_manage);
        if(userID.equals(""+userOwnerID)) {
            manage.setVisible(true);
            manage.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent in = new Intent(Detail2Activity.this, ShowUserRegisActivity.class);
                    in.putExtra("eventName",eventName);
                    in.putExtra("eventID", (String.valueOf(eventID)));
                    in.putExtra("joinedAmount", joinedAmount);
                    startActivity(in);
                    return true;
                }
            });
        }
        // edit
        /*MenuItem edit = (MenuItem) menu.findItem(R.id.action_edit);
        if(userID.equals(""+userOwnerID)){
            edit.setVisible(true);
            edit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent in = new Intent(Detail2Activity.this,EditEventActivity.class);
                    //in.putExtra("dao",dao);

                    startActivity(in);
                    finish();
                    return true;
                }
            });
        }*/
        // delete
        /*MenuItem delete = (MenuItem) menu.findItem(R.id.action_delete);
        if(userID.equals(""+userOwnerID)){
            delete.setVisible(true);
            delete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    new AlertDialog.Builder(Detail2Activity.this)
                            //final AlertDialog alertDialog = builder.create();
                            .setTitle("ยืนยันการลบ")
                            .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteMethod();
                                    Intent in = new Intent(getBaseContext(), MainActivity.class);
                                    startActivity(in);
                                    Toast.makeText(getBaseContext(), String.valueOf(eventID), Toast.LENGTH_SHORT).show();
                                    finish();

                                }
                            }).setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                    return true;
                }
            });
        }*/

        //TODO::addReport menu
        report = (MenuItem) menu.findItem(R.id.action_report);
        report.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Dialog
                final AlertDialog.Builder ad = new AlertDialog.Builder(Detail2Activity.this);
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.dialog_custom, null);
                ad.setView(view);
                ad.setMessage("เขียนข้อความเพื่อการรายงาน");
                final EditText reportText = (EditText) view.findViewById(R.id.report);
                ad.setPositiveButton("รายงาน", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(reportText!=null){
                            if(reportText.length()<200){
                                descriptReport = reportText.getText().toString();
                                // send data
                                clickReport();
                                //Toast.makeText(getApplicationContext(), "ส่งสำเร็จ",Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(getApplicationContext(), "กรุณากรอกข้อความเพื่อรายงาน", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                ad.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                ad.show();
                return true;
            }
        });
        return true;
    }

    private void clickReport() {
        //TODO:Add request
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest request = new StringRequest(Request.Method.POST, URLReport, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse", response);
                Toast.makeText(getBaseContext(), "รายงานสำเร็จ", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("onError", error.toString()+"\n"+error.networkResponse.statusCode
                        +"\n"+error.networkResponse.data+"\n"+error.getMessage());
                Toast.makeText(getBaseContext(), "Error!", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("eventID", String.valueOf(eventID));
                params.put("descriptReport",descriptReport);
                params.put("userID",userID);

                return params;
            }
        };
        requestQueue.add(request);
    }

    private void deleteMethod() {
        RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
        StringRequest request = new StringRequest(Request.Method.POST, URLDelete+"?eventID="+eventID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse", response);
                Toast.makeText(getBaseContext(), "Deleted !", Toast.LENGTH_SHORT).show();
                //Toast.makeText(getBaseContext()," "+r+" "+questionName,Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("onError", error.toString()+"\n"+error.networkResponse.statusCode
                        +"\n"+error.networkResponse.data+"\n"+error.getMessage());
                Toast.makeText(getBaseContext(), "error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("eventID", String.valueOf(eventID));
                Log.d("params :: ", params.toString());
                //params.put("userOwnerID",userOwnerID + "");
                return params;
            }
        };
        requestQueue.add(request);
    }

    private Intent getShareIntent(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT,"Subject");
        intent.putExtra(Intent.EXTRA_TEXT, "Come to join '"+eventName+"' event in Volunteer for KMUTT Application");
        return intent;
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

    private void initInstances(){
    }

    public boolean onTouchEvent(MotionEvent event){
        //this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }



    class LearnGesture extends GestureDetector.SimpleOnGestureListener{
        //SimpleOnGestureListener is listen for what we want to do and how

        //now put the required code for gesture side by side


        @Override
        public boolean onFling(MotionEvent event1,
                               MotionEvent event2,
                               float velocityX,
                               float velocityY){

            if(event2.getX() > event1.getX()){

            /*here is the place to put the code
            what ever u want to do with gesture
            or left to right swipe*/


            }else
            if(event2.getX() < event1.getX()){
            }
            return true;

        }
    }
}
