package com.example.mentiroso_final.game;

import android.util.Log;

import com.example.mentiroso_final.GameActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {

    ArrayList<Card> cartasJugadas = new ArrayList<>();
    ArrayList<Card> playerCards = new ArrayList<>();
    ArrayList<Card> remainingCards = new ArrayList<>();
    ArrayList<Integer> cartasDescartadas = new ArrayList<>();
    private GameActivity gameActivity;
    public int numeroJugada;
    private String archivoDesconfianza2 = "matrizDesconfianza2.txt";   //cambiado o final
    private  String archivoDesconfianza3 = "matrizDesconfianza3.txt";
    private  String archivoDesconfianza4 = "matrizDesconfianza4.txt";

    private String archivoIA2 = "estado2.txt";
    private String archivoIA3 = "estado3.txt";
    private String archivoIA4 = "estado4.txt";
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

    int valormentira=0;
    boolean melevantaronanteriorronda=false;

    private float[] cantarMentiras={(float)0.4,(float)0.4,(float)0.4,(float)0.4,(float)0.4,(float)0.4,(float)0.4,(float)0.4,(float)0.4,(float)0.4};
    private float[][] MatrizDeDesconfianza = { { (float) 0.333, (float) 0.333, (float) 0.333 },
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
            { (float) 0.333, (float) 0.333, (float) 0.333 },
            { (float) 0.333, (float) 0.333, (float) 0.333 },
            { (float) 0.333, (float) 0.333, (float) 0.333 },
            { (float) 0.333, (float) 0.333, (float) 0.333 }
    };


    float tresholdDesconfianza = (float) 0.8;
    float tresholdAllL = (float) 0.8;

    double desconfianza = 0.4*Math.random()+0.15;

    int cartasXogadorAnterior;
    int behaviour; // Comportamento que terá na ronda

    public ArrayList<Card> jugada;   //Variable global para acceder desde main

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
        initializeState();
        String archivoDesconfianza="";
        String archivoMentiras="";
        switch(id){
            case 2:
                archivoDesconfianza=archivoDesconfianza3;
                archivoMentiras="archivoMentiras2.txt";
                break;
            case 3:
                archivoDesconfianza=archivoDesconfianza3;
                archivoMentiras="archivoMentiras3.txt";

                break;
            case 4:
                archivoDesconfianza=archivoDesconfianza3;
                archivoMentiras="archivoMentiras4.txt";
                break;
        }
        MatrizDeDesconfianza=leerDesconfianza(archivoDesconfianza);
        leerMentiras(archivoMentiras);
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

    public int comprobarDescarte(){
        List<Card> descartadas = new ArrayList<>();

        for (int x=0; x<this.playerCards.size(); x++) {
            int contador = 0;
            for (int i=0; i<this.playerCards.size(); i++) {
                if (this.playerCards.get(i).getValue() == this.playerCards.get(x).getValue()) {
                    contador++;
                }
            }
            if (contador == 4) {
                descartadas.add(this.playerCards.get(x));
                Log.i("Contador", "contador a 4");
            }
        }

        if(descartadas.size() == 4){
            Log.i("DESCARTE", "SE DESCARTAN: " + descartadas.get(0).getValue() + " DE " + id);
            this.playerCards.removeAll(descartadas);
            return descartadas.get(0).getValue();
        }else {
            return 0;
        }
    }

    public void setCartasDescartadas(ArrayList<Integer> descartes){
        this.cartasDescartadas = descartes;
    }
    public void juegaIA(){

        jugada = new ArrayList<>();

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
                 valormentira=selecionarValorMentira(playerCards);
                 int valorCantar=valormentira;
                 switch (valormentira){
                     case 8:
                         valorCantar=10;
                         break;
                     case 9:
                         valorCantar=11;
                         break;
                     case 10:
                         valorCantar=12;
                         break;
                     default:
                         break;
                 }

                gameState.mentir(jugada, this, valorCantar);

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
        escribirDesconfianza();
    }

    public void seLaLevantan() {
        for(int i=0; i<estadosRonda.size(); i++){
            vReinforceActionAutomata(estadosRonda.get(i).sState, 0, jugadasRonda[i], learningRate);
        }
        estadosRonda.clear();
        Log.i("DEBERIA ESCRIBIR", "DEBERIA ESCRIBIR DESDE IA " + id);
      //  escribir();
        indices = new int[10];
        jugadasRonda = new int[10];
        interador = 0;
        reinforceDesconfianza(gameState.tableCards.size(), cartasJugadas.size(), 0); //estsba a 1 e creo que era erro

    //    Log.i("Escribe en Cantar Mentiras",String.valueOf(valormentira));
        reinforceMentiras(true, valormentira);
        escribirCantarMentiras();
        melevantaronanteriorronda=true;
        
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


    public int selecionarValorMentira(ArrayList<Card> manoIA){
        int contador=0;
        int cont2=0;
        int valorMentira=0;
        float[] posiblesMentiras=cantarMentiras;
      //  Collections.shuffle(manoIA);
        for (int i=0;i<cantarMentiras.length;i++){
            if(manoIA.isEmpty()){
                return 0;
            }
            float factor =(float) (Math.random()*0.5); //Para crear un num aleatorio distinto cada vez
            posiblesMentiras[i]=factor+posiblesMentiras[i];
        }
        float max=0;
        for (int k = 0; k < posiblesMentiras.length; k++) {
            if(max<posiblesMentiras[k]){
                max=posiblesMentiras[k];
           //     Log.i("VALOR MENTIRA",String.valueOf(max));
                valorMentira=k+1;      //porque a posicion 3 corresponde a carta 4
            }
        }

        return valorMentira;
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

    public void vReinforceActionAutomata(String estado, int iNActions, int plays, double learningRate) {
        int indice = 0;
        dFactorLR = learningRate;
        Log.i("REFUERZO", "REFUERZO DESDE IA " + id);
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
    public void reinforceMentiras(boolean melevantaron,int seleccion){
        double RD_increase=0.4;
        double RD_decrease=0.2;
        if(melevantaron){
            for (int i = 0; i < cantarMentiras.length; i++) {
                if ((seleccion-1)==cantarMentiras[i]) {
                   // cantarMentiras[i]=cantarMentiras[i]*(float)RD_decrease;
                    cantarMentiras[i]-=cantarMentiras[i]*(float)RD_decrease/16;
                }
                else{
                    cantarMentiras[i]+=cantarMentiras[i]*(float)RD_increase/16;
                }
             //   Log.i("ReinforceMentira","Valor post-reforzo=" +(cantarMentiras[i]));
                if(cantarMentiras[i]>(float)0.85){
                    cantarMentiras[i]=(float)0.35;
                }
                if(cantarMentiras[i]<(float)0.05){
                    cantarMentiras[i]=(float)0.2;

                }
            }
        }
        else{
            for (int i = 0; i < cantarMentiras.length; i++) {
                if ((seleccion-1)==cantarMentiras[i]) {
                    //cantarMentiras[i]=cantarMentiras[i]*(float)RD_increase;
                    cantarMentiras[i]+=cantarMentiras[i]*(float)RD_increase/16;

                }
                else{
                   // cantarMentiras[i]=cantarMentiras[i]*(float)RD_decrease;
                    cantarMentiras[i]-=cantarMentiras[i]*(float)RD_decrease/16;

                }
                if(cantarMentiras[i]>(float)0.85){
                    cantarMentiras[i]=(float)0.35;
                }
                if(cantarMentiras[i]<(float)0.05){
                    cantarMentiras[i]=(float)0.2;

                }
            }

        }
    }
    public void reinforceDesconfianza(int cartasEnMesa, int cartasXogadas,int reforzo) {

        double RD_increase=0.5;
        double RD_decrease=0.3;
        switch (reforzo) {
            case 1:    //Refuerzo positivo (acerté)

                for(int i=0; i<3; i++){
                    if(i==cartasXogadas){
                        MatrizDeDesconfianza[cartasEnMesa-1][i]+= RD_increase*MatrizDeDesconfianza[cartasEnMesa-1][i]/8;
                        if (MatrizDeDesconfianza[cartasEnMesa-1][i]>0.8){
                            MatrizDeDesconfianza[cartasEnMesa-1][i]=(float)0.8;
                        }
                    }
                    else{
                        MatrizDeDesconfianza[cartasEnMesa-1][i]-= RD_decrease*MatrizDeDesconfianza[cartasEnMesa-1][i]/8;
                        if(MatrizDeDesconfianza[cartasEnMesa-1][i]<0.15){
                            MatrizDeDesconfianza[cartasEnMesa-1][i]=(float)0.15;
                        }
                    }
                }
                break;

            case 0:    //   Fallei

                for(int i=0; i<3; i++){
                    if(i==cartasXogadas){
                        MatrizDeDesconfianza[cartasEnMesa-1][i]-= RD_decrease*MatrizDeDesconfianza[cartasEnMesa-1][i]/8;
                        if (MatrizDeDesconfianza[cartasEnMesa-1][i]<0.15){
                            MatrizDeDesconfianza[cartasEnMesa-1][i]=(float)0.15;
                        }
                    }
                    else{
                        MatrizDeDesconfianza[cartasEnMesa-1][i]+= RD_increase*MatrizDeDesconfianza[cartasEnMesa-1][i]/8;
                        if(MatrizDeDesconfianza[cartasEnMesa-1][i]>0.8){ //previamente 0.8
                            MatrizDeDesconfianza[cartasEnMesa-1][i]=(float)0.65;
                        }
                    }
                }

                break;

            default:
                break;
        }

    }

    public void leerMentiras(String archivoMentiras){
        File archivo = new File(gameActivity.getApplicationContext().getFilesDir(), archivoMentiras );
        try {
            FileReader fr = new FileReader(archivo);
            BufferedReader br = new BufferedReader(fr);
            String[] valor;
            String linea;
            while(!(linea=br.readLine()).isEmpty()){
                valor=linea.split(" /");
                int i=0;
                for (i=0;i<cantarMentiras.length;i++){
                   cantarMentiras[i]=Float.parseFloat(valor[i]);
                }
            }
            fr.close();

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public float[][] leerDesconfianza (String archivoMatriz){

        float[][] matriz = { { (float) 0.32432523, (float) 0.6002512, (float) 0.6002512 },
        { (float) 0.70688003, (float) 0.2638012, (float) 0.2669042 },
        { (float) 0.78326017, (float) 0.29855278, (float) 0.22972664 },
        { (float) 0.7309259, (float) 0.22972664, (float) 0.22948164 },
        { (float) 0.65, (float) 0.29638794, (float) 0.26188838 },
        { (float) 0.6411631, (float) 0.29638794, (float) 0.29638794 },
        { (float) 0.6111439, (float) 0.43592772, (float) 0.43592772 },
        { (float) 43276682, (float) 0.2942388, (float) 0.43276682 },
        { (float) 0.33543226, (float) 0.33543226, (float) 0.27658448 },
        { (float) 0.33, (float) 0.33, (float) 0.33 },
        { (float) 0.33, (float) 0.33, (float) 0.33 },
        { (float) 0.33, (float) 0.33, (float) 0.33 },
        { (float) 0.33, (float) 0.33, (float) 0.333 },
        { (float) 0.33, (float) 0.33, (float) 0.333 },
        { (float) 0.33, (float) 0.333, (float) 0.33 },
                { (float) 0.333, (float) 0.33, (float) 0.33 },
                { (float) 0.33, (float) 0.333, (float) 0.333 },
                { (float) 0.333, (float) 0.33, (float) 0.333 }
        };

       // matriz=MatrizDeDesconfianza;

        try {
            File archivo = new File(gameActivity.getApplicationContext().getFilesDir(), archivoMatriz);
            FileReader fr = new FileReader(archivo);
            BufferedReader br = new BufferedReader(fr);
            String[] valor;
            String linea;
            int num_fila=0;
            while(!(linea=br.readLine()).isEmpty()){
                valor=linea.split(" /");
                Log.i("V_LINEA",linea);
                int i=0,j=0;
                for (i=0;i<3;i++){
                    //matriz[num_fila][i]=Float.parseFloat(valor[i]);
                    MatrizDeDesconfianza[num_fila][i]=Float.parseFloat(valor[i]);
                    Log.i("LeoDescTXT"+id,"MD +["+num_fila+"]["+i+"]= "+MatrizDeDesconfianza[num_fila][i]);
                }
                num_fila++;
            }
            fr.close();

        }
        catch (FileNotFoundException flne) {
            try {
                File archivo = new File(gameActivity.getApplicationContext().getFilesDir(), archivoMatriz);
                MatrizDeDesconfianza=matriz;
                archivo.createNewFile();
            }
            catch (Exception e){
                //   Log.i("PUÑETA","Non habia ficheiro");
                e.printStackTrace();
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }


        return matriz;
    }
    //private void escribirDesconfianza(float[][] matriz,String archivoDesconfianza){

    public void escribirCantarMentiras(){
        String archivoMentiras="";
        switch(id){
            case 2:
                archivoMentiras="archivoMentiras2.txt";
                break;
            case 3:
                archivoMentiras="archivoMentiras3.txt";
                break;
            case 4:
                archivoMentiras="archivoMentiras4.txt";
                break;
        }
        File archivo = new File(gameActivity.getApplicationContext().getFilesDir(), archivoMentiras);
        try {
            FileWriter fw = new FileWriter(archivo);

            int i = 0;
            for (int k = 0; k < cantarMentiras.length; k++) {
                    fw.write(Float.toString(cantarMentiras[k]) + " /");
            }
            fw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public void escribirDesconfianza(){
    String archivoDesconfianza="";
        switch(id){
            case 2:
                archivoDesconfianza=archivoDesconfianza2;
                break;
            case 3:
                archivoDesconfianza=archivoDesconfianza3;
                break;
            case 4:
                archivoDesconfianza=archivoDesconfianza4;
                break;
        }
        File archivo = new File(gameActivity.getApplicationContext().getFilesDir(), archivoDesconfianza);
        try {
            FileWriter fw = new FileWriter(archivo);

            int i = 0;
            for (int k = 0; k < 9; k++) {
                for (i = 0; i < 3; i++) {
                    fw.write(Float.toString(MatrizDeDesconfianza[k][i]) + " /");
                    Log.i(archivoDesconfianza,Float.toString(MatrizDeDesconfianza[k][i]) + " /");
                }
                fw.write("\n");
            }
            fw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    };
    public void escribir(){
        String estados = "";
        try{
            if(id == 2) {
                File archivo = new File(gameActivity.getApplicationContext().getFilesDir(), archivoIA2);
                FileWriter escritor = new FileWriter(archivo);
                for (int i = 0; i < oVStateActions.size(); i++) {
                    estados = estados + oVStateActions.get(i).sGetStatePlusActions() + "\n";
                    Log.i("ESCRIBIENDO", oVStateActions.get(i).sGetStatePlusActions());
                }
                escritor.write(estados);
                escritor.close();
            }else if (id == 3){
                File archivo = new File(gameActivity.getApplicationContext().getFilesDir(), archivoIA3);
                FileWriter escritor = new FileWriter(archivo);
                for (int i = 0; i < oVStateActions.size(); i++) {
                    estados = estados + oVStateActions.get(i).sGetStatePlusActions() + "\n";
                    Log.i("ESCRIBIENDO", oVStateActions.get(i).sGetStatePlusActions());
                }
                escritor.write(estados);
                escritor.close();
            }else if (id == 4){
                File archivo = new File(gameActivity.getApplicationContext().getFilesDir(), archivoIA4);
                FileWriter escritor = new FileWriter(archivo);
                for (int i = 0; i < oVStateActions.size(); i++) {
                    estados = estados + oVStateActions.get(i).sGetStatePlusActions() + "\n";
                    Log.i("ESCRIBIENDO", oVStateActions.get(i).sGetStatePlusActions());
                }
                escritor.write(estados);
                escritor.close();
            }
        } catch (Exception e) {
            Log.i("EXCEPCION", String.valueOf(e));
        }
    }

    public void initializeState(){
        String estado = "";
        ArrayList<Double> actions = new ArrayList<>();
        String[] partes;
        try{
            if(id == 2) {
                File archivo = new File(gameActivity.getApplicationContext().getFilesDir(), archivoIA2);
                FileReader reader = new FileReader(archivo);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String linea;
                StringBuilder contenido = new StringBuilder();

                while ((linea = bufferedReader.readLine()) != null) {
                    partes = linea.split("\\s+");
                    estado = partes[0];
                    for (int i = 1; i < partes.length; i++) {
                        double numero = Double.parseDouble(partes[i]);
                        actions.add(numero);
                    }
                    oVStateActions.add(new StateAction(estado, actions.size(), actions));
                }
                // Imprime el contenido del archivo
                Log.i("CONTENIDO", oVStateActions.toString());
                // Cierra el lector
                reader.close();

            }else if (id == 3){
                File archivo = new File(gameActivity.getApplicationContext().getFilesDir(), archivoIA3);
                FileReader reader = new FileReader(archivo);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String linea;
                StringBuilder contenido = new StringBuilder();
                while ((linea = bufferedReader.readLine()) != null) {
                    partes = linea.split("\\s+");
                    estado = partes[0];
                    for (int i = 1; i < partes.length; i++) {
                        double numero = Double.parseDouble(partes[i]);
                        actions.add(numero);
                    }
                    oVStateActions.add(new StateAction(estado, actions.size(), actions));
                }
                // Imprime el contenido del archivo
                Log.i("CONTENIDO", oVStateActions.toString());
                // Cierra el lector
                reader.close();

            }else if (id == 4){
                File archivo = new File(gameActivity.getApplicationContext().getFilesDir(), archivoIA4);
                FileReader reader = new FileReader(archivo);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String linea;
                StringBuilder contenido = new StringBuilder();
                while ((linea = bufferedReader.readLine()) != null) {
                    partes = linea.split("\\s+");
                    estado = partes[0];
                    for (int i = 1; i < partes.length; i++) {
                        double numero = Double.parseDouble(partes[i]);
                        actions.add(numero);
                    }
                    oVStateActions.add(new StateAction(estado, actions.size(), actions));
                }
                // Imprime el contenido del archivo
                Log.i("CONTENIDO", oVStateActions.toString());
                // Cierra el lector
                reader.close();
            }
        } catch (Exception e) {
            Log.i("EXCEPCION", String.valueOf(e));
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

        StateAction(String sAuxState, int iNActions, ArrayList<Double> actions){
            sState = sAuxState;
            dValAction = new double[iNActions];
            for(int i = 0; i < actions.size(); i++){
                dValAction[i] = actions.get(i);
            }
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