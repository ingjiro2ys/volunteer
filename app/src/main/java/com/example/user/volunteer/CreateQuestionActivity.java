package com.example.user.volunteer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class CreateQuestionActivity extends AppCompatActivity {

    ScrollView scrollview;
    LinearLayout linearLayout;
    LinearLayout.LayoutParams layoutParams;
    Button btn_saveq;
    EditText view;
    EditText etloop;
    String eventID="1";
    String questionName;
    String questionID = "null";
    private static final String URL = "http://10.4.56.14:82/insertQuestion2.php";
    static int i;
    int etAmount;
    int n;
    int s;
    String [] question = new String[15];
    int r =0;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        //TODO: add sharePrefer
        SharedPreferences sp = getSharedPreferences("USER", Context.MODE_PRIVATE);
        userID = sp.getString("userID","");

        onClickAddQuestion();
        onClickSave();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.skip_question, menu);
        MenuItem menuItem = (MenuItem) menu.findItem(R.id.skipq);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
                return false;
            }
        });

        return true;

    }

    public void onClickAddQuestion(){
        scrollview = (ScrollView)findViewById(R.id.scrollview);
        linearLayout = (LinearLayout)findViewById(R.id.lo_q);
        Button b = (Button)findViewById(R.id.Button01);
        layoutParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        b.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                view = new EditText(scrollview.getContext());
                view.setId(i); //++i
                //view.setText(i+"");
                etAmount = i;
                i++;
                linearLayout.addView(view, layoutParams);
            }

        });
    }

    public void onClickSave(){
        btn_saveq = (Button)findViewById(R.id.btn_saveq);
        btn_saveq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                for(n = 0; n <= etAmount; n++){

                    view = (EditText)findViewById(n);
                    question[n] = view.getText().toString(); //questionName
                    questionName = question[n].toString();
                    //Toast.makeText(getBaseContext()," "+n+" "+questionName,Toast.LENGTH_SHORT).show();

                }

                //Toast.makeText(getBaseContext(),"in loop",Toast.LENGTH_SHORT).show();
                //Toast.makeText(getBaseContext(),n+"",Toast.LENGTH_SHORT).show();
                //Toast.makeText(getBaseContext()," "+n+" "+questionName,Toast.LENGTH_SHORT).show();
                RequestQueue requestQueue = Volley.newRequestQueue(getBaseContext());
                StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("onResponse", response);
                        Toast.makeText(getBaseContext(), "บันทึก", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getBaseContext()," "+r+" "+questionName,Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("onError", error.toString()+"\n"+error.networkResponse.statusCode
                                +"\n"+error.networkResponse.data+"\n"+error.getMessage());
                        Toast.makeText(getBaseContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        //params.put("eventID", eventID);
                        for(s = 0; s <= etAmount; s++){

                            if(!(question[s].toString()).equals("")){
                                params.put("questionName["+s+"]", question[s].toString());
                            }
                            //params.put("questionName["+s+"]", question[s].toString());

                            //params.put("questionID", questionID);

                            r++;
                        }
                        return params;
                    }
                };
                requestQueue.add(request);

                Intent in = new Intent(getBaseContext(), MainActivity.class);
                startActivity(in);
                finish();
            }
        });
    }


}
