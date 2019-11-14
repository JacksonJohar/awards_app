package com.johar_rewards.johar_rewards;

import android.app.Activity;
import android.content.Intent;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.johar_rewards.johar_rewards.R;

public class Profile extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        Bundle i = getIntent().getExtras();
        int pointsToGive = i.getInt("pointsToAward");
        List<Reward> rewardList = new ArrayList<Reward>();
        rewardList = (List<Reward>)getIntent().getSerializableExtra("rewards");
        RecyclerView rewardRecycler = (RecyclerView) findViewById(R.id.history);
        RewardAdapter rAdapter = new RewardAdapter(rewardList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rewardRecycler.setLayoutManager(mLayoutManager);
        rewardRecycler.setItemAnimator(new DefaultItemAnimator());
        rewardRecycler.setAdapter(rAdapter);
        TextView uName = (TextView) findViewById(R.id.username);
        TextView name = (TextView) findViewById(R.id.name);
        TextView dept = (TextView) findViewById(R.id.department);
        TextView pos = (TextView) findViewById(R.id.position);
        TextView myStory = (TextView) findViewById(R.id.story);
        TextView pointsToAward = (TextView) findViewById(R.id.toAward);
        TextView points = (TextView) findViewById(R.id.points);
        TextView location = (TextView) findViewById(R.id.location);
        location.setText("Chicago, IL");
        ImageView pic = (ImageView) findViewById(R.id.pic);
        uName.setText("(" + i.getString("username") + ")");
        name.setText(i.getString("lastName") + ", " + i.getString("firstName"));
        dept.setText(i.getString("department"));
        pos.setText(i.getString("position"));
        myStory.setText(i.getString("story"));
        pointsToAward.setText(String.valueOf(pointsToGive));
        points.setText(String.valueOf(i.getInt("points")));
        String byteArray = i.getString("image");
        byte[] bytes = Base64.decode(byteArray,Base64.NO_WRAP);
        InputStream inputStream  = new ByteArrayInputStream(bytes);
        Bitmap bm = BitmapFactory.decodeStream(inputStream);
        pic.setImageBitmap(bm);
    }

    public void onEditClick(View view) {
        Intent intent = new Intent(this, EditProfile.class);
        Bundle i = getIntent().getExtras();
        intent.putExtras(i);
        startActivity(intent);
    }

    public void onLeaderBoardClick(View view) {
        Intent intent = new Intent(this, Leaderboard.class);
        Bundle i = getIntent().getExtras();
        intent.putExtras(i);
        startActivity(intent);
    }
}
