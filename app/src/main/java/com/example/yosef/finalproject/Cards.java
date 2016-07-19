package com.example.yosef.finalproject;

import java.io.File;

/**
 * Created by Yosef on 22/05/2016.
 */
public class Cards {
    private String categoryName;
    private String categoryColor;
    private File itemPicture;
    private String [] itemsArray=new String [4];
    private int point;
    boolean customizeQuartest=false;

    public Cards(String categoryName, String categoryColor, File itemPicture, String[] itemsArray, int point) {
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
        this.itemPicture = itemPicture;
        this.itemsArray = itemsArray;
        this.point = point;
    }

    public Cards(String categoryName, String categoryColor, File itemPicture, String[] itemsArray, int point, boolean customize) {
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
        this.itemPicture = itemPicture;
        this.itemsArray = itemsArray;
        this.point = point;
        this.customizeQuartest=customize;
    }

    public boolean isCustomizeQuartest() {
        return customizeQuartest;
    }

    public void setCustomizeQuartest(boolean customizeQuartest) {
        this.customizeQuartest = customizeQuartest;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(String categoryColor) {
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
