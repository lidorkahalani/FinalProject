package com.example.yosef.finalproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        new getAllCards().execute();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public class getAllCards extends AsyncTask<String, Void, Boolean> {
        String get_all_card_url = "http://mysite.lidordigital.co.il/Quertets/db/getAllCard.php";
        HashMap<String, String> parms = new HashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {

            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(get_all_card_url, "POST", parms);

                if (response.getInt("succsses") == 1) {
                    JSONArray jsonArray = response.getJSONArray("AllCards");
                    for(int i = 0; i<jsonArray.length();i++){
                        Card card = new Card();
                        JSONObject jo = jsonArray.getJSONObject(i);
                        card.setCard_id(jo.getInt("card_id"));
                        card.setCategoryName(jo.getString("category_name"));
                        card.setCategoryColor(jo.getInt("category_color"));
                        card.setCardName(jo.getString("card_name"));
                        String[]cardLabels = new String[4];
                        JSONArray ja = jo.getJSONArray("card_labels");
                        for(int j = 0 ; j<ja.length(); j++){
                            JSONObject jo2 = ja.getJSONObject(j);
                            cardLabels[j] = jo2.getString("card_name");
                        }
                        card.setItemsArray(cardLabels);
                        card.setItemPicture(getResources().getDrawable(R.drawable.car));
                        deck.add(card);

                    }
                 /*   int k = 0;
                    int z = 0;
                    Card card;
                    //create all deck card
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String[] cardLabel = new String[4];
                        for (int j = 0; j < 4; k++, j++) {
                            if (k == jsonArray.length()) {
                                k = 0;
                            }
                            cardLabel[j] = jsonArray.getJSONObject(k).getString("card_name");
                        }
                        if (z == 4)
                            z = 0;
                       // cardLabel[z] = "<b>" + cardLabel[z] + "</b> ";

                        card = new Card(jsonArray.getJSONObject(i).getInt("card_id"),
                                jsonArray.getJSONObject((k - 1)).getString("category_name"),
                                jsonArray.getJSONObject((k - 1)).getInt("category_color"), cardLabel,jsonArray.getJSONObject((k-1)).getString("card_name"));
                        card.setItemPicture(getResources().getDrawable(R.drawable.car));
                        if (((i + 1) % (jsonArray.length() / 4)) == 0)
                            z++;
                        deck.add(card);
                    }*/
                    return true;
                } else {
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }


        }


        protected void onPostExecute(Boolean result) {
            if (result) {
                setCardsList();
                Toast.makeText(GameScreen.this, "the deck sucssfuly generate!!", Toast.LENGTH_SHORT).show();
                if (playerList.size() == 1)
                    giveCardEachPlayer();
            } else
                Toast.makeText(GameScreen.this, "the deck not load !", Toast.LENGTH_SHORT).show();
        }

        private void setCardsList(){
            cardsAdapter = new CardsAdapter(deck);
            myListView.setAdapter(cardsAdapter);
        }

        public class fucosCard {


        }


        public void painCurrentPlayer(View v) {
            v.setBackgroundColor(Color.GREEN);
        }

        public void onMyTurnEnd() {
            bottomLayout.setBackgroundColor(Color.TRANSPARENT);
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

}
