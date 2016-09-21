package com.example.yosef.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ShowMyCards extends AppCompatActivity implements AdapterView.OnItemClickListener {

   /* ListView lv;
    ListView myListView;
    MyClassAdapter adapter;
    ArrayList<User> uList =new ArrayList<User>();
    String myId;
    ArrayList<Card>deck=new ArrayList<>();*/

    private ArrayList<Card> deck = new ArrayList<Card>();
    private int PICK_IMAGE_REQUEST = 1;
    RecyclerView myListView;
    CardsAdapter cardsAdapter;
    int myId;
    Card selectedCardEditOrDelete;
    boolean correctInput;
    public static int MENU_ID = 0;
    private static final int CARDS_CLICK_MENU = 1;
    private static final int UPDATE_CARD = 2;

    public static View selectedCard;
    private boolean setCardBackgroundTransparent = true;


    private Button buttonChoose;

    private Button buttonView;

    private Button buttonUpload;

    private ImageView imageView;

    private Bitmap bitmap;
    private boolean imageCoosen;


    private Uri filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_card);
        myListView = (RecyclerView) findViewById(R.id.listcards);
        registerForContextMenu(myListView);
        SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(this);
        myId = myPref.getInt("user_id", 0);
        new GetMycards().execute(String.valueOf(myId));

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        myListView.setLayoutManager(lm);
        myListView.setItemAnimator(new DefaultItemAnimator());

        selectedCard = (View) findViewById(R.id.card_container);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        User user = (User) parent.getItemAtPosition(position);
        Toast.makeText(this, user.getUserName() + " Selcted", Toast.LENGTH_SHORT).show();
    }

    public class GetMycards extends AsyncTask<String, Void, Boolean> {
        String get_my_cards = "http://mysite.lidordigital.co.il/Quertets/db/getMyCards.php";

        LinkedHashMap<String, String> parms = new LinkedHashMap<>();


        // MySQLiteHelper dbHelper = new MySQLiteHelper(MainActivity.this, UserDBConstants.DBName, null, UserDBConstants.User_DB_VESRSION);
        String inputUserName;
        String inputPassword;

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("user_id", params[0]);
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(get_my_cards, "GET", parms);
                if (response.getInt("succsses") == 1) {
                    JSONArray jsonArray = response.getJSONArray("myCards");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Card card = new Card();
                        JSONObject jo = jsonArray.getJSONObject(i);
                        card.setCard_id(jo.getInt("card_id"));
                        card.setCategoryName(jo.getString("category_name"));
                        card.setCategoryColor(jo.getInt("category_color"));
                        card.setCardName(jo.getString("card_name"));
                        card.setImageName(jo.getString("image_name"));
                        String[] cardLabels = new String[4];
                        JSONArray ja = jo.getJSONArray("card_labels");
                        for (int j = 0; j < ja.length(); j++) {
                            JSONObject jo2 = ja.getJSONObject(j);
                            cardLabels[j] = jo2.getString("card_name");
                        }
                        card.setItemsArray(cardLabels);
                        // card.setItemPicture(getResources().getDrawable(R.drawable.car));
                        deck.add(card);
                    }
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
                if (deck.isEmpty()) {
                    Toast.makeText(ShowMyCards.this, "you dont have any cards in DB!", Toast.LENGTH_LONG).show();
                    finish();
                }
                setCardsList();
            } else
                Toast.makeText(ShowMyCards.this, "you dont have any cards", Toast.LENGTH_LONG).show();

        }
    }

    private void setCardsList() {
        cardsAdapter = new CardsAdapter(deck);
        myListView.setAdapter(cardsAdapter);
        registerForContextMenu(myListView);
        myListView.addOnItemTouchListener(
                new RecyclerItemClickListener(ShowMyCards.this, myListView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        MENU_ID = CARDS_CLICK_MENU;
                        openContextMenu(view);
                        LinearLayout cardContainer = (LinearLayout) view.findViewById(R.id.card_container);
                        selectedCard = cardContainer;
                        cardContainer.setBackgroundColor(getResources().getColor(R.color.light_green));
                        selectedCardEditOrDelete = ((CardsAdapter) myListView.getAdapter()).getItem(position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Toast.makeText(ShowMyCards.this, "Edit", Toast.LENGTH_LONG).show();
                    }
                })
        );
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
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.singelcard, parent, false);
            }
            TextView catgory = (TextView) convertView.findViewById(R.id.category);
            TextView name1 = (TextView) convertView.findViewById(R.id.card_name1);
            TextView name2 = (TextView) convertView.findViewById(R.id.card_name2);
            TextView name3 = (TextView) convertView.findViewById(R.id.card_name3);
            TextView name4 = (TextView) convertView.findViewById(R.id.card_name4);


            catgory.setText(deck.get(0).getCategoryName());
            name1.setText(deck.get(0).getItemsArray()[0]);
            name2.setText(deck.get(0).getItemsArray()[1]);
            name3.setText(deck.get(0).getItemsArray()[2]);
            name4.setText(deck.get(0).getItemsArray()[3]);

            return convertView;

        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        String menuItems[];
        switch (view.getId()) {
            case R.id.listcards:
                setCardBackgroundTransparent = true;
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
                menuItems = getResources().getStringArray(R.array.my_card);
                menu.setHeaderTitle(getResources().getString(R.string.choose));
                //selectedCardEditOrDelete=cardsAdapter.getItem(((AdapterView.AdapterContextMenuInfo)menuInfo).position);
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

        String delete_card = "http://mysite.lidordigital.co.il/Quertets/db/deleteSeries.php";
        if (MENU_ID == CARDS_CLICK_MENU) {
            String[] menuItems = getResources().getStringArray(R.array.my_card);
            String menuItemName = menuItems[menuItemIndex];//delete card
            if (menuItemName.equals(menuItems[0])) {
                setCardBackgroundTransparent = false;
                // cardId=deck.get(menuItemIndex).getCard_id();
                new deleteCard().execute(delete_card, String.valueOf(selectedCardEditOrDelete.getCard_id()));

            } else if (menuItemName.equals(menuItems[1])) {//Update Card
               openUpdateCardSCreen();
                /*
                LayoutInflater li = LayoutInflater.from(ShowMyCards.this);
                final View dialogView = li.inflate(R.layout.updatecard, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(ShowMyCards.this);
                builder.setView(dialogView);
                builder.setTitle(getResources().getString(R.string.update_Card_dialog_title));
                builder.setMessage(getResources().getString(R.string.update_Card_masage));
                final EditText category = (EditText) dialogView.findViewById(R.id.category);
                final EditText itemText = (EditText) dialogView.findViewById(R.id.item1);

                category.setText(selectedCardEditOrDelete.getCategoryName());
                itemText.setText(selectedCardEditOrDelete.getCardName());
                builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        correctInput = true;
                            /*if((category.getText().toString().matches("[a-zA-Z]+")))
                            {
                                if(category.getText().toString().length()<=11) {
                                    correctInput = true;
                                    Toast.makeText(ShowMyCards.this, "word to long", Toast.LENGTH_SHORT).show();
                                }

                            }*/
                 /*       if (correctInput) {
                            if (category.getText().toString().isEmpty() || itemText.getText().toString().isEmpty()) {
                                Toast.makeText(ShowMyCards.this, "ther is empty field", Toast.LENGTH_LONG).show();
                                return;
                            } else {

                                final String newCategory;
                                final String newItem;
                                int cardId;

                                newCategory = category.getText().toString();
                                newItem = itemText.getText().toString();
                                cardId = selectedCardEditOrDelete.getCard_id();
                                //age = a.getText().toString();
                                new UpdateCard().execute("http://mysite.lidordigital.co.il/Quertets/db/UpdateCard", newCategory, newItem, String.valueOf(cardId));
                            }
                        } else {
                            Toast.makeText(ShowMyCards.this, "number cannot bee user name", Toast.LENGTH_LONG).show();
                            return;
                        }

                    }
                });*/
                /*builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
                    */

                // new MyWebServiceTask().execute("http://localhost:8080/TestWebServicesAndJSON/rest/hello/checkUser?personName="
                //         +userName+"personId="+id+"personAge="+age);


            }


        }

        return true;
    }

    public void openUpdateCardSCreen(){
        Intent intent=new Intent(this, UpdateCard.class);
        intent.putExtra("selectedCard",selectedCardEditOrDelete);
        startActivityForResult(intent,UPDATE_CARD);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_CARD && resultCode == RESULT_OK && data != null && data.getData() != null) {

          //  new UpdateCard().execute("http://mysite.lidordigital.co.il/Quertets/db/UpdateCard", newCategory, newItem, String.valueOf(cardId));

        }
    }
    @Override
    public void onContextMenuClosed(Menu menu) {
        if (selectedCard != null && setCardBackgroundTransparent) {
            selectedCard.findViewById(R.id.card_container).setBackgroundColor(Color.TRANSPARENT);
            selectedCard.findViewById(R.id.card_container).setBackgroundDrawable(getResources().getDrawable(R.drawable.card_background));
        }

    }


    public class deleteCard extends AsyncTask<String, Void, Boolean> {
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("card_id", params[1]);
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(params[0], "GET", parms);

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
                Toast.makeText(ShowMyCards.this, "Delete Succsesful", Toast.LENGTH_LONG).show();
                finish();
                startActivity(getIntent());
            } else
                Toast.makeText(ShowMyCards.this, "Delete Failed!", Toast.LENGTH_SHORT).show();
        }


        public class fucosCard {


        }


        public void painCurrentPlayer(View v) {
            v.setBackgroundColor(Color.GREEN);
        }

        public void onMyTurnEnd() {
            // bottomLayout.setBackgroundColor(Color.TRANSPARENT);
        }


    }

    public class editCard extends AsyncTask<String, Void, Boolean> {
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("card_id", params[1]);
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(params[0], "GET", parms);

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
                Toast.makeText(ShowMyCards.this, "Delete Succsesful", Toast.LENGTH_LONG).show();
                finish();
                startActivity(getIntent());
            } else
                Toast.makeText(ShowMyCards.this, "Delete Failed!", Toast.LENGTH_SHORT).show();
        }


        public class fucosCard {


        }


        public void painCurrentPlayer(View v) {
            v.setBackgroundColor(Color.GREEN);
        }

        public void onMyTurnEnd() {
            // bottomLayout.setBackgroundColor(Color.TRANSPARENT);
        }


    }

    class UpdateCard extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(ShowMyCards.this, response, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {
            LinkedHashMap<String, String> parms = new LinkedHashMap<>();
            parms.put("category", params[1]);
            parms.put("card_name", params[2]);
            parms.put("card_id", params[3]);
            parms.put("card_image",params[4]);
            //parms.put("personAge",params[3]);
            JSONParser pars = new JSONParser();
            JSONObject response = pars.makeHttpRequest(params[0], "GET", parms);

            try {
                if (response.getInt("succsses") == 1) {
                    return "Update succsseful!";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "something wrong";
        }
    }
}
