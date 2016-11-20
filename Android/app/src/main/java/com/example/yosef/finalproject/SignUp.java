package com.example.yosef.finalproject;


import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

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
      //  String reg_url = "http://mysite.lidordigital.co.il/Quertets/db/register.php";
        String reg_url = "http://10.0.2.2/final_project/db/register.php";

        LinkedHashMap<String,String> parms=new LinkedHashMap<>();
        String uName=userName.getText().toString();
        String pass=password.getText().toString();
        String repass=repassword.getText().toString();
        @Override
        protected Boolean doInBackground(String... params) {
            Gson gson=new Gson();
            if(uName.equals("")||pass.equals("")||!repass.equals(pass))
                return false;

            parms.put("username",uName);
            parms.put("password",pass);

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
