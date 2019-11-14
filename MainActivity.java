package com.johar_rewards.johar_rewards;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.johar_rewards.johar_rewards.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private Location onlyOneLocation;
    private final int REQUEST_FINE_LOCATION = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProgressBar pb = (ProgressBar)findViewById(R.id.pBar3);
        pb.setVisibility(View.INVISIBLE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
    }

    public void createClick(View view){
        Intent intent = new Intent(this, CreateProfile.class);
        startActivity(intent);
    }

    public void loginClick(View view){
        ProgressBar pb = (ProgressBar)findViewById(R.id.pBar3);
        pb.setVisibility(View.VISIBLE);
        EditText uName = (EditText)findViewById(R.id.username);
        EditText pWord = (EditText)findViewById(R.id.password);
        String username = uName.getText().toString();
        String password = pWord.getText().toString();
        JSONObject newPerson = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        try {
            newPerson.put("studentId", "A20393623");
            newPerson.put("username", username);
            newPerson.put("password", password);

        } catch (JSONException e) {

        }

        String url = "http://inspirationrewardsapi-env.6mmagpm2pv.us-east-2.elasticbeanstalk.com/login";
        JsonObjectRequest thing = new JsonObjectRequest(Request.Method.POST, url, newPerson, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                try {
                    Intent intent = new Intent(MainActivity.this, Profile.class);
                    Bitmap bm = BitmapFactory.decodeByteArray(response.getString("imageBytes").getBytes(), 0, response.getString("imageBytes").length());
                    JSONArray rewards = response.getJSONArray("rewards");
                    List<Reward> rewardList = new ArrayList<Reward>();
                    for (int i = 0; i < rewards.length(); i++){
                        JSONObject tempReward = rewards.getJSONObject(i);
                        Reward reward = new Reward(tempReward.getString("name"), tempReward.getString("date"), tempReward.getString("notes"), tempReward.getInt("value"));
                        rewardList.add(reward);
                    }
                    intent.putExtra("firstName",response.getString("firstName"));
                    intent.putExtra("lastName", response.getString("lastName"));
                    intent.putExtra("username", response.getString("username"));
                    intent.putExtra("department", response.getString("department"));
                    intent.putExtra("story", response.getString("story"));
                    intent.putExtra("position", response.getString("position"));
                    intent.putExtra("password", response.getString("password"));
                    intent.putExtra("pointsToAward", response.getInt("pointsToAward"));
                    intent.putExtra("admin", response.getBoolean("admin"));
                    intent.putExtra("image", response.getString("imageBytes"));
                    intent.putExtra("location", response.getString("location"));
                    intent.putExtra("points", Person.calcPoints(rewards));
                    intent.putExtra("rewards", (Serializable) rewardList);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(MainActivity.this, "Incorrect username/password", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        queue.add(thing);
    }

}