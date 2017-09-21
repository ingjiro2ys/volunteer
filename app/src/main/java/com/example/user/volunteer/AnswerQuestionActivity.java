package com.example.user.volunteer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.volunteer.dao.Answer;
import com.example.user.volunteer.dao.AnswerAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class AnswerQuestionActivity extends AppCompatActivity {

    ScrollView scrollView;
    private ListView listView;
    Button button;
    String[] questionId;
    EditText editText;
    String[] userAnswer = new String[15];
    int r = 0;
    String userID;

    private List<Answer> answers; //Full Color Names
    private ArrayAdapter<Answer> answerArrayAdapter;

    private final String URL = "http://10.4.56.14/";
    private final String URL1 = "http://10.4.56.14/insertAnswer.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);

        SharedPreferences sp = getSharedPreferences("USER", Context.MODE_PRIVATE);
        userID = sp.getString("userID", "");

        ////// Get data
        listView = (ListView) findViewById(R.id.listQuestionView);
        scrollView = (ScrollView) findViewById(R.id.scrollView1);
        button = (Button) findViewById(R.id.sendAnsBtn);


        //Initialize Color's List
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofit.create(AnswerService.class).answers().enqueue(new Callback<List<Answer>>() {
            @Override
            public void onResponse(Call<List<Answer>> call, retrofit2.Response<List<Answer>> response) {
                answers = response.body();
                //answers = new ArrayList<>(answers);
                answerArrayAdapter = new AnswerAdapter(AnswerQuestionActivity.this, R.layout.list_question_layout, answers);
                listView.setAdapter(answerArrayAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(AnswerQuestionActivity.this, "lol", Toast.LENGTH_SHORT).show();

                        /*Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                        intent.putExtra("color",item);
                        intent.putExtra("code",code);
                        startActivity(intent);*/
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Answer>> call, Throwable t) {
            }
        });


        button.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < answers.size(); i++) {
                    // get answer from user
                    View po = listView.getChildAt(i);
                    editText = (EditText) po.findViewById(R.id.answer1);
                    userAnswer[i] = editText.getText().toString();

                    // send question id
                    //questionId[i] = answers.get(i).getQuestionID();

                    //Toast.makeText(getBaseContext(), answers.get(i).getQuestionID() + " " + userAnswer[i], Toast.LENGTH_SHORT).show();

                }

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
                        for (int s = 0; s < answers.size(); s++) {
                            params.put("answerDes[" + s + "]", userAnswer[s]);
                            params.put("questionID[" + s + "]", answers.get(s).getQuestionID());
                            params.put("userID[" + s + "]", userID);

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

            }
        });
    }

    interface AnswerService {
        /*@POST("show_questions.php")
        Call<List<Answer>> postEventId(@Field("eventID") int eventID);*/

        @GET("show_questions.php")
        Call<List<Answer>> answers();


    }
}
