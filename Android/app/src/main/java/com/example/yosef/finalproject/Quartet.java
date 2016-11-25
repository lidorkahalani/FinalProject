package com.example.yosef.finalproject;

import java.util.ArrayList;

/**
 * Created by lidor on 07/09/2016.
 */
public class Quartet {

    private String category;
    private ArrayList<Card>cards ;

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void addCard(Card c){
        cards.add(c);
    }
}
