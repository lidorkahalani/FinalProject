package com.example.yosef.finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DatabaseUtils;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.widget.LoginButton;

import java.util.ArrayList;

public class PersonalProfile extends AppCompatActivity {

    UsersDBHandler dbHandler;
    private ProfileTracker profileTracker;
    private AccessTokenTracker accsessTokenTracker;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_personalprofile);

        accsessTokenTracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                AccessToken.refreshCurrentAccessTokenAsync();
            }

        };
        accsessTokenTracker.startTracking();

        profileTracker=new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                profile=currentProfile;
                if(profile==null) {
                    SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(PersonalProfile.this);
                    SharedPreferences.Editor editor = myPref.edit();
                    editor.remove("username");
                    editor.commit();
                    Intent myIntent=new Intent(PersonalProfile.this,MainActivity.class);
                    startActivity(myIntent);
                    finish();

                }


            }
        };

        LoginButton authButton = (LoginButton)findViewById(R.id.facbookLogin);
        Button loginBtn=(Button)findViewById(R.id.logOut);
        if(profile==null) {
            authButton.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
        else {
            authButton.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        }

        dbHandler = new UsersDBHandler(this);
        TextView t=(TextView)findViewById(R.id.WelcomUserName);

        //get data from the device
        SharedPreferences myPref= PreferenceManager.getDefaultSharedPreferences(this);
        String uname=myPref.getString("username","");
        int score=myPref.getInt("score",0);

        t.setText("Hello "+uname+"\n"+"Toatal Score: "+Integer.toString(score));
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        accsessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    public void addCardToDB(View v){
        /* Intent myIntent=new Intent(this,AddCards.class);
        startActivity(myIntent);
        finish();*/
    }
    public void openRoom(View v){
        /* Intent myIntent=new Intent(this,GameWithFrnds.class);
        startActivity(myIntent);
        finish();*/
    }
    public void onBackPressed() {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PersonalProfile.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void startGame(View v){
        Intent myIntent=new Intent(this,GameScreen.class);
        startActivity(myIntent);
        finish();

    }
    public void joinToRoom(View v){
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                String roomName=userInput.getText().toString();
                                //now we need to cheek if the romm exist in dataBase
                                if(roomName.isEmpty()){//== DatabaseUtils.col["groupName"]){
                                    /* Intent myIntent=new Intent(this,watingRoom.class);
                                       startActivity(myIntent);
                                       finish();*/
                                }
                                else{
                                    new AlertDialog.Builder(PersonalProfile.this)
                                            .setTitle("Warning")
                                            .setMessage("Group Not Found!")
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // continue with delete
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                }

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
    public void showAllRecords(View v){
        ArrayList<User> UsersList = dbHandler.getAllUsers();
        Intent myIntent = new Intent(this,AllRecords.class);
        myIntent.putExtra("userList",UsersList);
        startActivity(myIntent); //app get crash her

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
