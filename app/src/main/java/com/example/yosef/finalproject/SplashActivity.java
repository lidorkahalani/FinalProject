package com.example.yosef.finalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    ProgressBar pbHorizontal;
    ProgressBar circleProgresBar;
    TextView Precent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        pbHorizontal= (ProgressBar)findViewById(R.id.progressBar);
        circleProgresBar= (ProgressBar)findViewById(R.id.circleProgresBar);
        Precent =(TextView)findViewById(R.id.Precent);
        MyLoaderTask openScreen=new MyLoaderTask();
        openScreen.execute("");
    }

    class MyLoaderTask extends AsyncTask<String ,Integer,Boolean> {

        //this run on UI
        @Override
        protected void onProgressUpdate(Integer... values) {
            circleProgresBar.setProgress(values[0]);
            Precent.setText(Integer.toString(values[0])+"%");
            pbHorizontal.setProgress(values[0]);

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
            pbHorizontal.setProgress(100);
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
}
