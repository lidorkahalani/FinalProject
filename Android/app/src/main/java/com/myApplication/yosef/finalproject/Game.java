package com.myApplication.yosef.finalproject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Yosef on 03-Nov-16.
 */

public class Game implements Serializable {
    private int game_id;
    private String game_name;
    private Date createDate;


    public Game(int game_id,String game_name) {
        setGame_id(game_id);
        setGame_name(game_name);
    }

    public Game(){

    }

    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}





