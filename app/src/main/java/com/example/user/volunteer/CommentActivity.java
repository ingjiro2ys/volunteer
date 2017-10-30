package com.example.user.volunteer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.volunteer.dao.Comment;
import com.example.user.volunteer.dao.CommentAdapter;

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

public class CommentActivity extends AppCompatActivity {

    ScrollView scrollComment;
    ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;
    EditText editComment;
    Button buttonComment;

    private List<Comment> comments;
    private ArrayAdapter<Comment> commentArrayAdapter;

    private final String URL = "http://10.4.56.14/";
    private final String URL1 = "http://10.4.56.14/insertComment.php";
    String userID;
    String eventID;
    String description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        //TODO: add sharePrefer
        SharedPreferences sp = getSharedPreferences("USER", Context.MODE_PRIVATE);
        userID = sp.getString("userID","");

        eventID = getIntent().getStringExtra("eventID");

        initInstance();

    }

    private void initInstance() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        scrollComment = (ScrollView)findViewById(R.id.scrollComment);
        listView = (ListView)findViewById(R.id.listComment);

        // add
        Map<String,String> map = new HashMap<>();
        map.put("eventID", eventID);
        //Initialize QA List
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofit.create(CommentService.class).comment(map).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                comments = response.body();
                commentArrayAdapter = new CommentAdapter(CommentActivity.this,R.layout.list_comment_layout,comments);
                listView.setAdapter(commentArrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

            }
        });
        editComment = (EditText)findViewById(R.id.etComment);
        //description = editComment.getText().toString();

        buttonComment = (Button)findViewById(R.id.sendCommentBtn);
        buttonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description = editComment.getText().toString();
                clickComment();

                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
                editComment.setText("");
            }
        });

        //////// Refresh data
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
                commentArrayAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void clickComment() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, URL1, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse", response);
                //eventID = 0;
                Toast.makeText(CommentActivity.this, "เพิ่มข้อมูลแล้ว", Toast.LENGTH_SHORT).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("onError",error.toString());
                Toast.makeText(CommentActivity.this,"เกิดข้อผิดพลาดโปรดลองอีกครั้ง",Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("eventID", ((String.valueOf(eventID)) ));
                params.put("userID", (userID));
                params.put("description",description);
                return params;
            }
        };
        requestQueue.add(request);
    }

    private void reloadData() {
        // add
        Map<String,String> map = new HashMap<>();
        map.put("eventID", eventID);
        //Initialize QA List
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofit.create(CommentService.class).comment(map).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                comments = response.body();
                commentArrayAdapter = new CommentAdapter(CommentActivity.this,R.layout.list_comment_layout,comments);
                listView.setAdapter(commentArrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

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

    interface CommentService {
        @GET("show_comment.php")
        Call<List<Comment>> comment(@QueryMap Map<String, String> map);
    }
}
