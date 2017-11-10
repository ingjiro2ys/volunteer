package com.example.user.volunteer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.volunteer.dao.Noti;
import com.example.user.volunteer.dao.NotiAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

import static com.example.user.volunteer.R.id.swipeRefreshLayout;

public class NotificationActivity extends AppCompatActivity {
    String url,userID,clickedUser;
    private final String URL = "http://10.4.56.14:82/";
    private List<Noti> noti;
    private ArrayAdapter<Noti> notiArrayAdapter;
    ListView listView;
    ScrollView scrollView;
    TextView tv_noti,tv_eventID,tv_regisID,tv_isJoined,tv_clicked;
    String listAmount="9";
    SharedPreferences sp2;
    SharedPreferences.Editor editor;
    final String LIST = "LIST";
    Timer repeatTask = new Timer();
    int repeatInterval = 5000;
    String readCode;
    RequestQueue requestQueue;
    String URLRead="http://10.4.56.14:82/insert_click_notification.php";
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        SharedPreferences sp = getSharedPreferences("USER", Context.MODE_PRIVATE);
        userID = sp.getString("userID","");

        // Toolbar Back
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView)findViewById(R.id.listNoti);
        scrollView = (ScrollView)findViewById(R.id.scrollViewShowNoti);
        //tv_noti = (TextView)findViewById(R.id.tv_noti);

        url="http://10.4.56.14:82/get_isJoined_count.php/?userID="+userID;
        initInstance();
        //countRepeat();

        //////// Refresh data
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initInstance();
                notiArrayAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    public void countRepeat() {
        repeatTask.schedule(new TimerTask() {
            @Override
            public void run() {
                //Log.d("repeat","hello");
                //Toast.makeText(getBaseContext(),"repeat",Toast.LENGTH_SHORT).show();
                countNoti();
            }
        }, 0, repeatInterval);

    }

    private void initInstance() {
        // add
        Map<String, String> map = new HashMap<>();
        map.put("userID", userID);
        //Initialize regis user's List
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofit.create(NotiService.class).eventNameNoti(map).enqueue(new Callback<List<Noti>>() {
            @Override
            public void onResponse(Call<List<Noti>> call, Response<List<Noti>> response) {
                Log.d("onResponse", "Called");
                noti = response.body();
                Log.d("onResponse", noti.toString());
                notiArrayAdapter = new NotiAdapter(NotificationActivity.this, R.layout.list_notification, noti);
                listView.setAdapter(notiArrayAdapter);
                //listAmount = listView.getAdapter().getCount();
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        View po = listView.getChildAt(i);
                        tv_noti = (TextView) po.findViewById(R.id.tv_noti);
                        tv_eventID = (TextView) po.findViewById(R.id.tv_eventID);
                        tv_regisID = (TextView) po.findViewById(R.id.tv_regisID);
                        tv_isJoined = (TextView) po.findViewById(R.id.tv_isJoined);
                        tv_clicked = (TextView) po.findViewById(R.id.tv_clicked);

                        final String regisID = tv_regisID.getText().toString();


                        if(!tv_noti.getTypeface().isBold()){ //default
                            readCode="1"; //already click
                            //Toast.makeText(getBaseContext(),"Default", Toast.LENGTH_SHORT).show();
                        }else{ //not click yet
                            //Toast.makeText(getBaseContext(),"Bold", Toast.LENGTH_SHORT).show();
                            tv_noti.setTypeface(Typeface.DEFAULT);
                            //Toast.makeText(getBaseContext(),"read", Toast.LENGTH_SHORT).show();
                            //connect to DB
                            requestQueue = Volley.newRequestQueue(NotificationActivity.this);
                            StringRequest request = new StringRequest(Request.Method.POST, URLRead, new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("onResponse", response);
                                    Toast.makeText(getBaseContext(), "done", Toast.LENGTH_SHORT).show();

                                }
                            }, new com.android.volley.Response.ErrorListener() {
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
                                    params.put("regisID", regisID);
                                    return params;
                                }
                            };
                            requestQueue.add(request);
                        }

                        Intent in = new Intent(NotificationActivity.this,Detail2Activity.class);
                        in.putExtra("eventID",tv_eventID.getText().toString());
                        startActivity(in);

                    }
                });

            }

            @Override
            public void onFailure(Call<List<Noti>> call, Throwable t) {
                Log.d("onFailure", "Called");
                Log.d("onFailure", call.toString());
                Log.d("onFailure", t.getMessage());
                Log.d("onFailure", t.toString());
            }
        });
        // end
        //listAmount = listView.getAdapter().getCount();
        //Toast.makeText(getBaseContext(),"item amount : "+listAmount,Toast.LENGTH_SHORT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    interface NotiService {
        @GET("show_eventName_notification.php")
        Call<List<Noti>> eventNameNoti(@QueryMap Map<String, String> map);
    }

    public void countNoti(){
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
                    JSONArray data = jsonResult.getJSONArray("Count");
                    for(int p =0; p < data.length(); p++ ){
                        JSONObject productObject = data.optJSONObject(p);
                        output = productObject.optString("count(1)");
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
            //Toast.makeText(getBaseContext(),"output: "+s,Toast.LENGTH_SHORT).show();
            listAmount=s;
        }
    }

    public String getListAmount() {
        return listAmount;
    }

}
