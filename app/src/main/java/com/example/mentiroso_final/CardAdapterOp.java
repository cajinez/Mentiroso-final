package com.example.mentiroso_final;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mentiroso_final.GameActivity;


import com.example.mentiroso_final.game.Card;
import com.example.mentiroso_final.game.Game;

import java.util.ArrayList;
import java.util.List;

public class CardAdapterOp extends RecyclerView.Adapter<CardAdapterOp.ViewHolder>  {
    private GameActivity juegoActividad = new GameActivity();
    private ArrayList<Card> cardList;
    private Context context;
    public CardAdapterOp(){

    }
    public CardAdapterOp(ArrayList<Card> cardList, Context context, GameActivity activity) {
        this.cardList = cardList;
        this.context = context;
        this.juegoActividad = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Card card = cardList.get(position);
        holder.imageCard.setTag(card);
        // Aquí, configura la imagen correspondiente según la carta
        holder.imageCard.setImageResource(R.drawable.reverse);

    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCard = itemView.findViewById(R.id.image_card);
        }
    }

}