package com.example.yosef.finalproject;

import android.content.Context;
import android.os.AsyncTask;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class AllRecords extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lv;
    ListView myListView;
    MyClassAdapter adapter;
    ArrayList<User> uList =new ArrayList<User>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_records);
        myListView = (ListView) findViewById(R.id.UserList);
        new getAllUserList().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        User user = (User) parent.getItemAtPosition(position);
        Toast.makeText(this,user.getUserName()+" Selcted",Toast.LENGTH_SHORT).show();
    }

   /* @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.UserList) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_list, menu);
        }
    }*/

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.delete:

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
            TextView score=(TextView) convertView.findViewById(R.id.score);


            userName.setText(user.getUserName());
            //pass.setText(user.getPassword());
            score.setText("     "+Integer.toString(user.getScore()));
            return convertView;

        }
    }

    public class getAllUserList extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            LinkedHashMap<String, String> parms = new LinkedHashMap<>();
            try {
                JSONParser json = new JSONParser();
                JSONObject response = json.makeHttpRequest(ServerUtils.getAllUserList, "POST",parms);
                JSONArray res=response.getJSONArray("usersList");
                uList.clear();
                for (int i = 0; i < res.length(); i++) {
                    JSONObject jo = res.getJSONObject(i);
                    User u = new User(  jo.getString("user_name"),
                                        jo.getString("user_password"),
                                        jo.getInt("score"),
                                        jo.getInt("user_id"));
                    uList.add(u);
                }


            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;

        }
        protected void onPostExecute(Boolean result) {
            if(result) {
                if (uList.isEmpty()) {
                    Toast.makeText(AllRecords.this, "no users in db", Toast.LENGTH_LONG).show();
                    finish();
                }
                adapter = new MyClassAdapter(AllRecords.this, R.layout.single_user_layout, uList);

                myListView.setAdapter(adapter);

                myListView.setOnItemClickListener(AllRecords.this);

                lv = (ListView) findViewById(R.id.UserList);
                registerForContextMenu(lv);


            }else
                Toast.makeText(AllRecords.this,getResources().getString(R.string.connection_error),Toast.LENGTH_LONG).show();

        }
    }
}