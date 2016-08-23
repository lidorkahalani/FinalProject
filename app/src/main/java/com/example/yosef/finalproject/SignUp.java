package com.example.yosef.finalproject;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SignUp extends AppCompatActivity {
    EditText userName;
    EditText password;
    EditText repassword;
    UsersDBHandler dbHandler;
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
        finish();
    }
    public void addNewUserToDB(View v){
        new signUp().execute();
    }

    public class signUp  extends AsyncTask<String, Void, Boolean>{
        String reg_url = "http://mysite.lidordigital.co.il/Quertets/register.php";
        HashMap<String,String> parms=new HashMap<>();
        String uName=userName.getText().toString();
        String pass=password.getText().toString();
        String repass=repassword.getText().toString();
        @Override
        protected Boolean doInBackground(String... params) {

            if(uName.equals("")||pass.equals("")||!repass.equals(pass))
                return false;

            parms.put("password",pass);
            parms.put("username",uName);
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
                Toast.makeText(SignUp.this, "User added succesfuly", Toast.LENGTH_SHORT).show();
                finish();
            }
            else
                Toast.makeText(SignUp.this, "User NOT added", Toast.LENGTH_SHORT).show();





        }
    }
}
