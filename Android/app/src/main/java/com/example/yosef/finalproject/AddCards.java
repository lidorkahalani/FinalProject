package com.example.yosef.finalproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class AddCards extends AppCompatActivity implements View.OnClickListener {
    //public static final String UPLOAD_URL = "http://mysite.lidordigital.co.il/Quertets/db/add_image.php";
    public static final String UPLOAD_URL = "http://10.0.2.2/final_project/db/add_image.php";

    public static final String UPLOAD_KEY = "image";
    public static final String TAG = "MY MESSAGE";

    private int PICK_IMAGE_REQUEST = 1;

    private Button buttonChoose;

    private Button buttonView;

    private Button buttonUpload;

    private ImageView imageView;
    private EditText category;
    private EditText card1;
    private EditText card2;
    private EditText card3;
    private EditText card4;

    static private ArrayList<Card>newSeries=new ArrayList<>();

    private Bitmap bitmap;
    private boolean imageCoosen;



    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cards);

        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonView = (Button) findViewById(R.id.buttonViewImage);
        category=(EditText)findViewById(R.id.category);
        card1=(EditText)findViewById(R.id.card1);
        card2=(EditText)findViewById(R.id.card2);
        card3=(EditText)findViewById(R.id.card3);
        card4=(EditText)findViewById(R.id.card4);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Matias_Webfont.ttf");
        buttonChoose.setTypeface(typeface);
        buttonUpload.setTypeface(typeface);
        buttonView.setTypeface(typeface);

        imageView = (ImageView) findViewById(R.id.imageView);


        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,getResources().getString(R.string.Select_Picture)), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                imageCoosen=true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,Boolean>{

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AddCards.this,
                        getResources().getString(R.string.string_upload_image),
                        getResources().getString(R.string.please_wait),true,true);
            }

            @Override
            protected void onPostExecute(Boolean s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s)
                    Toast.makeText(AddCards.this,
                            getResources().getString(R.string.upload_image_success),
                            Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(AddCards.this,
                            getResources().getString(R.string.upload_image_failed),
                            Toast.LENGTH_LONG).show();

            }

            @Override
            protected Boolean doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                LinkedHashMap<String,String> data = new LinkedHashMap<>();
                //data.put("card_id",String.valueOf(37));
                data.put("ThisImage", uploadImage);
                JSONParser json = new JSONParser();
                //String result = rh.sendPostRequest(UPLOAD_URL,data);
                JSONObject response = json.makeHttpRequest(UPLOAD_URL, "POST", data);
                try {
                    if (response.getInt("succsses") == 1) {
                        return true;
                    }else
                         return false;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    public void onBackPressed() {

        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.back_Main_Menu))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent myIntent = new Intent(AddCards.this, MainMenu.class);
                        startActivity(myIntent);
                        //finish();
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
    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
        }
        if(v == buttonUpload){
            if(imageCoosen)
                uploadImage();
            else
                Toast.makeText(AddCards.this,
                        getResources().getString(R.string.please_choose_image),
                        Toast.LENGTH_SHORT).show();
        }

    }

    public void addThisCard(View v){
        if(newSeries.size()<4) {
            int defuletColor = 111;
            String cardsList[] = new String[4];
            cardsList[0] = card1.getText().toString();
            cardsList[1] = card2.getText().toString();
            cardsList[2] = card3.getText().toString();
            cardsList[3] = card4.getText().toString();

            Card card = new Card(category.getText().toString(),
                    card1.getText().toString(),
                    defuletColor, filePath.toString(), cardsList);

            newSeries.add(card);
            //refrashe screen
           // StartActivity(getIntent());

        }
    }

    public void sendSerie(View v){
        if(newSeries.size()==4){
            new sendSeriesToServer().execute();
        }
    }



    public class sendSeriesToServer extends AsyncTask<String, Void, Boolean> {
        String upload_series = "http://10.0.2.2/final_project/db/upload_series.php";
        // String get_all_card_url = "http://mysite.lidordigital.co.il/Quertets/db/upload_series.php";
        LinkedHashMap<String, String> parms = new LinkedHashMap<>();

        @Override
        protected Boolean doInBackground(String... params) {
          /*  parms.put("card1",newSeries.get(0));
            parms.put("card2",newSeries.get(1));
            parms.put("card3",newSeries.get(2));
            parms.put("card4",newSeries.get(3));

            */
            JSONParser json = new JSONParser();
            try {
                JSONObject response = json.makeHttpRequest(upload_series, "GET", parms);
                if (response.getInt("succsses")==1) {
                    newSeries.clear();
                    return true;
                }
            } catch(Exception ex) {
                ex.printStackTrace();
            }
            return false;

        }


        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(AddCards.this, "Series upload sucssfully", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(AddCards.this, "Series upload failed", Toast.LENGTH_SHORT).show();
        }

    }
}
