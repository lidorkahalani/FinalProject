package com.myApplication.yosef.finalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;


public class SplashActivity extends AppCompatActivity {
    private ProgressBar circleProgresBar;
    private TextView Precent;
    private TextView noConnectionMassage;
    private ArrayList<Card> deck = new ArrayList<>();
     static int counter=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        circleProgresBar = (ProgressBar) findViewById(R.id.circleProgresBar);
        Precent = (TextView) findViewById(R.id.Precent);
        noConnectionMassage=(TextView)findViewById(R.id.noInternetMAssafe);

        //new getAllCards().execute();
        MyLoaderTask openScreen = new MyLoaderTask();
        openScreen.execute("");
    }

    class MyLoaderTask extends AsyncTask<String, Integer, Integer> {

        //this run on UI
        @Override
        protected void onProgressUpdate(Integer... values) {
            circleProgresBar.setProgress(values[0]);
            //Precent.setText(Integer.toString(values[0]) + "%");



        }

        @Override
        protected Integer doInBackground(String... params) {
                try {
                    JSONParser json = new JSONParser();
                    ConnectivityManager cm =
                            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo netInfo = cm.getActiveNetworkInfo();
                    Thread.sleep(2500);

                    if(netInfo==null||!netInfo.isConnectedOrConnecting())
                        return 1;
                    LinkedHashMap<String, String> parms = new LinkedHashMap<>();
                    JSONObject response = json.makeHttpRequest(ServerUtils.connectionToDB, "GET", parms);
                    if (response!= null) {
                        return 2;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            return 0;
        }

        //this run on UI
        @Override
        protected void onPostExecute(Integer result) {

            if (result==0) {
                finish();
                Intent myIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(myIntent);
            } else if(result==1){
                noConnectionMassage.setVisibility(View.VISIBLE);
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setMessage(getResources().getString(R.string.no_internet))
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SplashActivity.this.finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }else if(result==2){
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setMessage(getResources().getString(R.string.server_not_responding))
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SplashActivity.this.finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }

    }



    public void checkServerResponse(){

    }
}
