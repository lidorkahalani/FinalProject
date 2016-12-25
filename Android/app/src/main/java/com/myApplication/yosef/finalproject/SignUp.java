package com.myApplication.yosef.finalproject;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.LinkedHashMap;

public class SignUp extends AppCompatActivity {
    private EditText userName;
    private EditText password;
    private EditText repassword;
    int score;
    int userId;
    private UsersDBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        userName=(EditText)findViewById(R.id.UserName);
        password=(EditText)findViewById(R.id.password);
        repassword=(EditText)findViewById(R.id.repassword);
        dbHandler = new UsersDBHandler(this);
    }


    public void goBack(View v){
        Intent myIntent = new Intent(SignUp.this, MainActivity.class);
        startActivity(myIntent);
        finish();
    }

    public void addNewUserToDB(View v){

        String uName=userName.getText().toString();
        String pass=password.getText().toString();
        String repass=repassword.getText().toString();

            if (uName.equals("") || pass.equals(""))
                Toast.makeText(this, getResources().getString(R.string.empty_field), Toast.LENGTH_SHORT).show();
            else if(!(pass.equals(repass)))
                Toast.makeText(this,getResources().getString(R.string.password_no_match),Toast.LENGTH_SHORT).show();
            else
                new signUp().execute(uName,pass);
    }

    public class signUp  extends AsyncTask<String, Void, Boolean>{
        LinkedHashMap<String,String> parms=new LinkedHashMap<>();
        String user_Name;
        String pass;
        @Override
        protected Boolean doInBackground(String... params) {
            Gson gson=new Gson();
            user_Name=params[0];
            pass=params[1];
            parms.put("username",user_Name);
            parms.put("password",pass);

            JSONParser json=new JSONParser();
            try {
                JSONObject response=json.makeHttpRequest(ServerUtils.reg_url,"POST",parms);


                if(response.getInt("sucsses")==1){

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
                new logIn().execute(user_Name,pass);
                Toast.makeText(SignUp.this, getResources().getString(R.string.user_sign_up_success), Toast.LENGTH_SHORT).show();
               /* Intent myIntent = new Intent(SignUp.this, MainActivity.class);
                startActivity(myIntent);
                finish();*/
            }
            else
                Toast.makeText(SignUp.this, getResources().getString(R.string.user_sign_up_failed), Toast.LENGTH_SHORT).show();





        }
        }

    public class logIn extends AsyncTask<String, Void, Boolean> {

        LinkedHashMap<String, String> parms = new LinkedHashMap<>();
        String inputUserName ;
        String inputPassword ;

        @Override
        protected Boolean doInBackground(String... params) {
            inputUserName = params[0];
            inputPassword = params[1];

            //parms.put("password", inputPassword);
            //  parms.put("username", inputUserName);
            JSONParser json = new JSONParser();
            parms.put("password", inputPassword);
            parms.put("username", inputUserName);
            try {
                JSONObject response = json.makeHttpRequest(ServerUtils.login_url, "POST", parms);
                if (response.getInt("succsses") == 1) {
                    JSONArray jsonArray = response.getJSONArray("User");
                    if (jsonArray.getJSONObject(0).getString("user_name").equals(inputUserName) &&
                            jsonArray.getJSONObject(0).getString("user_password").equals(inputPassword)) {
                        score = jsonArray.getJSONObject(0).getInt("score");
                        userId = jsonArray.getJSONObject(0).getInt("user_id");
                        return true;
                    }
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
            SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(SignUp.this);

            if (result) {
                SharedPreferences.Editor editor = myPref.edit();
                editor.putString("username", inputUserName);
                editor.putString("password", inputPassword);
                editor.putInt("score", score);
                editor.putInt("user_id", userId);
                editor.commit();
                User currentPlayer = new User(inputUserName, inputPassword, score, userId);

                Intent myIntent = new Intent(SignUp.this, MainMenu.class);
                myIntent.putExtra("User", currentPlayer);
                startActivity(myIntent);
                finish();
            } else
                Toast.makeText(SignUp.this, getResources().getString(R.string.invalid_user_login), Toast.LENGTH_LONG).show();

        }

    }
}
