package com.example.yosef.finalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import static org.apache.http.HttpHeaders.USER_AGENT;

public class GameScreen extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ArrayList<Card> deck = new ArrayList<Card>();
    private ArrayList<User> playerList = new ArrayList<>();
    private User currentPlayer;
    private TextView card1;
    private TextView card2;
    private TextView card3;
    private TextView card4;
    private Random randomGenerator;

    private RelativeLayout bottomLayout;
    private RelativeLayout left_layout;
    private RelativeLayout top_layout;
    private RelativeLayout right_layout;

    RecyclerView myListView;
    CardsAdapter cardsAdapter;
    MyClassAdapter adapter;

    public static int MENU_ID = 0;
    private static final int CARDS_CLICK_MENU = 1;

    public static View selectedCard;
    private boolean setCardBackgroundTransparent = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
      //  new getAllCards().execute();
        new getFromServerMycard().execute();
        randomGenerator = new Random();
        Intent i = getIntent();
        currentPlayer = (User) i.getSerializableExtra("currentPlayer");
        playerList.add(currentPlayer);
        bottomLayout = (RelativeLayout) findViewById(R.id.myPlayerBackground);
        left_layout = (RelativeLayout) findViewById(R.id.myPlayerBackground);
        top_layout = (RelativeLayout) findViewById(R.id.myPlayerBackground);
        right_layout = (RelativeLayout) findViewById(R.id.myPlayerBackground);
        myListView = (RecyclerView) findViewById(R.id.listcards);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        myListView.setLayoutManager(lm);
        myListView.setItemAnimator(new DefaultItemAnimator());


    }

    public void giveCardEachPlayer() {
        //give each player 4 card from deck and remove it from the server not in client!
        for (int i = 0; i < 1; i++) {
            ArrayList<Card> onePlayercard = new ArrayList<>();
            for (int k = 0; k < 4; k++) {
                int index = randomGenerator.nextInt(deck.size() + i);
                System.out.println("rand number: " + index);
                //(k*7)
                onePlayercard.add(deck.get(index));
            }
            playerList.get(i).setPlayerCards(onePlayercard);
        }
        //for(int i=0;i<playerList.size();i++)

        ArrayList<Card> playerCards = playerList.get(0).getPlayerCards();
        for (int i = 0; i < playerCards.size(); i++) {
            // insertDataToCard(playerCards.get(i), i);
        }
    }

    public void passCardByNFC() {

    }

    public void insertDataToCard(Card card, int cardNumber) {
        String cardData;

        cardData = card.getCategoryName() + "<br>";
        cardData += card.getItemsArray()[0] + "<br>";
        cardData += card.getItemsArray()[1] + "<br>";
        cardData += card.getItemsArray()[2] + "<br>";
        cardData += card.getItemsArray()[3] + "<br>";
        if (cardNumber == 0)
            card1.setText(Html.fromHtml(cardData));
        else if (cardNumber == 1)
            card2.setText(Html.fromHtml(cardData));
        else if (cardNumber == 2)
            card3.setText(Html.fromHtml(cardData));
        else if (cardNumber == 3)
            card4.setText(Html.fromHtml(cardData));

    }

    public void onBackPressed() {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You realy want exit the game?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent myIntent = new Intent(GameScreen.this, MainMenu.class);
                        startActivity(myIntent);
                        //finish();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public class getAllCards extends AsyncTask<String, Void, Boolean> {
       String get_all_card_url = "http://10.0.2.2:8080/Quartets/db/getAllCard.php";
       // String get_all_card_url = "http://10.0.2.2/Quartets/db/getAllCard.php";
       // String get_all_card_url = "http://10.0.2.2:8080/Quartets_Server/GetAllCrds";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            Gson gson=new Gson();
            JSONParser json = new JSONParser();
           try {
                JSONObject response = json.makeHttpRequest(get_all_card_url, "POST", parms);
                String res = gson.toJson(response);
                if (response!=null) {
                    JSONArray jsonArray = response.getJSONArray("AllCards");
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
                        //cardLabels=(String[]) ja.get(0);
                        card.setItemsArray(cardLabels);
                        // card.setItemPicture(getResources().getDrawable(R.drawable.car));
                        deck.add(card);

                    }
                    return true;
                }
            } catch(Exception ex) {
                ex.printStackTrace();
            }
          /* try
            {
                String serverResponse = "";

                // Create Request to server and get response

                HttpGet httpget = new HttpGet(get_all_card_url);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                serverResponse = Client.execute(httpget, responseHandler);
                JSONArray res=new JSONArray(serverResponse);

                if(serverResponse!=null) {
                    try {
                            for(JSONArray j : res) {
                            Card card = gson.fromJson(serverResponse, Card.class);
                            deck.add(card);
                        }
                        return true;
                    }catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }catch (JsonParseException e){
                        e.printStackTrace();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }

                }
                else
                    return false;

            } catch(Exception ex) {
                ex.printStackTrace();
            }
          catch (Exception e) {
                e.printStackTrace();
                return false;
            }*/


           /* HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(get_all_card_url);

            // add request header
            request.addHeader("User-Agent", USER_AGENT);

            HttpResponse response = null;
            try {
                response = client.execute(request);
                System.out.println("\nSending 'GET' request to URL : " + get_all_card_url);
                System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
                BufferedReader rd = null;
                rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer result = new StringBuffer();
                 String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                JSONObject res = new JSONObject(result.toString());
                //JSONArray res=new JSONArray(result.toString());
                    Card card = gson.fromJson(result.toString(), Card.class);
                    deck.add(card);

                System.out.println(result.toString());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
            return false;

        }


        protected void onPostExecute(Boolean result) {
            if (result) {
                setCardsList();
              /*  Toast.makeText(GameScreen.this, "the deck sucssfuly generate!!", Toast.LENGTH_SHORT).show();
                if (playerList.size() == 1)
                    giveCardEachPlayer();*/
            } else
                Toast.makeText(GameScreen.this, "the deck not load !", Toast.LENGTH_SHORT).show();
        }

        public void painCurrentPlayer(View v) {
            v.setBackgroundColor(Color.GREEN);
        }

        public void onMyTurnEnd() {
            bottomLayout.setBackgroundColor(Color.TRANSPARENT);
        }

    }


    public class getFromServerMycard extends AsyncTask<String, Void, Boolean> {
       // String get_all_card_url = "http://10.0.2.2:8080/Quartets/db/getAllCard.php";
        // String get_all_card_url = "http://10.0.2.2/Quartets/db/getAllCard.php";
        String get_all_card_url = "http://10.0.2.2:8080/Quartets_Server/GetAllCrds";
        HttpClient Client = new DefaultHttpClient();
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            Gson gson=new Gson();
            JSONParser json = new JSONParser();

           try
            {
                String serverResponse = "";

                // Create Request to server and get response

                HttpGet httpget = new HttpGet(get_all_card_url);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                serverResponse = Client.execute(httpget, responseHandler);
             //   JSONObject jO=new JSONObject(serverResponse);
                //JSONArray  res=jO.getJSONArray("AllCards");
                JSONArray  res=new JSONArray(serverResponse);

                if(serverResponse!=null) {
                    try {
                            for(int i=0;i<res.length();i++) {

                                JSONObject jo = res.getJSONObject(i);
                            //Card card = gson.fromJson(String.valueOf(res.getJSONObject(i)), Card.class);
                                Card tempCard = new Card();
                                tempCard.setCard_id(jo.getInt("card_id"));
                                tempCard.setCategoryName(jo.getString("category_name"));
                                tempCard.setCategoryColor(jo.getInt("category_color"));
                                tempCard.setCard_id(jo.getInt("category_id"));
                                tempCard.setCardName(jo.getString("card_name"));
                                tempCard.setImageName(jo.getString("image_name"));
                                String[] cardLabels = new String[4];
                                JSONArray ja = jo.getJSONArray("card_labels");
                                for (int j = 0; j < ja.length(); j++) {
                                    JSONObject jo2 = ja.getJSONObject(j);
                                    cardLabels[j] = jo2.getString("card_name");
                                }
                                //cardLabels=(String[]) ja.get(0);
                                tempCard.setItemsArray(cardLabels);
                            deck.add(tempCard);
                        }
                        return true;
                    }catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    }catch (JsonParseException e){
                        e.printStackTrace();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }

                }
                else
                    return false;

            } catch(Exception ex) {
                ex.printStackTrace();
            }
            return false;

           /* HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(get_all_card_url);

            // add request header
            request.addHeader("User-Agent", USER_AGENT);

            HttpResponse response = null;
            try {
                response = client.execute(request);
                System.out.println("\nSending 'GET' request to URL : " + get_all_card_url);
                System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
                BufferedReader rd = null;
                rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer result = new StringBuffer();
                 String line = "";
                while ((line = rd.readLine()) != null) {
                    result.append(line);
                }
                JSONObject res = new JSONObject(result.toString());
                //JSONArray res=new JSONArray(result.toString());
                    Card card = gson.fromJson(result.toString(), Card.class);
                    deck.add(card);

                System.out.println(result.toString());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }*/
        }


        protected void onPostExecute(Boolean result) {
            if (result) {
                setCardsList();
              /*  Toast.makeText(GameScreen.this, "the deck sucssfuly generate!!", Toast.LENGTH_SHORT).show();
                if (playerList.size() == 1)
                    giveCardEachPlayer();*/
            } else
                Toast.makeText(GameScreen.this, "the deck not load !", Toast.LENGTH_SHORT).show();
        }

        public void painCurrentPlayer(View v) {
            v.setBackgroundColor(Color.GREEN);
        }

        public void onMyTurnEnd() {
            bottomLayout.setBackgroundColor(Color.TRANSPARENT);
        }

    }


    private void setCardsList() {
        cardsAdapter = new CardsAdapter(deck);
        myListView.setAdapter(cardsAdapter);
        registerForContextMenu(myListView);
        myListView.addOnItemTouchListener(
                new RecyclerItemClickListener(GameScreen.this, myListView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        MENU_ID = CARDS_CLICK_MENU;
                        openContextMenu(view);
                        LinearLayout cardContainer = (LinearLayout) view.findViewById(R.id.card_container);
                        cardContainer.setBackgroundColor(getResources().getColor(R.color.light_green));
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

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
                menuItems = getResources().getStringArray(R.array.card_click_menu);
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
        int menuItemIndex = item.getItemId();
        if (MENU_ID == CARDS_CLICK_MENU) {

            String[] menuItems = getResources().getStringArray(R.array.card_click_menu);
            String menuItemName = menuItems[menuItemIndex];
            if (menuItemName.equals(menuItems[0])) {//שלח קלף
                Toast.makeText(GameScreen.this,
                        menuItemName + this.deck,
                        Toast.LENGTH_LONG).show();
                setCardBackgroundTransparent = false;


            } else if (menuItemName.equals(menuItems[1])) {//קבל קלף
                Toast.makeText(GameScreen.this,
                        menuItemName,
                        Toast.LENGTH_LONG).show();

            }


        }

        return true;
    }

    @Override
    public void onContextMenuClosed(Menu menu) {
        if (selectedCard != null && setCardBackgroundTransparent) {
            selectedCard.findViewById(R.id.card_container).setBackgroundColor(Color.TRANSPARENT);
            selectedCard.findViewById(R.id.card_container).setBackgroundDrawable(getResources().getDrawable(R.drawable.card_background));
        }

    }

}
