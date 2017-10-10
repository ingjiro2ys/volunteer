package com.example.user.volunteer;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.volunteer.dao.UserRegis;
import com.example.user.volunteer.dao.UserRegisAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public class ShowUserRegisActivity extends AppCompatActivity {

    ListView listView;
    ScrollView scrollView;
    Button sendButton, checkButton;

    private List<UserRegis> userRegises;
    private ArrayAdapter<UserRegis> userRegisesArrayAdapter;

    private final String URL = "http://10.4.56.14/";
    private final String URL1 = "http://10.4.56.14/updateIsJoin.php";

    CheckBox checkBox;
    String[] userWhoPass = new String[300];
    int r = 0;
    int b, c = 0;
    String eventID, userID, isJoined, clickedUser, joinedAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_regis);

        SharedPreferences sp = getSharedPreferences("USER", Context.MODE_PRIVATE);
        userID = sp.getString("userID", "");

        eventID = getIntent().getStringExtra("eventID");
        joinedAmount = getIntent().getStringExtra("joinedAmount");
        //Toast.makeText(getBaseContext(),eventID,Toast.LENGTH_SHORT).show();
        initInstance();
    }

    private void initInstance() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.listUserRegis);
        scrollView = (ScrollView) findViewById(R.id.scrollViewShowUser);
        sendButton = (Button) findViewById(R.id.sendUserRegisBtn);
        checkButton = (Button) findViewById(R.id.checkCountBtn);

        // add
        Map<String, String> map = new HashMap<>();
        map.put("eventID", eventID);
        //Initialize regis user's List
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofit.create(UserRegisService.class).userRegis(map).enqueue(new Callback<List<UserRegis>>() {
            @Override
            public void onResponse(Call<List<UserRegis>> call, retrofit2.Response<List<UserRegis>> response) {
                Log.d("onResponse", "Called");
                userRegises = response.body();
                Log.d("onResponse", userRegises.toString());
                userRegisesArrayAdapter = new UserRegisAdapter(ShowUserRegisActivity.this, R.layout.list_user_regis_layout, userRegises);
                listView.setAdapter(userRegisesArrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        clickedUser = userRegises.get(position).getUserID();
                        Intent intent = new Intent(ShowUserRegisActivity.this, ShowQuesAnsUserActivity.class);
                        intent.putExtra("eventID", eventID);
                        intent.putExtra("clickedUser", clickedUser);
                        startActivity(intent);
                        Toast.makeText(ShowUserRegisActivity.this, "id" + userRegises.get(position).getUserID(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<List<UserRegis>> call, Throwable t) {
                Log.d("onFailure", "Called");
                Log.d("onFailure", call.toString());
                Log.d("onFailure", t.getMessage());
                Log.d("onFailure", t.toString());
                //Log.d("onFailure", call.);
            }
        });

        // end

        clickSave();
        clickCheckCount();
    }


    private void clickCheckCount() {
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < userRegises.size(); i++) {
                    // get answer from user
                    View po = listView.getChildAt(i);
                    checkBox = (CheckBox) po.findViewById(R.id.checkboxName);

                    // TODO: add
                    if (checkBox.isChecked()) {
                        //Toast.makeText(ShowUserRegisActivity.this, userWhoPass[b] + " user ja " + userRegises.get(i).getUserID(), Toast.LENGTH_SHORT).show();
                        c++;
                    }
                }
                Toast.makeText(ShowUserRegisActivity.this, "จำนวนผู้ผ่าน  " + c + " / " + joinedAmount, Toast.LENGTH_SHORT).show();
                c = 0;
            }
        });
    }

    private void clickSave() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < userRegises.size(); i++) {
                    // get answer from user
                    View po = listView.getChildAt(i);
                    checkBox = (CheckBox) po.findViewById(R.id.checkboxName);

                    // TODO: add
                    if (checkBox.isChecked()) {
                        userWhoPass[b] = userRegises.get(i).getUserID();
                        //Toast.makeText(ShowUserRegisActivity.this, userWhoPass[b] + " user ja " + userRegises.get(i).getUserID(), Toast.LENGTH_SHORT).show();
                        b++;
                    }
                }

                if (b != 0) {
                    RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
                    StringRequest request = new StringRequest(Request.Method.POST, URL1, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("onResponse", response);
                            Toast.makeText(getBaseContext(), "บันทึก", Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getBaseContext()," "+r+" "+questionName,Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("onError", error.toString() + "\n" + error.networkResponse.statusCode
                                    + "\n" + error.networkResponse.data + "\n" + error.getMessage());
                            Toast.makeText(getBaseContext(), "error", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            //params.put("eventID", eventID);
                            for (int s = 0; s < b; s++) {
                                params.put("userID[" + s + "]", userWhoPass[s]);
                                params.put("eventID[" + s + "]", eventID);
                                Log.d("params :: ", params.toString());

                                //params.put("questionName["+s+"]", question[s].toString());

                                //params.put("questionID", questionID);

                                r++;
                            }
                            //params.put("eventID", eventID);
                            return params;
                        }
                    };
                    requestQueue.add(request);

                    Intent in = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(in);
                    finish();

                }else{
                    Toast.makeText(getBaseContext(),"กรุณาเช็คชื่อก่อนส่ง",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    interface UserRegisService {
        @GET("show_user_regis.php")
        Call<List<UserRegis>> userRegis(@QueryMap Map<String, String> map);
    }
}
