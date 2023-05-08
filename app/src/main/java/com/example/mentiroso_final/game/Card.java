package com.example.mentiroso_final.game;
import java.util.Comparator;

public class Card implements Comparable<Card> {
    int suit;
    int value;
    int imageId;
    int payoff;

    public Card(int suit, int value, int imageId) {
        this.suit = suit;
        this.imageId = imageId;
        this.payoff = value;
        //sota, carballo y rey se consideran como 8, 9 y 19 // El payoff sigue siendo 10,11,12
        if (value < 8) {
            this.value = value;
        } else {
            this.value = value - 2;
        }
    }

    public int getImageId() {
        return imageId;
    }

    public int getValue() {
        return value;
    }

    public int getPayoff() {
        return this.payoff;
    }

    @Override
    public int compareTo(Card o) {
        return Integer.compare(this.value, o.value);
    }
}

class SortByRank implements Comparator<Card> {

    @Override
    public int compare(Card o1, Card o2) {
        if (o1.suit < o2.suit) {
            return -1;
        } else if (o1.suit == o2.suit) {
            if (o1.value < o2.value) {
                return -1;
            } else return 1;
        } else return 1;
    }

}