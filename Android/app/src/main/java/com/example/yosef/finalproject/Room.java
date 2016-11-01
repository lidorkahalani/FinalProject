package com.example.yosef.finalproject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Room extends AppCompatActivity {
    private String nameRoom;
    private  EditText roomName;
    private  Socket socket;
    private  OutputStream output;
    private  ObjectOutputStream oos ;
    private  InputStream input ;
    private ObjectInputStream ois ;
    private  User curentUser;
    boolean isConnected;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         roomName=(EditText)findViewById(R.id.roomName);
        setContentView(R.layout.activity_room);

    }

    public void creatRoom(View v){
        nameRoom=roomName.getText().toString();
       //send the room name to server
        new ConnectToServerTask().execute();

    }

    class ConnectToServerTask extends AsyncTask<Void, Void, Boolean>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            runOnUiThread(new Runnable(){
                public void run()
                {

                }
            });

			/*dialog =  ProgressDialog.show(GameScreenActivity.this, "wait!!!",
				    "connecting to server...", true);*/
        }
        @Override
        protected Boolean doInBackground(Void... pharms)
        {

            Log.e("TEST DEBUG", "start of connection");
            try {
                // IP of the server
                socket = new Socket(MyServerTask.HOST, MyServerTask.PORT);
                output = socket.getOutputStream();

                oos = new ObjectOutputStream(output);
                input = socket.getInputStream();
                ois = new ObjectInputStream(input);

                isConnected = true;
                Log.e("TEST DEBUG", "end of connection - true");

            } catch (Exception e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                Log.e("Server Connection", "Connection Error");
                return false;
            }

            return true;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if(result)
            {

            }
        }

    }
}
