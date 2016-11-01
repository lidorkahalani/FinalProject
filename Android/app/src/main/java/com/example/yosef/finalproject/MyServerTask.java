package com.example.yosef.finalproject;

import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Yosef on 12/09/2016.
 */
public class MyServerTask extends AsyncTask<ConnectionData, Integer, ConnectionData> {
    protected static String HOST = "212.143.78.149";
    protected static int PORT = 54321;

    @Override
    protected ConnectionData doInBackground(ConnectionData... params) {

        try {
            // IP of the server
            Socket socket = new Socket(HOST, PORT);
            super.publishProgress(33);
            OutputStream output = socket.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(output);
            InputStream input = socket.getInputStream();
            ObjectInputStream ois = new ObjectInputStream(input);

            oos.writeObject(params[0]);
            super.publishProgress(66);
            ConnectionData responseData = (ConnectionData) ois.readObject();
            super.publishProgress(100);
            socket.close();

            return responseData;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            Log.e("Server Connection", "Connection Error");
            return null;
        }
    }

    @Override
    protected void onPostExecute(ConnectionData result) {
        super.onPostExecute(result);
        if(result==null){
            Log.e("Server Connection", "Connection Error");
        }
    }
}
