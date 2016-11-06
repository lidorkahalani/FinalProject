package com.example.yosef.finalproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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
import com.google.gson.JsonParser;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;

import static org.apache.http.HttpHeaders.USER_AGENT;

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

    String roomName;
    boolean correctInput = false;
    LoginButton facebookButton;
    Button logOut;
    ProgressDialog pDialog;
    Timer timer;
    boolean timerFlag = false;
    Game newGame = new Game();

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
        currentPlayer = new User(uname, password, score, userId);
        dbHandler = new UsersDBHandler(this);
        setLayout();

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
        t.setText(getResources().getString(R.string.wellcome) + ":  " + uname + "\n" + getResources().getString(R.string.score) + ":  " + Integer.toString(score));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accsessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    public void addCardToDB(View v) {
        Intent myIntent = new Intent(this, AddCards.class);
        startActivity(myIntent);
        finish();
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
                new openNewRoom().execute(roomName);


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
                .setNegativeButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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
                                if (roomName != null) {
                                    new joinToRoom().execute(roomName);

                                    //now we need to cheek if the romm exist in dataBase
                                    //and open new screen thats shows all players already connected
                                   /*  Intent myIntent=new Intent(this,watingRoom.class);
                                       startActivity(myIntent);
                                       finish();*/
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

    public void ShowMyCards(View v) {
        Intent myIntent = new Intent(this, ShowMyCards.class);
        startActivity(myIntent); //app get crash her

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

    public class openNewRoom extends AsyncTask<String, User, Integer> {
        LinkedHashMap parms = new LinkedHashMap<>();
        String roomName;
        ObjectInputStream ois;
        ObjectOutputStream oos;

        @Override
        protected Integer doInBackground(String... params) {
            String openNewRoom = "http://10.0.2.2/final_project/db/openNewRoom.php";
            //String openNewRoom="http://mysite.lidordigital.co.il/Quertets/db/openNewRoom.php";


            parms.put("room_name", params[0]);
            parms.put("user_id", currentPlayer.getUserID());
            // parms.put("current_user",currentPlayer);
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(openNewRoom, "POST", parms);

                if (response.getInt("successes") == 1) {
                    newGame.setGame_name(roomName);
                    newGame.setGame_id(response.getInt("game_id"));
                    return 1;
                } else
                    return 0;

            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }

        }


        protected void onPostExecute(Integer result) {
            if (result == 1) {//all good
                //2. show progress dialog - waiting for 3 more players
                pDialog = new ProgressDialog(MainMenu.this);
                pDialog.setIndeterminate(true);
                pDialog.setCancelable(false);
                pDialog.setMessage(getResources().getString(R.string.waiting_for_players));

                pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timer.cancel();
                        timerFlag = false;
                    }
                });

                pDialog.show();
                timer = new Timer();
                timerFlag = true;
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (timerFlag) {
                            new waitForOtherPlayer().execute(roomName);
                            //new GetRoomStatus().execute(roomName);
                        }
                    }
                }, 3000);
            } else if (result == 0) {//room name already in use
                Toast.makeText(MainMenu.this
                        , getResources().getString(R.string.room_name_in_use),
                        Toast.LENGTH_LONG).show();
            } else if (result == -1) {//connection problem
                Toast.makeText(MainMenu.this
                        , getResources().getString(R.string.connection_error),
                        Toast.LENGTH_LONG).show();
            }

        }

    }

    public class joinToRoom extends AsyncTask<String, Void, Boolean> {
        String joinToRoom = "http://10.0.2.2/final_project/db/joinToRoom.php";
        // String joinToRoom = "http://localhost/final_project/db/joinToRoom.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();


        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("room_name", params[0]);
            parms.put("user_id", String.valueOf(currentPlayer.getUserID()));
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(joinToRoom, "POST", parms);
                if (response.getInt("succsses") == 1) {
                    newGame.setGame_name(params[0]);
                    newGame.setGame_id(response.getInt("game_id"));
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }


        protected void onPostExecute(Boolean result) {
            if (result) {
                //Toast.makeText(MainMenu.this, getResources().getString(R.string.you_join_to_room) + " " + roomName, Toast.LENGTH_LONG).show();
                pDialog = new ProgressDialog(MainMenu.this);
                pDialog.setIndeterminate(true);
                pDialog.setCancelable(false);
                pDialog.setMessage(getResources().getString(R.string.waiting_for_players));

                pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timer.cancel();
                        timerFlag = false;
                    }
                });

                pDialog.show();
                timer = new Timer();
                timerFlag = true;
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (timerFlag) {
                            new waitForOtherPlayer().execute(roomName);
                            //new GetRoomStatus().execute(roomName);
                        }
                    }
                }, 3000);
            } else {
                new AlertDialog.Builder(MainMenu.this)
                        .setTitle(getResources().getString(R.string.Warning))
                        .setMessage(getResources().getString(R.string.Group_Not_Found))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

            //Toast.makeText(MainMenu.this, getResources().getString(R.string.you_didnt_join_to_room) + " " + roomName, Toast.LENGTH_LONG).show();
        }
    }

    public class waitForOtherPlayer extends AsyncTask<String, Void, Boolean> {
        String checkIfRoomFull = "http://10.0.2.2/final_project/db/checkIfRoomFull.php";
        // String chekIfStartGame = "http://localhost/final_project/db/checkIfRoomReadyToStartPlay.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();


        @Override
        protected Boolean doInBackground(String... params) {

            parms.put("room_name", params[0]);
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(checkIfRoomFull, "POST", parms);

                if (response.getInt("succsses")== 1) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }


        protected void onPostExecute(Boolean result) {
            if (result) {
                timer.cancel();
                timerFlag = false;
                Intent i = new Intent(getApplicationContext(), GameScreen.class);
                i.putExtra("Game", newGame);
                i.putExtra("currentPlayer", currentPlayer);
                startActivity(i);
                finish();
                pDialog.dismiss();
            }

        }

    }

    public void startGame(View v) {

        Intent myIntent = new Intent(this, GameScreen.class);
        myIntent.putExtra("currentPlayer", currentPlayer);
        startActivity(myIntent);
        finish();

    }
}
/*
    public class OpenNewRoom extends AsyncTask<String, Void, Integer> {
        LinkedHashMap parms = new LinkedHashMap<>();

        @Override
        protected Integer doInBackground(String... params) {
            parms.put("room_name", params[1]);
            parms.put("user_id", params[2]);
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(params[0], "POST", parms);
                return Integer.parseInt(response.toString());

            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }

        }

        protected void onPostExecute(Integer result) {


            /*all good show progress dialog - waiting for 3 more players*/
    //public void getAllPerson(View v) {
        //Intent myIntent = new Intent(this, AllPerson.class);
       // startActivity(myIntent);
        //String movieTitle = movieTitleText.getText().toString();
        // new MyWebServiceTask().execute("http://localhost:8080/TestWebServicesAndJSON/rest/hello/getAllPerson");
        // new MyWebServiceTask().execute("http://localhost:8080/TestJersey/rest/hello/getAll");
        // new MyWebServiceTask().execute("http://10.0.2.2:8080/TestJersey/rest/hello/getAll","");

   /* public void addNewPerson(View v) {


        LayoutInflater li = LayoutInflater.from(MainMenu.this);
        View dialogView = li.inflate(R.layout.addpersondialog, null);

        final EditText u = (EditText) dialogView.findViewById(R.id.PersonName);
        final EditText i = (EditText) dialogView.findViewById(R.id.PersonID);
        final EditText a = (EditText) dialogView.findViewById(R.id.PersonAge);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
        builder.setView(dialogView);
        builder.setTitle(getResources().getString(R.string.addPerson_dialog_title));
        builder.setMessage(getResources().getString(R.string.addPerson_dialog_masge));
        builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if ((u.getText().toString().matches("[a-zA-Z]+"))) {
                    try {
                        Integer.parseInt(i.getText().toString());
                        Integer.parseInt(a.getText().toString());
                        correctInput = true;

                    } catch (NumberFormatException n) {
                        Toast.makeText(MainMenu.this, "please insert number in id&age field", Toast.LENGTH_LONG).show();
                        return;
                    }

                }
                if (correctInput) {
                    if (u.getText().toString().isEmpty() || i.getText().toString().isEmpty() || a.getText().toString().isEmpty()) {
                        Toast.makeText(MainMenu.this, "ther is emty field", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        final String id;
                        final String userName;
                        final String age;
                        userName = u.getText().toString();
                        id = i.getText().toString();
                        age = a.getText().toString();
                        new SetPerson().execute("http://10.0.2.2:8080/TestJersey/rest/hello/createPerson", userName, id, age);
                    }
                } else {
                    Toast.makeText(MainMenu.this, "number cannot bee user name", Toast.LENGTH_LONG).show();
                    return;
                }

            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();


        // new MyWebServiceTask().execute("http://localhost:8080/TestWebServicesAndJSON/rest/hello/checkUser?personName="
        //         +userName+"personId="+id+"personAge="+age);
    }*/





