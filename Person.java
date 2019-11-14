package com.johar_rewards.johar_rewards;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Person implements Serializable {
    private String username, firstName, lastName, location, department, position, story, password;
    private transient Bitmap image;
    private int points, pointsToAward;
    private boolean admin;
    JSONArray rewards;

    public String toString() {
        return "Person{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", location='" + location + '\'' +
                ", department='" + department + '\'' +
                ", position='" + position + '\'' +
                ", story='" + story + '\'' +
                ", password='" + password + '\'' +
                ", image='" + image + '\'' +
                ", points=" + points +
                ", pointsToAward=" + pointsToAward +
                ", admin=" + admin +
                ", rewards=" + rewards +
                '}';
    }

    public Person(){

    }

    public Person(String firstName, String lastName, String username, String department, String story, String position, String password,
                  int pointsToAward, boolean admin, Bitmap image, String location, JSONArray rewards){
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.department = department;
        this.story = story;
        this.position = position;
        this.password = password;
        this.pointsToAward = pointsToAward;
        this.admin = admin;
        this.image = image;
        this.location = location;
        this.rewards = rewards;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPointsToAward() {
        return pointsToAward;
    }

    public void setPointsToAward(int pointsToAward) {
        this.pointsToAward = pointsToAward;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public JSONArray getRewards() {
        return rewards;
    }

    public void setRewards(JSONArray rewards) {
        this.rewards = rewards;
    }

    public static int calcPoints(JSONArray rewards) {
        rewards = rewards;
        int points = 0;
        for (int i = 0; i < rewards.length(); i++){
            try {
                JSONObject obj = rewards.getJSONObject(i);
                int currPoint = obj.getInt("value");
                points = points + currPoint;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return points;
    }
}
