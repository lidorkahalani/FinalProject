package com.example.yosef.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Student on 03/05/2016.
 */
public class UsersDBHandler {

    private MySQLiteHelper dbHelper;

    public UsersDBHandler(Context context)
    {
        dbHelper = new MySQLiteHelper(context, UserDBConstants.DBName, null, UserDBConstants.User_DB_VESRSION);
    }

    // returns true/false if the addition was successful
    public boolean AddUser(User newUser)
    {

        // this opens the connection to the DB
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues columnValues = new ContentValues();
        columnValues.put(UserDBConstants.User_Name, newUser.getUserName());
        columnValues.put(UserDBConstants.Password, newUser.getPassword());
        columnValues.put(UserDBConstants.Score, newUser.getScore());

        long result = db.insert(UserDBConstants.User_Table_Name, null, columnValues);

        db.close();

        // when result is -1 it means the insert has failed, so when NOT -1 it was successful
        return (result != -1);

    }

    public ArrayList<User> getAllUsers()
    {
        ArrayList<User> usersList = new ArrayList<User>();
        // this opens the connection to the DB
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // select * from BOOKS table
        Cursor usersCursor = db.query(UserDBConstants.User_Table_Name, null, null, null, null, null, null);
        // each round in the loop is a record in the DB
        while(usersCursor.moveToNext()) {
            String userName = usersCursor.getString(0);
            String password = usersCursor.getString(1);
            int score=usersCursor.getInt(2);
            User b = new User(userName,password,score);
            usersList.add(b);
        }

        return usersList;

    }

}
