package com.example.yosef.finalproject;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

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

        uList = (ArrayList<User>)getIntent().getSerializableExtra("userList");
        if(uList.isEmpty()){
            Toast.makeText(this,"Ther is no users in DB!",Toast.LENGTH_LONG).show();
            finish();
        }
        adapter = new MyClassAdapter(this, R.layout.single_user_layout, uList);

        myListView.setAdapter(adapter);

        myListView.setOnItemClickListener(this);

        lv = (ListView) findViewById(R.id.userlist);
        registerForContextMenu(lv);

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
               /* for(int i = 0; i< allCities.size(); i++) {
                    if(allCities.get(i).getCityName().equals(allCities.get(info.position).getCityName())) {
                        Toast.makeText(this,allCities.get(info.position).getCityName()+ " Sucssefuly Deleted! ", Toast.LENGTH_SHORT).show();
                        allCities.remove(i);
                        adapter= new MyClassAdapter(this, R.layout.single_city_layout, allCities);
                        myListView.setAdapter(adapter);
                        break;
                    }
                }*/

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
            score.setText("Score: "+Integer.toString(user.getScore()));
            return convertView;

        }
    }
}
