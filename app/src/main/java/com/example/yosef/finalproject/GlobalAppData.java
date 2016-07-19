package com.example.yosef.finalproject;

import android.app.Application;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Yosef on 17/05/2016.
 */
public class GlobalAppData extends Application {
    private static GlobalAppData myApp;

    public static GlobalAppData getInstance(){
        if(myApp==null) {
            myApp = new GlobalAppData();
            return myApp;
        }
        else
            return myApp;

    }

    private String userName;
    private String password;
    private ArrayList<User> userList;

    @Override
    public void onCreate(){
        super.onCreate();
        Toast.makeText(this,"Applection Create",Toast.LENGTH_SHORT).show();

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
}
