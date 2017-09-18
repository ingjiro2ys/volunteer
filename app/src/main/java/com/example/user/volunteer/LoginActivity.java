package com.example.user.volunteer;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.asynctask.AsyncResponse;
import com.kosalgeek.asynctask.PostResponseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements AsyncResponse{

    EditText userName, userPass;
    Button loginBtn;
    TextView forgetPass;
    JSONObject c;

    String stdName, stdFaculty;
    int num;

    final String TAG = this.getClass().getName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        initInstance();
    }

    private void initInstance() {

        /////////////////////////////
        userName = (EditText) findViewById(R.id.userName);
        userPass = (EditText) findViewById(R.id.userPass);

        loginBtn = (Button) findViewById(R.id.loginBtn);

        forgetPass = (TextView) findViewById(R.id.forgetPass);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap postData = new HashMap();
                postData.put("mobile", "volunteer");
                postData.put("userName", userName.getText().toString());//userName มาจากไฟล์ PHP
                postData.put("userPass", userPass.getText().toString());

                PostResponseAsyncTask task = new PostResponseAsyncTask(LoginActivity.this, postData);
                task.execute("http://10.4.56.14/login.php");
            }
        });

    }

    public void processFinish(String s) {
        Log.d(TAG, s);
        if (s.contains("success")) {
            Intent in = new Intent(LoginActivity.this, MainActivity.class);
            in.putExtra("userName",userName.getText().toString());
            startActivity(in);
        }else{
            Toast.makeText(LoginActivity.this,"Wrong username or password", Toast.LENGTH_LONG).show();
        }
    }

    private void checkLogin() {
    }
}
