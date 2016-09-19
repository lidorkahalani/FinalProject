package com.example.yosef.finalproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

public class SplashActivity extends AppCompatActivity {
    ProgressBar circleProgresBar;
    TextView Precent;
    ArrayList <Card> deck=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        circleProgresBar= (ProgressBar)findViewById(R.id.circleProgresBar);
        Precent =(TextView)findViewById(R.id.Precent);
        //new getAllCards().execute();
        MyLoaderTask openScreen=new MyLoaderTask();
        openScreen.execute("");
    }

    class MyLoaderTask extends AsyncTask<String ,Integer,Boolean> {

        //this run on UI
        @Override
        protected void onProgressUpdate(Integer... values) {
            circleProgresBar.setProgress(values[0]);
            Precent.setText(Integer.toString(values[0])+"%");


        }

        @Override
        protected Boolean doInBackground(String... params) {
            for(int i=0;i<11;i++) {
                try {
                    publishProgress(i*10);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return true;
            //after finsh onPostExecute() method execute
        }

        //this run on UI
        @Override
        protected void onPostExecute(Boolean result) {

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(result) {
                finish();
                Intent myIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        }
    }

    public class getAllCards extends AsyncTask<String, Void, Boolean> {
        String get_all_card_url = "http://mysite.lidordigital.co.il/Quertets/db/getAllCard.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();



        @Override
        protected Boolean doInBackground(String... params) {
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(get_all_card_url, "POST", parms);
                if (response.getInt("succsses") == 1) {
                    return true;
                } else {
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }


        }


        protected void onPostExecute(Boolean result) {
            if (result) {
                finish();
                Intent myIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(myIntent);
            }

        }

    }


}
