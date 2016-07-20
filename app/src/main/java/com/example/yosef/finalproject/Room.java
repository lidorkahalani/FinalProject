package com.example.yosef.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Room extends AppCompatActivity {
    String nameRoom;
    EditText roomName;
    User curentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         roomName=(EditText)findViewById(R.id.roomName);
        setContentView(R.layout.activity_room);
    }

    public void creatRoom(View v){
        nameRoom=roomName.getText().toString();
        // add the nameRoom The appropriate column  in data base


        wattingForPlayers(v);
    }

    public void wattingForPlayers(View v){



    }
}
