package com.example.yosef.finalproject;

import android.graphics.drawable.Drawable;

import java.io.File;
import java.io.Serializable;
import java.security.PrivateKey;

/**
 * Created by Yosef on 22/05/2016.
 */
public class Card implements Serializable {
    private String categoryName;
    private String cardName;
    private String imageName;
    private int categoryColor;
    private int card_id;
    private Drawable itemPicture;
    private String [] itemsArray=new String [4];

    public boolean isClicked = false;

    //private int point;
    private boolean customizeQuartest=false;

    public Card(){

    }

    public Card(String categoryName, int categoryColor, Drawable itemPicture, String[] itemsArray) {
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
        this.itemPicture = itemPicture;
        this.itemsArray = itemsArray;

    }
    public Card(int card_id, String categoryName, int categoryColor, String[] itemsArray,String cardName ) {
        this.card_id=card_id;
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
        this.itemsArray = itemsArray;
        this.cardName = cardName;
    }

    public Card(String categoryName, int categoryColor, Drawable itemPicture, String[] itemsArray, boolean customize) {
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
        this.itemPicture = itemPicture;
        this.itemsArray = itemsArray;

        this.customizeQuartest=customize;
    }

    public boolean isCustomizeQuartest() {
        return customizeQuartest;
    }

    public void setCustomizeQuartest(boolean customizeQuartest) {
        this.customizeQuartest = customizeQuartest;
    }


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(int categoryColor) {
        this.categoryColor = categoryColor;
    }

    public Drawable getItemPicture() {
        return itemPicture;
    }

    public void setItemPicture(Drawable itemPicture) {
        this.itemPicture = itemPicture;
    }

    public String[] getItemsArray() {
        return itemsArray;
    }

    public void setItemsArray(String[] itemsArray) {
        this.itemsArray = itemsArray;
    }

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id=card_id;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
