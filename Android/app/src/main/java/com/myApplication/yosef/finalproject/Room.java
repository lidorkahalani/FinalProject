package com.myApplication.yosef.finalproject;

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

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class Room extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView personList;
    private ListView lv;
    private MyClassAdapter adapter;
    private ArrayList<User> allUsers =new ArrayList();
    private ProgressDialog pDialog;
    private Timer timer;
    boolean timerFlag = false;
    private Boolean isGameActive=false;
    private Boolean isAdmin=false;
    private Boolean startGame=false;
    private Boolean isRoomSizeSet=false;
    boolean isAdminOpenTheRoomAndCloseIt=false;
    private Timer timer2;
    boolean timerFlag2 = false;
    private TextView roomName;
    private User curentUser;
    private Game game;
    boolean isNewRoom;
    private String []allConnectedUsersId;
    private String playerCount="0";
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
        playerCount=(String)getIntent().getSerializableExtra("playerCount");

        if(isNewRoom) {
            allConnectedUsersId=new String [Integer.parseInt(playerCount)];
            new checkIfRoomNameAvilabale().execute(game.getGame_name());
        } else {
            new getRoomSize().execute(String.valueOf(game.getGame_name()));
            //new joinToRoom().execute(game.getGame_name());
        }
    }

    public void onBackPressed() {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.exitWarning))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new setGameToInactive().execute(String.valueOf(game.getGame_id()));
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

    public void openMainMenu(){
        timer2.cancel();
        timerFlag2 = false;
        Intent myIntent = new Intent(Room.this, MainMenu.class);
        startActivity(myIntent);
        //Toast.makeText(Room.this, "Admin was lost Connection", Toast.LENGTH_SHORT).show();
        finish();
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

    public class joinToRoom extends AsyncTask<String, Void, Boolean> {
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();
        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("room_name", params[0]);
            parms.put("user_id", String.valueOf(curentUser.getUserID()));
            parms.put("player_count", playerCount);
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.joinToRoom, "POST", parms);
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

                pDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.exit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timer.cancel();
                        timerFlag = false;
                        if(timerFlag2)
                            timer2.cancel();
                        new setGameToInactive().execute(String.valueOf(game.getGame_id()));
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
                }, 2500);
            } else {
                new AlertDialog.Builder(Room.this)
                        .setTitle(getResources().getString(R.string.Warning))
                        .setMessage(getResources().getString(R.string.Group_Not_Found))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
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
        @Override
        protected Boolean doInBackground(String... params) {
            LinkedHashMap<String, String> parms = new LinkedHashMap<>();
            parms.put("game_id",String.valueOf(game.getGame_id()));
            try {
                JSONParser json = new JSONParser();
                JSONObject response = json.makeHttpRequest(ServerUtils.getPlayerLIst, "POST",parms);
                JSONArray res=response.getJSONArray("usersList");
                allUsers.clear();
                for (int i = 0; i < res.length(); i++) {
                    JSONObject jo = res.getJSONObject(i);
                    User u = new User(jo.getString("user_name"),jo.getString("user_password"),jo.getInt("user_id"));
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
                if (allUsers.isEmpty()&&timerFlag) {
                    Toast.makeText(Room.this, getResources().getString(R.string.room_empty), Toast.LENGTH_LONG).show();
                    if(timerFlag) {
                        timerFlag = false;
                        timer.cancel();
                    }
                    if(timerFlag2) {
                        timerFlag2 = false;
                        timer2.cancel();
                    }
                    Toast.makeText(Room.this,getResources().getString(R.string.serverNotRespond),Toast.LENGTH_LONG).show();
                    finish();

                }
                adapter = new MyClassAdapter(Room.this, R.layout.single_user_in_room_list,allUsers);

                personList.setAdapter(adapter);

                personList.setOnItemClickListener(Room.this);

                lv = (ListView) findViewById(R.id.PlayerList);
                registerForContextMenu(lv);
                new checkIfRoomFull().execute(String.valueOf(game.getGame_id()));


            }else
                Toast.makeText(Room.this,getResources().getString(R.string.connection_error),Toast.LENGTH_LONG).show();

        }
    }

    public class checkIfRoomFull extends AsyncTask<String, Void, Boolean> {
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("game_id", params[0]);
            parms.put("player_count", playerCount);
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.checkIfRoomFull, "POST", parms);

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
            }else
                 if(timerFlag)
                    new GetAllConnectedPlayers().execute();
                 else
                     Toast.makeText(Room.this,getResources().getString(R.string.back_Main_Menu),Toast.LENGTH_LONG).show();

        }

    }

    public class checkIfImAdmin extends AsyncTask<String, Void, Boolean> {
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("game_id", params[0]);
            parms.put("user_id",String.valueOf(curentUser.getUserID()));
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.checkIfImAdmin, "POST", parms);

                if (response.getInt("succsses")== 1)
                    if(response.getInt("result")==1)
                        isAdmin=true;
                    else
                        isAdmin=false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        protected void onPostExecute(Boolean result) {
            if (result) {
                if(isAdmin) {
                    timer.cancel();
                    timerFlag = false;
                    pDialog.dismiss();

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
            }else {
                pDialog.setMessage("check admin was failed");
            }

        }

    }

    public class checkIfGameIsActive extends AsyncTask<String, Void, Boolean> {
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("game_id", params[0]);
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.checkIfGameIsActive, "POST", parms);

                if (response.getInt("succsses") == 1) {
                    if (response.getJSONObject("result").getInt("is_active") == 1)
                        isGameActive = true;
                    else if (response.getJSONObject("result").getInt("is_active") == 4)
                        startGame = true;
                    else
                        return false;

                }
                }catch(Exception e){
                    e.printStackTrace();
                    return false;
                }
                return true;
            }

        protected void onPostExecute(Boolean result) {
            if (result) {
                if (isGameActive && isAdminOpenTheRoomAndCloseIt) {
                    timer.cancel();
                    timerFlag = false;
                    pDialog.dismiss();

                    timer2.cancel();
                    timerFlag2 = false;

                    Intent i = new Intent(getApplicationContext(), GameScreen.class);
                    i.putExtra("Game", game);
                    i.putExtra("currentPlayer", curentUser);
                    i.putExtra("debug", false);
                    startActivity(i);
                    finish();
                } else {
                    timer2 = new Timer();
                    timerFlag2 = true;
                    timer2.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (timerFlag2) {
                                new checkIfGameIsActive().execute(String.valueOf(game.getGame_id()));
                            }
                        }
                    }, 2000);
                }
                if (!isGameActive) {//somone is disconected go back to mainMenu
                    isAdminOpenTheRoomAndCloseIt = true;
                    timer2 = new Timer();
                    timerFlag2 = true;
                    timer2.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (timerFlag2) {
                                openMainMenu();
                            }
                        }
                    }, 2000);
                }else if(startGame){
                    /*timerStartGame.cancel();
                    timerStartGameFlag=false;*/

                    timer.cancel();
                    timerFlag = false;
                    pDialog.dismiss();

                    timer2.cancel();
                    timerFlag2 = false;

                    Intent i = new Intent(getApplicationContext(), GameScreen.class);
                    i.putExtra("Game", game);
                    i.putExtra("currentPlayer", curentUser);
                    i.putExtra("debug", false);
                    startActivity(i);
                    finish();
                }
            }else
                new checkIfGameIsActive().execute(String.valueOf(game.getGame_id()));
               // Toast.makeText(Room.this,"internet Connection problem GameIsActive",Toast.LENGTH_SHORT).show();


        }

    }

    public class checkIfRoomNameAvilabale extends AsyncTask<String, User, Integer> {
        LinkedHashMap parms = new LinkedHashMap<>();
        String roomName;
        @Override
        protected Integer doInBackground(String... params) {
            parms.put("room_name", params[0]);
            parms.put("user_id", String.valueOf(curentUser.getUserID()));
            // parms.put("current_user",currentPlayer);
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.checkIfRoomNameAvailable, "POST", parms);

                if (response.getInt("room_status") == 1) {
                    game.setGame_name(roomName);
                    game.setGame_id(response.getJSONArray("game").getJSONObject(0).getInt("game_id"));
                    return 1;
                }
                else
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
                        timerFlag = false;
                        timer.cancel();
                        if(timerFlag2) {
                            timerFlag2 = false;
                            timer2.cancel();
                        }
                        Intent myIntent = new Intent(Room.this, MainMenu.class);
                        startActivity(myIntent);
                        finish();
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
                        if(!isRoomSizeSet) {
                            isRoomSizeSet = true;
                            new UpdateRoomSize().execute(playerCount,String.valueOf(game.getGame_id()));
                        }
                    }
                }, 2000);

                //new GetAllConnectedPlayers().execute();
            } else if (result == 0) {//room name already in use
                Toast.makeText(Room.this
                        , getResources().getString(R.string.room_name_in_use),
                        Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(Room.this, MainMenu.class);
                startActivity(myIntent);
                finish();
            } else if (result == -1) {//connection problem
                Toast.makeText(Room.this
                        , getResources().getString(R.string.connection_error),
                        Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(Room.this, MainMenu.class);
                startActivity(myIntent);
                finish();
            }

        }

    }

    public class setGameToInactive extends AsyncTask<String, Void, Boolean> {
        //String setGameToInactive = "http://10.0.2.2/final_project/db/setGameToInactive.php";
        //String setGameToInactive = "http://mysite.lidordigital.co.il/Quertets/php/db/setGameToInactive.php";

        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("game_id", params[0]);

            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.setGameToInactive, "POST", parms);
                if (response.getInt("successes") == 1)
                    return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                if(timerFlag) {
                    timer.cancel();
                    timerFlag = false;
                }
                pDialog.dismiss();
                if(timerFlag2) {
                    timer2.cancel();
                    timerFlag2 = false;
                }

                Intent myIntent = new Intent(Room.this, MainMenu.class);
                startActivity(myIntent);
                finish();
            } else
                Toast.makeText(Room.this, getResources().getString(R.string.failed_to_set_game_inactive), Toast.LENGTH_SHORT).show();
        }

    }

    public class UpdateRoomSize extends AsyncTask<String, Void, Boolean> {
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("room_size",params[0]);
            parms.put("game_id",params[1]);

            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.UpdateRoomSize, "POST", parms);

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
            if(!result) {
                Toast.makeText(Room.this, "Cant set game size", Toast.LENGTH_LONG).show();
                timerFlag = false;
                timer.cancel();
                if(timerFlag2) {
                    timerFlag2 = false;
                    timer2.cancel();
                }
                Intent myIntent = new Intent(Room.this, MainMenu.class);
                startActivity(myIntent);
                finish();
            }
        }

    }

    public class getRoomSize extends AsyncTask<String, Void, Boolean> {
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("game_name",params[0]);

            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.getRoomSize, "POST", parms);

                if (response.getInt("succsses")== 1) {
                    playerCount=response.getString("room_size");
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
            if(result) {
                allConnectedUsersId=new String [Integer.parseInt(playerCount)];
                new joinToRoom().execute(game.getGame_name());
            }
            else{
                new AlertDialog.Builder(Room.this)
                        .setTitle(getResources().getString(R.string.Warning))
                        .setMessage(getResources().getString(R.string.Group_Not_Found))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
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

}
