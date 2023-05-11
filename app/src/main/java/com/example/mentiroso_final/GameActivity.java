package com.example.mentiroso_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.example.mentiroso_final.game.Card;
import com.example.mentiroso_final.game.Player;

public class GameActivity extends AppCompatActivity {

    ArrayList<ImageView> cardViews = new ArrayList<>();
    ArrayList<ImageView> opCardViews = new ArrayList<>();
    ArrayList<ImageView> flippedCardsView = new ArrayList<>();
    static ArrayList<Card> cardsDeck = new ArrayList<>();
    ArrayList<Card> allCards;
    ImageView deck;
    Button echarBtt, mentirBtt;
    Boolean turn;
    Player player1 = new Player(1);
    Player player2 = new Player(2);
    //ArrayList<Card> selectedCards = new ArrayList<>();


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

        for(int i=0;i<6;i++){
            cardViews.get(i).setVisibility(View.INVISIBLE);
            opCardViews.get(i).setVisibility(View.INVISIBLE);
        }

        //aqui por os seOnClickListeners dos botóns

        //click listener de las cartas
        /* for (int i = 0; i < 8; i++) {
            cardViews.get(i).setOnLongClickListener(listenClick);
            cardViews.get(i).setOnDragListener(listenDrag);
            cardViews.get(i).setOnClickListener(v -> {
                if (turn && player1.getPlayerCards().size() == 8) {
                    selectCardView(v);
                }
            });
        } */

        //click listeners restantes

        //inicializar baraja
        setDeck();

        cards = new ArrayList<>();

        cards.add(101);
        cards.add(102);
        cards.add(103);
        cards.add(104);
        cards.add(105);
        cards.add(106);
        cards.add(107);
        cards.add(110);
        cards.add(111);
        cards.add(112);
        cards.add(201);
        cards.add(202);
        cards.add(203);
        cards.add(204);
        cards.add(205);
        cards.add(206);
        cards.add(207);
        cards.add(210);
        cards.add(211);
        cards.add(212);
        cards.add(301);
        cards.add(302);
        cards.add(303);
        cards.add(304);
        cards.add(305);
        cards.add(306);
        cards.add(307);
        cards.add(310);
        cards.add(311);
        cards.add(312);
        cards.add(401);
        cards.add(402);
        cards.add(403);
        cards.add(404);
        cards.add(405);
        cards.add(406);
        cards.add(407);
        cards.add(410);
        cards.add(411);
        cards.add(412);


    deck.setOnClickListener (new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //barajar
            Collections.shuffle(cards);
            //repartir
            assignImage(cards.get(0), card1);
            assignImage(cards.get(1), card2);
            assignImage(cards.get(2), card3);
            assignImage(cards.get(3), card4);
            assignImage(cards.get(4), card5);
            assignImage(cards.get(5), card6);
            assignImage(cards.get(6), cardOp1);
            assignImage(cards.get(7), cardOp2);
            assignImage(cards.get(8), cardOp3);
            assignImage(cards.get(9), cardOp4);
            assignImage(cards.get(10), cardOp5);
            assignImage(cards.get(11), cardOp6);

            card1.setVisibility(View.VISIBLE);
            card2.setVisibility(View.VISIBLE);
            card3.setVisibility(View.VISIBLE);
            card4.setVisibility(View.VISIBLE);
            card5.setVisibility(View.VISIBLE);
            card6.setVisibility(View.VISIBLE);
            cardOp1.setVisibility(View.VISIBLE);
            cardOp2.setVisibility(View.VISIBLE);
            cardOp3.setVisibility(View.VISIBLE);
            cardOp4.setVisibility(View.VISIBLE);
            cardOp5.setVisibility(View.VISIBLE);
            cardOp6.setVisibility(View.VISIBLE);
        }
        });
    }

    public void assignImage (int card, ImageView image) {
        switch (card) {
            case 101:
                image.setImageResource(R.drawable.bastos_01);
                break;
            case 102:
                image.setImageResource(R.drawable.bastos_02);
                break;
            case 103:
                image.setImageResource(R.drawable.bastos_03);
                break;
            case 104:
                image.setImageResource(R.drawable.bastos_04);
                break;
            case 105:
                image.setImageResource(R.drawable.bastos_05);
                break;
            case 106:
                image.setImageResource(R.drawable.bastos_06);
                break;
            case 107:
                image.setImageResource(R.drawable.bastos_07);
                break;
            case 110:
                image.setImageResource(R.drawable.bastos_10);
                break;
            case 111:
                image.setImageResource(R.drawable.bastos_11);
                break;
            case 112:
                image.setImageResource(R.drawable.bastos_12);
                break;
            case 201:
                image.setImageResource(R.drawable.copas_01);
                break;
            case 202:
                image.setImageResource(R.drawable.copas_02);
                break;
            case 203:
                image.setImageResource(R.drawable.copas_03);
                break;
            case 204:
                image.setImageResource(R.drawable.copas_04);
                break;
            case 205:
                image.setImageResource(R.drawable.copas_05);
                break;
            case 206:
                image.setImageResource(R.drawable.copas_06);
                break;
            case 207:
                image.setImageResource(R.drawable.copas_07);
                break;
            case 210:
                image.setImageResource(R.drawable.copas_10);
                break;
            case 211:
                image.setImageResource(R.drawable.copas_11);
                break;
            case 212:
                image.setImageResource(R.drawable.copas_12);
                break;
            case 301:
                image.setImageResource(R.drawable.espadas_01);
                break;
            case 302:
                image.setImageResource(R.drawable.espadas_02);
                break;
            case 303:
                image.setImageResource(R.drawable.espadas_03);
                break;
            case 304:
                image.setImageResource(R.drawable.espadas_04);
                break;
            case 305:
                image.setImageResource(R.drawable.espadas_05);
                break;
            case 306:
                image.setImageResource(R.drawable.espadas_06);
                break;
            case 307:
                image.setImageResource(R.drawable.espadas_07);
                break;
            case 310:
                image.setImageResource(R.drawable.espadas_10);
                break;
            case 311:
                image.setImageResource(R.drawable.espadas_11);
                break;
            case 312:
                image.setImageResource(R.drawable.espadas_12);
                break;
            case 401:
                image.setImageResource(R.drawable.oros_01);
                break;
            case 402:
                image.setImageResource(R.drawable.oros_02);
                break;
            case 403:
                image.setImageResource(R.drawable.oros_03);
                break;
            case 404:
                image.setImageResource(R.drawable.oros_04);
                break;
            case 405:
                image.setImageResource(R.drawable.oros_05);
                break;
            case 406:
                image.setImageResource(R.drawable.oros_06);
                break;
            case 407:
                image.setImageResource(R.drawable.oros_07);
                break;
            case 410:
                image.setImageResource(R.drawable.oros_10);
                break;
            case 411:
                image.setImageResource(R.drawable.oros_11);
                break;
            case 412:
                image.setImageResource(R.drawable.oros_12);
                break;
        }
    }
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
        for (int i = 0; i < 7; i++) {
            player1.addCard(cardsDeck.get(0));
            cardsDeck.remove(0);
        }
        for (int i = 0; i < 7; i++) {
            player2.addCard(cardsDeck.get(0));
            cardsDeck.remove(0);
        }
        cardsDeck.remove(0);
    }
}