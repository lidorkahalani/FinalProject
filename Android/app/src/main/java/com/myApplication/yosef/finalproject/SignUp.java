package com.myApplication.yosef.finalproject;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.LinkedHashMap;

public class SignUp extends AppCompatActivity {
    private EditText userName;
    private EditText password;
    private EditText repassword;
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

        @Override
        protected Boolean doInBackground(String... params) {
            Gson gson=new Gson();

            parms.put("username",params[0]);
            parms.put("password",params[1]);

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
                Toast.makeText(SignUp.this, getResources().getString(R.string.user_sign_up_success), Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(SignUp.this, MainActivity.class);
                startActivity(myIntent);
                finish();
            }
            else
                Toast.makeText(SignUp.this, getResources().getString(R.string.user_sign_up_failed), Toast.LENGTH_SHORT).show();





        }
    }
}
