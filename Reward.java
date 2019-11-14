package com.johar_rewards.johar_rewards;

import java.io.Serializable;

public class Reward implements Serializable {
    private String name, date, comment;
    private int value;

    public Reward(String name, String date, String comment, int value) {
        this.name = name;
        this.date = date;
        this.comment = comment;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
