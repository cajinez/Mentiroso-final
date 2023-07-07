package com.example.mentiroso_final.game;

public class Card  {
    int suit;
    int value;
    int imageId;

    public Card(int suit, int value, int imageId) {
        this.suit = suit;
        this.imageId = imageId;
        this.value = value;
    }

    public int getImageId() {
        return imageId;
    }

    public int getValue() {
        return value;
    }
    public int getSuit() { return suit; }


}