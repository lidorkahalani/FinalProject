package com.example.yosef.finalproject;


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
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class GameScreen extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ArrayList<Card> deck = new ArrayList<Card>();
    private ArrayList<User> playerList = new ArrayList<>();
    private User currentPlayer;

    private Random randomGenerator;

    private RelativeLayout bottomLayout;
    private RelativeLayout left_layout;
    private RelativeLayout top_layout;
    private RelativeLayout right_layout;
    private RelativeLayout activePlayer;


    private RecyclerView myListView;
    private CardsAdapter cardsAdapter;

    public static int MENU_ID = 0;
    private static final int CARDS_CLICK_MENU = 1;

    public static View selectedCard;
    private View cardView;
    private boolean setCardBackgroundTransparent = true;
    private Game newGame;
    private Boolean debugStatus;
    private Boolean deckIsOver=false;
    private Boolean isMyTurnStatus;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        newGame = (Game) getIntent().getSerializableExtra("Game");
        Intent i = getIntent();
        currentPlayer = (User) i.getSerializableExtra("currentPlayer");
        debugStatus=getIntent().getExtras().getBoolean("debug");
        if(debugStatus)
            new getAllCards().execute();
        else
            new startPlay().execute();

        randomGenerator = new Random();

        playerList.add(currentPlayer);
        activePlayer=(RelativeLayout) findViewById(R.id.activePlayer);
        bottomLayout = (RelativeLayout) findViewById(R.id.myPlayerBackground);
        left_layout = (RelativeLayout) findViewById(R.id.myPlayerBackground);
        top_layout = (RelativeLayout) findViewById(R.id.myPlayerBackground);
        right_layout = (RelativeLayout) findViewById(R.id.myPlayerBackground);
        myListView = (RecyclerView) findViewById(R.id.listcards);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        myListView.setLayoutManager(lm);
        myListView.setItemAnimator(new DefaultItemAnimator());


    }

   /* @Override
    public void onResume() {
        super.onResume();
        timer = new Timer();
        // This timer task will be executed every 1 sec.
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
               new refresh().execute();
            }
        }, 0, 1000);
    }*/

    public void onBackPressed() {
        // TODO Auto-generated method stub
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.exitWarning))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent myIntent = new Intent(GameScreen.this, MainMenu.class);
                        startActivity(myIntent);
                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void refresh(View v) {
        new refresh().execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void takeCard(View v){
        if(isMyTurnStatus)
            new takeOneCardFromDeck().execute();
        else
            Toast.makeText(this,"you cannot take card",Toast.LENGTH_SHORT).show();
           // new tryTakeOneCardFromDeck().execute();
    }

    private void setCardsList() {
        cardsAdapter = new CardsAdapter(deck);
        myListView.setAdapter(cardsAdapter);
        registerForContextMenu(myListView);
        myListView.addOnItemTouchListener(
                new RecyclerItemClickListener(GameScreen.this, myListView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        cardView=view;
                       // position=myListView.getChildPosition(view);
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
                if(!isMyTurnStatus)
                    new sendSelectedCard().execute(String.valueOf(this.deck.get(myListView.getChildPosition(cardView)).getCard_id()));
               else Toast.makeText(GameScreen.this,"you cannot send card on your turn",
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

    public class startPlay extends AsyncTask<String, Void, Boolean> {
        String get4Cards = "http://10.0.2.2/final_project/db/giveMe4Cards.php";
        // String get4Cards = "http://mysite.lidordigital.co.il/Quertets/giveMe4Cards.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("user_id", String.valueOf(currentPlayer.getUserID()));
            parms.put("game_id", String.valueOf(newGame.getGame_id()));

            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(get4Cards, "POST", parms);
                if (response.getInt("succsses") == 1) {
                    deck.clear();
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
                        card.setItemsArray(cardLabels);
                        deck.add(card);
                    }
                    return true;
                } else
                    return false;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }
        protected void onPostExecute(Boolean result) {
            if (result) {
                setCardsList();

                new isMyTurn().execute();
                //new checkQuartets().execute();
            } else
                Toast.makeText(GameScreen.this, getResources().getString(R.string.card_not_load), Toast.LENGTH_SHORT).show();
        }


    }

    public class sendSelectedCard extends AsyncTask<String,Void,Boolean>{
        String sendSelctedCard="http://10.0.2.2/final_project/db/sendSelctedCard.php";
        //String sendSelctedCard="http://mysite.lidordigital.co.il/Quertets/db/sendSelctedCard.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();
        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("game_id",String.valueOf(newGame.getGame_id()));
            parms.put("card_id",params[0]);
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(sendSelctedCard, "POST", parms);
                if(response.getInt("successes")==1)
                    return true;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
            return false;
        }
        protected void onPostExecute(Boolean res){
            if(res) {
                deck.remove(myListView.getChildLayoutPosition(cardView));
                setCardsList();
            } else
                Toast.makeText(GameScreen.this,"Card didnt send", Toast.LENGTH_LONG).show();
        }
    }

    public class takeOneCardFromDeck extends AsyncTask<String, Void, Boolean> {
        String takeOneCardFromDeck = "http://10.0.2.2/final_project/db/takeOneCardFromDeck.php";
        //String takeOneCardFromDeck = "http://mysite.lidordigital.co.il/Quertets/db/takeOneCardFromDeck.php";

        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("user_id", String.valueOf(currentPlayer.getUserID()));
            parms.put("game_id", String.valueOf(newGame.getGame_id()));

            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(takeOneCardFromDeck, "POST", parms);
                if (response.getInt("succsses") == 1) {
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
                        card.setItemsArray(cardLabels);
                        deck.add(card);
                    }
                } else{// if(response.getInt("succsses") ==-1){
                    deckIsOver=true;
                }
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                setCardsList();
                if(!deckIsOver)
                 new moveToNextPlayer().execute();
                else
                    Toast.makeText(GameScreen.this,"deck end game over!", Toast.LENGTH_SHORT).show();
                    //goCheckWhoWin
                //new checkQuartets().execute();
                //new isTheGameOver().execute();
            } else
                Toast.makeText(GameScreen.this, "Take one card failed", Toast.LENGTH_SHORT).show();
        }

    }

    public class moveToNextPlayer extends AsyncTask<String, Void, Boolean> {
        String setTurnOrder = "http://10.0.2.2/final_project/db/moveToNextPlayer.php";
        // String setTurnOrder = "http://mysite.lidordigital.co.il/Quertets/db/moveToNextPlayer.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("game_id", String.valueOf(newGame.getGame_id()));
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(setTurnOrder, "POST", parms);

                if (response.getInt("successes")== 1) {
                    isMyTurnStatus=false;
                } else {
                    isMyTurnStatus=true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        protected void onPostExecute(Boolean result) {
            if (result) {
                if(isMyTurnStatus)
                    activePlayer.setBackgroundColor(Color.GREEN);
                else
                    activePlayer.setBackgroundColor(Color.TRANSPARENT);
                //new refresh().execute();
            }else
                Toast.makeText(GameScreen.this,"move To Next Player failed",Toast.LENGTH_LONG).show();

        }

    }

    public class isMyTurn extends AsyncTask<String, Void, Boolean> {
        String isMyTurn = "http://10.0.2.2/final_project/db/isMyTurn.php";
        //String isMyTurn = "http://mysite.lidordigital.co.il/Quertets/db/isMyTurn.php";

        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("user_id", String.valueOf(currentPlayer.getUserID()));
            parms.put("game_id", String.valueOf(newGame.getGame_id()));

            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(isMyTurn, "POST", parms);
                if (response.getInt("successes") == 1) {
                    isMyTurnStatus =true;
                } else if(response.getInt("successes") == 0) {
                    isMyTurnStatus =false;
                }else
                    return false;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {

                if(isMyTurnStatus)
                    activePlayer.setBackgroundColor(Color.GREEN);
                else
                    activePlayer.setBackgroundColor(Color.TRANSPARENT);
                new refresh().execute();
                //Toast.makeText(GameScreen.this, "is my turn!", Toast.LENGTH_LONG).show();
                //painCurrentPlayer();
            } else
                Toast.makeText(GameScreen.this, "there was problem check if is my turn", Toast.LENGTH_LONG).show();
        }
    }

    public class refresh extends AsyncTask<String, Void, Boolean> {
        /*need to add check if all player stil in the game*/
        String refresh_all = "http://10.0.2.2/final_project/db/refresh_all.php";
        // String refresh_all = "http://mysite.lidordigital.co.il/Quertets/db/refresh_all.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("game_id",String.valueOf(newGame.getGame_id()));
            parms.put("user_id",String.valueOf(currentPlayer.getUserID()));
           /* parms.put("game_id","6");
            parms.put("user_id","16");*/

            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(refresh_all, "POST", parms);
                if (response.getInt("succsses") == 1) {
                    deck.clear();
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
                        //card.setItemPicture(getResources().getDrawable(R.drawable.car));
                        deck.add(card);
                    }
                   if( response.getInt("isMyturn")==1   )
                       isMyTurnStatus =true;
                    else
                       isMyTurnStatus =false;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
            return true;

        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                setCardsList();
               if(isMyTurnStatus) {
                    activePlayer.setBackgroundColor(Color.GREEN);
                }
                else
                   activePlayer.setBackgroundColor(Color.TRANSPARENT);
            } else
                Toast.makeText(GameScreen.this, "refresh failed !", Toast.LENGTH_SHORT).show();
        }

    }

    public class getAllCards extends AsyncTask<String, Void, Boolean> {
        String get_all_card_url = "http://10.0.2.2/final_project/db/getAllCard.php";
        // String get_all_card_url = "http://mysite.lidordigital.co.il/Quertets/db/getAllCard.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(get_all_card_url, "GET", parms);
                if (response.getInt("succsses") == 1) {
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
                        card.setItemsArray(cardLabels);
                        //card.setItemPicture(getResources().getDrawable(R.drawable.car));
                        deck.add(card);
                    }
                    return true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;

        }
        protected void onPostExecute(Boolean result) {
            if (result) {
                setCardsList();
            } else
                Toast.makeText(GameScreen.this, "the deck not load !", Toast.LENGTH_SHORT).show();
        }

    }

    public class checkQuartets extends AsyncTask<String, Void, Boolean> {
        String checkQuarets = "http://10.0.2.2/final_project/db/checkQuarets.php";
        //String isMyTurn = "http://mysite.lidordigital.co.il/Quertets/db/checkQuarets.php";

        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("user_id", String.valueOf(currentPlayer.getUserID()));
            parms.put("game_id", String.valueOf(newGame.getGame_id()));

            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(checkQuarets, "POST", parms);
                if (response.getInt("successes") == 1) {
                    isMyTurnStatus =true;
                } else if(response.getInt("successes") == 0) {
                    isMyTurnStatus =false;
                }else
                    return false;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {

            } else
                Toast.makeText(GameScreen.this, "wait to your turn", Toast.LENGTH_LONG).show();
        }
    }

    public class isTheGameOver extends AsyncTask<String, Void, Boolean> {
        String isTheGameOver = "http://10.0.2.2/final_project/db/isTheGameOver.php";
        //String isTheGameOver = "http://mysite.lidordigital.co.il/Quertets/db/isTheGameOver.php";

        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("user_id", String.valueOf(currentPlayer.getUserID()));
            parms.put("game_id", String.valueOf(newGame.getGame_id()));

            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(isTheGameOver, "POST", parms);
                if (response.getInt("successes") == 1) {
                    isMyTurnStatus =true;
                } else if(response.getInt("successes") == 0) {
                    isMyTurnStatus =false;
                }else
                    return false;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
            return true;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {

            } else
                Toast.makeText(GameScreen.this, "wait to your turn", Toast.LENGTH_LONG).show();
        }
    }

    public class tryTakeOneCardFromDeck extends AsyncTask<String, Void, Boolean> {
        String takeOneCardFromDeck = "http://10.0.2.2/final_project/db/takeOneCardFromDeck.php";
        //String takeOneCardFromDeck = "http://mysite.lidordigital.co.il/Quertets/db/takeOneCardFromDeck.php";

        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("user_id",String.valueOf(currentPlayer.getUserID()));
            parms.put("game_id", String.valueOf(newGame.getGame_id()));

            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(takeOneCardFromDeck, "POST", parms);
                if (response.getInt("successes") == 1) {
                    return true;
                } else
                    return false;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }


        protected void onPostExecute(Boolean result) {
            if (result) {
                new takeOneCardFromDeck().execute();
            } else
                Toast.makeText(GameScreen.this,"wait to your turn!",Toast.LENGTH_LONG).show();
        }


    }

    public void passCardByNFC() {

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

    public void painCurrentPlayer(View v) {
        v.setBackgroundColor(Color.GREEN);
    }

    public void onMyTurnEnd() {
        bottomLayout.setBackgroundColor(Color.TRANSPARENT);
    }


}
