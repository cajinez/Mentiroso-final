package com.example.mentiroso_final;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mentiroso_final.game.Card;
import com.example.mentiroso_final.game.Player;

import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends AppCompatActivity {

    Boolean DEBUG = false;
    ArrayList<ImageView> cardViews = new ArrayList<>();
    ArrayList<ImageView> opCardViews = new ArrayList<>();
    ArrayList<ImageView> flippedCardsView = new ArrayList<>();
    static ArrayList<Card> cardsDeck = new ArrayList<>();
    ArrayList<Card> allCards;
    ImageView deck;
    Button echarBtt, mentirBtt;
    Boolean turn = true;
    Player player1 = new Player(1);
    Player player2 = new Player(2);
    Card selectedCard;
    ArrayList<Card> selectedCards = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        deck = (ImageView) findViewById(R.id.deck);
        cardViews.add(findViewById(R.id.card1));
        cardViews.add(findViewById(R.id.card2));
        cardViews.add(findViewById(R.id.card3));
        cardViews.add(findViewById(R.id.card4));
        cardViews.add(findViewById(R.id.card5));
        cardViews.add(findViewById(R.id.card6));
        opCardViews.add(findViewById(R.id.cardOp1));
        opCardViews.add(findViewById(R.id.cardOp2));
        opCardViews.add(findViewById(R.id.cardOp3));
        opCardViews.add(findViewById(R.id.cardOp4));
        opCardViews.add(findViewById(R.id.cardOp5));
        opCardViews.add(findViewById(R.id.cardOp6));

        echarBtt = findViewById(R.id.echarBtt);
        mentirBtt = findViewById(R.id.mentirBtt);
        echarBtt.setEnabled(false);
        mentirBtt.setEnabled(false);


        //aqui por os setOnClickListeners dos botóns

        //click listener de las cartas
        for (int i = 0; i < 6; i++) {
            cardViews.get(i).setOnClickListener(v -> {
                if (turn ) {
                    selectCardView(v);
                }
            });
        }

        //click listeners restantes




        player1.setPlayerCards(new ArrayList<>());
        player2.setPlayerCards(new ArrayList<>());
        //inicializar baraja
        setDeck();
        //repartir
        distributeCards();
        //mostrar imagenes
        displayPlayerCards();

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
        int i = 0;
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
        }
        cardViews.forEach(ImageView::clearColorFilter);
    }
    void selectCardView(View cardView) {
        echarBtt.setEnabled(false); //al volver a entrar volvemos inactivo el boton de echar hasta comprobar si puede
        cardViews.forEach(cv -> {
            if (cv.getId() == cardView.getId()){   //si é a que clico
                if(selectedCards.contains((Card) cardView.getTag())){  //si está dentro das cartas seleccionadas quitoa do array e quitolle o color filter
                   selectedCards.remove((Card) cardView.getTag());
                    cv.clearColorFilter();
                }

                else { //se non está engadoa e poñolle o color filter
                    selectedCards.add((Card) cardView.getTag());
                    cv.setColorFilter(Color.parseColor("#41AFB42B"));
                }
                //Log.i("INFO",Integer.toString(selectedCards.get(0).getValue()));

            }
        });
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
        if(puedeEchar) echarBtt.setEnabled(true);
        //mentirBtt.setEnabled(true);
    }
}
