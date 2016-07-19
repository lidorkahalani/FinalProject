package com.example.yosef.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PersonalProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalprofile);
        TextView t=(TextView)findViewById(R.id.WelcomUserName);

        //get data from the device
        SharedPreferences myPref= PreferenceManager.getDefaultSharedPreferences(this);
        String uname=myPref.getString("username","");
        int score=myPref.getInt("score",0);

       // GlobalAppData appData=GlobalAppData.getInstance(); //get data from appliction
        t.setText("Hello "+uname+"\n"+"Toatal Score: "+Integer.toString(score));
    }

    public void addCardToDB(View v){


    }

    public void logOut(View v){
        //remove the data from device
        SharedPreferences myPref= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor=myPref.edit();
        editor.remove("username");
        editor.commit();

        Intent myIntent=new Intent(this,MainActivity.class);
        startActivity(myIntent);
        finish();
    }
}
