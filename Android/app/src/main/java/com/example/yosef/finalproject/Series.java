package com.example.yosef.finalproject;

import android.media.Image;

import java.io.Serializable;

/**
 * Created by Yosef on 13-Nov-16.
 */

public class Series implements Serializable {
    private String category_name;
    private int category_id;
    private String [] cardsName=new String[4];
    private String [] cardsImage=new String[4];
    private String card_name1;
    private String card_name2;
    private String card_name3;
    private String card_name4;
    private String image1;
    private String image2;
    private String image3;
    private String image4;



    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String[] getCardsName() {
        return cardsName;
    }

    public void setCardsName(String[] cardsName) {
        this.cardsName = cardsName;
    }

    public String[] getCardsImage() {
        return cardsImage;
    }

    public void setCardsImage(String[] cardsImage) {
        this.cardsImage = cardsImage;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCard_name1() {
        return card_name1;
    }

    public void setCard_name1(String card_name1) {
        this.card_name1 = card_name1;
    }

    public String getCard_name2() {
        return card_name2;
    }

    public void setCard_name2(String card_name2) {
        this.card_name2 = card_name2;
    }

    public String getCard_name3() {
        return card_name3;
    }

    public void setCard_name3(String card_name3) {
        this.card_name3 = card_name3;
    }

    public String getCard_name4() {
        return card_name4;
    }

    public void setCard_name4(String card_name4) {
        this.card_name4 = card_name4;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Series) {
            if (this.getCategory_id()==(((Series) o).getCategory_id()))
                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getCategory_id();
    }

}
