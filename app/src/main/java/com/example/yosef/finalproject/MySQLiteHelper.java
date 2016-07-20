package com.example.yosef.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Yosef on 03/05/2016.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {


    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_USERS = "CREATE TABLE "
                + UserDBConstants.User_Table_Name + "("
                + UserDBConstants.User_Name + " TEXT PRIMARY KEY," +
                UserDBConstants.Password + " TEXT," +
                UserDBConstants.Score + " INTEGER" + ")";

        db.execSQL(CREATE_TABLE_USERS);

       /* db.execSQL("CREATE TABLE "+ UserDBConstants.User_Table_Name +
        "( "+ UserDBConstants.User_Name + " TEXT PRIMARY KEY,"+
        "( "+ UserDBConstants.Password + " TEXT,"+
                UserDBConstants.Score + " INTEGER)");*/

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
