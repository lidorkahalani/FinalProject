package com.example.yosef.finalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ShowMySerie extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView category_list;
    final String imageRelativePat = "http://10.0.2.2/final_project/images/";

    ListView lv;
    MyClassAdapter adapter;
    ArrayList<Card> allMyCard =new ArrayList<Card>();
   // ArrayList<String> categorys=new ArrayList();
    ArrayList<Category> categorys=new ArrayList();
    int myId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_serie);
        category_list=(ListView)findViewById(R.id.CategoryListView);
        SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(this);
        myId = myPref.getInt("user_id", 0);
        if(myId!=0)
            new GetMySeries().execute(String.valueOf(myId));
        else
            Toast.makeText(this,"user id not exist",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    Toast.makeText(ShowMySerie.this,categorys.get(position).getCategory_name()+" was clicked ",Toast.LENGTH_SHORT).show();
    }

    /*class MyClassAdapter extends ArrayAdapter<Card> {

        public MyClassAdapter(Context context, int resource, List<Card> objects) {
            super(context,resource,objects);
        }

        // the method getView is in charge of creating a single line in the list
        // it receives the position (index) of the line to be created
        // the method populates the view with the data from the relevant object (according to the position)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            String fullPath;
            Log.i("TEST getView", "inside getView position " + position);

            Card card = getItem(position);
            //String card2 = getItem(position);
            if (convertView == null) {
                Log.e("TEST getView", "inside if with position " + position);
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_quartets_layot, parent, false);
            }
            TextView categoryName = (TextView) convertView.findViewById(R.id.category_name);
            ImageView image1=(ImageView)convertView.findViewById(R.id.pic1);
            fullPath = imageRelativePat + card.getImageName();
            ImageLoader imageLoader = new ImageLoader(getContext());
            imageLoader.DisplayImage(fullPath, R.mipmap.ic_launcher, image1);

            categoryName.setText(card.getCategoryName());

            return convertView;

        }
    }*/

    public class GetMySeries extends AsyncTask<String, Void, Boolean> {
        String GetMySeries = "http://10.0.2.2/final_project/db/getMySeries.php";
        //String GetMySeries = "http://mysite.lidordigital.co.il/Quertets/db/getMySeries.php";


        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("user_id", params[0]);
            JSONParser json = new JSONParser();
            Category temp_categorys=new Category();;
            try {
                JSONObject response = json.makeHttpRequest(GetMySeries, "GET", parms);
                if (response.getInt("succsses") == 1) {
                    JSONArray jsonArray = response.getJSONArray("myCards");
                    for (int i = 0; i < jsonArray.length(); i+=4) {
                     //   Card card = new Card();
                        if(i%4==0)
                             temp_categorys=new Category();

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
                        categorys.add(temp_categorys);
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
                if (categorys.isEmpty()) {//allMyCard.isEmpty()) {
                    Toast.makeText(ShowMySerie.this, getResources().getString(R.string.dont_have_cards), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    adapter = new MyClassAdapter(ShowMySerie.this, R.layout.single_quartets_layot,categorys);
                   // adapter = new MyClassAdapter(ShowMySerie.this, R.layout.single_quartets_layot,allMyCard);

                    category_list.setAdapter(adapter);

                    category_list.setOnItemClickListener(ShowMySerie.this);

                    lv = (ListView) findViewById(R.id.CategoryListView);
                    registerForContextMenu(lv);
                }
            } else {
                Toast.makeText(ShowMySerie.this, getResources().getString(R.string.dont_have_cards), Toast.LENGTH_LONG).show();
                finish();
            }

        }
    }


    class MyClassAdapter extends ArrayAdapter<Category> {

        public MyClassAdapter(Context context, int resource, List<Category> objects) {
            super(context,resource,objects);
        }

        // the method getView is in charge of creating a single line in the list
        // it receives the position (index) of the line to be created
        // the method populates the view with the data from the relevant object (according to the position)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Bitmap bitmap;
            String fullPath;
            File imgFile;
            Uri uri;
            Log.i("TEST getView", "inside getView position " + position);

            Category category = getItem(position);
            //String card2 = getItem(position);
            if (convertView == null) {
                Log.e("TEST getView", "inside if with position " + position);
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_quartets_layot, parent, false);
            }
            TextView categoryName = (TextView) convertView.findViewById(R.id.category_name);
            ImageView image1=(ImageView)convertView.findViewById(R.id.pic1);
            ImageView image2=(ImageView)convertView.findViewById(R.id.pic2);
            ImageView image3=(ImageView)convertView.findViewById(R.id.pic3);
            ImageView image4=(ImageView)convertView.findViewById(R.id.pic4);

           /* imgFile = new File((imageRelativePat + category.getImage1()));
            uri = Uri.fromFile(imgFile);
            image1.setImageURI(uri);

            imgFile = new File((imageRelativePat + category.getImage2()));
            uri = Uri.fromFile(imgFile);
            image2.setImageURI(uri);

            imgFile = new File((imageRelativePat + category.getImage3()));
            uri = Uri.fromFile(imgFile);
            image3.setImageURI(uri);

            imgFile = new File((imageRelativePat + category.getImage4()));
            uri = Uri.fromFile(imgFile);
            image4.setImageURI(uri);*/

           // fullPath = imageRelativePat + category.getImage1();
            ImageLoader imageLoader = new ImageLoader(getContext());
            imageLoader.DisplayImage((imageRelativePat + category.getImage1()), R.mipmap.ic_launcher, image1);

            imageLoader.DisplayImage(imageRelativePat + category.getImage2(), R.mipmap.ic_launcher, image2);

            imageLoader.DisplayImage(imageRelativePat + category.getImage3(), R.mipmap.ic_launcher, image3);

            imageLoader.DisplayImage(imageRelativePat + category.getImage4(), R.mipmap.ic_launcher, image4);

            categoryName.setText(category.getCategory_name());

            return convertView;

        }
    }
}
