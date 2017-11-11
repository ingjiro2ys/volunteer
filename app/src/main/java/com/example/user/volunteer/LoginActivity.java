package com.example.user.volunteer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.session.MediaSessionManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

public class LoginActivity extends AppCompatActivity implements AsyncResponse{

    EditText userName, userPass;
    Button loginBtn;
    TextView forgetPass;
    JSONObject c;

    String stdName, stdFaculty;
    int num;

    String userID,userFullName,studentID;
    String url,urlName,urlID;
    final String TAG = this.getClass().getName();

    //TODO:add
    final String USER = "USER";
    final String USER1 = "USER1";
    final String USER2 = "USER2";

    SharedPreferences sp;
    SharedPreferences sp1;
    SharedPreferences sp2;
    SharedPreferences.Editor editor;

    boolean isLogin;
    String userID_af_p;

    // Session Manager Class
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        //OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.DEBUG);

        // Session Manager
        session = new SessionManager(getApplicationContext());
        
        initInstance();
    }

    private void initInstance() {
        /////////////////////////////
        userName = (EditText) findViewById(R.id.userName);
        userPass = (EditText) findViewById(R.id.userPass);
        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();

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
                task.execute("http://10.4.56.14:82/loginuser.php");
                //TODO:ADD
                url="http://10.4.56.14:82/getUserID.php/?query=SELECT%20*%20FROM%20user%20where%20userName="+userName.getText().toString();
                getUserID();

                urlName="http://10.4.56.14:82/getUserID.php/?query=SELECT%20*%20FROM%20user%20where%20userName="+userName.getText().toString();
                getUserFullName();

                urlID="http://10.4.56.14:82/getUserID.php/?query=SELECT%20*%20FROM%20user%20where%20userName="+userName.getText().toString();
                getStudentId();

            }
        });

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this,"สมน้ำหน้า",Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void processFinish(String s) {
        Log.d(TAG, s);
        if (s.contains("success")) {
            //TODO:ADD
            session.createLoginSession(userFullName, userName.getText().toString());
            Intent in = new Intent(LoginActivity.this, MainActivity.class);
            //in.putExtra("userName",userName.getText().toString());
            startActivity(in);
            finish();
        }else{
            Toast.makeText(LoginActivity.this,"Wrong username or password", Toast.LENGTH_LONG).show();
        }
    }

    public void getUserID(){
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
                    JSONArray data = jsonResult.getJSONArray("User");
                    for(int p =0; p < data.length(); p++ ){
                        JSONObject productObject = data.optJSONObject(p);
                        output = productObject.optString("userID");


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
            userID=s;
            //Toast.makeText(getBaseContext(),"userID: "+userID,Toast.LENGTH_SHORT).show();
            sp = getSharedPreferences(USER, Context.MODE_PRIVATE);
            editor = sp.edit();
            editor.putString("userID",userID);
            editor.commit();

//
//            if(sp.getString("USER",null)!=null){
//                finish();
//                startActivity(new Intent(getBaseContext(),LoginActivity.class));
//            }

            /*userID_af_p = sp.getString("userID","");
            Toast.makeText(getBaseContext(),"userID after Share Pre2: "+userID_af_p,Toast.LENGTH_SHORT).show();*/

        }
    }

    public void getUserFullName(){
        new MyAsyncTask1().execute(urlName,"parse");
    }
    class MyAsyncTask1 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                java.net.URL url = new URL(params[0]);
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                httpCon.setDoInput(true);
                httpCon.connect();

                InputStream inStream = httpCon.getInputStream();
                Scanner scanner = new Scanner(inStream, "Windows-874");
                response = scanner.useDelimiter("\\A").next();
            } catch (Exception ex) {
            }

            if (params[1].equals("show")) {
                return response;

            } else if (params[1].equals("parse")) {
                String output = "";
                try {
                    JSONObject jsonResult = new JSONObject(response);
                    JSONArray data = jsonResult.getJSONArray("User");
                    for (int p = 0; p < data.length(); p++) {
                        JSONObject productObject = data.optJSONObject(p);
                        output = productObject.optString("userFName");
                        output += "    " + productObject.optString("userLname");


                        /*output += "* eventID "+productObject.optString("eventID");
                        output += " - userID "+productObject.optString("userID");
                        output += " - FavCode "+productObject.optString("chooseFavorite")+"\n";*/
                    }
                    //output += "\n";
                } catch (JSONException e) {
                }
                return output;
            } else {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            userFullName = s;
            //Toast.makeText(getBaseContext(), "userFName: " + userFullName, Toast.LENGTH_SHORT).show();
            sp1 = getSharedPreferences(USER1, Context.MODE_PRIVATE);
            editor = sp1.edit();
            editor.putString("userFullName", userFullName);
            editor.commit();

            /*userID_af_p = sp.getString("userID","");
            Toast.makeText(getBaseContext(),"userID after Share Pre2: "+userID_af_p,Toast.LENGTH_SHORT).show();*/

        }
    }

    public void getStudentId(){
        new MyAsyncTask2().execute(urlID,"parse");
    }
    class MyAsyncTask2 extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String response = "";
            try {
                java.net.URL url = new URL(params[0]);
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                httpCon.setDoInput(true);
                httpCon.connect();

                InputStream inStream = httpCon.getInputStream();
                Scanner scanner = new Scanner(inStream, "Windows-874");
                response = scanner.useDelimiter("\\A").next();
            } catch (Exception ex) {
            }

            if (params[1].equals("show")) {
                return response;

            } else if (params[1].equals("parse")) {
                String output = "";
                try {
                    JSONObject jsonResult = new JSONObject(response);
                    JSONArray data = jsonResult.getJSONArray("User");
                    for (int p = 0; p < data.length(); p++) {
                        JSONObject productObject = data.optJSONObject(p);
                        output = productObject.optString("userName");

                    }
                } catch (JSONException e) {
                }
                return output;
            } else {
                return "";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            studentID = s;
            //Toast.makeText(getBaseContext(), "userFName: " + userFullName, Toast.LENGTH_SHORT).show();
            sp2 = getSharedPreferences(USER2, Context.MODE_PRIVATE);
            editor = sp2.edit();
            editor.putString("studentID", studentID);
            editor.commit();

            /*userID_af_p = sp.getString("userID","");
            Toast.makeText(getBaseContext(),"userID after Share Pre2: "+userID_af_p,Toast.LENGTH_SHORT).show();*/

        }
    }
}
