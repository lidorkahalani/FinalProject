package com.example.yosef.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

public class AllRecords extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lv;
    ListView myListView;
    MyClassAdapter adapter;
    ArrayList<User> uList =new ArrayList<User>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allrecords);
        myListView = (ListView) findViewById(R.id.userlist);

        //uList = (ArrayList<User>)getIntent().getSerializableExtra("userList");

        new GetHigheRecrods().execute();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        User user = (User) parent.getItemAtPosition(position);
        Toast.makeText(this,user.getUserName()+" Selcted",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.userlist) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.delete:
                Toast.makeText(this,"Delet",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_user_layout, parent, false);
            }
            TextView userName = (TextView) convertView.findViewById(R.id.userName);
            TextView pass = (TextView) convertView.findViewById(R.id.password);
            TextView score=(TextView) convertView.findViewById(R.id.score);


            userName.setText(user.getUserName());
            //pass.setText(user.getPassword());
            score.setText(Integer.toString(user.getScore()));
            return convertView;

        }
    }

    public class GetHigheRecrods extends AsyncTask<String, Void, Boolean> {
        String get_hige_score_url = "http://mysite.lidordigital.co.il/Quertets/db/getHighRecords.php";

        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

       // MySQLiteHelper dbHelper = new MySQLiteHelper(MainActivity.this, UserDBConstants.DBName, null, UserDBConstants.User_DB_VESRSION);
        String inputUserName ;
        String inputPassword ;

        @Override
        protected Boolean doInBackground(String... params) {

            JSONParser json = new JSONParser();
            try{
                JSONObject response = json.makeHttpRequest(get_hige_score_url, "POST", parms);
                if (response.getInt("succsses") == 1) {
                    JSONArray jsonArray=response.getJSONArray("HighesRecordesUsers");
                    User user;
                    for (int i = 0; i < jsonArray.length(); i++) {

                        user = new User(jsonArray.getJSONObject(i).getString("user_name"),
                                jsonArray.getJSONObject(i).getString("user_password"),
                                jsonArray.getJSONObject(i).getInt("score"));

                        uList.add(user);
                    }
                    return true;
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            if(result) {
                if (uList.isEmpty()) {
                    Toast.makeText(AllRecords.this, "Ther is no users in DB!", Toast.LENGTH_LONG).show();
                    finish();
                }
                adapter = new MyClassAdapter(AllRecords.this, R.layout.single_user_layout, uList);

                myListView.setAdapter(adapter);

                myListView.setOnItemClickListener(AllRecords.this);

                lv = (ListView) findViewById(R.id.userlist);
                registerForContextMenu(lv);
            }else
                Toast.makeText(AllRecords.this,"no player or connection problem",Toast.LENGTH_LONG).show();

        }
    }
}
