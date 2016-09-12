package com.example.yosef.finalproject;

import java.io.Serializable;

/**
 * Created by Yosef on 12/09/2016.
 */
public class ConnectionData implements Serializable {
    public static final int NEW_ROOM = 1;
    public static final int JOIN_ROOM = 2;


    private int requestCode;
    private int responseStatus;
    private String userName;


    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
