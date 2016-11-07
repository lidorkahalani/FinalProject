package com.example.yosef.finalproject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Yosef on 17/05/2016.
 */
public class User implements Serializable {
    private String userName;
    private String password;
    private int score;
    private int userID;
    private ArrayList<Card> playerCards=new ArrayList<>();

    public User(){

    }


    public User(String userName, String password,int score,int userID) {
        setUserName(userName);
        setPassword(password);
        setScore(score);
        setUserID(userID);
    }

    public User(String userName, String password,int userID) {
        setUserName(userName);
        setPassword(password);
        setUserID(userID);
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

    public ArrayList<Card> getPlayerCards() {
        return playerCards;
    }

    public void setPlayerCards(ArrayList<Card> playerCards) {
        this.playerCards = playerCards;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
