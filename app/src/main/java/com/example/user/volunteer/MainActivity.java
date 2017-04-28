package com.example.user.volunteer;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    Toolbar toolBar;


    ListView listView;
    PhotoListAdapter listAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    Button btnNewPhotos;
    
    // Menu
    LinearLayout btn_1_favorite;
    LinearLayout btn_2_regis;
    LinearLayout btn_3_join;
    LinearLayout btn_4_history;
    LinearLayout btn_5_logout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initInstance();


    }

    private void initInstance() {
        // Initialize Toolbar
        toolBar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);


        //Initialize FloatingActionButton
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_view);
        //Setting OnClick Listener to The Floating Action Button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Do Something Code Goes Here
                Toast.makeText(getApplicationContext(),
                        "You Clicked FAB! You Are FABulous!",
                        Toast.LENGTH_SHORT).show();
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
        btn_1_favorite = (LinearLayout) findViewById(R.id.btn_1_favorite);
        btn_1_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Your Favorite Event", Toast.LENGTH_SHORT).show();
            }
        });

        btn_2_regis = (LinearLayout) findViewById(R.id.btn_2_regis);
        btn_2_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Your Register Event", Toast.LENGTH_SHORT).show();
            }
        });

        btn_3_join = (LinearLayout) findViewById(R.id.btn_3_join);
        btn_3_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Your Join Event", Toast.LENGTH_SHORT).show();
            }
        });

        btn_4_history = (LinearLayout) findViewById(R.id.btn_4_history);
        btn_4_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Event history", Toast.LENGTH_SHORT).show();
            }
        });

        btn_5_logout = (LinearLayout) findViewById(R.id.btn_5_logout);
        btn_5_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "LOGOUT", Toast.LENGTH_SHORT).show();
            }
        });



        // Initialize Fragment ///***************************
        listView = (ListView) findViewById(R.id.listView);


        // Show Result to second activity
        // INSERT HERE !!!

        //////// Refresh data
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //refreshData();
            }
        });

        listAdapter = new PhotoListAdapter();
        listView.setAdapter(listAdapter);
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Position: "+position,
                        Toast.LENGTH_SHORT).show();
            }
        });*/




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
        return super.onOptionsItemSelected(item);

    }

    // To display the SearchView in the app bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }
}
