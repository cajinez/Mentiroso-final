package com.example.mentiroso_final.game;

import com.example.mentiroso_final.GameActivity;

import java.sql.Array;
import java.util.ArrayList;

public class Game {
    public ArrayList<Card> allCards;
    public ArrayList<Card> deck;
    public ArrayList<Card> tableCards;
    public ArrayList<Card> discardCards;

    public int roundNumber;
    public boolean gameOver = false;
    private Player player1;
    private Player player2;
    private Player currentPlayer;

    public Game(ArrayList<Card> deck){
        this.deck = deck;
        this.player1 = GameActivity.player1;
        this.player2 = GameActivity.player2;
        this.currentPlayer = this.player1;
        //estado jugador cogiendo
    }
    public Game(Game gameState){
        this.deck = new ArrayList<>(gameState.deck);
        this.player1 = new Player(gameState.player1);
        this.player2 = new Player(gameState.player2);
        this.currentPlayer = this.player2;
        this.allCards = gameState.allCards;
        this.gameOver = gameState.gameOver;

    }
    public void echarCarta(ArrayList<Card> cards, Player player){
        //quitar las cartas del arraylis
    }
    public void mentir(ArrayList<Card> card, Player player){

    }

}
