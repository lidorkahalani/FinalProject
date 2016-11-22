package com.example.yosef.finalproject;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Room extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView personList;
    ListView lv;
    MyClassAdapter adapter;
    ArrayList<User> allUsers =new ArrayList();
    ProgressDialog pDialog;
    Timer timer;
    boolean timerFlag = false;
    Boolean isGameActive;
    Boolean isAdmin;


    ProgressDialog pDialog2;
    Timer timer2;
    boolean timerFlag2 = false;
   // private  EditText roomName;
    private TextView roomName;
    private  User curentUser;
    private Game game;
    boolean isNewRoom;
    String []allConnectedUsersId=new String [4];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        personList=(ListView)findViewById(R.id.PlayerList);
        roomName=(TextView)findViewById(R.id.roomName);
        game=(Game)getIntent().getSerializableExtra("Game");
        curentUser =(User) getIntent().getSerializableExtra("currentPlayer");
        isNewRoom=getIntent().getExtras().getBoolean("isNewRoom");
        roomName.setText(getResources().getString(R.string.wellcome)+" To: "+game.getGame_name()+" Room");
        if(isNewRoom)
            new openNewRoom().execute(game.getGame_name());
        else {
            new joinToRoom().execute(game.getGame_name());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    class MyClassAdapter extends ArrayAdapter<User> {

        public MyClassAdapter(Context context, int resource, List<User> objects) {
            super(context, resource, objects);
        }

        // the method getView is in charge of creating a single line in the list
        // it receives the position (index) of the line to be created
        // the method populates the view with the data from the relevant object (according to the position)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.i("TEST getView", "inside getView position " + position);

            User user = getItem(position);
            if (convertView == null) {
                Log.e("TEST getView", "inside if with position " + position);
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_user_in_room_list, parent, false);
            }
            TextView userName = (TextView) convertView.findViewById(R.id.playerName);

            userName.setText(user.getUserName());
            return convertView;

        }
    }

    public class openNewRoom extends AsyncTask<String, User, Integer> {
        String openNewRoom = "http://10.0.2.2/final_project/db/openNewRoom.php";
        // String openNewRoom="http://mysite.lidordigital.co.il/Quertets/db/openNewRoom.php";
        LinkedHashMap parms = new LinkedHashMap<>();
        String roomName;
        @Override
        protected Integer doInBackground(String... params) {
            parms.put("room_name", params[0]);
            parms.put("user_id", String.valueOf(curentUser.getUserID()));
            // parms.put("current_user",currentPlayer);
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(openNewRoom, "POST", parms);

                if (response.getInt("successes") == 1) {
                    game.setGame_name(roomName);
                    game.setGame_id(response.getJSONArray("game").getJSONObject(0).getInt("game_id"));

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
                pDialog = new ProgressDialog(Room.this);
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
                            new GetAllConnectedPlayers().execute();
                            // new Room.waitForOtherPlayer().execute(roomName);
                            //new GetRoomStatus().execute(roomName);
                        }
                    }
                }, 2000);

                //new GetAllConnectedPlayers().execute();
            } else if (result == 0) {//room name already in use
                Toast.makeText(Room.this
                        , getResources().getString(R.string.room_name_in_use),
                        Toast.LENGTH_LONG).show();

            } else if (result == -1) {//connection problem
                Toast.makeText(Room.this
                        , getResources().getString(R.string.connection_error),
                        Toast.LENGTH_LONG).show();
            }

        }

    }

    public class joinToRoom extends AsyncTask<String, Void, Boolean> {
        String joinToRoom = "http://10.0.2.2/final_project/db/joinToRoom.php";
        // String joinToRoom = "http://mysite.lidordigital.co.il/Quertets/db/joinToRoom.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();
        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("room_name", params[0]);
            parms.put("user_id", String.valueOf(curentUser.getUserID()));
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(joinToRoom, "POST", parms);
                if (response.getInt("succsses") == 1) {
                    game.setGame_name(params[0]);
                    game.setGame_id(response.getInt("game_id"));
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
                pDialog = new ProgressDialog(Room.this);
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
                            //new checkIfRoomFull().execute(game.getGame_name());
                            //new GetRoomStatus().execute(roomName);
                            new GetAllConnectedPlayers().execute();
                        }
                    }
                }, 3000);
            } else {
                new AlertDialog.Builder(Room.this)
                        .setTitle(getResources().getString(R.string.Warning))
                        .setMessage(getResources().getString(R.string.Group_Not_Found))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(Room.this);
                                Boolean faceBookLogIN=myPref.getBoolean("loginWhitFacebook",false);

                                SharedPreferences.Editor editor = myPref.edit();
                                editor.putString("username", curentUser.getUserName());
                                editor.putString("password", curentUser.getPassword());
                                editor.putInt("score", curentUser.getScore());
                                editor.putInt("user_id",curentUser.getUserID());

                                editor.commit();

                                Intent myIntent = new Intent(Room.this, MainMenu.class);
                                startActivity(myIntent);
                                finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }

    public class GetAllConnectedPlayers extends AsyncTask<String, Void, Boolean> {
        String getPlayerLIst="http://10.0.2.2/final_project/db/getListNameOfThePLayerInRoom.php";
       // String getPlayerLIst="http://mysite.lidordigital.co.il/Quertets/db/getListNameOfThePLayerInRoom.php";
        @Override
        protected Boolean doInBackground(String... params) {
            LinkedHashMap<String, String> parms = new LinkedHashMap<>();
            parms.put("game_id",String.valueOf(game.getGame_id()));
            try {
                JSONParser json = new JSONParser();
                JSONObject response = json.makeHttpRequest(getPlayerLIst, "POST",parms);
                JSONArray res=response.getJSONArray("usersList");
                allUsers.clear();
                for (int i = 0; i < res.length(); i++) {
                    JSONObject jo = res.getJSONObject(i);
                    User u = new User(jo.getString("user_name"), jo.getString("user_id"),Integer.parseInt(jo.getString("user_password")));
                    allUsers.add(u);

                }


            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;

        }
        protected void onPostExecute(Boolean result) {
            if(result) {
                if (allUsers.isEmpty()) {
                    Toast.makeText(Room.this, getResources().getString(R.string.room_empty), Toast.LENGTH_LONG).show();
                    finish();
                }
                adapter = new MyClassAdapter(Room.this, R.layout.single_user_in_room_list,allUsers);

                personList.setAdapter(adapter);

                personList.setOnItemClickListener(Room.this);

                lv = (ListView) findViewById(R.id.PlayerList);
                registerForContextMenu(lv);
              /* pDialog = new ProgressDialog(Room.this);
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
                            new checkIfRoomFull().execute(roomName.getText().toString());
                            // new Room.waitForOtherPlayer().execute(roomName);
                            //new GetRoomStatus().execute(roomName);
                        }
                    }
                }, 3000);*/

                new checkIfRoomFull().execute(String.valueOf(game.getGame_id()));


            }else
                //new GetAllConnectedPlayers().execute();
                Toast.makeText(Room.this,getResources().getString(R.string.connection_error),Toast.LENGTH_LONG).show();

        }
    }

    public class checkIfRoomFull extends AsyncTask<String, Void, Boolean> {
        String checkIfRoomFull = "http://10.0.2.2/final_project/db/checkIfRoomFull.php";
       // String checkIfRoomFull = "http://mysite.lidordigital.co.il/Quertets/db/checkIfRoomFull.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("game_id", params[0]);
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(checkIfRoomFull, "POST", parms);

                if (response.getInt("succsses")== 1) {
                    JSONArray jsonArray = response.getJSONArray("all_users_id");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jo = jsonArray.getJSONObject(i);
                        allConnectedUsersId[i]=String.valueOf(jo.getInt("user_id"));
                    }
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

                new checkIfImAdmin().execute(String.valueOf(game.getGame_id()));

                /*Intent i = new Intent(getApplicationContext(), AdminChooseSeries.class);
                game.setGame_name(game.getGame_name());
                i.putExtra("Game",game);
                i.putExtra("currentPlayer",curentUser);
                i.putExtra("isNewRoom",true);
                i.putExtra("allConnectedUsersId",allConnectedUsersId);
                i.putExtra("allUsers",allUsers);
                startActivity(i);
                finish();*/
                //new setTurnOrder().execute();
            }else
                new GetAllConnectedPlayers().execute();

        }

    }

    public class checkIfImAdmin extends AsyncTask<String, Void, Boolean> {
        String checkIfImAdmin = "http://10.0.2.2/final_project/db/checkIfAdmin.php";
        // String checkIfImAdmin = "http://mysite.lidordigital.co.il/Quertets/db/checkIfAdmin.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("game_id", params[0]);
            parms.put("user_id",String.valueOf(curentUser.getUserID()));
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(checkIfImAdmin, "POST", parms);

                if (response.getInt("succsses")== 1) {
                    if(response.getInt("result")==1)
                        isAdmin=true;
                    else
                        isAdmin=false;

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
                if(isAdmin) {
                    Intent i = new Intent(getApplicationContext(), AdminChooseSeries.class);
                    game.setGame_name(game.getGame_name());
                    i.putExtra("Game", game);
                    i.putExtra("currentPlayer", curentUser);
                    i.putExtra("allConnectedUsersId", allConnectedUsersId);
                    i.putExtra("allUsers", allUsers);
                    startActivity(i);
                    finish();
                }else {
                    pDialog.setMessage(getResources().getString(R.string.wait_for_admin_chose));
                    new checkIfGameIsActive().execute(String.valueOf(game.getGame_id()));
                }
                //new setTurnOrder().execute();
            }else {
                pDialog.setMessage(getResources().getString(R.string.wait_for_admin_chose));
                new checkIfGameIsActive().execute(String.valueOf(game.getGame_id()));
            }
                //new GetAllConnectedPlayers().execute();

        }

    }

    public class checkIfGameIsActive extends AsyncTask<String, Void, Boolean> {
        String checkIfGameIsActive = "http://10.0.2.2/final_project/db/checkIfGameIsActive.php";
        // String checkIfGameIsActive = "http://mysite.lidordigital.co.il/Quertets/db/checkIfGameIsActive.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("game_id", params[0]);
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(checkIfGameIsActive, "POST", parms);

                if (response.getInt("succsses")== 1) {
                    if(response.getJSONObject("result").getInt("is_active")==1)
                          isGameActive=true;
                    else
                          isGameActive=false;
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
                if(isGameActive) {
                    timer.cancel();
                    timerFlag = false;
                    pDialog.dismiss();

                    timer2.cancel();
                    timerFlag2 = false;

                    Intent i = new Intent(getApplicationContext(), GameScreen.class);
                    i.putExtra("Game", game);
                    i.putExtra("currentPlayer", curentUser);
                    //i.putExtra("selctedCard",selctedCard);
                    i.putExtra("debug", false);
                    startActivity(i);
                    finish();
                }else{
                    timer2 = new Timer();
                    timerFlag2 = true;
                    timer2.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (timerFlag2) {
                                new checkIfGameIsActive().execute(String.valueOf(game.getGame_id()));
                            }
                        }
                    }, 3000);
                    }
            }else {

                timer2 = new Timer();
                timerFlag2 = true;
                timer2.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (timerFlag2) {
                            new checkIfGameIsActive().execute(String.valueOf(game.getGame_id()));
                        }
                    }
                }, 3000);
            }


        }

    }



    public class setTurnOrder extends AsyncTask<String, Void, Boolean> {
        String setTurnOrder = "http://10.0.2.2/final_project/db/moveToNextPlayer.php";
        // String setTurnOrder = "http://mysite.lidordigital.co.il/Quertets/db/moveToNextPlayer.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("game_id", String.valueOf(game.getGame_id()));
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(setTurnOrder, "POST", parms);

                if (response.getInt("successes")== 1) {
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
                i.putExtra("Game", game);
                i.putExtra("currentPlayer", curentUser);
                i.putExtra("allConnectedUsersId",allConnectedUsersId);
                startActivity(i);
                finish();
                pDialog.dismiss();

               /* Intent i = new Intent(getApplicationContext(), AdminChooseSeries.class);
                game.setGame_name(game.getGame_name());
                i.putExtra("Game",game);
                i.putExtra("currentPlayer",curentUser);
                //i.putExtra("isNewRoom",true);
                i.putExtra("allConnectedUsersId",allConnectedUsersId);
               // i.putExtra("allUsers",allUsers);
                startActivity(i);
                finish();*/

                //pDialog.dismiss();
            }else
                Toast.makeText(Room.this,"There is problem no order set",Toast.LENGTH_LONG).show();

        }

    }

    public void getGameId(String gameName){
        final String s=gameName;
        class GetGameId extends AsyncTask<String, Void, Integer> {
            String getGameId="http://10.0.2.2/final_project/db/getGameId.php";
            LinkedHashMap<String, String> parms = new LinkedHashMap<>();
            @Override
            protected Integer doInBackground(String... params) {
                parms.put("game_name",s);
                JSONParser json = new JSONParser();
                try {
                    JSONObject response = json.makeHttpRequest(getGameId, "GET", parms);
                    if(response.getInt("succsses")==1)
                        return (response.getJSONObject("game_id").getInt("game_id"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return 0;
            }

            protected void onPostExecute(Integer s) {
                if(s!=0)
                    game.setGame_id(s);
            }
        }
        GetGameId g= new GetGameId();
        g.execute();
    }

}
