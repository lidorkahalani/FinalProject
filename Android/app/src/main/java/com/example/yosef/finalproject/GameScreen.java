package com.example.yosef.finalproject;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class GameScreen extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ArrayList<Card> deck = new ArrayList<Card>();
    private ArrayList<Card> tempDeck = new ArrayList<Card>();
    private ArrayList<User> playerList = new ArrayList<>();
    private User currentPlayer;
    private ArrayList<Integer> fullSereiesId=new ArrayList<Integer>();

    private Random randomGenerator;

    /*private RelativeLayout bottomLayout;
    private RelativeLayout left_layout;
    private RelativeLayout top_layout;
    private RelativeLayout right_layout;*/
    private RelativeLayout activePlayer;
    private TextView point;
    static private int currentPoint=0;
    private ArrayList<Integer> finishSeriesList=new ArrayList<Integer>();
    private Boolean reloadData=false;


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
    private Boolean gameIsActive=true;
    private Boolean isMyTurnStatus;
    private Boolean newHighScore=false;
    private Timer myTimer = new Timer("MyTimer", true);
    private String winnerName="";
    private Card currentSelectedCard;

    private User winerUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        point=(TextView)findViewById(R.id.pointField);
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

        /*bottomLayout = (RelativeLayout) findViewById(R.id.myPlayerBackground);
        left_layout = (RelativeLayout) findViewById(R.id.myPlayerBackground);
        top_layout = (RelativeLayout) findViewById(R.id.myPlayerBackground);
        right_layout = (RelativeLayout) findViewById(R.id.myPlayerBackground);*/

        myListView = (RecyclerView) findViewById(R.id.listcards);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        myListView.setLayoutManager(lm);
        myListView.setItemAnimator(new DefaultItemAnimator());

        myTimer.scheduleAtFixedRate(new MyTask(), 6000, 3000);
    }

    private class MyTask extends TimerTask {

        public void run(){
            if(!gameIsActive)
                openMainMenu();
            else if(!debugStatus)
              new refresh().execute();

        }

    }

    public void openMainMenu(){
        myTimer.cancel();
        Intent myIntent = new Intent(GameScreen.this, MainMenu.class);
        startActivity(myIntent);
        finish();
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.exitWarning))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new setGameToInactive().execute(String.valueOf(newGame.getGame_id()));
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
        Display dis = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        dis.getSize(size);
        int width = size.x / 3;

        if (cardsAdapter == null){
            cardsAdapter = new CardsAdapter(deck, width);
            myListView.setAdapter(cardsAdapter);
            registerForContextMenu(myListView);
            myListView.addOnItemTouchListener(
                    new RecyclerItemClickListener(GameScreen.this, myListView, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            cardView = view;
                            MENU_ID = CARDS_CLICK_MENU;
                            openContextMenu(view);
                            LinearLayout cardContainer = (LinearLayout) view.findViewById(R.id.card_container);
                           // cardContainer.setBackgroundColor(getResources().getColor(R.color.light_green));
                        }


                        @Override
                        public void onLongItemClick(View view, int position) {

                        }
                    })
            );
         }
        else {
            cardsAdapter.setCards(deck);
            cardsAdapter.notifyDataSetChanged();
        }
    }

    public void addPoint(){
        Toast.makeText(this,"Series Complete! cards remove from screen",Toast.LENGTH_LONG).show();
        new refresh().execute();
        point.setText(getResources().getString(R.string.points)+": "+(++currentPoint));
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
                currentSelectedCard=deck.get(myListView.getChildPosition(cardView));
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
                if(!isMyTurnStatus&&!debugStatus) {
                    new sendSelectedCard().execute(String.valueOf(this.currentSelectedCard.getCard_id()));
                }
               else Toast.makeText(GameScreen.this,getResources().getString(R.string.cannot_send_card),
                        Toast.LENGTH_SHORT).show();
                setCardBackgroundTransparent = false;
            }

        }

        return true;
    }

    @Override
    public void onContextMenuClosed(Menu menu) {
        if (selectedCard != null && setCardBackgroundTransparent) {
           // selectedCard.findViewById(R.id.card_container).setBackgroundColor(Color.TRANSPARENT);

            //this makes the unmatche size of card
           // selectedCard.findViewById(R.id.card_container).setBackgroundDrawable((getResources().getDrawable(R.drawable.card_background)));
        }
    }

    public class startPlay extends AsyncTask<String, Void, Boolean> {
        //String get4Cards = "http://10.0.2.2/final_project/db/giveMe4Cards.php";
        //String get4Cards = "http://mysite.lidordigital.co.il/Quertets/php/db/giveMe4Cards.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("user_id", String.valueOf(currentPlayer.getUserID()));
            parms.put("game_id", String.valueOf(newGame.getGame_id()));

            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.get4Cards, "POST", parms);
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
        //String sendSelctedCard="http://10.0.2.2/final_project/db/sendSelctedCard.php";
        //String sendSelctedCard="http://mysite.lidordigital.co.il/Quertets/php/db/sendSelctedCard.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();
        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("game_id",String.valueOf(newGame.getGame_id()));
            parms.put("card_id",params[0]);
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.sendSelctedCard, "POST", parms);
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
       //String takeOneCardFromDeck = "http://10.0.2.2/final_project/db/takeOneCardFromDeck.php";
        //String takeOneCardFromDeck = "http://mysite.lidordigital.co.il/Quertets/php/db/takeOneCardFromDeck.php";

        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("user_id", String.valueOf(currentPlayer.getUserID()));
            parms.put("game_id", String.valueOf(newGame.getGame_id()));

            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.takeOneCardFromDeck, "POST", parms);
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
                } else if(response.getInt("succsses") ==-1){
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
                else {
                    //new moveToNextPlayer().execute();
                    new setGameOverStatus().execute("3",String.valueOf(newGame.getGame_id()));
                    //Toast.makeText(GameScreen.this, "deck end game over!", Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(GameScreen.this, "Take one card failed", Toast.LENGTH_SHORT).show();
        }

    }

    public class moveToNextPlayer extends AsyncTask<String, Void, Boolean> {
        //String setTurnOrder = "http://10.0.2.2/final_project/db/moveToNextPlayer.php";
        // String setTurnOrder = "http://mysite.lidordigital.co.il/Quertets/php/db/moveToNextPlayer.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("game_id", String.valueOf(newGame.getGame_id()));
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.setTurnOrder, "POST", parms);

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
        //String isMyTurn = "http://10.0.2.2/final_project/db/isMyTurn.php";
        //String isMyTurn = "http://mysite.lidordigital.co.il/Quertets/php/db/isMyTurn.php";

        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("user_id", String.valueOf(currentPlayer.getUserID()));
            parms.put("game_id", String.valueOf(newGame.getGame_id()));

            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.isMyTurn, "POST", parms);
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
        //String refresh_all = "http://10.0.2.2/final_project/db/refresh_all.php";
         //String refresh_all = "http://mysite.lidordigital.co.il/Quertets/php/db/refresh_all.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            reloadData=true;
            parms.put("game_id",String.valueOf(newGame.getGame_id()));
            parms.put("user_id",String.valueOf(currentPlayer.getUserID()));

            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.refresh_all, "POST", parms);
                if (response.getInt("succsses") == 1) {
                    tempDeck=deck;
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
                        deck.add(card);
                    }

                   if( response.getInt("isMyturn")==1   )
                       isMyTurnStatus =true;
                    else
                       isMyTurnStatus =false;

                    finishSeriesList.clear();
                    if(response.getInt("finishSeries")==1) {
                        jsonArray = response.getJSONArray("SeriesIds");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            finishSeriesList.add(jsonArray.getJSONObject(i).getInt("category_id"));
                        }
                    }

                     if(response.getInt("is_active")==0) {
                         gameIsActive=false;
                        }
                    }
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
            return true;

        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                    if(!finishSeriesList.isEmpty()) {
                        new UpdateFinishSeries().execute();
                       // Toast.makeText(GameScreen.this,"fulll sereis!",Toast.LENGTH_SHORT).show();
                    }
                if(tempDeck.size()<deck.size())
                    Toast.makeText(GameScreen.this,"New Card recived",Toast.LENGTH_SHORT).show();
                else if(tempDeck.size()>deck.size())
                    Toast.makeText(GameScreen.this,"Card send",Toast.LENGTH_SHORT).show();

                setCardsList();
                    if (isMyTurnStatus) {
                        activePlayer.setBackgroundColor(Color.GREEN);
                    } else
                        activePlayer.setBackgroundColor(Color.TRANSPARENT);

                    if(!gameIsActive)
                        new GetWinnerName().execute();


            } else
                Toast.makeText(GameScreen.this, "refresh failed !", Toast.LENGTH_SHORT).show();
            reloadData=false;
        }

    }

    public class UpdateFinishSeries extends AsyncTask<String, Void, Boolean> {
        //String UpdateFinishSeries = "http://10.0.2.2/final_project/db/UpdateFinishSeries.php";
        //String UpdateFinishSeries = "http://mysite.lidordigital.co.il/Quertets/php/db/UpdateFinishSeries.php";

        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            JSONArray jsonArr = new JSONArray();
            JSONObject jsonOb = new JSONObject();
            int index=0;
            for (int i = 0; i < finishSeriesList.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("category_id"+index, finishSeriesList.get(i));
                        index++;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArr.put(jsonObject);

            }

            try {
                jsonOb.put("finish_categorys_id",jsonArr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            parms.put("user_id", String.valueOf(currentPlayer.getUserID()));
            parms.put("game_id", String.valueOf(newGame.getGame_id()));
            parms.put("finish_category",jsonOb.toString());

            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.UpdateFinishSeries, "POST", parms);
                if (response.getInt("successes") == 1)
                    return true;
                else
                    return false;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }

        protected void onPostExecute(Boolean result) {

            if (result) {
                addPoint();
            } else
                Toast.makeText(GameScreen.this, "Update finish series failed", Toast.LENGTH_LONG).show();
        }
    }

    public class getAllCards extends AsyncTask<String, Void, Boolean> {
        //String get_all_card_url = "http://10.0.2.2/final_project/db/getAllCard.php";
         //String get_all_card_url = "http://mysite.lidordigital.co.il/Quertets/php/db/getAllCard.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.get_all_card_url, "GET", parms);
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

    public class setGameToInactive extends AsyncTask<String, Void, Boolean> {
        //String setGameToInactive = "http://10.0.2.2/final_project/db/setGameToInactive.php";
        //String setGameToInactive = "http://mysite.lidordigital.co.il/Quertets/php/db/setGameToInactive.php";

        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("game_id", params[0]);

            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.setGameToInactive, "POST", parms);
                if (response.getInt("successes") == 1)
                  return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            if (result) {
                Intent myIntent = new Intent(GameScreen.this, MainMenu.class);
                startActivity(myIntent);
                finish();
            } else
                Toast.makeText(GameScreen.this, "There was problem on log out", Toast.LENGTH_SHORT).show();
        }

    }

    public class GetWinnerName extends AsyncTask<String, Void, Boolean> {
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();
        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("game_id",String.valueOf(newGame.getGame_id()));

            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.GetWinnerName, "POST", parms);
                JSONArray res=response.getJSONArray("winner");

               // if (response.getInt("successes") == 1) {
                    for (int i = 0; i < res.length(); i++) {
                        JSONObject jo = res.getJSONObject(i);
                        winerUser= new User(jo.getString("user_name"),jo.getString("user_password"),jo.getInt("user_id"));
                        winnerName = winerUser.getUserName();
               //     }

                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
            return true;
        }
        protected void onPostExecute(Boolean result) {
            if (result) {


                //who is the winner
                if(winnerName.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(GameScreen.this);
                    builder.setTitle(getResources().getString(R.string.game_over));
                    builder.setMessage(getResources().getString(R.string.no_one_win))
                            .setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent myIntent = new Intent(GameScreen.this, MainActivity.class);
                                    startActivity(myIntent);
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GameScreen.this);
                    builder.setTitle(getResources().getString(R.string.game_over));
                    builder.setMessage(winnerName + " " + getResources().getString(R.string.win))
                            .setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if(currentPlayer.getUserID()==winerUser.getUserID())
                                        new UpdateScore().execute(String.valueOf(currentPoint),String.valueOf(winerUser.getUserID()));
                                    else {
                                        Intent myIntent = new Intent(GameScreen.this, MainActivity.class);
                                        startActivity(myIntent);
                                        finish();
                                    }
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }//need to add equal alert


            } else
                Toast.makeText(GameScreen.this, "There was problem during gameOver execute", Toast.LENGTH_SHORT).show();

            //stop all timers
            myTimer.cancel();
        }

    }

    public class setGameOverStatus extends AsyncTask<String, Void, Boolean> {
        //String setGameToActive = "http://10.0.2.2/final_project/db/setGameToActive.php";
        //String setGameToActive = "http://mysite.lidordigital.co.il/Quertets/php/db/setGameToActive.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("opertion",params[0]);
            parms.put("game_id",params[1]);

            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.setGameToActive, "POST", parms);

                if (response.getInt("succsses")== 1) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        protected void onPostExecute(Boolean result) {
            if(result)
                new GetWinnerName().execute();
            else
                Toast.makeText(GameScreen.this,"Cant set game Over",Toast.LENGTH_LONG).show();
        }

    }

    public class UpdateScore extends AsyncTask<String, Void, Boolean> {
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
            parms.put("score",params[0]);
            parms.put("user_id",params[1]);

            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.UpdateScore, "POST", parms);

                if (response.getInt("succsses")== 1)
                    newHighScore=true;
                else if(response.getInt("succsses")== 0)
                    newHighScore=false;
                else
                    return false;

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        protected void onPostExecute(Boolean result) {
            if(result) {
                if(newHighScore)
                Toast.makeText(GameScreen.this,"You got new high score!",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(GameScreen.this,"You have been a higher score!",Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(GameScreen.this, MainActivity.class);
                startActivity(myIntent);
                finish();
            }
            else
                Toast.makeText(GameScreen.this,"Cant set game Over",Toast.LENGTH_LONG).show();
        }

    }

    public void passCardByNFC() {

    }


}
