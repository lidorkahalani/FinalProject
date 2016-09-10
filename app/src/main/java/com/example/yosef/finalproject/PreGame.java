package com.example.yosef.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PreGame extends AppCompatActivity {

    private ArrayList<String> Category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_game);


        ArrayList<Quartet> quartets = GlobalAppData.getInstance().getQuartets();
        for(Quartet q : quartets){
            Category.add(q.getCategory());
        }
        ListView categoryListView = (ListView)findViewById(R.id.category_List_View);
        //categoryListView.setAdapter(new ArrayAdapter<>());


    }
}
