package com.johar_rewards.johar_rewards;

import android.content.DialogInterface;
import android.content.Intent;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.johar_rewards.johar_rewards.R;

import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CreateProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile);
    }

    public void onSaveClick(View view){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        EditText uName = (EditText)findViewById(R.id.username);
                        EditText pWord = (EditText)findViewById(R.id.password);
                        EditText fName = (EditText)findViewById(R.id.first_name);
                        EditText lName = (EditText)findViewById(R.id.last_name);
                        EditText dept = (EditText)findViewById(R.id.department);
                        EditText pos = (EditText)findViewById(R.id.position);
                        EditText myStory = (EditText)findViewById(R.id.story);
                        ImageView pic = (ImageView)findViewById(R.id.pic);
                        CheckBox administrator = (CheckBox)findViewById(R.id.admin);
                        Boolean admin = false;
                        if (administrator.isChecked()){
                            admin =  true;
                        }
                        pic.setDrawingCacheEnabled(true);
                        pic.buildDrawingCache();
                        Bitmap bm = pic.getDrawingCache();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        String username = uName.getText().toString();
                        String password = pWord.getText().toString();
                        String firstName = fName.getText().toString();
                        String lastName = lName.getText().toString();
                        String department = dept.getText().toString();
                        String position = pos.getText().toString();
                        String story = myStory.getText().toString();
                        JSONObject newPerson = new JSONObject();
                        JSONArray arr = new JSONArray();
                        String image = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        RequestQueue queue = Volley.newRequestQueue(CreateProfile.this);
                        try {
                            newPerson.put("studentId", "A20393623");
                            newPerson.put("username", username);
                            newPerson.put("password", password);
                            newPerson.put("firstName", firstName);
                            newPerson.put("lastName", lastName);
                            newPerson.put("pointsToAward", 1000);
                            newPerson.put("department", department);
                            newPerson.put("story", story);
                            newPerson.put("position", position);
                            newPerson.put("admin", admin);
                            newPerson.put("location", "Chicago, IL");
                            newPerson.put("imageBytes", image);
                            newPerson.put("rewardRecords", arr);

                        } catch (JSONException e) {

                        }

                        String url = "http://inspirationrewardsapi-env.6mmagpm2pv.us-east-2.elasticbeanstalk.com/profiles";
                        JsonObjectRequest thing = new JsonObjectRequest(Request.Method.POST, url, newPerson, new Response.Listener<JSONObject>() {
                            public void onResponse(JSONObject response) {
                                try {
                                    Intent intent = new Intent(CreateProfile.this, Profile.class);
                                    Bitmap bm = BitmapFactory.decodeByteArray(response.getString("imageBytes").getBytes(), 0, response.getString("imageBytes").length());
                                    List<Reward> rewardList = new ArrayList<Reward>();
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
                                    intent.putExtra("points", 0);
                                    intent.putExtra("rewards", (Serializable) rewardList);
                                    LayoutInflater inflater = getLayoutInflater();
                                    View layout = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.custom_toast_container));
                                    TextView tv = (TextView) layout.findViewById(R.id.txtvw);
                                    tv.setText("Profile Created");
                                    Toast toast = new Toast(getApplicationContext());
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    toast.setView(layout);
                                    toast.show();
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast toast = Toast.makeText(CreateProfile.this, "An account with that username already exists", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                        queue.add(thing);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateProfile.this);
        builder.setMessage("Are you sure you want to create this account?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).setIcon(R.drawable.logo).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView pic = (ImageView)findViewById(R.id.pic);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    pic.setImageBitmap(bitmap);
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    pic.setImageBitmap(bitmap);
                }
                break;
        }
    }

    public void onPicClick(View view){
        new AlertDialog.Builder(CreateProfile.this)
                .setTitle("Add Image")
                .setMessage("Add Image from")

                .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(photoCaptureIntent, 0);
                    }
                })

                .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto , 1);
                    }
                })
                .setIcon(R.drawable.logo)
                .show();
    }


    public void onBackClick(View view){
        finish();
    }
}
