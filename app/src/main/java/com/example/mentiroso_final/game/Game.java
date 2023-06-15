package com.example.mentiroso_final.game;

import android.util.Log;

import com.example.mentiroso_final.GameActivity;

import java.sql.Array;
import java.util.ArrayList;

public class Game {
    public ArrayList<Card> allCards;
    public ArrayList<Card> deck;
    public ArrayList<Card> tableCards = new ArrayList<>();
    public ArrayList<Card> discardCards;
    public int numeroJugada;

    public int roundNumber;
    public boolean gameOver = false;
    public boolean fueMentira = false;
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private int currentIdPlayer;

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
        //this.currentPlayer = this.player2;
        this.currentIdPlayer = 2;
        this.allCards = gameState.allCards;
        this.gameOver = gameState.gameOver;

    }
    public void echarCarta(ArrayList<Card> cards, Player player){
        //quitar las cartas del arraylist
        int i=0;
        for (Card c : cards ){
            player.deleteCard(cards.get(i));
            i++;
        }
        //añadirlas a las cartas de la mesa
        i=0;
        for (Card c : cards ){
            tableCards.add(cards.get(i));
            i++;
        }
        //como sabemos que se echaron por las restricciones al seleccionar carta, no fue mentira
        this.fueMentira=false;
        this.numeroJugada = cards.get(0).getValue();
        Log.i("numero jugada", String.valueOf(numeroJugada));
    }
    public void levantarCarta(){
        //fueMentira?
        //IMPORTANTE saber qué jugador fue el anterior
        //si sí, ese jugador se lleva las cartas
        //si no me las lelvo yo
        //y se retiran del mazo de la mesa
        tableCards.clear();
    }

    public void fueMentira(ArrayList<Card> cards){
        //comprobar si todas las cartas eran iguales o no
        //this.fueMentira =
    }
    public void mentir(ArrayList<Card> cards, Player player, int valorMentira){
        //hacer lo mismo que en levantar pero poniendo fuementira a true
        //quitar las cartas del arraylist
        int i=0;
        for (Card c : cards ){
            player.deleteCard(cards.get(i));
            i++;
        }
        if(tableCards.size() == 0){
            this.numeroJugada = valorMentira;
        }
        //añadirlas a las cartas de la mesa
        i=0;
        for (Card c : cards ){
            tableCards.add(cards.get(i));
            i++;
        }
        //como sabemos que se echaron por las restricciones al seleccionar carta, no fue mentira
        Log.i("numero jugada", String.valueOf(numeroJugada));

        this.fueMentira=true;
    }

}
