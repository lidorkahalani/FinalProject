package com.example.yosef.finalproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class AddCards extends AppCompatActivity implements View.OnClickListener {
    public static final String UPLOAD_URL = "http://mysite.lidordigital.co.il/Quertets/add_image.php";
    public static final String UPLOAD_KEY = "image";
    public static final String TAG = "MY MESSAGE";

    private int PICK_IMAGE_REQUEST = 1;

    private Button buttonChoose;

    private Button buttonView;

    private Button buttonUpload;

    private ImageView imageView;

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
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
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
        class UploadImage extends AsyncTask<Bitmap,Void,String>{

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AddCards.this, "Uploading Image", "Please wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
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
                        return "upload Succse";
                    }else
                         return "upload failed";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return "Server Eror";
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    public void onBackPressed() {
        // TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Go back to Main Menu?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent myIntent = new Intent(AddCards.this, MainMenu.class);
                        startActivity(myIntent);
                        //finish();
                        finish();
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
    @Override
    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
        }
        if(v == buttonUpload){
            if(imageCoosen)
                uploadImage();
            else
                Toast.makeText(AddCards.this, "please choose image", Toast.LENGTH_SHORT).show();
        }

    }
}
