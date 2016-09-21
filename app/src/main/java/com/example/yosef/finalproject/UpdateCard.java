package com.example.yosef.finalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;

public class UpdateCard extends AppCompatActivity {
    Card card;
    EditText category;
    EditText card_name;
    ImageView image;
    Button choseImage;
    Button updateCard;
    private Uri filePath;
    private Bitmap bitmap;

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
}
