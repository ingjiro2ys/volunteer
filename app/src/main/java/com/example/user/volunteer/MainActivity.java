package com.example.user.volunteer;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SearchEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.user.volunteer.dao.PhotoItemCollectionDao;
import com.example.user.volunteer.dao.PhotoItemDao;
import com.example.user.volunteer.dao.SearchAdapter;
import com.example.user.volunteer.manager.HttpManager;
import com.example.user.volunteer.manager.PhotoListManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;
import com.koushikdutta.ion.Ion;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{  //implements SearchView.OnQueryTextListener

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    MaterialSearchView searchView;
    Toolbar toolBar;
    ListView lstvw;

    // Fragment
    ListView listView;
    PhotoListAdapter listAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    PhotoListManager photoListManager = PhotoListManager.getInstance();

    TextView studentId, studentName;

    // Menu
    LinearLayout btn_1_favorite;
    LinearLayout btn_2_regis;
    LinearLayout btn_3_join;
    LinearLayout btn_4_owner;
    LinearLayout btn_5_logout;

    String userName, userFullName;
    String userID;
    RequestQueue requestQueue;
    String url;

    //TODO:add
    final String USER = "USER";
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String userID_af_p;

    // For search
    private List<PhotoItemDao> colors; //Full Color Names
    private ArrayList filteredColors; //Full Color Names
    private ArrayAdapter<PhotoItemDao> colorArrayAdapter;
    private final String URL = "http://10.4.56.14:82/searcher.php";
    Timer repeatTask = new Timer();
    int repeatInterval = 5000;
    String listAmount="0";
    MenuItem itemCart;
    LayerDrawable icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //userName = getIntent().getStringExtra("userName");
        //Toast.makeText(MainActivity.this,userName,Toast.LENGTH_SHORT).show();
        requestQueue = Volley.newRequestQueue(this);
//        url="http://10.4.56.14:82/getUserID.php/?query=SELECT%20*%20FROM%20user%20where%20userName="+userName;
//        getUserFullName();
//        Toast.makeText(MainActivity.this,userFullName,Toast.LENGTH_SHORT).show();

        //TODO:ADD
        SharedPreferences sp = getSharedPreferences("USER", Context.MODE_PRIVATE);
        SharedPreferences sp1 = getSharedPreferences("USER1", Context.MODE_PRIVATE);
        SharedPreferences sp2 = getSharedPreferences("USER2", Context.MODE_PRIVATE);
        userID = sp.getString("userID","");
        userFullName = sp1.getString("userFullName","");
        userName = sp2.getString("studentID","");

        //Toast.makeText(getBaseContext(),"userID after Share Pre1: "+userID+"  "+userFullName,Toast.LENGTH_SHORT).show();

        // Initialize Toolbar
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        /************************ SEARCH VIEW********************************/

        searchView = (MaterialSearchView)findViewById(R.id.searchView);
        lstvw = (ListView)findViewById(R.id.lstvw);

        // TODO: add tonggnee
        try {
            colors = new Gson().fromJson(Ion.with(this).load(URL).asString().get(), new TypeToken<ArrayList<PhotoItemDao>>() {}.getType());
            filteredColors =  new ArrayList<>(colors);
            colorArrayAdapter = new SearchAdapter(this, R.layout.list_search_event_row, filteredColors);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);

                lstvw.setAdapter(colorArrayAdapter); //Only Create One time...

                lstvw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        PhotoItemDao dao = (PhotoItemDao)lstvw.getItemAtPosition(position);
//                        PhotoItemDao dao = photoListManager.getDao().getEvents().get(position);
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra("dao",dao);
                        startActivity(intent);

                        Log.d("$$dao", dao.toString());
                        //Toast.makeText(MainActivity.this,colors.get(position).getImagePath(), Toast.LENGTH_SHORT).show();
                        //listView.setAdapter(listAdapter);
                    }
                });

                reloadData();

                return false;
            }
        });
        VersionHelper.refreshActionBarMenu(this);
        initInstance();
        OneSignal.sendTag("User_ID",userID);
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new OneSignal.NotificationOpenedHandler() {
                    @Override
                    public void notificationOpened(OSNotificationOpenResult result) {
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .init();



    }

    private Iterator<PhotoItemDao> colorItr;

    public void filter(String query) {
        filteredColors.clear();
        filteredColors.addAll(this.colors);
        colorItr = filteredColors.listIterator();
        while (colorItr.hasNext()) {
            if (!colorItr.next().getEventName().contains(query)) {
                colorItr.remove();
            }
        }
        colorArrayAdapter.notifyDataSetChanged();
    }

    private void initInstance() {
        //Initialize FloatingActionButton
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_view);
        //Setting OnClick Listener to The Floating Action Button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Do Something Code Goes Here
                Intent intent = new Intent(MainActivity.this,CreateEventActivity.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
                /*Toast.makeText(getApplicationContext(),
                        "You Clicked FAB! You Are FABulous!",
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout,
                R.string.open_drawer, R.string.close_drawer);
        // replace setDrawerListener with addDrawerListener
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ///////////////////////////////////// Handle Click on Hamburger menu

        studentId = (TextView) findViewById(R.id.studentId);
        studentId.setText(userName);

        studentName = (TextView) findViewById(R.id.studentName);
        studentName.setText(userFullName);

        btn_1_favorite = (LinearLayout) findViewById(R.id.btn_1_favorite);
        btn_1_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this,FavoriteActivity.class);
                startActivity(in);
                //Toast.makeText(MainActivity.this, "Your Favorite Event", Toast.LENGTH_SHORT).show();
            }
        });

        btn_2_regis = (LinearLayout) findViewById(R.id.btn_2_regis);
        btn_2_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
                //Toast.makeText(MainActivity.this, "Your Register Event", Toast.LENGTH_SHORT).show();
            }
        });

        btn_3_join = (LinearLayout) findViewById(R.id.btn_3_join);
        btn_3_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,JoinEventActivity.class);
                startActivity(intent);
                //Toast.makeText(MainActivity.this, "Your Join Event", Toast.LENGTH_SHORT).show();
            }
        });

        btn_4_owner = (LinearLayout) findViewById(R.id.btn_4_owner);
        btn_4_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,OwnerEventActivity.class);
                startActivity(intent);
                //Toast.makeText(MainActivity.this, "Event's Owner", Toast.LENGTH_SHORT).show();

            }
        });


        btn_5_logout = (LinearLayout) findViewById(R.id.btn_5_logout);
        btn_5_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "LOGOUT", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Initialize Fragment ///***********************
        listView = (ListView) findViewById(R.id.listView);
        listAdapter = new PhotoListAdapter();
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO:ต้องให้ตัว dao รับเป็น Parcelable แล้วส่งไปหาอีก activity นึง
                    PhotoItemDao dao = photoListManager.getDao().getEvents().get(position);
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("dao", dao);
                    intent.putExtra("userID",userID);
                    startActivity(intent);
                //Toast.makeText(MainActivity.this,dao.getEventName()+"",Toast.LENGTH_LONG).show();

                //Toast.makeText(MainActivity.this,dao.getJoinedAmount()+"",Toast.LENGTH_LONG).show();
                //Toast.makeText(MainActivity.this,"Position: "+position,Toast.LENGTH_SHORT).show();
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
        Call<PhotoItemCollectionDao> call = HttpManager.getInstance().getService().loadPhotoList();
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
                    Toast.makeText(MainActivity.this,"Load Successful",
                            Toast.LENGTH_SHORT).show();
                }else {
                    // Handle
                    try {
                        Toast.makeText(MainActivity.this,
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
                Toast.makeText(MainActivity.this,
                        t.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        if(item.getItemId()== R.id.filter){

            Intent intent = new Intent(MainActivity.this,FilterActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    // To display the SearchView in the app bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the options menu from XML
        getMenuInflater().inflate(R.menu.options_menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        searchView.setMenuItem(item);  //SERACH HERE FROM DUMP
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                lstvw.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSearchViewClosed() {
                lstvw.setVisibility(View.GONE);
            }
        });
//        // add code hereeeee
//        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                return true;
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                return true;
//            }
//        });

        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        itemCart = menu.findItem(R.id.action_cart);
        icon = (LayerDrawable) itemCart.getIcon();
        setBadgeCount(this, icon, listAmount);
        //Toast.makeText(getBaseContext(),"listAmount: "+listAmount,Toast.LENGTH_SHORT).show();
        itemCart.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
                return true;
            }
        });
        return true;
    }


    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
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
    public void countNoti(){
        new MyAsyncTask().execute("http://10.4.56.14:82/get_isJoined_count.php/?userID="+userID,"parse");
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

            listAmount=s;
            //Toast.makeText(getBaseContext(),"listAmount: "+listAmount,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        countNoti();
        //Toast.makeText(getBaseContext(),"Resume : "+listAmount,Toast.LENGTH_SHORT).show();
        VersionHelper.refreshActionBarMenu(this);
    }


}