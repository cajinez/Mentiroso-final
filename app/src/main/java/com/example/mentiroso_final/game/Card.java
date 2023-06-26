package com.example.mentiroso_final.game;
import java.util.Comparator;

public class Card implements Comparable<Card> {
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

    @Override
    public int compareTo(Card o) {
        return Integer.compare(this.value, o.value);
    }
}