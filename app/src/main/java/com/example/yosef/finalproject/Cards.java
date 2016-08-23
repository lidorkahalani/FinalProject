package com.example.yosef.finalproject;

import java.io.File;

/**
 * Created by Yosef on 22/05/2016.
 */
public class Cards {
    private String categoryName;
    private int categoryColor;
    private int card_id;
    private File itemPicture;
    private String [] itemsArray=new String [4];
    //private int point;
    boolean customizeQuartest=false;

    public Cards(String categoryName, int categoryColor, File itemPicture, String[] itemsArray) {
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
        this.itemPicture = itemPicture;
        this.itemsArray = itemsArray;

    }
    public Cards(int card_id,String categoryName, int categoryColor, String[] itemsArray ) {
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
        this.itemsArray = itemsArray;

    }

    public Cards(String categoryName, int categoryColor, File itemPicture, String[] itemsArray, boolean customize) {
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

    public File getItemPicture() {
        return itemPicture;
    }

    public void setItemPicture(File itemPicture) {
        this.itemPicture = itemPicture;
    }

    public String[] getItemsArray() {
        return itemsArray;
    }

    public void setItemsArray(String[] itemsArray) {
        this.itemsArray = itemsArray;
    }

}
