package com.example.yosef.finalproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UpdateCard extends AppCompatActivity {
    Card card;
    EditText category;
    EditText card_name;
    ImageView image;
    Button choseImage;
    Button updateCard;
    //public static final String UPLOAD_URL = "http://mysite.lidordigital.co.il/Quertets/upload_image.php";
    public static final String UPLOAD_URL = "http://localhost/Quertets/upload_image.php";
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
        setContentView(R.layout.activity_update_card);
        Intent i = getIntent();
        card =i.getParcelableExtra("selectedCard");
        category=(EditText)findViewById(R.id.category);
        card_name=(EditText)findViewById(R.id.cardName);
        image=(ImageView) findViewById(R.id.card_image);
        choseImage=(Button) findViewById(R.id.changeImage);;
        updateCard=(Button) findViewById(R.id.UpdateCard);;

        category.setText(card.getCategoryName());
        card_name.setText(card.getCardName());

        filePath = i.getData();

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            image.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void onClick(View v) {
        if (v == buttonChoose) {
            showFileChooser();
        }
        if(v == buttonUpload){
            if(imageCoosen)
                UpdateCard();
                //  new UpdateCard().execute("http://mysite.lidordigital.co.il/Quertets/db/UpdateCard", newCategory, newItem, String.valueOf(cardId));
            else
                Toast.makeText(UpdateCard.this, "please choose image", Toast.LENGTH_SHORT).show();
        }

    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void UpdateCard(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(UpdateCard.this, "Uploading Image", "Please wait...",true,true);
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

                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);

                String result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }
}
