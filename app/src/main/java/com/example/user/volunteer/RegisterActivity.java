package com.example.user.volunteer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user.volunteer.dao.PhotoItemCollectionDao;
import com.example.user.volunteer.dao.PhotoItemDao;
import com.example.user.volunteer.manager.HttpManager;
import com.example.user.volunteer.manager.PhotoListManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    ListView listView;
    PhotoListAdapter listAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    PhotoListManager photoListManager = PhotoListManager.getInstance();
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //TODO: add sharePrefer
        SharedPreferences sp = getSharedPreferences("USER", Context.MODE_PRIVATE);
        userID = sp.getString("userID","");
        //Toast.makeText(getBaseContext(),""+userID,Toast.LENGTH_SHORT).show();

        initInstance();
    }

    private void initInstance() {

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        listView = (ListView) findViewById(R.id.listView);
        listAdapter = new PhotoListAdapter();
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO:ต้องให้ตัว dao รับเป็น Parcelable แล้วส่งไปหาอีก activity นึง
                PhotoItemDao dao = photoListManager.getDao().getEvents().get(position);
                Intent intent = new Intent(RegisterActivity.this, DetailActivity.class);
                intent.putExtra("dao", dao);
                //intent.putExtra("userID", userID);
                startActivity(intent);
                //Toast.makeText(RegisterActivity.this, dao.getEventID() + "", Toast.LENGTH_LONG).show();
            }
        });

        //////// Refresh data
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
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

    private void reloadData() {
        //add
        Map<String,String> map = new HashMap<>();
        map.put("userID", userID);
        //add
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance().getService().loadListRegis(map);
        call.enqueue(new Callback<PhotoItemCollectionDao>() {
            @Override
            public void onResponse(Call<PhotoItemCollectionDao> call,
                                   Response<PhotoItemCollectionDao> response) {
                swipeRefreshLayout.setRefreshing(false);
                if(response.isSuccessful()){
                    photoListManager.setDao(response.body());
                    listAdapter.setDao(photoListManager.getDao());
                    listAdapter.notifyDataSetChanged();
                    //ลบข้างบน
                    Toast.makeText(RegisterActivity.this,"Load Successful",
                            Toast.LENGTH_SHORT).show();
                }else {
                    // Handle
                    try {
                        Toast.makeText(RegisterActivity.this,
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
                Toast.makeText(RegisterActivity.this,
                        t.toString(),
                        Toast.LENGTH_SHORT).show();
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
}