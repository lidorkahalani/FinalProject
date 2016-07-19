package com.example.yosef.finalproject;
//import com.facebook.FacebookSdk;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText userName;
    EditText password;
    UsersDBHandler dbHandler;
    String inputUserName;
    String inputPassword;
    String Name;
    String passwo;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHandler = new UsersDBHandler(this);
        SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(this);
        String uname = myPref.getString("username", null);
        if (uname != null && !uname.equals("")) {

            Intent myIntent = new Intent(this, PersonalProfile.class);
            startActivity(myIntent);
            finish();
        }
        userName = (EditText) findViewById(R.id.UserName);
        password = (EditText) findViewById(R.id.password);

      /* String []a={"dsd","sds","cd","ds"};
        Cards animel=new Cards("Animles","red",
                             getFileStreamPath("C:\\Users\\Yosef\\AndroidStudioProjects\\FinalProject\\app\\src\\main\\res\\mipmap-hdpi")
                             ,a,1);*/
    }

    public void logIn(View v) {
        boolean userExsist=true;
        MySQLiteHelper dbHelper=new MySQLiteHelper(this, UserDBConstants.DBName, null, UserDBConstants.User_DB_VESRSION);
        inputUserName = userName.getText().toString();
        inputPassword = password.getText().toString();

        if(inputUserName.equals("")||inputPassword.equals("")){
            userExsist=false;
        }
        else {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            // select * from BOOKS User
            Cursor usersCursor = db.query(UserDBConstants.User_Table_Name, null, null, null, null, null, null);
            // each round in the loop is a record in the DB
            while (usersCursor.moveToNext()) {
                Name = usersCursor.getString(0);
                passwo = usersCursor.getString(1);
                score=Integer.parseInt(usersCursor.getString(2));
                if (inputUserName.equals(Name) && inputPassword.equals(passwo)) {
                    userExsist = true;
                    break;
                }
                userExsist = false;
            }
        }

        if (userExsist) {
            SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = myPref.edit();
            editor.putString("username", Name);
            editor.putInt("score",score);

            editor.commit();

            Intent myIntent = new Intent(this, PersonalProfile.class);
            startActivity(myIntent);
            finish();

        } else
            Toast.makeText(this, "invalid user ! please try Again", Toast.LENGTH_LONG).show();

    }

    public void SignUp(View v) {
        Intent myIntent = new Intent(this, SignUp.class);
        startActivity(myIntent);
    }

    public void showAllRecords(View v) {
        ArrayList<User> UsersList = dbHandler.getAllUsers();
        Intent myIntent = new Intent(this,AllRecords.class);
        myIntent.putExtra("userList",UsersList);
        startActivity(myIntent); //app get crash her

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.exit) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage(R.string.exitWarning);
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }  else if (item.getItemId() == R.id.changeLang) {
            Configuration newConfig = new Configuration();
            newConfig.locale = Locale.ENGLISH; //cant change to hebrew
            onConfigurationChanged(newConfig);
        }
        return true;
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);

        // Checks the active language
        if (newConfig.locale == Locale.ENGLISH) {
            Toast.makeText(this, "English", Toast.LENGTH_SHORT).show();
        } /*else if (newConfig.locale.toString() == LOCALE_IW){
            Toast.makeText(this, "Hebrew", Toast.LENGTH_SHORT).show();*/
        }

    public void logInToFacebook(View v){

        Toast.makeText(this,"Log in facebook",Toast.LENGTH_SHORT).show();

        }
    }

