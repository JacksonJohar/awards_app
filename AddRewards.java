package com.johar_rewards.johar_rewards;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.johar_rewards.johar_rewards.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddRewards extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_rewards);
        Bundle i = getIntent().getBundleExtra("b2");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView name = (TextView) findViewById(R.id.name);
        TextView dept = (TextView) findViewById(R.id.department);
        TextView pos = (TextView) findViewById(R.id.position);
        TextView myStory = (TextView) findViewById(R.id.story);
        TextView points = (TextView) findViewById(R.id.points);
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        ImageView pic = (ImageView) findViewById(R.id.pic);
        Bitmap b = BitmapFactory.decodeByteArray(
                getIntent().getByteArrayExtra("image"),0,getIntent()
                        .getByteArrayExtra("image").length);
        pic.setImageBitmap(b);
        title.setText(i.getString("name"));
        name.setText(i.getString("name"));
        dept.setText(i.getString("department"));
        pos.setText(i.getString("position"));
        myStory.setText(i.getString("story"));
        points.setText(i.getString("points"));
    }

    public void onSaveClick(View view){
        Bundle b = getIntent().getBundleExtra("b");
        Bundle b2 = getIntent().getBundleExtra("b2");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        String date = dateFormat.format(calendar.getTime());
        EditText newPoints = (EditText) findViewById(R.id.pointstosend);
        int points = Integer.parseInt(newPoints.getText().toString());
        EditText comment = (EditText) findViewById(R.id.comment);
        String comm = comment.getText().toString();
        JSONObject source = new JSONObject();
        JSONObject target = new JSONObject();
        JSONObject obj = new JSONObject();
        try {
            source.put("studentId", "A20393623");
            source.put("username", b.getString("username"));
            source.put("password", b.getString("password"));
            target.put("studentId", "A20393623");
            target.put("username", b2.getString("username"));
            target.put("name", b2.getString("name2"));
            target.put("date", date);
            target.put("notes", comm);
            target.put("value", points);
            obj.put("source", source);
            obj.put("target", target);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = obj.toString();
        RequestQueue queue = Volley.newRequestQueue(AddRewards.this);
        String url = "http://inspirationrewardsapi-env.6mmagpm2pv.us-east-2.elasticbeanstalk.com/rewards";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Bundle b2 = getIntent().getBundleExtra("b2");
                new AlertDialog.Builder(AddRewards.this)
                        .setTitle("Add Points")
                        .setMessage("Add points to " + b2.getString("name2") + "?")

                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                LayoutInflater inflater = getLayoutInflater();
                                View layout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.custom_toast_container));
                                TextView tv = (TextView) layout.findViewById(R.id.txtvw);
                                tv.setText("Add Reward Succeeded");
                                Toast toast = new Toast(getApplicationContext());
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setView(layout);
                                toast.show();
                                Bundle b = getIntent().getBundleExtra("b");
                                Intent i = new Intent(AddRewards.this, Leaderboard.class);
                                i.putExtras(b);
                                startActivity(i);
                            }
                        })

                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(R.drawable.logo)
                        .show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("qq", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    // can get more details such as response.headers
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };
        queue.add(stringRequest);
    }

}
