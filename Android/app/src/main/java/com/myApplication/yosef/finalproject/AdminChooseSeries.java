package com.myApplication.yosef.finalproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AdminChooseSeries extends AppCompatActivity implements AdapterView.OnItemClickListener {
    static HashSet<Series> series =new HashSet();
    private ListView lv;
    private MyClassAdapter adapter;
    private CheckBox checkBoxIndicator;
    private ListView category_list;
    private User currentPlayer;
    private boolean isSomoneDisconected=false;
    private ArrayList<User> allUsers =new ArrayList();
    private String allConnectedUsersId[]=new String[4];
    private Game game;
    private User curentUser;
    int i=1;
    boolean []chosenList;
    private Boolean setBoolenList=false;
    private Boolean adminChose=false;
    private Timer timer = new Timer();
    boolean timerFlag;
    private  Timer myTimer = new Timer("MyTimer", true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_choose_series);
        category_list=(ListView)findViewById(R.id.choseSeriesList);
        game=(Game)getIntent().getSerializableExtra("Game");
        curentUser =(User) getIntent().getSerializableExtra("currentPlayer");
        allUsers=(ArrayList<User>)getIntent().getSerializableExtra("allUsers");
        allConnectedUsersId=getIntent().getExtras().getStringArray("allConnectedUsersId");


        new GetMySeries().execute(String.valueOf(allConnectedUsersId[0]));


        myTimer.scheduleAtFixedRate(new MyTask(), 5000, 2000);


    }

    private class MyTask extends TimerTask {

        public void run(){
            new checkIfGameIsActive().execute(String.valueOf(game.getGame_id()));
        }

    }

    public void onBackPressed() {
        // TODO Auto-generated method stub
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.exitWarning))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new setGameToInactive().execute(String.valueOf(game.getGame_id()));
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void startGame(View v){
       // CheckBox cb = (CheckBox) v.getRootView().findViewById(R.id.checkBox);
        //CheckBox checkBox=(CheckBox)v.findViewById(R.id.checkBox);
       // Toast.makeText(AdminChooseSeries.this,cb.getText().toString(),Toast.LENGTH_SHORT).show();

         int counter=0;
        final int cnt;
        final List<Series> list = new ArrayList<Series>(series);
        for (int i=0;i<series.size();i++)
            if(chosenList[i])//if current item checked
                counter++;
        cnt=counter;


        if(counter>=5){
            adminChose=true;
            //new sendActiveSerie().execute();
            //List<Series> list = new ArrayList<Series>(series);
            new setGameToActive().execute("4",String.valueOf(game.getGame_id()));
          //  new setGameToActive().execute("2",String.valueOf(game.getGame_id()));
        }else
            Toast.makeText(this,"please select minimum 5 series",Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public class GetMySeries extends AsyncTask<String, Void, Boolean> {
        //String GetMySeries = "http://10.0.2.2/final_project/db/getMySeries.php";
        //String GetMySeries = "http://mysite.lidordigital.co.il/Quertets/php/db/getMySeries.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();


        @Override
        protected Boolean doInBackground(String... params) {
            //String userName= params[0];
            parms.put("user_id", params[0]);
            JSONParser json = new JSONParser();
            Series temp_categorys=new Series();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.GetMySeries, "GET", parms);
                if (response.getInt("succsses") == 1) {
                    JSONArray jsonArray = response.getJSONArray("myCards");
                    for (int i = 0; i < jsonArray.length(); i+=4) {
                        if(i%4==0)
                            temp_categorys=new Series();

                        JSONObject jo = jsonArray.getJSONObject(i);
                        temp_categorys.setCategory_id(jo.getInt("category_id"));
                        temp_categorys.setCategory_name(jo.getString("category_name"));
                        temp_categorys.setCard_name1(jo.getString("card_name"));
                        temp_categorys.setCard_name2(jsonArray.getJSONObject(i+1).getString("card_name"));
                        temp_categorys.setCard_name3(jsonArray.getJSONObject(i+2).getString("card_name"));
                        temp_categorys.setCard_name4(jsonArray.getJSONObject(i+3).getString("card_name"));
                        temp_categorys.setImage1(jo.getString("image_name"));
                        temp_categorys.setImage2(jsonArray.getJSONObject(i+1).getString("image_name"));
                        temp_categorys.setImage3(jsonArray.getJSONObject(i+2).getString("image_name"));
                        temp_categorys.setImage4(jsonArray.getJSONObject(i+3).getString("image_name"));
                        series.add(temp_categorys);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;

        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                for(;i<(allConnectedUsersId.length-1);i++)
                    new GetMySeries().execute(String.valueOf(allConnectedUsersId[i]));
            }
            if(i==(allConnectedUsersId.length-1))
                new GetDefultSeries().execute("0");


        }
    }

    public class GetDefultSeries extends AsyncTask<String, Void, Boolean> {
        //String GetMySeries = "http://10.0.2.2/final_project/db/getMySeries.php";
        String GetMySeries = "http://mysite.lidordigital.co.il/Quertets/php/db/getMySeries.php";


        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("user_id", params[0]);
            JSONParser json = new JSONParser();
            Series temp_categorys=new Series();;
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.GetMySeries, "GET", parms);
                if (response.getInt("succsses") == 1) {
                    JSONArray jsonArray = response.getJSONArray("myCards");
                    for (int i = 0; i < jsonArray.length(); i+=4) {
                        //   Card card = new Card();
                        if(i%4==0)
                            temp_categorys=new Series();

                        JSONObject jo = jsonArray.getJSONObject(i);
                        temp_categorys.setCategory_id(jo.getInt("category_id"));
                        temp_categorys.setCategory_name(jo.getString("category_name"));
                        temp_categorys.setCard_name1(jo.getString("card_name"));
                        temp_categorys.setCard_name2(jsonArray.getJSONObject(i+1).getString("card_name"));
                        temp_categorys.setCard_name3(jsonArray.getJSONObject(i+2).getString("card_name"));
                        temp_categorys.setCard_name4(jsonArray.getJSONObject(i+3).getString("card_name"));
                        temp_categorys.setImage1(jo.getString("image_name"));
                        temp_categorys.setImage2(jsonArray.getJSONObject(i+1).getString("image_name"));
                        temp_categorys.setImage3(jsonArray.getJSONObject(i+2).getString("image_name"));
                        temp_categorys.setImage4(jsonArray.getJSONObject(i+3).getString("image_name"));
                        series.add(temp_categorys);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;

        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                chosenList= new boolean[series.size()];
                setBoolenList=true;
                List<Series> list = new ArrayList<Series>(series);
                adapter = new MyClassAdapter(AdminChooseSeries.this, R.layout.single_chose_series, list);

                category_list.setAdapter(adapter);

                category_list.setOnItemClickListener(AdminChooseSeries.this);

                lv = (ListView) findViewById(R.id.choseSeriesList);
                registerForContextMenu(lv);

                new setGameToActive().execute("1",String.valueOf(game.getGame_id()));

            }else
                Toast.makeText(AdminChooseSeries.this,"there was problem to get defult series",Toast.LENGTH_SHORT).show();
        }
    }

    public class sendActiveSerie extends AsyncTask<String, Void, Boolean> {
        //String sendActiveSerie = "http://10.0.2.2/final_project/db/sendActiveSerie.php";
        //String sendActiveSerie = "http://mysite.lidordigital.co.il/Quertets/php/db/sendActiveSerie.php";


        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {


            final List<Series> list = new ArrayList<Series>(series);
            JSONArray jsonArr = new JSONArray();
            JSONObject json = new JSONObject();
            int index=0;
            for (int i = 0; i < list.size(); i++) {

                if (chosenList[i]) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("seriesId"+index, list.get(i).getCategory_id());
                        index++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArr.put(jsonObject);
                }
            }

            try {
                json.put("seriesIds",jsonArr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String message = json.toString();
            Log.d("JSON", message);


            parms.put("game_id",String.valueOf(game.getGame_id()));
            parms.put("user_id",String.valueOf(curentUser.getUserID()));
            parms.put("json",message);


            JSONParser jsonPars = new JSONParser();
            try {
                JSONObject response = jsonPars.makeHttpRequest(ServerUtils.sendActiveSerie, "POST", parms);

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
            if(result) {
               // new setGameToActive().execute("1",String.valueOf(game.getGame_id()));
                new setTurnOrder().execute();
            }else
                Toast.makeText(AdminChooseSeries.this,"set series for game failed",Toast.LENGTH_SHORT).show();
        }
    }

    public class setGameToActive extends AsyncTask<String, Void, Boolean> {
        //String setGameToActive = "http://10.0.2.2/final_project/db/setGameToActive.php";
         //String setGameToActive = "http://mysite.lidordigital.co.il/Quertets/php/db/setGameToActive.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("opertion",params[0]);
            parms.put("game_id",params[1]);

            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.setGameToActive, "POST", parms);

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
            if(result)
                new sendActiveSerie().execute();
           else
                Toast.makeText(AdminChooseSeries.this,"Cant set game to be active",Toast.LENGTH_LONG).show();
        }

    }

    public class setTurnOrder extends AsyncTask<String, Void, Boolean> {
        //String setTurnOrder = "http://10.0.2.2/final_project/db/moveToNextPlayer.php";
        //String setTurnOrder = "http://mysite.lidordigital.co.il/Quertets/php/db/moveToNextPlayer.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("game_id", String.valueOf(game.getGame_id()));
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.setTurnOrder, "POST", parms);

                if (response.getInt("successes") == 1) {
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
            if (result&&adminChose) {
                myTimer.cancel();
                Intent i = new Intent(getApplicationContext(), GameScreen.class);
                i.putExtra("Game", game);
                i.putExtra("currentPlayer", curentUser);
                i.putExtra("debug", false);
                startActivity(i);
                finish();
            } else
                Log.d("setTurrnBag", "There is problem no order set");

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
                /*Intent myIntent = new Intent(AdminChooseSeries.this, MainMenu.class);
                startActivity(myIntent);
                finish();*/
            } else
                Toast.makeText(AdminChooseSeries.this,getResources().getString(R.string.failed_to_set_game_inactive), Toast.LENGTH_SHORT).show();
        }

    }

    public class checkIfGameIsActive extends AsyncTask<String, Void, Boolean> {
        //String checkIfGameIsActive = "http://10.0.2.2/final_project/db/checkIfGameIsActive.php";
        // String checkIfGameIsActive = "http://mysite.lidordigital.co.il/Quertets/php/db/checkIfGameIsActive.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("game_id", params[0]);
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.checkIfGameIsActive, "POST", parms);

                if (response.getInt("succsses")== 1) {
                    if(response.getJSONObject("result").getInt("is_active")==0)
                        return true;
                    else
                        return false;
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
                //someone was disconected

                myTimer.cancel();
                Intent myIntent = new Intent(AdminChooseSeries.this, MainMenu.class);
                startActivity(myIntent);
                Toast.makeText(AdminChooseSeries.this,getResources().getString(R.string.room_close),Toast.LENGTH_SHORT).show();
                finish();

            }
        }

    }

    class MyClassAdapter extends ArrayAdapter<Series> {

        public MyClassAdapter(Context context, int resource, List<Series> objects) {
            super(context, resource, objects);
        }

        // the method getView is in charge of creating a single line in the list
        // it receives the position (index) of the line to be created
        // the method populates the view with the data from the relevant object (according to the position)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            Log.i("TEST getView", "inside getView position " + position);

            Series series = getItem(position);

            //String card2 = getItem(position);
            if (convertView == null) {
                Log.e("TEST getView", "inside if with position " + position);
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_chose_series, parent, false);
            }


            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            checkBox.setText(series.getCategory_name());
            if (setBoolenList)
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        chosenList[position] = isChecked;
                    }
                });


            return convertView;

        }
    }

   /* public class checkIfSomoneDisconected extends AsyncTask<String, Void, Boolean> {
        //String GetMySeries = "http://10.0.2.2/final_project/db/getMySeries.php";
        //String GetMySeries = "http://mysite.lidordigital.co.il/Quertets/php/db/getMySeries.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();


        @Override
        protected Boolean doInBackground(String... params) {
            //String userName= params[0];
            parms.put("user_id", params[0]);
            JSONParser json = new JSONParser();
            Series temp_categorys = new Series();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.GetMySeries, "GET", parms);
                if (response.getInt("succsses") == 1) {
                    JSONArray jsonArray = response.getJSONArray("myCards");
                    for (int i = 0; i < jsonArray.length(); i += 4) {
                        if (i % 4 == 0)
                            temp_categorys = new Series();

                        JSONObject jo = jsonArray.getJSONObject(i);
                        temp_categorys.setCategory_id(jo.getInt("category_id"));
                        temp_categorys.setCategory_name(jo.getString("category_name"));
                        temp_categorys.setCard_name1(jo.getString("card_name"));
                        temp_categorys.setCard_name2(jsonArray.getJSONObject(i + 1).getString("card_name"));
                        temp_categorys.setCard_name3(jsonArray.getJSONObject(i + 2).getString("card_name"));
                        temp_categorys.setCard_name4(jsonArray.getJSONObject(i + 3).getString("card_name"));
                        temp_categorys.setImage1(jo.getString("image_name"));
                        temp_categorys.setImage2(jsonArray.getJSONObject(i + 1).getString("image_name"));
                        temp_categorys.setImage3(jsonArray.getJSONObject(i + 2).getString("image_name"));
                        temp_categorys.setImage4(jsonArray.getJSONObject(i + 3).getString("image_name"));
                        series.add(temp_categorys);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;

        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(AdminChooseSeries.this);
                Boolean faceBookLogIN=myPref.getBoolean("loginWhitFacebook",false);

                SharedPreferences.Editor editor = myPref.edit();
                editor.putString("username", curentUser.getUserName());
                editor.putString("password", curentUser.getPassword());
                editor.putInt("score", curentUser.getScore());
                editor.putInt("user_id",curentUser.getUserID());

                editor.commit();

                Intent myIntent = new Intent(AdminChooseSeries.this, MainMenu.class);
                startActivity(myIntent);
                Toast.makeText(AdminChooseSeries.this,"Somone Disconected",Toast.LENGTH_SHORT).show();
                finish();

            }
        }

    }*/


}
