package com.example.yosef.finalproject;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MainMenu extends AppCompatActivity {

    private UsersDBHandler dbHandler;
    private ProfileTracker profileTracker;
    private AccessTokenTracker accsessTokenTracker;
    private CallbackManager callBack;
    private Profile profile;
    private User currentPlayer;
    private ArrayList<User> allUsers = new ArrayList<User>();
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private TextView title;

    private String roomName;
    private LoginButton facebookButton;
    private Button logOut;
    private Game newGame = new Game();
    final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE =1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_personalprofile);
        accsessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                AccessToken.refreshCurrentAccessTokenAsync();
            }

        };
        accsessTokenTracker.startTracking();

        callBack = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callBack, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                profileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                        profile = currentProfile;
                        profileTracker.stopTracking();


                    }
                };
                profileTracker.startTracking();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainMenu.this, error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        facebookButton = (LoginButton) findViewById(R.id.facbookLogin);
        logOut = (Button) findViewById(R.id.btnlogOut);

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                profile = currentProfile;

                if (profile == null) {
                    SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(MainMenu.this);
                    SharedPreferences.Editor editor = myPref.edit();
                    editor.remove("username");
                    editor.commit();
                    Intent myIntent = new Intent(MainMenu.this, MainActivity.class);
                    startActivity(myIntent);
                    finish();
                }
                setLayout();

            }
        };

        title = (TextView) findViewById(R.id.WelcomUserName);
        btn1 = (Button) findViewById(R.id.btnOpenRoomId);
        btn3 = (Button) findViewById(R.id.btnAddCardId);
        btn4 = (Button) findViewById(R.id.btnShowMyCardId);
        btn2 = (Button) findViewById(R.id.btnJoinRoomId);
        btn5 = (Button) findViewById(R.id.btnlogOut);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "Matias_Webfont.ttf");
        btn1.setTypeface(typeface);
        btn2.setTypeface(typeface);
        btn3.setTypeface(typeface);
        btn4.setTypeface(typeface);
        btn5.setTypeface(typeface);
        title.setTypeface(typeface);

        SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(MainMenu.this);
        String uname = myPref.getString("username", "");
        String password = myPref.getString("password", "");
        int score = myPref.getInt("score", 0);
        int userId = myPref.getInt("user_id", 0);

       // currentPlayer = (User) getIntent().getSerializableExtra("User");


        currentPlayer = new User(uname, password, score, userId);


        dbHandler = new UsersDBHandler(this);
        setLayout();
        checkPermission();

    }

    public void checkPermission(){

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(getResources().getString(R.string.must_permission))
                            .setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    checkPermission();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (callBack.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }

    public void setLayout() {
        if (AccessToken.getCurrentAccessToken() == null) {
            facebookButton.setVisibility(View.GONE);
            logOut.setVisibility(View.VISIBLE);
        } else {
            facebookButton.setVisibility(View.VISIBLE);
            logOut.setVisibility(View.GONE);
        }
        TextView t = (TextView) findViewById(R.id.WelcomUserName);
        SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(MainMenu.this);
        String uname = myPref.getString("username", "");
        String password = myPref.getString("password", "");
        int score = myPref.getInt("score", 0);
        //int score = currentPlayer.getScore();
        t.setText(getResources().getString(R.string.wellcome) + ":  " + uname + "\n" + getResources().getString(R.string.score) + ":  " +score );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accsessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    public void addNewSeries(View v) {

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompt_category, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.categoryEdit);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                String categoryName=null;
                                categoryName =new String( userInput.getText().toString());

                                if (!categoryName.isEmpty()) {
                                    Intent myIntent = new Intent(MainMenu.this, AddNewSeries.class);
                                    myIntent.putExtra("currenUsrt",currentPlayer);
                                    myIntent.putExtra("categoryName",categoryName);
                                    myIntent.putExtra("Game",newGame);
                                    startActivity(myIntent);
                                    //finish();
                                } else {
                                    new AlertDialog.Builder(MainMenu.this)
                                            .setTitle(getResources().getString(R.string.Warning))
                                            .setMessage(getResources().getString(R.string.empty_field))
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
                .setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }

    public void openRoom(View v) {
      LayoutInflater li = LayoutInflater.from(MainMenu.this);
        View dialogView = li.inflate(R.layout.open_room_dialog_layout, null);

        final EditText roomNameInput = (EditText) dialogView.findViewById(R.id.room_name_input);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
        builder.setView(dialogView);
        builder.setTitle(getResources().getString(R.string.open_room_dialog_title));
        builder.setMessage(getResources().getString(R.string.open_room_dialog_message));
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("Room name:", roomNameInput.getText().toString());
                roomName=roomNameInput.getText().toString();
                //1. send the room name to server
                //new SendRoomName().execute(roomNameInput.getText().toString());
                //new openNewRoom().execute(roomName);

                Intent intent=new Intent(MainMenu.this,Room.class);
                newGame.setGame_name(roomName);
                intent.putExtra("Game",newGame);
                intent.putExtra("currentPlayer",currentPlayer);
                intent.putExtra("isNewRoom",true);
                startActivity(intent);
                finish();


            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    public void joinToRoom(View v) {
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
                .setPositiveButton(getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                roomName = userInput.getText().toString();
                                if (!roomName.isEmpty()) {
                                    Intent intent=new Intent(MainMenu.this,Room.class);
                                    newGame.setGame_name(roomName);
                                    intent.putExtra("Game",newGame);
                                    intent.putExtra("currentPlayer",currentPlayer);
                                    intent.putExtra("isNewRoom",false);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    new AlertDialog.Builder(MainMenu.this)
                                            .setTitle(getResources().getString(R.string.Warning))
                                            .setMessage(getResources().getString(R.string.empty_field))
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
                .setNegativeButton(getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    public void onBackPressed() {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.exitWarning))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainMenu.this.finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void ShowMySerie(View v) {
         Intent myIntent = new Intent(this,ShowMySerie.class);
        startActivity(myIntent);
    }

    public void logOut(View v) {
        //remove the data from device
        SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = myPref.edit();
        editor.remove("username");
        editor.commit();
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
        finish();
    }

    public void startGame(View v){

        //checkPermission();

        Intent intent=new Intent(MainMenu.this,GameScreen.class);
        intent.putExtra("debug",true);
        startActivity(intent);
    }

    public void RecordTable(View v){
        Intent myIntent = new Intent(this,AllRecords.class);
        startActivity(myIntent);
    }




}
