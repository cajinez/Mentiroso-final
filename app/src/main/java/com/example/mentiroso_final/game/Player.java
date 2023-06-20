package com.example.mentiroso_final.game;

import android.util.Log;

import com.example.mentiroso_final.GameActivity;

import java.util.ArrayList;
import java.util.List;

public class Player {

    ArrayList<Card> playerCards = new ArrayList<>();
    ArrayList<Card> remainingCards = new ArrayList<>();
    private GameActivity gameActivity;
    public int numeroJugada;
    Game gameState;

    int score = 0;
    int id;

    public void   deleteRemainingCards(ArrayList<Card>a){
        this.remainingCards.removeAll(a);
    }

    public Player(int id, GameActivity gameActivity) {
        this.id = id;
        this.gameActivity = gameActivity;
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

    public void setGameState(Game gameState){
        this.gameState = gameState;
    }
    public void addCard(Card card) {
        this.playerCards.add(card);
    }
    public void deleteCard(Card card) {
        this.playerCards.remove(card);
    }
    public void setNumeroJugada(int numJugada){
        this.numeroJugada = numJugada;
    }

    public void comprobarDescarte(){
        List<Card> descartadas = new ArrayList<>();

        for (Card carta : playerCards) {
            int contador = 0;
            for (Card c : playerCards) {
                if (c.getValue() == carta.getValue()) {
                    contador++;
                }
            }
            if (contador == 4) {
                descartadas.add(carta);

            }
        }

        if(descartadas.size() == 4){
            Log.i("DESCARTE", "SE DESCARTAN: " + descartadas.get(0).getValue() + " DE " + id);
        }
        playerCards.removeAll(descartadas);

    }
    public void play(){
        ArrayList<Card> jugada = new ArrayList<>();
        if(gameState.tableCards.size() != 0){
            for(int i = 0; i < playerCards.size(); i++){
                Log.i("CARTAS QUE TIENE LA IA", Integer.toString(playerCards.get(i).getValue()));
                if(playerCards.get(i).getValue() == numeroJugada){
                    jugada.add(this.playerCards.get(i));
                }
            }
        }else {
            jugada.add(playerCards.get(0));
        }

        gameState.echarCarta(jugada, this);
        gameActivity.displayPlayerCards();
    }
}