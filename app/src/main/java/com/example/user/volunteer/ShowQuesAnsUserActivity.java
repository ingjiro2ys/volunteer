package com.example.user.volunteer;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.user.volunteer.dao.QuestionAns;
import com.example.user.volunteer.dao.QuestionAnsAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public class ShowQuesAnsUserActivity extends AppCompatActivity {

    ListView listView;
    ScrollView scrollView;

    String userID,eventID,clickedUser;

    private List<QuestionAns> questionAnses;
    private ArrayAdapter<QuestionAns> questionAnsArrayAdapter;

    private final String URL = "http://10.4.56.14/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ques_ans_user);

        //TODO:ADD
        SharedPreferences sp = getSharedPreferences("USER", Context.MODE_PRIVATE);
        userID = sp.getString("userID","");

        eventID = getIntent().getStringExtra("eventID");
        clickedUser = getIntent().getStringExtra("clickedUser");

        initInstance();
    }

    private void initInstance() {

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ////// Get data
        listView = (ListView) findViewById(R.id.listViewQA);
        scrollView = (ScrollView) findViewById(R.id.scrollViewQA);

        // add
        Map<String,String> map = new HashMap<>();
        map.put("userID", clickedUser);
        map.put("eventID", eventID);
        //Initialize QA List
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofit.create(QuesAnsService.class).quesAns(map).enqueue(new Callback<List<QuestionAns>>() {
            @Override
            public void onResponse(Call<List<QuestionAns>> call, Response<List<QuestionAns>> response) {
                questionAnses = response.body();
                questionAnsArrayAdapter = new QuestionAnsAdapter(ShowQuesAnsUserActivity.this,R.layout.list_ques_ans_user,questionAnses);
                listView.setAdapter(questionAnsArrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    }
                });
            }

            @Override
            public void onFailure(Call<List<QuestionAns>> call, Throwable t) {

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

    interface QuesAnsService {
        @GET("show_quesAns_user.php")
        Call<List<QuestionAns>> quesAns(@QueryMap Map<String, String> map);
    }
}
