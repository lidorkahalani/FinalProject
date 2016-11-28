package com.example.yosef.finalproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
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

public class AdminChooseSeries extends AppCompatActivity implements AdapterView.OnItemClickListener {
    static HashSet<Series> series =new HashSet();
    ListView lv;
    MyClassAdapter adapter;
    CheckBox checkBoxIndicator;
    private ListView category_list;
    private User currentPlayer;
    ArrayList<User> allUsers =new ArrayList();
    String allConnectedUsersId[]=new String[4];
    Game game;
    User curentUser;
    int i=1;
    boolean []chosenList;
    Boolean setBoolenList=false;

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



    }



    public void startGame(View v){
       // CheckBox cb = (CheckBox) v.getRootView().findViewById(R.id.checkBox);
        //CheckBox checkBox=(CheckBox)v.findViewById(R.id.checkBox);
       // Toast.makeText(AdminChooseSeries.this,cb.getText().toString(),Toast.LENGTH_SHORT).show();

         int counter=0;
        final int cnt;
        final List<Series> list = new ArrayList<Series>(series);
        for (int i=0;i<series.size();i++)
            if(chosenList[i])
                counter++;
        cnt=counter;

        new Thread(new Runnable() {
            @Override
            public void run() {
                OutputStream os = null;
                InputStream is = null;
                HttpURLConnection conn = null;
                try {
                    //constants
                    URL url = new URL("http://10.0.2.2/final_project/db/sendActiveSerie.php");
                    JSONObject jsonObject = new JSONObject();
                    for (int i=0;i<list.size();i++){
                        if(chosenList[i])
                        jsonObject.put("seriesId"+i, list.get(i).getCategory_id());
                    }
                    String message = jsonObject.toString();

                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout( 10000 /*milliseconds*/ );
                    conn.setConnectTimeout( 15000 /* milliseconds */ );
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setFixedLengthStreamingMode(message.getBytes().length);

                    //make some HTTP header nicety
                    conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                    conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                    //open
                    conn.connect();

                    //setup send
                    os = new BufferedOutputStream(conn.getOutputStream());
                    os.write(message.getBytes());
                    //clean up
                    os.flush();

                    //do somehting with response
                    is = conn.getInputStream();
                    if(is!=null)
                        Toast.makeText(AdminChooseSeries.this,"insert Good!",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(AdminChooseSeries.this,"insert BAD!",Toast.LENGTH_SHORT).show();

                    //String contentAsString = readIt(is,len);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
               finally {
                    //clean up
                    try {
                        os.close();
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    conn.disconnect();
                }
            }
        }).start();



        if(counter>=7){
            //List<Series> list = new ArrayList<Series>(series);
            //new setGameToActive().execute("1",String.valueOf(game.getGame_id()));
        }else
            Toast.makeText(this,"please select minimum 7 series",Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public class GetMySeries extends AsyncTask<String, Void, Boolean> {
        String GetMySeries = "http://10.0.2.2/final_project/db/getMySeries.php";
        //String GetMySeries = "http://mysite.lidordigital.co.il/Quertets/db/getMySeries.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();


        @Override
        protected Boolean doInBackground(String... params) {
            //String userName= params[0];
            parms.put("user_id", params[0]);
            JSONParser json = new JSONParser();
            Series temp_categorys=new Series();
            try {
                JSONObject response = json.makeHttpRequest(GetMySeries, "GET", parms);
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
        String GetMySeries = "http://10.0.2.2/final_project/db/getMySeries.php";
        //String GetMySeries = "http://mysite.lidordigital.co.il/Quertets/db/getMySeries.php";


        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("user_id", params[0]);
            JSONParser json = new JSONParser();
            Series temp_categorys=new Series();;
            try {
                JSONObject response = json.makeHttpRequest(GetMySeries, "GET", parms);
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

            }
        }
    }

    class MyClassAdapter extends ArrayAdapter<Series> {

        public MyClassAdapter(Context context, int resource, List<Series> objects) {
            super(context,resource,objects);
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


            CheckBox checkBox=(CheckBox)convertView.findViewById(R.id.checkBox);
            checkBox.setText(series.getCategory_name());
            if(setBoolenList)
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    chosenList[position]=isChecked;
                }
            });



            return convertView;

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

                Intent i = new Intent(getApplicationContext(), GameScreen.class);
                i.putExtra("Game", game);
                i.putExtra("currentPlayer",curentUser);
                i.putExtra("debug",false);
                startActivity(i);
                finish();
            }else
                Toast.makeText(AdminChooseSeries.this,"There is problem no order set",Toast.LENGTH_LONG).show();

        }

    }

    public class sendActiveSerie extends AsyncTask<String, Void, Boolean> {
        String sendActiveSerie = "http://10.0.2.2/final_project/db/sendActiveSerie.php";
        //String sendActiveSerie = "http://mysite.lidordigital.co.il/Quertets/db/sendActiveSerie.php";


        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            final List<Series> list = new ArrayList<Series>(series);
            JSONObject jsonObject = new JSONObject();
            for (int i = 0; i < list.size(); i++) {
                if (chosenList[i])
                    try {
                        jsonObject.put("seriesId" + i, list.get(i).getCategory_id());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
            String message = jsonObject.toString();
            parms.put("json",message);
            parms.put("game_id",String.valueOf(game.getGame_id()));
            parms.put("user_id",String.valueOf(curentUser.getUserID()));


            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(sendActiveSerie, "POST", parms);

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
            if(result) {
                //new setGameToActive().execute("1",String.valueOf(game.getGame_id()));
            }else
                Toast.makeText(AdminChooseSeries.this,"set cards game trow eror",Toast.LENGTH_SHORT).show();
        }
    }

    public class setGameToActive extends AsyncTask<String, Void, Boolean> {
        String setGameToActive = "http://10.0.2.2/final_project/db/setGameToActive.php";
        // String setGameToActive = "http://mysite.lidordigital.co.il/Quertets/db/setGameToActive.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("opertion",params[0]);
            parms.put("game_id",params[1]);

            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(setGameToActive, "POST", parms);

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
                new setTurnOrder().execute();
            }else
                Toast.makeText(AdminChooseSeries.this,"Cant set game to be active",Toast.LENGTH_LONG).show();

        }

    }

    public String getMyName(String user_id){
        final String[] userName = new String[1];
        class GetMyName extends AsyncTask<String,Void,String>{
            //public static final String getMyName = "http://mysite.lidordigital.co.il/Quertets/db/getMyName.php";
            public static final String getMyName = "http://10.0.2.2/final_project/db/getMyName.php";

            @Override
            protected String doInBackground(String... params) {
                LinkedHashMap<String,String> data = new LinkedHashMap<>();
                data.put("user_id",params[0]);

                JSONParser json = new JSONParser();
                JSONObject response = json.makeHttpRequest(getMyName, "POST", data);
                try {
                    if (response.getInt("succsses") == 1) {
                        userName[0] = response.getString("user_name");
                    }else
                        return null;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
                return null;
            }
        }

        GetMyName ui = new GetMyName();
        ui.execute(user_id);
        return userName[1];
    }

}
