package com.example.mentiroso_final.game;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mentiroso_final.GameActivity;

import java.sql.Array;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class Game {
    public ArrayList<Card> allCards;
    public ArrayList<Card> deck;
    public ArrayList<Card> tableCards = new ArrayList<>();
    public ArrayList<Card> discardCards;

    public boolean personaYaHaJugado = false;
    //private ArrayList<Player> IAs = new ArrayList<>();
    public int numeroJugada = 0;
    public int roundNumber;
    public boolean gameOver = false;
    public boolean fueMentira = false;
    public Player player1;
    public Player player2;
    public Player player3;
    public Player player4;
    public ArrayList<Player> players = new ArrayList<>();
    public int currentPlayer;
    private GameActivity gameActivity;

    private Player winner;
    private CountDownLatch latch;
    private GameTask gameTask;
    int tamañoLevantar=0;


    public Game(ArrayList<Card> deck, GameActivity gameActivity){
        this.deck = deck;
        this.gameActivity = gameActivity;
        this.player1 = gameActivity.player1;
        this.player2 = gameActivity.player2;
        this.player3 = gameActivity.player3;
        this.player4 = gameActivity.player4;
        this.currentPlayer = this.player1.getId();
        latch = new CountDownLatch(1);
        gameTask = new GameTask();
        //estado jugador cogiendo
    }

    public void startGame(){
        players.add(this.player1);
        players.add(this.player2);
        players.add(this.player3);
        players.add(this.player4);

        gameTask.execute();
    }

    private void playTurn() {
        // Lógica para el turno de un jugador
        if (currentPlayer != 1) {
            Log.i("LE TOCA A EN PLAYTURN", Integer.toString(currentPlayer));
            players.get(currentPlayer - 1).juegaIA();

            if (currentPlayer==2){
                if (player2.jugada.size()==0){
                    if (fueMentira) gameActivity.textOp1.setText("He levantado y era mentira, el jugador 1 se lleva " + tamañoLevantar);
                    else gameActivity.textOp1.setText("He levantado y era verdad, ,me llevo " + tamañoLevantar);
                }
                else gameActivity.textOp1.setText("He jugado " + player2.jugada.size() + " de "+ numeroJugada);
            }
            if (currentPlayer==3){
                if (player3.jugada.size()==0){
                    if (fueMentira) gameActivity.textOp2.setText("He levantado y era mentira, el jugador 2 se lle va " + tamañoLevantar);
                    else gameActivity.textOp2.setText("He levantado y era verdad, ,me llevo " + tamañoLevantar);
                }
                else gameActivity.textOp2.setText("He jugado " + player3.jugada.size() + " de "+ numeroJugada);
            }
            if (currentPlayer==4){
                if (player4.jugada.size()==0){
                    if (fueMentira) gameActivity.textOp3.setText("He levantado y era mentira, el jugador 3 se lleva " + tamañoLevantar);
                    else gameActivity.textOp3.setText("He levantado y era verdad, ,me llevo " + tamañoLevantar);
                }
                else gameActivity.textOp3.setText("He jugado " + player4.jugada.size() + " de "+ numeroJugada);
            }

        }
    }
    public void nextPlayer() {
        // Cambia al siguiente jugador
        if(currentPlayer == 4){
            currentPlayer = 1;
            gameActivity.setTurnTrue();
        }
        else{
            currentPlayer += 1;
        }
    }

    public void echarCarta(ArrayList<Card> cards, Player player){
        //quitar las cartas del arraylist
        int i=0;
        String cartasMesa = "";
        String cartasJugadas = "Jugador " + player.getId() + " juega:\n";
        if(player.getId() == 1){ player2.setNumeroJugadorAnterior(cards.size()); }
        if(player.getId() == 2){ player3.setNumeroJugadorAnterior(cards.size()); }
        if(player.getId() == 3){ player4.setNumeroJugadorAnterior(cards.size()); }
        for (Card c : cards ){
            cartasJugadas = cartasJugadas + c.getValue() + " de " + c.getSuit() + "\n";
            player.deleteCard(cards.get(i));
            i++;
        }
        if(tableCards.size() == 0){
            this.numeroJugada = cards.get(0).getValue();
            player1.setNumeroJugada(numeroJugada);
            player2.setNumeroJugada(numeroJugada);
            player3.setNumeroJugada(numeroJugada);
            player4.setNumeroJugada(numeroJugada);
        }
        //añadirlas a las cartas de la mesa
        i=0;
        for (Card c : cards ){
            tableCards.add(cards.get(i));
            i++;
        }

        //como sabemos que se echaron por las restricciones al seleccionar carta, no fue mentira
        this.fueMentira=false;
        for(int j = 0; j < tableCards.size(); j++){
            cartasMesa = cartasMesa + tableCards.get(j).getValue() + " de " + tableCards.get(j).getSuit() + "\n";
        }
        Log.i("SE JUEGA A ", String.valueOf(numeroJugada));
        Log.i("CARTAS JUGADAS", cartasJugadas);
        Log.i("CARTAS EN LA MESA", tableCards.toString());
        gameActivity.textGen.setText("Se juega a "+ numeroJugada);
        latch.countDown();
    }
    public void mentir(ArrayList<Card> cards, Player player, int valorMentira){
        //hacer lo mismo que en levantar pero poniendo fuementira a true
        //quitar las cartas del arraylist
        int i=0;
        String cartasMesa = "";
        String cartasJugadas = "Jugador " + player.getId() + " juega:\n";

        if(player.getId() == 1){ player2.setNumeroJugadorAnterior(cards.size()); }
        if(player.getId() == 2){ player3.setNumeroJugadorAnterior(cards.size()); }
        if(player.getId() == 3){ player4.setNumeroJugadorAnterior(cards.size()); }

        for (Card c : cards ){
            cartasJugadas = cartasJugadas + c.getValue() + " de " + c.getSuit() + "\n";
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
        for(int j = 0; j < tableCards.size(); j++){
            cartasMesa = cartasMesa + tableCards.get(j).getValue() + " de " + tableCards.get(j).getSuit() + "\n";
        }
        //como sabemos que se echaron por las restricciones al seleccionar carta, no fue mentira
        Log.i("SE JUEGA A ", String.valueOf(numeroJugada));
        Log.i("CARTAS JUGADAS", cartasJugadas);
        Log.i("CARTAS EN LA MESA", tableCards.toString());
        this.fueMentira=true;
        gameActivity.textGen.setText("Se juega a "+ numeroJugada);
        latch.countDown();
    }
    /*
     * De momento así funciona, va a haber que cambiarlo al tener 4 jugadores
     */
    public void levantarCarta(){
        String cartasMesa = "";
        player1.setNumeroJugada(0);
        player2.setNumeroJugada(0);
        player3.setNumeroJugada(0);
        player4.setNumeroJugada(0);
        numeroJugada = 0;
        Log.i("MENTIRA?", Boolean.toString(fueMentira));
        player2.setNumeroJugadorAnterior(0);
        player3.setNumeroJugadorAnterior(0);
        player4.setNumeroJugadorAnterior(0);

        if(fueMentira){
            for(int i = 0; i < tableCards.size(); i++){
                if(currentPlayer == 1){
                    player4.playerCards.add(tableCards.get(i));

                }else if(currentPlayer == 2){
                    player1.playerCards.add(tableCards.get(i));

                }else if(currentPlayer == 3){
                    player2.playerCards.add(tableCards.get(i));

                }else if(currentPlayer == 4){
                    player3.playerCards.add(tableCards.get(i));

                }
            }
            if(currentPlayer == 1){
                player2.seLaLevantan();
                player3.seLaLevantan();
            }else if(currentPlayer == 2){
                player2.desconfie(1);
                player3.seLaLevantan();
                player4.seLaLevantan();
            }else if(currentPlayer == 3){
                player3.desconfie(1);
                player4.seLaLevantan();
            }else if(currentPlayer == 4){
                player2.seLaLevantan();
                player4.desconfie(1);
            }
        }else {
            for(int i = 0; i < tableCards.size(); i++){
                if (currentPlayer == 1) {
                    player1.playerCards.add(tableCards.get(i));
                }else if (currentPlayer == 2) {
                    player2.playerCards.add(tableCards.get(i));
                }else if (currentPlayer == 3) {
                    player3.playerCards.add(tableCards.get(i));
                }else if (currentPlayer == 4) {
                    player4.playerCards.add(tableCards.get(i));
                }
            }
            if (currentPlayer == 1) {
                player2.seLaLevantan();
                player3.seLaLevantan();
                player4.seLaLevantan();
            }else if (currentPlayer == 2) {
                player2.desconfie(0);
                player3.seLaLevantan();
                player4.seLaLevantan();
            }else if (currentPlayer == 3) {
                player2.seLaLevantan();
                player3.desconfie(0);
                player4.seLaLevantan();
            }else if (currentPlayer == 4) {
                player2.seLaLevantan();
                player3.seLaLevantan();
                player4.desconfie(0);
            }
        }

        for(int i = 0; i < tableCards.size(); i++){
            cartasMesa = cartasMesa + tableCards.get(i).getValue() + " de " + tableCards.get(i).getSuit() + "\n";
        }
        Log.i("CARTAS LEVANTADAS", cartasMesa);
        tamañoLevantar=tableCards.size();
        tableCards.clear();
        gameActivity.textGen.setText("");
        latch.countDown();
    }


    //La comprobación para ver si se gano será mirar si el jugador anterior al anterior tiene 0 cartas
    //es decir, si la persona es el jugador 1, hay una ia 2 que va despues de la persona, una ia 3 que va despues
    //de la ia 2, si cuando es el turno de la ia 3 el juagdor 1 tiene 0 cartas, gana el jugador 1
    // ó
    //si un jugador levanta y con las cartas que chupa descarta y se queda con 0, gana
    public Boolean isOver(Boolean descartoYGano, Player player){
        Boolean isOver = false;

        if(descartoYGano){
            isOver = true;
            winner = player;
        }else {
            //turno persona
            if(currentPlayer == 1){
                if(player3.getPlayerCards().size() == 0){
                    isOver = true;
                    winner = player3;
                }
            }
            if(currentPlayer == 2){
                if(player4.getPlayerCards().size() == 0){
                    isOver = true;
                    winner = player4;
                }
            }
            if(currentPlayer == 3){
                if(player1.getPlayerCards().size() == 0){
                    isOver = true;
                    winner = player1;
                }
            }
            if(currentPlayer == 4){
                if(player2.getPlayerCards().size() == 0){
                    isOver = true;
                    winner = player2;
                }
            }
        }
        if(isOver){
            Log.i("GANADOR", "GANO " + winner.getId());
        }
        return isOver;
    }
    // Método para reiniciar el latch
    public void resetSemaforo() {
        latch = new CountDownLatch(1);
    }

    private class GameTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            while(!isOver(false, player1)){ //aquí false y player1 no hace nada, se mete xq necesitamos mandarle que jugador gana por descarte, pero al mandarle un false no hace nada
                playTurn();
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                resetSemaforo();

                nextPlayer();
            }
            return null;
        }
    }

}
