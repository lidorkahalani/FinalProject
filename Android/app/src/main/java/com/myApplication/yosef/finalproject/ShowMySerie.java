package com.myApplication.yosef.finalproject;

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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ShowMySerie extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView category_list;
    private ListView lv;
    private MyClassAdapter adapter;
    public static int MENU_ID = 0;
    private static final int SERIES_CLICK_MENU = 1;
    private static final int UPDATE_SERIES_REQUEST = 2;
    private ArrayList<Card> allMyCard =new ArrayList<Card>();
    private ArrayList<Series> series =new ArrayList();
    private Series selectedSeries;
    int position;
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
            Toast.makeText(this,getResources().getString(R.string.user_id_not_known),Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //selectedSeries= series.get(position);
    //Toast.makeText(ShowMySerie.this,series.get(position).getCategory_name()+" was clicked ",Toast.LENGTH_SHORT).show();
    Toast.makeText(ShowMySerie.this,getResources().getString(R.string.push_hold),Toast.LENGTH_SHORT).show();
    }

    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        String menuItems[];
        switch (view.getId()) {
            case R.id.CategoryListView:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                position=info.position;
                menuItems = getResources().getStringArray(R.array.my_card);
                menu.setHeaderTitle(getResources().getString(R.string.choose));
                break;
            default:
                return;
        }

        for (int i = 0; i < menuItems.length; i++) {
            menu.add(Menu.NONE, i, i, menuItems[i]);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final int menuItemIndex = item.getItemId();
        MENU_ID = SERIES_CLICK_MENU;
        /*localhost*/
        //final String delete_series = "http://10.0.2.2/final_project/db/deleteSeries.php";
        //final String delete_series = "http://mysite.lidordigital.co.il/Quertets/php/db/deleteSeries.php";


        if (MENU_ID == SERIES_CLICK_MENU) {
            String[] menuItems = getResources().getStringArray(R.array.my_card);
            String menuItemName = menuItems[menuItemIndex];//delete card
            if (menuItemName.equals(menuItems[0])) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getResources().getString(R.string.Are_you_sure_delet_series))
                        .setCancelable(false)
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new deleteSeries().execute(ServerUtils.delete_series, String.valueOf(series.get(position).getCategory_id()));
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else if (menuItemName.equals(menuItems[1])) {//Update series
                    Intent intent = new Intent(this, UpdateSeries.class);
                    intent.putExtra("SelectedSeries", series.get(position));
                    intent.putExtra("user_id",myId);
                    startActivity(intent);
                    //finish();
            }


        }

        return true;
    }

    @Override
    public void onContextMenuClosed(Menu menu) {

    }

    public class GetMySeries extends AsyncTask<String, Void, Boolean> {
        //String GetMySeries = "http://10.0.2.2/final_project/db/getMySeries.php";
        //String GetMySeries = "http://mysite.lidordigital.co.il/Quertets/php/db/getMySeries.php";


        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("user_id", params[0]);
            JSONParser json = new JSONParser();
            Series temp_categorys=new Series();
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
            if (result) {
                if (series.isEmpty()) {//allMyCard.isEmpty()) {
                    Toast.makeText(ShowMySerie.this, getResources().getString(R.string.dont_have_cards), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    adapter = new MyClassAdapter(ShowMySerie.this, R.layout.single_quartets_layot, series);
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

    class MyClassAdapter extends ArrayAdapter<Series> {

        public MyClassAdapter(Context context, int resource, List<Series> objects) {
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

            Series series = getItem(position);
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

           /* imgFile = new File((imageRelativePat + series.getImage1()));
            uri = Uri.fromFile(imgFile);
            image1.setImageURI(uri);

            imgFile = new File((imageRelativePat + series.getImage2()));
            uri = Uri.fromFile(imgFile);
            image2.setImageURI(uri);

            imgFile = new File((imageRelativePat + series.getImage3()));
            uri = Uri.fromFile(imgFile);
            image3.setImageURI(uri);

            imgFile = new File((imageRelativePat + series.getImage4()));
            uri = Uri.fromFile(imgFile);
            image4.setImageURI(uri);*/

           // fullPath = imageRelativePat + series.getImage1();
            ImageLoader imageLoader = new ImageLoader(getContext());
            imageLoader.DisplayImage((ServerUtils.imageRelativePat + series.getImage1()), R.mipmap.ic_launcher, image1);

            imageLoader.DisplayImage(ServerUtils.imageRelativePat + series.getImage2(), R.mipmap.ic_launcher, image2);

            imageLoader.DisplayImage(ServerUtils.imageRelativePat + series.getImage3(), R.mipmap.ic_launcher, image3);

            imageLoader.DisplayImage(ServerUtils.imageRelativePat + series.getImage4(), R.mipmap.ic_launcher, image4);

            categoryName.setText(series.getCategory_name());

            return convertView;

        }
    }

    public class deleteSeries extends AsyncTask<String, Void, Boolean> {
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("category_id", params[1]);
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(params[0], "POST", parms);

                if (response.getInt("succsses") == 1) {
                    return true;
                }

            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            return false;
        }
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(ShowMySerie.this, getResources().getString(R.string.delete_succsessfull), Toast.LENGTH_LONG).show();

                //startActivity(getIntent());
            } else
                Toast.makeText(ShowMySerie.this, getResources().getString(R.string.delete_failed), Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
