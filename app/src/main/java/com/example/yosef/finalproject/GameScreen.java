package com.example.yosef.finalproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameScreen extends AppCompatActivity {
    private ArrayList<Cards> deck = new ArrayList<Cards>();
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





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        new getAllCards().execute();
        randomGenerator = new Random();
        Intent i = getIntent();
        currentPlayer = (User) i.getSerializableExtra("currentPlayer");
        playerList.add(currentPlayer);
        card1 = (TextView) findViewById(R.id.card1);
        card2 = (TextView) findViewById(R.id.card2);
        card3 = (TextView) findViewById(R.id.card3);
        card4 = (TextView) findViewById(R.id.card4);
        bottomLayout = (RelativeLayout) findViewById(R.id.myPlayerBackground);
        left_layout = (RelativeLayout) findViewById(R.id.myPlayerBackground);
        top_layout = (RelativeLayout) findViewById(R.id.myPlayerBackground);
        right_layout = (RelativeLayout) findViewById(R.id.myPlayerBackground);





    }
    public void giveCardEachPlayer() {
        //give each player 4 card from deck and remove it from the server not in client!
        for (int i = 0; i < 1; i++) {
            ArrayList<Cards> onePlayercard = new ArrayList<>();
            for (int k = 0; k < 4; k++) {
                int index = randomGenerator.nextInt(deck.size()+i);
                System.out.println("rand number: "+index);
                //(k*7)
                onePlayercard.add(deck.get(index));
            }
            playerList.get(i).setPlayerCards(onePlayercard);
        }
        //for(int i=0;i<playerList.size();i++)

        ArrayList<Cards> playerCards = playerList.get(0).getPlayerCards();
        for (int i = 0; i < playerCards.size(); i++) {
            insertDataToCard(playerCards.get(i), i);
        }
    }

    public void passCardByNFC() {

    }

    public void insertDataToCard(Cards card, int cardNumber) {
        String cardData;

        cardData= card.getCategoryName()+ "<br>";
        cardData += card.getItemsArray()[0] + "<br>";
        cardData += card.getItemsArray()[1] + "<br>";
        cardData += card.getItemsArray()[2] + "<br>";
        cardData += card.getItemsArray()[3] + "<br>";
        if (cardNumber == 0)
            card1.setText(Html.fromHtml(cardData ));
        else if (cardNumber == 1)
            card2.setText(Html.fromHtml(cardData ));
        else if (cardNumber == 2)
            card3.setText(Html.fromHtml(cardData ));
        else if (cardNumber == 3)
            card4.setText(Html.fromHtml(cardData ));

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
                    int k = 0;
                    int z = 0;
                    Cards card;
                    //create all deck card
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String[] cardLabel = new String[4];
                        for (int j = 0; j < 4; k++, j++) {
                            if (k == jsonArray.length()) {
                                k = 0;
                            }
                            cardLabel[j] = jsonArray.getJSONObject(k).getString("card_name");
                        }
                        if(z==4)
                            z=0;
                        cardLabel[z] = "<b>" + cardLabel[z] + "</b> ";

                            card = new Cards(jsonArray.getJSONObject(i).getInt("card_id"),
                                    jsonArray.getJSONObject((k - 1)).getString("category_name"),
                                    jsonArray.getJSONObject((k - 1)).getInt("category_color"), cardLabel)
                            ;
                        if(((i+1)%(jsonArray.length()/4))==0)
                            z++;
                            deck.add(card);
                }
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
                Toast.makeText(GameScreen.this, "the deck sucssfuly generate!!", Toast.LENGTH_SHORT).show();
                if (playerList.size() == 1)
                    giveCardEachPlayer();
            } else
                Toast.makeText(GameScreen.this, "the deck not load !", Toast.LENGTH_SHORT).show();
        }


        public class fucosCard{




        }

        public void painCurrentPlayer(View v){
            v.setBackgroundColor(Color.GREEN);
        }

        public void onMyTurnEnd(){
            bottomLayout.setBackgroundColor(Color.TRANSPARENT);
        }

    }
}
