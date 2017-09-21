package com.example.user.volunteer;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.user.volunteer.dao.Answer;
import com.example.user.volunteer.dao.UserRegisEvent;
import com.example.user.volunteer.dao.UserRegisEventAdapter;

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

    ScrollView scrollView;
    ListView listView;
    Button button;
    CheckBox checkBox;

    String userID;
    String eventID;

    private List<UserRegisEvent> userRegis; //Full Color Names
    private ArrayAdapter<UserRegisEvent> userRegisArrayAdapter;

    private final String URL = "http://10.4.56.14/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_regis);

        eventID = getIntent().getStringExtra("eventID");
        Toast.makeText(ShowUserRegisActivity.this,"event id="+eventID,Toast.LENGTH_SHORT).show();

        SharedPreferences sp = getSharedPreferences("USER", Context.MODE_PRIVATE);
        userID = sp.getString("userID", "");

        ////// Get data
        listView = (ListView) findViewById(R.id.listView);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        button = (Button) findViewById(R.id.userPassBtn);

        //TODO:add
        /*Map<String,String> map = new HashMap<>();
        map.put("eventID", eventID);*/
        //Initialize Color's List
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofit.create(UserRegisService.class).userRegis().enqueue(new Callback<List<UserRegisEvent>>() {
            @Override
            public void onResponse(Call<List<UserRegisEvent>> call, retrofit2.Response<List<UserRegisEvent>> response) {
                userRegis = response.body();
                //answers = new ArrayList<>(answers);
                userRegisArrayAdapter = new UserRegisEventAdapter(ShowUserRegisActivity.this, R.layout.list_user_regis_layout, userRegis);
                listView.setAdapter(userRegisArrayAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(ShowUserRegisActivity.this, userRegis.get(position).getUserFName()+"++", Toast.LENGTH_SHORT).show();
                        /*Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                        intent.putExtra("color",item);
                        intent.putExtra("code",code);
                        startActivity(intent);*/
                    }
                });
            }

            @Override
            public void onFailure(Call<List<UserRegisEvent>> call, Throwable t) {
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ShowUserRegisActivity.this,"Hello jajaja",Toast.LENGTH_SHORT).show();
            }
        });

    }

    interface UserRegisService {
        @GET("show_user_regis.php")
        Call<List<UserRegisEvent>> userRegis();
        //@QueryMap Map<String, String> map
    }
}
