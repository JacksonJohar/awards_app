package com.johar_rewards.johar_rewards;

import android.content.Intent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.johar_rewards.johar_rewards.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Leaderboard extends AppCompatActivity {

    List<Person> people= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard);
        Bundle i = getIntent().getExtras();
        /*PersonAdapter pAdapter;
        RecyclerView personRecycler;
        personRecycler =(RecyclerView) findViewById(R.id.people);
        pAdapter = new PersonAdapter(people);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        personRecycler.setLayoutManager(mLayoutManager);
        personRecycler.setItemAnimator(new DefaultItemAnimator());
        personRecycler.setAdapter(pAdapter);*/

        JSONObject obj = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(Leaderboard.this);
        try {
            obj.put("studentId", "A20393623");
            obj.put("username", i.getString("username"));
            obj.put("password", i.getString("password"));

        } catch (JSONException e) {

        }
        String url = "http://inspirationrewardsapi-env.6mmagpm2pv.us-east-2.elasticbeanstalk.com/allprofiles";
        CustomJsonArrayRequest thing = new CustomJsonArrayRequest(Request.Method.POST, url, obj, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                PersonAdapter pAdapter;
                RecyclerView personRecycler;
                personRecycler =(RecyclerView) findViewById(R.id.people);
                pAdapter = new PersonAdapter(people);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                personRecycler.setLayoutManager(mLayoutManager);
                personRecycler.setItemAnimator(new DefaultItemAnimator());
                personRecycler.setAdapter(pAdapter);
                pAdapter.setOnItemClickListener(new PersonAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Bundle b = getIntent().getExtras();
                        Bundle b2 = new Bundle();
                        Intent i = new Intent(Leaderboard.this, AddRewards.class);
                        Person temp = new Person();
                        temp = people.get(position);
                        b2.putString("name", temp.getLastName() + ", " + temp.getFirstName());
                        b2.putString("department", temp.getDepartment());
                        b2.putString("position", temp.getPosition());
                        b2.putString("story", temp.getStory());
                        b2.putString("points", "" + temp.getPoints());
                        b2.putString("username", temp.getUsername());
                        b2.putString("name2", temp.getFirstName() + " " + temp.getLastName());
                        ByteArrayOutputStream bs = new ByteArrayOutputStream();
                        temp.getImage().compress(Bitmap.CompressFormat.PNG, 50, bs);
                        i.putExtra("image", bs.toByteArray());
                        i.putExtra("b2", b2);
                        i.putExtra("b", b);
                        startActivity(i);
                    }
                });
                for (int i = 0; i < response.length(); i++){
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        JSONArray temp = new JSONArray();
                        int points = 0;
                        String byteArray = obj.getString("imageBytes");
                        byte[] bytes = Base64.decode(byteArray,Base64.NO_WRAP);
                        InputStream inputStream  = new ByteArrayInputStream(bytes);
                        Bitmap bm = BitmapFactory.decodeStream(inputStream);
                        //Bitmap bm = BitmapFactory.decodeByteArray(obj.getString("imageBytes").getBytes(), 0, obj.getString("imageBytes").length());
                        Person person = new Person(obj.getString("firstName"), obj.getString("lastName"), obj.getString("username"),
                                obj.getString("department"), obj.getString("story"), obj.getString("position"), obj.getString("password"),
                                obj.getInt("pointsToAward"), obj.getBoolean("admin"), bm, obj.getString("location"),
                                temp);
                        person.setPoints(points);
                        people.add(person);
                        people.get(i).setPoints(Person.calcPoints(obj.getJSONArray("rewards")));
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
                Collections.sort(people, new Comparator<Person>() {
                    @Override
                    public int compare(Person lhs, Person rhs) {
                        return rhs.getPoints() - lhs.getPoints();
                    }
                });
            }
            },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(thing);

    }

    public void onBackClick(View view){
        finish();
    }
}
