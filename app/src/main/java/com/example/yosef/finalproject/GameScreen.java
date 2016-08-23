package com.example.yosef.finalproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GameScreen extends AppCompatActivity {
    static ArrayList<Integer> colors=new ArrayList<>();
    ArrayList<Cards>deck=new ArrayList<Cards>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_screen);
        new getAllCards().execute();
    }

    public class getAllCards  extends AsyncTask<String, Void, Boolean> {
        String get_all_card_url = "http://mysite.lidordigital.co.il/Quertets/db/getAllCard.php";
        HashMap<String,String> parms=new HashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {

            JSONParser json=new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(get_all_card_url, "POST", parms);

                if(response.getInt("succsses")==1){

                    JSONArray jsonArray=response.getJSONArray("AllCards");
                    int[] rainbow = getApplicationContext().getResources().getIntArray(R.array.allColors);
                    String[] cardLabel=new String[4];
                        for (int i = 0; i < 4; i++) {
                            cardLabel[i] = jsonArray.getJSONObject(i).getString("card_name");
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Cards card = new Cards(jsonArray.getJSONObject(i).getInt("card_id"),
                                    jsonArray.getJSONObject(i).getString("category_name"), (i+10), cardLabel);

                            deck.add(card);
                        }
                    return true;
                }else{
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }


        }
        protected void onPostExecute(Boolean result) {
            if(result) {
                Toast.makeText(GameScreen.this, "the deck sucssfuly generate!!", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(GameScreen.this, "the deck not load !", Toast.LENGTH_SHORT).show();
        }
    }
}
