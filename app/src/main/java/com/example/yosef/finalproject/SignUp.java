package com.example.yosef.finalproject;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {
    EditText userName;
    EditText password;
    UsersDBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        userName=(EditText)findViewById(R.id.UserName);
        password=(EditText)findViewById(R.id.password);
        dbHandler = new UsersDBHandler(this);
    }
    public void goBack(View v){
        finish();
    }
    public void addNewUserToDB(View v){
        boolean emptyField=false;
        boolean succsesAdd=false;
        String uName=userName.getText().toString();
        String pass=password.getText().toString();
        if(uName.equals("")||pass.equals(""))
            emptyField=true;

        if(!emptyField) {
            User b = new User(uName, pass, 0);
                if (dbHandler.AddUser(b)) {
                    succsesAdd=true;
                    userName.setText("");
                    password.setText("");
                    Toast.makeText(this, "User added succesfuly", Toast.LENGTH_SHORT).show();
                }else //need to cathch the exception but didnt sucseed so i use if else (not good)
                    Toast.makeText(this, "User Name already in use", Toast.LENGTH_SHORT).show();
        }
        else if(emptyField)
            Toast.makeText(this, "Cannot get empty field", Toast.LENGTH_SHORT).show();

        else if(!succsesAdd)
            Toast.makeText(this, "User NOT added", Toast.LENGTH_SHORT).show();

    }
}
