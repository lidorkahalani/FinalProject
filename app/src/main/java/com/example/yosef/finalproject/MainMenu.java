package com.example.yosef.finalproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainMenu extends AppCompatActivity {

    private UsersDBHandler dbHandler;
    private ProfileTracker profileTracker;
    private AccessTokenTracker accsessTokenTracker;
    private CallbackManager callBack;
    private Profile profile;
    private User currentPlayer;
    private ArrayList<User> allUsers;

    LoginButton facebookButton;
    Button logOut;
    ProgressDialog pDialog;
    Timer timer;
    boolean timerFlag = false;
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
        logOut = (Button) findViewById(R.id.logOut);

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
        SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(MainMenu.this);
        String uname = myPref.getString("username", "");
        String password = myPref.getString("password", "");
        int score = myPref.getInt("score", 0);
        currentPlayer = new User(uname, password, score);
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
        t.setText("Hello " + uname + "\n" + "Toatal Score: " + Integer.toString(score));
    }

    public void getAllPerson(View v) {
        //String movieTitle = movieTitleText.getText().toString();
       // new MyWebServiceTask().execute("http://localhost:8080/TestWebServicesAndJSON/rest/hello/getAllPerson");
       // new MyWebServiceTask().execute("http://localhost:8080/TestJersey/rest/hello/getAll");
        new MyWebServiceTask().execute("http://10.0.2.2:8080/TestJersey/rest/hello/getAll");
    }

    public String getUrRequest(String urlString) throws Exception {

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            Log.e("TEST_Web", "Failed to connect to: " + urlString);
            return null;
        }

        BufferedReader input = new BufferedReader(
                new InputStreamReader(conn .getInputStream()));

        String line;
        StringBuilder response = new StringBuilder();
        //StringBuffer res=new StringBuffer();
        while ((line = input.readLine()) != null) {
            response.append(line + "\n");
        }

        input.close();

        conn.disconnect();
        return response.toString();

    }

    public void addNewPerson(View v){
        String id="111";
        String userName="lolo";
        String age="100";
        new MyWebServiceTask().execute("http://10.0.2.2:8080/TestJersey/rest/hello/CreatePerson?personName="
                +userName+"personId="+id+"personAge="+age);

       // new MyWebServiceTask().execute("http://localhost:8080/TestWebServicesAndJSON/rest/hello/checkUser?personName="
       //         +userName+"personId="+id+"personAge="+age);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accsessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    public void addCardToDB(View v) {
        Intent myIntent=new Intent(this,AddCards.class);
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
                //1. send the room name to server
                new SendRoomName().execute(roomNameInput.getText().toString());


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
        builder.setMessage("Are you sure you want to Exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainMenu.this.finish();
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

    public void startGame(View v) {

        Intent myIntent = new Intent(this, GameScreen.class);
        myIntent.putExtra("currentPlayer", currentPlayer);
        startActivity(myIntent);
        finish();

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
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                String roomName = userInput.getText().toString();
                                //now we need to cheek if the romm exist in dataBase
                                if (roomName.isEmpty()) {//== DatabaseUtils.col["groupName"]){
                                    /* Intent myIntent=new Intent(this,watingRoom.class);
                                       startActivity(myIntent);
                                       finish();*/
                                } else {
                                    new AlertDialog.Builder(MainMenu.this)
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
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    public void showAllRecords(View v) {
        // ArrayList<User> UsersList = dbHandler.getAllUsers();
        Intent myIntent = new Intent(this, AllRecords.class);
        // myIntent.putExtra("userList", UsersList);
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

    class MyWebServiceTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String response) {
           // System.out.println("responOnPos: "+response);
            String plotString = null;
          //  try {
               // Gson gson=new Gson();
               // plotString=gson.toJson(response);

                //JSONObject jsonObj = new JSONObject(response);
                //plotString = jsonObj.getString("");
                Toast.makeText(MainMenu.this, response, Toast.LENGTH_LONG).show();


           // } catch (JSONException e) {
            //    e.printStackTrace();
        //    }

           //Toast.makeText(MainMenu.this,plotString,Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> parms = new HashMap<>();
            String response = null;
            try {
                JSONParser pars=new JSONParser();
                JSONObject res=pars.makeHttpRequest(params[0],"GET",parms);
                JSONArray jsonArray =res.getJSONArray("AllPersons");
                for(int i=0;i<jsonArray.length();i++){

                    JSONObject jo = jsonArray.getJSONObject(i);
                    User u=new User(jo.getString("name"),jo.getString("id"),Integer.parseInt(jo.getString("age")));

                    allUsers.add(u);
                }
                response=allUsers.toString();
                //response = getUrRequest(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }
    }

    public class SendRoomName extends AsyncTask<String, Void, Integer> {
        String set_rom_name_url = "setRoomName.php";
        HashMap<String, String> parms = new HashMap<>();
        String roomName;

        @Override
        protected Integer doInBackground(String... params) {
            roomName = params[0];
            parms.put("room_name", roomName);
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(set_rom_name_url, "POST", parms);

                return response.getInt("succsses");

            } catch (JSONException e) {
                e.printStackTrace();
                return -1;
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }


        }


        protected void onPostExecute(Integer result) {
            if (result==1) {//all good
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
                        if(timerFlag){
                            new GetRoomStatus().execute(roomName);
                        }
                    }
                }, 3000);
            } else if(result==0){//room name already in use
                Toast.makeText(MainMenu.this
                        ,getResources().getString(R.string.room_name_in_use),
                        Toast.LENGTH_LONG).show();
            } else if(result==-1){//connection problem
                Toast.makeText(MainMenu.this
                        ,getResources().getString(R.string.connection_error),
                        Toast.LENGTH_LONG).show();
            }

        }

    }

    public class GetRoomStatus extends AsyncTask<String, Void, Boolean> {
        String get_room_status_url = "getRoomStatus.php";
        HashMap<String, String> parms = new HashMap<>();


        @Override
        protected Boolean doInBackground(String... params) {

            parms.put("room_name", params[0]);
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(get_room_status_url, "POST", parms);

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
                timer.cancel();
                timerFlag = false;


                startActivity(new Intent(MainMenu.this, GameScreen.class));
                pDialog.dismiss();
            }

        }

    }


}
