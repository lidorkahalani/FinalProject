package com.example.yosef.finalproject;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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

public class AllPerson extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView personList;
    ListView lv;
    MyClassAdapter adapter;
    ArrayList<User> allUsers =new ArrayList<User>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_person);
        personList=(ListView)findViewById(R.id.PersonListView);

        new GetAllPerson().execute("http://10.0.2.2:8080/TestJersey/rest/hello/getAll");

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
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_person_layout, parent, false);
            }
            TextView userName = (TextView) convertView.findViewById(R.id.personNameSingle);
            TextView id = (TextView) convertView.findViewById(R.id.personidSingle);
            TextView age=(TextView) convertView.findViewById(R.id.personAgeSingle);


            userName.setText(user.getUserName());
            id.setText(user.getPassword());
            age.setText(Integer.toString(user.getScore()));
            return convertView;

        }
    }

    public class GetAllPerson extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            HashMap<String, String> parms = new HashMap<>();
            String response = null;
            try {
                JSONArrayParser pars = new JSONArrayParser();
                JSONArray res = pars.makeHttpRequest(params[0], "GET", parms);

                for (int i = 0; i < res.length(); i++) {
                    JSONObject jo = res.getJSONObject(i);
                    User u = new User(jo.getString("name"), jo.getString("id"), Integer.parseInt(jo.getString("age")));
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
                    Toast.makeText(AllPerson.this, "Ther is no users in DB!", Toast.LENGTH_LONG).show();
                    finish();
                }
                adapter = new MyClassAdapter(AllPerson.this, R.layout.single_user_layout, allUsers);

                personList.setAdapter(adapter);

                personList.setOnItemClickListener(AllPerson.this);

                lv = (ListView) findViewById(R.id.PersonListView);
                registerForContextMenu(lv);
            }else
                Toast.makeText(AllPerson.this,"no User or connection problem",Toast.LENGTH_LONG).show();

        }
    }

}
