package com.example.yosef.finalproject;

import java.io.Serializable;

/**
 * Created by Yosef on 17/05/2016.
 */
public class User implements Serializable {
    private String userName;
    private String password;
    private int score;



    public User(String userName, String password,int score) {
        setUserName(userName);
        setPassword(password);
        setScore(score);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + getUserName() + '\'' +
                ", password='" + getPassword() + '\'' +
                ", Score='" + getScore() + '\'' +
                '}';
    }
}