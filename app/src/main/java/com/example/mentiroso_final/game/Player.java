package com.example.mentiroso_final.game;
/*********************************************************************** IMPORTANTE LEER LINEAS 212 Y 213 ***********************************************************/
import android.util.Log;

import com.example.mentiroso_final.GameActivity;

import java.util.ArrayList;
import java.util.List;

public class Player {

    ArrayList<Card> cartasJugadas = new ArrayList<>();
    ArrayList<Card> playerCards = new ArrayList<>();
    ArrayList<Card> remainingCards = new ArrayList<>();
    private GameActivity gameActivity;
    public int numeroJugada;
    Game gameState;

    int score = 0;
    int id;

    /*********************************** Variables de la ia ******************************/

    private int estado = 0;
    private ArrayList<Card> manoIA = new ArrayList<>();
    int numCartas = 0, ronda = 0;
    String tipoCarta;

    boolean desconfie, xoguei_cartas, primeiraRonda;

    double dFactorLR = 0.04; // learning rate
    double dFactorLRAux = 0.002; // learning rate in case we don't win but there is not disaster
    // final double dMINLearnRate = 0.005; // We keep learning, after convergence,
    // during 5% of times

    boolean bAllActions = false; // At the beginning we did not try all actions
    int iNewAction2Play; // This is the new action to be played
    int iNumActions = 2; // For C or D for instance
    int iLastAction; // The last action that has been played by this player
    int[] iNumTimesAction = new int[iNumActions]; // Number of times an action has been played
    double[] dPayoffAction = new double[iNumActions]; // Accumulated payoff obtained by the different actions
    StateAction oPresentStateAction; // Contains the present state we are and the actions that are available
    ArrayList<StateAction> oVStateActions = new ArrayList<>(); // A vector containing strings with the possible States
    // and Actions available at each one

    ArrayList<StateAction> estadosRonda = new ArrayList<>();
    int[] jugadasRonda = new int[10];
    int[] indices = new int[10];
    String tMyId;
    boolean acusei;

    float[][] MatrizDeDesconfianza = { { (float) 0.333, (float) 0.333, (float) 0.333 },
            { (float) 0.333, (float) 0.333, (float) 0.333 },
            { (float) 0.333, (float) 0.333, (float) 0.333 },
            { (float) 0.333, (float) 0.333, (float) 0.333 },
            { (float) 0.333, (float) 0.333, (float) 0.333 },
            { (float) 0.333, (float) 0.333, (float) 0.333 },
            { (float) 0.333, (float) 0.333, (float) 0.333 },
            { (float) 0.333, (float) 0.333, (float) 0.333 },
            { (float) 0.333, (float) 0.333, (float) 0.333 },
            { (float) 0.333, (float) 0.333, (float) 0.333 },
            { (float) 0.333, (float) 0.333, (float) 0.333 },
            { (float) 0.333, (float) 0.333, (float) 0.333 },
            { (float) 0.333, (float) 0.333, (float) 0.333 },
            { (float) 0.333, (float) 0.333, (float) 0.333 },
            { (float) 0.333, (float) 0.333, (float) 0.333 }
    };


    float tresholdDesconfianza = (float) 0.8;
    float tresholdAllL = (float) 0.8;

    double desconfianza = Math.random();

    int cartasXogadorAnterior;
    int behaviour; // Comportamento que terá na ronda

    public ArrayList<String> jugada;   //Variable global para acceder desde main

    // Parametros learning
    float learningRate = (float) 0.6;

    int numVerdades = 0, interador = 0, numCartasJugadorAnterior = 0;
    /****************************************************************************************/

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
    public void setNumeroJugadorAnterior(int numCartasJugadorAnterior) { this.numCartasJugadorAnterior = numCartasJugadorAnterior; }

    public void comprobarDescarte(){
        List<Card> descartadas = new ArrayList<>();

        for (int x=0; x<playerCards.size(); x++) {
            int contador = 0;
            for (int i=0; i<playerCards.size(); i++) {
                if (playerCards.get(i).getValue() == playerCards.get(x).getValue()) {
                    contador++;
                }
            }
            if (contador == 4) {
                descartadas.add(playerCards.get(x));
                Log.i("Contador", "contador a 4");

            }
        }

        if(descartadas.size() == 4){
            Log.i("DESCARTE", "SE DESCARTAN: " + descartadas.get(0).getValue() + " DE " + id);
        }
        playerCards.removeAll(descartadas);

    }
    public void play(){
        ArrayList<Card> jugada = new ArrayList<>();
        if(gameState.tableCards.size() != 0){   //Si es distinto de cero, ya hay cartas en la mesa
            for(int i = 0; i < playerCards.size(); i++){
                Log.i("CARTAS QUE TIENE LA IA", Integer.toString(playerCards.get(i).getValue()));
                if(playerCards.get(i).getValue() == numeroJugada){
                    jugada.add(this.playerCards.get(i));
                }
            }
        }else { //Empieza jugando
            jugada.add(playerCards.get(0));
        }

        if(jugada.size() == 0){
            gameState.levantarCarta();

        }else {
            //if jugada solo tiene verdades (si todas jugada.get(i).getValue() == numJugada) { gameState.echarCarta(jugada, this) } else { estas echando mentira}
        }

        gameState.echarCarta(jugada, this);
        gameActivity.displayPlayerCards();
    }

    public void juegaIA(){

        ArrayList<Card> jugada = new ArrayList<>();

        numVerdades = numVerdadesTengo(playerCards);

        if (numVerdades == 0) {
            behaviour = vGetNewActionAutomata(
                    "CartasMesa" + gameState.tableCards.size() + "NumVerdades" + numVerdades, 3, 0);
        } else if (numVerdades == 1) {
            behaviour = vGetNewActionAutomata(
                    "CartasMesa" + gameState.tableCards.size() + "NumVerdades" + numVerdades, 4, 0);
        } else if (numVerdades == 2) {
            behaviour = vGetNewActionAutomata(
                    "CartasMesa" + gameState.tableCards.size() + "NumVerdades" + numVerdades, 5, 0);
        } else if (numVerdades == 3) {
            behaviour = vGetNewActionAutomata(
                    "CartasMesa" + gameState.tableCards.size() + "NumVerdades" + numVerdades, 6, 0);
        }

        if(behaviour==1 && playerCards.size()<2){

            behaviour = playerCards.size() - 1;
        }
        else if(behaviour==2 && playerCards.size()<3){
            behaviour = playerCards.size() - 1;
        }
        else if(behaviour==4 && playerCards.size()<2){
            behaviour = playerCards.size() - 1;
        }
        else if(behaviour==5 && playerCards.size()<3){
            behaviour = playerCards.size() - 1;
        }


        if(gameState.tableCards.size() != 0) {
            if(MatrizDeDesconfianza[numCartas][numCartasJugadorAnterior -1] < desconfianza) {
                gameState.levantarCarta();
                gameActivity.displayPlayerCards();
                return;
            }
        }

        jugadasRonda[interador] = behaviour;
        interador++;


        //Queda que la IA decide si levanta o no y se llamen a las funciones de la clase Game (revisar de linea 165 a 170 de esta clase)
        //Queda ver la comprobacion de cuando se gana
        switch (behaviour) {

            case 0:
                jugada = seleccionarMentiras(playerCards, tipoCarta, 1, behaviour);
                for(int i=0; i<jugada.size(); i++){
                    cartasJugadas.add(jugada.get(i));
                }
                break;
            case 1:
                jugada = seleccionarMentiras(playerCards, tipoCarta, 2, behaviour);
                for(int i=0; i<jugada.size(); i++){
                    cartasJugadas.add(jugada.get(i));
                }
                break;
            case 2:
                jugada = seleccionarMentiras(playerCards, tipoCarta, 3, behaviour);
                for(int i=0; i<jugada.size(); i++){
                    cartasJugadas.add(jugada.get(i));
                }
                break;
            case 3:
                jugada = seleccionarVerdades(playerCards, tipoCarta, 1);
                for(int i=0; i<jugada.size(); i++){
                    cartasJugadas.add(jugada.get(i));
                }
                break;
            case 4:
                jugada = seleccionarVerdades(playerCards, tipoCarta, 2);
                for(int i=0; i<jugada.size(); i++){
                    cartasJugadas.add(jugada.get(i));
                }
                break;
            case 5:
                jugada = seleccionarVerdades(playerCards, tipoCarta, 3);
                for(int i=0; i<jugada.size(); i++){
                    cartasJugadas.add(jugada.get(i));
                }
                break;
        }

        if(behaviour == 0 || behaviour == 1 || behaviour == 2) {
            if(gameState.tableCards.size() == 0) {
                gameState.mentir(jugada, this, 1);

            }else {
                gameState.mentir(jugada, this, 4);
            }
        }else {
            gameState.echarCarta(jugada, this);
        }

        gameActivity.displayPlayerCards();

    }

    public void desconfie(int refuerzo) {
        //Pedrito pedrito que ostias es acusei y por que va aqui: if(acusei && numCartas<8){
        reinforceDesconfianza(gameState.tableCards.size(), cartasJugadas.size(), refuerzo);
    }

    public void seLaLevantan() {
        for(int i=0; i<estadosRonda.size(); i++){
            vReinforceActionAutomata(estadosRonda.get(i).sState, 0, jugadasRonda[i], learningRate);
        }
        estadosRonda.clear();
        indices = new int[10];
        jugadasRonda = new int[10];
        interador = 0;
        //Pedrito pedrito que ostias es acusei y por que va aqui: if(acusei && numCartas<8){
        reinforceDesconfianza(gameState.tableCards.size(), cartasJugadas.size(), 1);
    }


    public int numVerdadesTengo(ArrayList<Card> manoIA) {
        int numVerdades = 0;
        String split;

        for (int i = 0; i < manoIA.size(); i++) {
            if (manoIA.get(i).getValue() == numeroJugada) {
                numVerdades++;
            }
        }
        return numVerdades;
    }

    public ArrayList<Card> seleccionarVerdades(ArrayList<Card> manoIA, String tipoCarta, int numVerdades) {
        ArrayList<Card> posicionVerdades = new ArrayList<>();

        String carta;
        for (int x = 0; x < manoIA.size(); x++) {
            if (manoIA.get(x).getValue() == numeroJugada) {
                if(posicionVerdades.size() != numVerdades) {
                    posicionVerdades.add(manoIA.get(x));
                }
            }
        }

        return posicionVerdades;
    }

    public ArrayList<Card> seleccionarMentiras(ArrayList<Card> manoIA, String tipoCarta, int numMentiras, int behaviour) {
        ArrayList<Card> posicionMentiras = new ArrayList<>();

        String carta;
        for (int x = 0; x < manoIA.size(); x++) {
            if (manoIA.get(x).getValue() != numeroJugada) {
                if(posicionMentiras.size() != numMentiras){
                    posicionMentiras.add(manoIA.get(x));
                }
            }
        }

        int x = 0;
        if (posicionMentiras.size() < behaviour + 1) {
            while (posicionMentiras.size() < behaviour + 1) {
                if (manoIA.get(x).getValue() == numeroJugada) {
                    if(posicionMentiras.size() != numMentiras){
                        posicionMentiras.add(manoIA.get(x));
                    }
                }
                x++;
            }
        }

        return posicionMentiras;
    }

    /*public String seleccionarTipoCarta(ArrayList<String> manoIA) {
        String tipo = "";

        if (behaviour == 3) { // T
            tipo = manoIA.get(0).substring(0, 1);

        } else if (behaviour == 4) { // TT

            for (int x = 0; x < manoIA.size(); x++) {
                tipo = manoIA.get(x).substring(0, 1);

                if (numVerdadesTengo(manoIA, tipo) == 2) {
                    break;
                }
            }
        } else if (behaviour == 5) { // TTT

            for (int x = 0; x < manoIA.size(); x++) {
                tipo = manoIA.get(x).substring(0, 1);

                if (numVerdadesTengo(manoIA, tipo) == 3) {
                    break;
                }
            }

        }

        // System.out.println(tipo);
        return tipo;
    }
    */

    public void vReinforceActionAutomata(String estado, int iNActions, int plays, double learningRate) {
        int indice = 0;
        dFactorLR = learningRate;

        for(int i = 0; i < oVStateActions.size(); i++){
            if(oVStateActions.get(i).sState.equals(estado)){
                indice = i;
                break;
            }
        }
        int nActions = oVStateActions.get(indice).getNumAcciones();
        oVStateActions.get(indice).dValAction[plays] += dFactorLR
                * (1.0 - oVStateActions.get(indice).dValAction[plays]); // Reinforce the last action
        for (int j = 0; j < nActions; j++) {
            if (j != plays) {
                oVStateActions.get(indice).dValAction[j] *= (1.0 - dFactorLR); // The rest are weakened
            }
        }
    }

    public void reinforceDesconfianza(int cartasEnMesa, int cartasXogadas,int reforzo) {
        double RD_increase=0.7;
        double RD_decrease=0.3;

        switch (reforzo) {
            case 1:    //Refuerzo positivo (acerté)

                for(int i=0; i<3; i++){
                    if(i==cartasXogadas){
                        MatrizDeDesconfianza[cartasEnMesa-1][i]+= RD_increase*MatrizDeDesconfianza[cartasEnMesa-1][i]/3;
                        if (MatrizDeDesconfianza[cartasEnMesa-1][i]>0.8){
                            MatrizDeDesconfianza[cartasEnMesa-1][i]=(float)0.8;
                        }
                    }
                    else{
                        MatrizDeDesconfianza[cartasEnMesa-1][i]-= RD_decrease*MatrizDeDesconfianza[cartasEnMesa-1][i];
                        if(MatrizDeDesconfianza[cartasEnMesa-1][i]<0.15){
                            MatrizDeDesconfianza[cartasEnMesa-1][i]=(float)0.15;
                        }
                    }
                }
                //escribirDesconfianza();
                break;

            case 0:    //   Fallei

                for(int i=0; i<3; i++){
                    if(i==cartasXogadas){
                        MatrizDeDesconfianza[cartasEnMesa-1][i]-= RD_decrease*MatrizDeDesconfianza[cartasEnMesa-1][i];
                        if (MatrizDeDesconfianza[cartasEnMesa-1][i]<0.15){
                            MatrizDeDesconfianza[cartasEnMesa-1][i]=(float)0.15;
                        }
                    }
                    else{
                        MatrizDeDesconfianza[cartasEnMesa-1][i]+= RD_increase*MatrizDeDesconfianza[cartasEnMesa-1][i]/3;
                        if(MatrizDeDesconfianza[cartasEnMesa-1][i]>0.8){
                            MatrizDeDesconfianza[cartasEnMesa-1][i]=(float)0.8;
                        }
                    }
                }

                //escribirDesconfianza();
                break;

            default:
                break;
        }

    }






    public int vGetNewActionAutomata(String sState, int iNActions, double dReward) {
        boolean bFound;
        StateAction oStateProbs;
        int iNewAction = 0;

        bFound = false; // Searching if we already have the state
        for (int i = 0; i < oVStateActions.size(); i++) {
            oStateProbs = (StateAction) oVStateActions.get(i);
            if (oStateProbs.sState.equals(sState)) {
                oPresentStateAction = oStateProbs;
                estadosRonda.add(oVStateActions.get(i)); //si este estado ya existe, lo guardo en el arraylist de estados por los que paso en esta ronda
                bFound = true;
                break;
            }
        }
        // If we didn't find it, then we add it
        if (!bFound) {
            oPresentStateAction = new StateAction(sState, iNActions, true);
            oVStateActions.add(oPresentStateAction);
            estadosRonda.add(oPresentStateAction); //si no existe, guardo el nuevo estado en el arraylist de estados por los que paso en esta ronda
        }
        double dValAcc = 0; // Generating the new action based on probabilities
        double dValRandom = Math.random();
        for (int i = 0; i < iNActions; i++) {
            dValAcc += oPresentStateAction.dValAction[i];
            if (dValRandom < dValAcc) {
                iNewAction = i;
                break;
            }
        }

        return iNewAction;
    }

    public class StateAction {
        String sState;
        double[] dValAction;

        StateAction(String sAuxState, int iNActions) {
            sState = sAuxState;
            dValAction = new double[iNActions];
        }

        StateAction(String sAuxState, int iNActions, boolean bLA) {
            this(sAuxState, iNActions);
            if (bLA)
                for (int i = 0; i < iNActions; i++) // This constructor is used for LA and sets up initial probabilities
                    dValAction[i] = 1.0 / iNActions;
        }

        public String sGetState() {
            return sState;
        }

        public double dGetQAction(int i) {
            return dValAction[i];
        }

        public String sGetStatePlusActions() {
            String result = this.sState;
            for (int i = 0; i < dValAction.length; i++) {
                result = result + " " + dValAction[i];
            }
            return result;
        }

        public int getNumAcciones() {
            return dValAction.length;
        }
    }
}