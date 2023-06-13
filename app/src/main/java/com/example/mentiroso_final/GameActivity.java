package com.example.mentiroso_final;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mentiroso_final.game.Card;
import com.example.mentiroso_final.game.Game;
import com.example.mentiroso_final.game.Player;
import com.example.mentiroso_final.CardAdapter;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    Boolean DEBUG = false;
    ArrayList<ImageView> cardViews = new ArrayList<>();
    ArrayList<ImageView> opCardViews = new ArrayList<>();
    ArrayList<ImageView> flippedCardsView = new ArrayList<>();
    static ArrayList<Card> cardsDeck = new ArrayList<>(); //as do mazo (eliminar ao acabar)
    ArrayList<Card> tableCards = new ArrayList<>();
    ArrayList<Card> allCards; //todas
    ImageView deck, imgTableCard;
    Button echarBtt, mentirBtt, levantarBtt;
    Boolean turn = true;
    public static Player player1 = new Player(1);
    public static Player player2 = new Player(2);
    ArrayList<Card> selectedCards = new ArrayList<>();
    Game gameState;
    private CardAdapter cardAdapter;
    RecyclerView recyclerView;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        deck = (ImageView) findViewById(R.id.deck);
        imgTableCard = (ImageView) findViewById(R.id.imgTableCard);


        opCardViews.add(findViewById(R.id.cardOp1));
        opCardViews.add(findViewById(R.id.cardOp2));
        opCardViews.add(findViewById(R.id.cardOp3));
        opCardViews.add(findViewById(R.id.cardOp4));
        opCardViews.add(findViewById(R.id.cardOp5));
        opCardViews.add(findViewById(R.id.cardOp6));


        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_select, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        echarBtt = findViewById(R.id.echarBtt);
        mentirBtt = findViewById(R.id.mentirBtt);
        levantarBtt = findViewById(R.id.levantarBtt);
        echarBtt.setEnabled(false);
        mentirBtt.setEnabled(false);
        levantarBtt.setEnabled(false);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                String textSpinner = spinner.getSelectedItem().toString();
                Log.i("spinner",textSpinner);
                Log.i("spinner", (String) item);
            }
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        //aqui por os setOnClickListeners dos botóns
        levantarBtt.setOnClickListener(v ->{
            Log.i("levantar", String.valueOf(turn));
            turn = false;
            gameState.levantarCarta();

            displayPlayerCards();
            levantarBtt.setEnabled(false);
        });
        echarBtt.setOnClickListener(v ->{
            //imgTableCard.setEnabled(false);
            turn = false;
            gameState.echarCarta(selectedCards, player1);
            selectedCards.clear();
            displayPlayerCards();
            echarBtt.setEnabled(false);
            //habria que deshabilitar el recycler view pero con esto creo que llega
            for(int i = 0; i<recyclerView.getChildCount();i++) { //recorremos todos los elementos del recycler para quitar el colorfilter

                View view = recyclerView.getChildAt(i);
                if (view instanceof LinearLayout) {
                    LinearLayout ly = (LinearLayout) view;
                    ImageView cv = (ImageView) ly.getChildAt(0);
                    cv.clearColorFilter();
                }
            }
        });



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));




        //click listeners restantes




        player1.setPlayerCards(new ArrayList<>());
        player2.setPlayerCards(new ArrayList<>());
        //inicializar baraja
        setDeck();
        //repartir
        distributeCards();
        gameState = new Game(cardsDeck);
        gameState.allCards = new ArrayList<>(this.allCards);
        //mostrar imagenes
        cardAdapter = new CardAdapter(player1.getPlayerCards(), getApplicationContext(), this);
        recyclerView.setAdapter(cardAdapter);
        recyclerView.setEnabled(false);
        displayPlayerCards();




        //click listener de las cartas
        /*for (int i = 0; i < player1.getPlayerCards().size(); i++) {
            cardViews.get(i).setOnClickListener(v -> {
                if (turn) {
                    selectCardView(v);
                }
            });
        } */



    }



    @SuppressLint("DiscouragedApi")
    void setDeck(){
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
        for (int i = 0; i < 6; i++) {
            player1.addCard(cardsDeck.get(0));
            cardsDeck.remove(0);
        }
        for (int i = 0; i < 6; i++) {
            player2.addCard(cardsDeck.get(0));
            cardsDeck.remove(0);
        }
        cardsDeck.remove(0);
    }

    public void displayPlayerCards() {
       /* int i = 0;
        for (Card c : player1.getPlayerCards()) {
            cardViews.get(i).setImageResource(c.getImageId());
            cardViews.get(i).setTag(c);
            i++;
        }
        i = 0;
        for (Card c : player2.getPlayerCards()) {
            if (DEBUG) {
                opCardViews.get(i).setImageResource(c.getImageId());
            } else {
                opCardViews.get(i).setImageResource(R.drawable.reverse);
            }
            opCardViews.get(i).setVisibility(View.VISIBLE);
            i++;
        } */
        if (turn && tableCards.size()!=0) levantarBtt.setEnabled(true);
        cardAdapter.notifyDataSetChanged(); //notificamos que ha cambiado el conjunto de datos del recycler
        //si el array de cartas del tablero no está vacío mostramos el reverso de la carta, si no no mostramos nada
        if(gameState.tableCards.isEmpty()) imgTableCard.setVisibility(View.INVISIBLE);
        else {
            imgTableCard.setImageResource(R.drawable.reverse);
            imgTableCard.setVisibility(View.VISIBLE);
            Log.i("CARTAS EN EL MAZO DE LA MESA", gameState.tableCards.toString());
        }
        Log.i("player1 cards", Integer.toString(player1.getPlayerCards().size()));

    }
    void selectCardView(View cardView) {
        echarBtt.setEnabled(false); //al volver a entrar volvemos inactivo el boton de echar hasta comprobar si puede
        Log.i("TAG de cardView", cardView.getTag().toString());
        ImageView cv = (ImageView) cardView;


        if(selectedCards.contains((Card) cardView.getTag())){  //si está dentro das cartas seleccionadas quitoa do array e quitolle o color filter
            selectedCards.remove((Card) cardView.getTag());
            cv.clearColorFilter();
        }

        else { //se non está engadoa e poñolle o color filter
            selectedCards.add((Card) cardView.getTag());
            cv.setColorFilter(Color.parseColor("#41AFB42B"));
        }
        Log.i("CARTAS SELECCIONADAS", selectedCards.toString());

        /* for(int i = 0; i<recyclerView.getChildCount();i++){

            View view = recyclerView.getChildAt(i);
            if (view instanceof LinearLayout) {
                LinearLayout ly = (LinearLayout) view;
                ImageView cv = (ImageView) ly.getChildAt(0);

               if (cv.getTag() == cardView.getTag()){   //si é a que clico
                    if(selectedCards.contains((Card) cardView.getTag())){  //si está dentro das cartas seleccionadas quitoa do array e quitolle o color filter
                        selectedCards.remove((Card) cardView.getTag());
                        cv.clearColorFilter();
                    }

                    else { //se non está engadoa e poñolle o color filter
                        selectedCards.add((Card) cardView.getTag());
                        cv.setColorFilter(Color.parseColor("#41AFB42B"));
                    }
                    Log.i("CARTAS SELECCIONADAS", selectedCards.toString());

                }
            } */



        /*cardViews.forEach(cv -> {
            if (cv.getId() == cardView.getId()){   //si é a que clico
                if(selectedCards.contains((Card) cardView.getTag())){  //si está dentro das cartas seleccionadas quitoa do array e quitolle o color filter
                   selectedCards.remove((Card) cardView.getTag());
                    cv.clearColorFilter();
                }

                else { //se non está engadoa e poñolle o color filter
                    selectedCards.add((Card) cardView.getTag());
                    cv.setColorFilter(Color.parseColor("#41AFB42B"));
                }
                Log.i("CARTAS SELECCIONADAS", selectedCards.toString());

            }
        }); */
        int i=0;
        boolean puedeEchar = true;
        for (Card c : selectedCards) {
            for (int j = 0; j < selectedCards.size(); j++) {
                if (selectedCards.get(i).getValue() != selectedCards.get(j).getValue() ) {
                    puedeEchar= false;
                }
            }
            i++;
        }
        if(selectedCards.size()==0) puedeEchar=false;
        if(puedeEchar && turn) echarBtt.setEnabled(true);


        //mentirBtt.setEnabled(true);
    }

}
