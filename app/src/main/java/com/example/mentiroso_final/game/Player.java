package com.example.mentiroso_final.game;

import java.util.ArrayList;

public class Player {

    ArrayList<Card> playerCards = new ArrayList<>();
    ArrayList<Card> remainingCards = new ArrayList<>();


    int score = 0;
    int id;

    public void   deleteRemainingCards(ArrayList<Card>a){
        this.remainingCards.removeAll(a);
    }

    public Player(int id) {
        this.id = id;
    }

    //Constructor to clone
    public Player(Player player){
        this.playerCards = new ArrayList<>(player.playerCards);
        this.remainingCards = new ArrayList<>(player.remainingCards);
        this.score = player.score;
        this.id = player.id;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Card> getPlayerCards() {
        return playerCards;
    }

    public ArrayList<Card> getRemainingCards() {
        return remainingCards;
    }

    public void setPlayerCards(ArrayList<Card> cards) {
        this.playerCards = cards;
    }

    public void setRemainingCards(ArrayList<Card> cards) {
        this.remainingCards = new ArrayList<>(cards);
    }

    public void addCard(Card card) {
        this.playerCards.add(card);
    }

    public void deleteCard(Card card) {
        this.playerCards.remove(card);
    }
}