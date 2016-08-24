package com.example.yosef.finalproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class GameScreen extends AppCompatActivity {
    static ArrayList<Integer> colors = new ArrayList<>();
    private ArrayList<Cards> deck = new ArrayList<Cards>();
    private ArrayList<User> playerList = new ArrayList<>();
    private User currentPlayer;
    TextView card1;
    TextView card2;
    TextView card3;
    TextView card4;

    private Random randomGenerator;

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


    }

    public void giveCardEachPlayer() {
        //give each player 4 card from deck and remove it from the server not in client!
        for (int i = 0; i < 1; i++) {
            ArrayList<Cards> onePlayercard = new ArrayList<>();
            for (int k = 0; k < 4; k++) {
                int index = randomGenerator.nextInt(deck.size());
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
        cardData = card.getCategoryName() + "\n";
        cardData += card.getItemsArray()[0] + "\n";
        cardData += card.getItemsArray()[1] + "\n";
        cardData += card.getItemsArray()[2] + "\n";
        cardData += card.getItemsArray()[3] + "\n";
        if (cardNumber == 0)
            card1.setText(cardData);
        else if (cardNumber == 1)
            card2.setText(cardData);
        else if (cardNumber == 2)
            card3.setText(cardData);
        else if (cardNumber == 3)
            card4.setText(cardData);

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
                    int colorNumber = 1;
                    int k = 0;
                    int z=0;
                    boolean finsihSidra=false;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String[] cardLabel = new String[4];
                        for (int j = 0; j < 4; k++, j++) {
                            if (k == 28) {
                                finsihSidra=true;
                                k = 0;
                            }
                            cardLabel[j] = jsonArray.getJSONObject(k).getString("card_name");
                           /* if(finsihSidra&&j==0) {
                                cardLabel[z]+="BOLD";
                                z++;
                            }*/
                        }
                        z=0;
                        Cards card = new Cards(jsonArray.getJSONObject(i).getInt("card_id"),
                                jsonArray.getJSONObject((k - 1)).getString("category_name"),
                                jsonArray.getJSONObject((k - 1)).getInt("category_color"), cardLabel);
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
    }
}
