package com.example.mentiroso_final;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentiroso_final.game.Card;
import com.example.mentiroso_final.game.Game;
import com.example.mentiroso_final.game.Player;


import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends AppCompatActivity {
    Boolean turn = true;
    ArrayList<Card> allCards; //todas
    ArrayList<Card> selectedCards = new ArrayList<>();
    static ArrayList<Card> cardsDeck = new ArrayList<>(); //as do mazo (eliminar ao acabar)

    ImageView imgTableCard;
    Button echarBtt, mentirBtt, levantarBtt;

    public Player player1 = new Player(1, this);
    public Player player2 = new Player(2, this);
    public Player player3 = new Player(3, this);
    public Player player4 = new Player(4, this);

    private CardAdapter cardAdapter;
    private CardAdapterOp cardAdapterOp2;
    private CardAdapterOpLateral cardAdapterOp1, cardAdapterOp3;
    RecyclerView recyclerView, recyclerViewOp1, recyclerViewOp2, recyclerViewOp3;
    Game gameState;
    private int valorSpinner;
    public TextView textOp1;
    public TextView textOp2;
    public TextView textOp3;
    public TextView textGen;
    public Toast toast;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        /*
         * Vistas
         */
        imgTableCard = (ImageView) findViewById(R.id.imgTableCard);

        echarBtt = findViewById(R.id.echarBtt);
        mentirBtt = findViewById(R.id.mentirBtt);
        levantarBtt = findViewById(R.id.levantarBtt);

        echarBtt.setEnabled(false);
        mentirBtt.setEnabled(false);
        levantarBtt.setEnabled(false);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array_select, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        recyclerViewOp1 = findViewById(R.id.recyclerViewOp1);
        recyclerViewOp1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        recyclerViewOp2 = findViewById(R.id.recyclerViewOp2);
        recyclerViewOp2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        recyclerViewOp3 = findViewById(R.id.recyclerViewOp3);
        recyclerViewOp3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        textOp1 = findViewById(R.id.textOp1);
        textOp2 = findViewById(R.id.textOp2);
        textOp3 = findViewById(R.id.textOp3);
        textGen = findViewById(R.id.textGen);
        ////////////////////////////////


        /*
         * Event listeners
         */
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //Object item = parent.getItemAtPosition(pos);
                //String textSpinner = spinner.getSelectedItem().toString();
                valorSpinner = Integer.parseInt((String) spinner.getSelectedItem());
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        levantarBtt.setOnClickListener(v -> {
            turn = false;
            gameState.levantarCarta();
            int numero;
            numero = player1.comprobarDescarte();
            if(numero != 0) {
                toast = Toast.makeText(this,"Se han descartado los "+numero+" del jugador 1", Toast.LENGTH_SHORT);
                toast.show();
            }
            numero = player2.comprobarDescarte();
            if(numero != 0){
                toast = Toast.makeText(this,"Se han descartado los "+numero+" del jugador 2", Toast.LENGTH_SHORT);
                toast.show();
            }
            numero = player3.comprobarDescarte();
            if(numero != 0){
                toast = Toast.makeText(this,"Se han descartado los "+numero+" del jugador 3", Toast.LENGTH_SHORT);
                toast.show();
            }
            numero = player4.comprobarDescarte();
            if(numero != 0){
                toast = Toast.makeText(this,"Se han descartado los "+numero+" del jugador 4", Toast.LENGTH_SHORT);
                toast.show();
            }
            //displayPlayerCards();
            levantarBtt.setEnabled(false);
            if (gameState.fueMentira) toast = Toast.makeText(this, "Era mentira! El jugador 4 se lleva "+gameState.tamañoLevantar, Toast.LENGTH_SHORT);
            else toast = Toast.makeText(this, "Era verdad! Te llevas "+gameState.tamañoLevantar, Toast.LENGTH_SHORT);
            toast.show();
        });

        echarBtt.setOnClickListener(v -> {
            //imgTableCard.setEnabled(false);
            echarBtt.setEnabled(false);
            mentirBtt.setEnabled(false);
            turn = false;
            gameState.echarCarta(selectedCards, player1);
            selectedCards.clear();
            //displayPlayerCards();
        });

        mentirBtt.setOnClickListener(v -> {
            mentirBtt.setEnabled(false);
            echarBtt.setEnabled(false);
            turn = false;
            gameState.mentir(selectedCards, player1, valorSpinner);
            selectedCards.clear();
            //displayPlayerCards();
            //clearColorFilterRecycler();
        });
        ///////////////////////////////////////////////////


        player1.setPlayerCards(new ArrayList<>());
        player2.setPlayerCards(new ArrayList<>());
        player3.setPlayerCards(new ArrayList<>());
        player4.setPlayerCards(new ArrayList<>());

        //inicializar baraja
        setDeck();

        //repartir
        distributeCards();

        //comprobamos si un jugador tiene que descartar
        int numero;
        numero = player1.comprobarDescarte();
        if(numero != 0){
            toast = Toast.makeText(this,"Se han descartado los "+numero+" del jugador 1", Toast.LENGTH_SHORT);
            toast.show();
        }
        numero = player2.comprobarDescarte();
        if(numero != 0){
            toast = Toast.makeText(this,"Se han descartado los "+numero+" del jugador 2", Toast.LENGTH_SHORT);
            toast.show();
        }
        numero = player3.comprobarDescarte();
        if(numero != 0){
            toast = Toast.makeText(this,"Se han descartado los "+numero+" del jugador 3", Toast.LENGTH_SHORT);
            toast.show();
        }
        numero = player4.comprobarDescarte();
        if(numero != 0){
            toast = Toast.makeText(this,"Se han descartado los "+numero+" del jugador 4", Toast.LENGTH_SHORT);
            toast.show();
        }

        gameState = new Game(cardsDeck, this);
        gameState.allCards = new ArrayList<>(this.allCards);

        player1.setGameState(gameState);
        player2.setGameState(gameState);
        player3.setGameState(gameState);
        player4.setGameState(gameState);

        //mostrar imagenes
        cardAdapter = new CardAdapter(player1.getPlayerCards(), getApplicationContext(), this);
        recyclerView.setAdapter(cardAdapter);
        //recyclerView.setEnabled(false);

        cardAdapterOp1 = new CardAdapterOpLateral(player2.getPlayerCards(), getApplicationContext(), this);
        recyclerViewOp1.setAdapter(cardAdapterOp1);
        recyclerViewOp1.setEnabled(false);

        cardAdapterOp2 = new CardAdapterOp(player3.getPlayerCards(), getApplicationContext(), this);
        recyclerViewOp2.setAdapter(cardAdapterOp2);
        recyclerViewOp2.setEnabled(false);

        cardAdapterOp3 = new CardAdapterOpLateral(player4.getPlayerCards(), getApplicationContext(), this);
        recyclerViewOp3.setAdapter(cardAdapterOp3);
        recyclerViewOp3.setEnabled(false);
        displayPlayerCards();


        gameState.startGame();
    }

    @SuppressLint("DiscouragedApi")
    void setDeck() {
        cardsDeck.clear();
        int imageId = 0;
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 12; j++) {
                if (j == 8 || j == 9) continue;
                if (i == 1) {
                    if (j < 10)
                        imageId = getResources().getIdentifier("oros_0" + j, "drawable", getPackageName());
                    else
                        imageId = getResources().getIdentifier("oros_" + j, "drawable", getPackageName());
                }
                if (i == 2) {
                    if (j < 10)
                        imageId = getResources().getIdentifier("copas_0" + j, "drawable", getPackageName());
                    else
                        imageId = getResources().getIdentifier("copas_" + j, "drawable", getPackageName());
                }
                if (i == 3) {
                    if (j < 10)
                        imageId = getResources().getIdentifier("espadas_0" + j, "drawable", getPackageName());
                    else
                        imageId = getResources().getIdentifier("espadas_" + j, "drawable", getPackageName());
                }
                if (i == 4) {
                    if (j < 10)
                        imageId = getResources().getIdentifier("bastos_0" + j, "drawable", getPackageName());
                    else
                        imageId = getResources().getIdentifier("bastos_" + j, "drawable", getPackageName());
                }
                cardsDeck.add(new Card(i, j, imageId));
            }
        }
        this.allCards = new ArrayList<>(cardsDeck);

    }

    public void distributeCards() {
        Collections.shuffle(cardsDeck);
        for (int i = 0; i < 10; i++) {
            player1.addCard(cardsDeck.get(0));
            cardsDeck.remove(0);
        }
        for (int i = 0; i < 10; i++) {
            player2.addCard(cardsDeck.get(0));
            cardsDeck.remove(0);
        }
        for (int i = 0; i < 10; i++) {
            player3.addCard(cardsDeck.get(0));
            cardsDeck.remove(0);
        }
        for (int i = 0; i < 10; i++) {
            player4.addCard(cardsDeck.get(0));
            cardsDeck.remove(0);
        }
    }

    public void displayPlayerCards() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (gameState.gameOver){
                    Intent intent = new Intent(getApplicationContext(), EndActivity.class);
                    intent.putExtra("winner" , gameState.winner.getId());
                    startActivity(intent);
                }

                if (turn && gameState.tableCards.size() != 0) levantarBtt.setEnabled(true);
                cardAdapter.notifyDataSetChanged(); //notificamos que ha cambiado el conjunto de datos del recycler
                cardAdapterOp1.notifyDataSetChanged();
                cardAdapterOp2.notifyDataSetChanged();
                cardAdapterOp3.notifyDataSetChanged();
                //si el array de cartas del tablero no está vacío mostramos el reverso de la carta, si no no mostramos nada
                if (gameState.tableCards.isEmpty()) imgTableCard.setVisibility(View.INVISIBLE);
                else {
                    imgTableCard.setImageResource(R.drawable.reverse);
                    imgTableCard.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    void selectCardView(View cardView) {
        ImageView cv = (ImageView) cardView;
        if (selectedCards.contains((Card) cardView.getTag())) {  //si está dentro das cartas seleccionadas quitoa do array e quitolle o color filter
            selectedCards.remove((Card) cardView.getTag());
            cv.clearColorFilter();
        } else { //se non está engadoa e poñolle o color filter
            selectedCards.add((Card) cardView.getTag());
            cv.setColorFilter(Color.parseColor("#41AFB42B"));
        }

        int i = 0;
        boolean puedeEchar = true;
        for (Card c : selectedCards) {
            for (int j = 0; j < selectedCards.size(); j++) {
                if (selectedCards.get(i).getValue() != selectedCards.get(j).getValue()) {
                    puedeEchar = false;
                }
            }
            i++;
        }
        if (selectedCards.size() == 0) puedeEchar = false;
        if (puedeEchar && turn && (gameState.numeroJugada==0 || selectedCards.get(0).getValue()==gameState.numeroJugada)) echarBtt.setEnabled(true);
        else echarBtt.setEnabled(false);

        i = 0;
        boolean puedeMentir = true;
        /*for (Card c : selectedCards) {
            for (int j = 0; j < selectedCards.size(); j++) {
                if (selectedCards.get(i).getValue() != selectedCards.get(j).getValue()) {
                    puedeMentir = true;
                }
            }
            i++;
        } */
        if (selectedCards.size() == 1) puedeMentir = true;
        if (selectedCards.size() == 0) puedeMentir = false;
        if (puedeMentir && turn) mentirBtt.setEnabled(true);
        else mentirBtt.setEnabled(false);
        Log.i("CARTAS SELECCIONADAS",selectedCards.toString());

    }

    /*
     * Deshabilita botones para que el jugador no pueda tirar/levantar al no ser su turno
     */
    private void setTurnFalse() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mentirBtt.setEnabled(false);
                echarBtt.setEnabled(false);
                levantarBtt.setEnabled(false);
            }
        });
    }

    /*
     * Habilita botones para que el jugador pueda tirar/levantar al ser su turno
     */
    public void setTurnTrue() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                turn = true;
                //mentirBtt.setEnabled(true);
                //echarBtt.setEnabled(true);

                if (gameState.tableCards.size() != 0) levantarBtt.setEnabled(true);
                else levantarBtt.setEnabled(false);
                toast = Toast.makeText(getApplicationContext(), "Es tu turno!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

}
