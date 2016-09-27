package com.example.yosef.finalproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private EditText userName;
    private EditText password;
    private UsersDBHandler dbHandler;
    int score;
    int userId;
    private CallbackManager callBack;
    private ProfileTracker profileTracker;
    private AccessTokenTracker accsessTokenTracker;
    private Profile profile;
    Button btn1;
    Button btn2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);



        SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(this);
        String uname = myPref.getString("username", null);
        //if the user alredy connected skip to Personal screen
        if (uname != null && !uname.equals("")) {
            Intent myIntent = new Intent(this, MainMenu.class);
            startActivity(myIntent);
            finish();
            return;
        }

        accsessTokenTracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                AccessToken.refreshCurrentAccessTokenAsync();
            }

        };
        accsessTokenTracker.startTracking();


        callBack=CallbackManager.Factory.create();
       // LoginButton facebookBtn=(LoginButton)findViewById(R.id.facbookLogin);
        LoginManager.getInstance().registerCallback(callBack, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        profileTracker=new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                profile=currentProfile;
                if(profile!=null) {
                    SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor = myPref.edit();
                    editor.putBoolean("loginWhitFacebook", true);
                    editor.commit();

                    new logIn().execute(profile.getFirstName(),profile.getId());
                    editor.putString("username", profile.getFirstName());
                    editor.putInt("score", score);
                    editor.commit();

                    Log.v("Facebook user name", profile.getFirstName());
                    Toast.makeText(MainActivity.this, "Welcom! " + profile.getName(), Toast.LENGTH_LONG).show();
                }else{
                    SharedPreferences myPref= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor=myPref.edit();
                    editor.remove("username");
                    editor.commit();
                }
            }
        };

        btn1=(Button)findViewById(R.id.logInBtn);
        btn2=(Button)findViewById(R.id.SignUpBtn);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "Matias_Webfont.ttf");
        btn1.setTypeface(typeface);
        btn2.setTypeface(typeface);



        dbHandler = new UsersDBHandler(this);


        userName = (EditText) findViewById(R.id.UserName);
        password = (EditText) findViewById(R.id.password);

    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        if(accsessTokenTracker!=null&&profileTracker!=null) {
            accsessTokenTracker.stopTracking();
            profileTracker.stopTracking();
        }
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(callBack.onActivityResult(requestCode,resultCode,data)){
            return;
        }
    }
    public void logIn(View v) {
        new logIn().execute(userName.getText().toString(),password.getText().toString());
    }

    public void onBackPressed() {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Exit")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
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

    public void SignUp(View v) {
        Intent myIntent = new Intent(this, SignUp.class);
        startActivity(myIntent);
    }

    public void showAllRecords(View v) {
        ArrayList<User> UsersList = dbHandler.getAllUsers();
        Intent myIntent = new Intent(this, ShowMyCards.class);
        myIntent.putExtra("userList", UsersList);
        startActivity(myIntent); //app get crash her

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.exit) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage(R.string.exitWarning);
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        } else if (item.getItemId() == R.id.changeLang) {
            Configuration newConfig = new Configuration();
            newConfig.locale = Locale.ENGLISH; //cant change to hebrew
            onConfigurationChanged(newConfig);
        }
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);

        // Checks the active language
        if (newConfig.locale == Locale.ENGLISH) {
            Toast.makeText(this, "English", Toast.LENGTH_SHORT).show();
        } /*else if (newConfig.locale.toString() == LOCALE_IW){
            Toast.makeText(this, "Hebrew", Toast.LENGTH_SHORT).show();*/
    }

    public class logIn extends AsyncTask<String, Void, Boolean> {
        String login_url = "http://mysite.lidordigital.co.il/Quertets/login.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        MySQLiteHelper dbHelper = new MySQLiteHelper(MainActivity.this, UserDBConstants.DBName, null, UserDBConstants.User_DB_VESRSION);
        String inputUserName ;
        String inputPassword ;

        @Override
        protected Boolean doInBackground(String... params) {
            inputUserName=params[0];
            inputPassword=params[1];

            parms.put("password", inputPassword);
            parms.put("username", inputUserName);
            JSONParser json = new JSONParser();
          try{
            JSONObject response = json.makeHttpRequest(login_url, "POST", parms);
                if (response.getInt("succsses") == 1) {
                    JSONArray jsonArray=response.getJSONArray("User");
                       if(jsonArray.getJSONObject(0).getString("user_name").equals(inputUserName)&&
                               jsonArray.getJSONObject(0).getString("password").equals(inputPassword)){
                           score = jsonArray.getJSONObject(0).getInt("score");
                           userId=jsonArray.getJSONObject(0).getInt("user_id");
                           return true;
                       }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }catch (Exception e) {
              e.printStackTrace();
              return false;
          }
            return false;
        }

        protected void onPostExecute(Boolean result) {
            SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            Boolean faceBookLogIN=myPref.getBoolean("loginWhitFacebook",false);

            if (result) {
                SharedPreferences.Editor editor = myPref.edit();
                editor.putString("username", inputUserName);
                editor.putString("password", inputPassword);
                editor.putInt("score", score);
                editor.putInt("user_id",userId);

                editor.commit();

                Intent myIntent = new Intent(MainActivity.this, MainMenu.class);
                startActivity(myIntent);
                finish();

            } else if(faceBookLogIN)
            {
                new signUp().execute(inputUserName,inputPassword);

            }else
                Toast.makeText(MainActivity.this, "invalid user ! please try Again", Toast.LENGTH_LONG).show();

        }
    }

    public class signUp  extends AsyncTask<String, Void, Boolean>{
        String reg_url = "http://mysite.lidordigital.co.il/Quertets/register.php";
        LinkedHashMap<String,String> parms=new LinkedHashMap<>();
        String userName;
        String password;

        @Override
        protected Boolean doInBackground(String... params) {

            userName=params[0];
             password=params[1];

            if(userName.equals("")||password.equals(""))
                return false;

            parms.put("password",password);
            parms.put("username",userName);
            JSONParser json=new JSONParser();
            try {
                JSONObject response=json.makeHttpRequest(reg_url,"POST",parms);


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
                SharedPreferences myPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                Toast.makeText(MainActivity.this, "User added succesfuly", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(MainActivity.this, MainMenu.class);
                startActivity(myIntent);
                SharedPreferences.Editor editor = myPref.edit();
                editor.putString("username", userName);
                editor.putInt("score", score);

                editor.commit();
                finish();
            }
            else
                Toast.makeText(MainActivity.this, "User NOT added", Toast.LENGTH_SHORT).show();
        }
    }


}

