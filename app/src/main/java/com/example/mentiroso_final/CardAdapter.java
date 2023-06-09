package com.example.mentiroso_final;

import android.content.Context;
import android.graphics.Color;
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

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>  {
    private GameActivity juegoActividad = new GameActivity();
    private ArrayList<Card> cardList;
    private Context context;
    public CardAdapter(){

    }
    public CardAdapter(ArrayList<Card> cardList, Context context, GameActivity activity) {
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
        holder.imageCard.setImageResource(card.getImageId());

        holder.imageCard.setOnClickListener(v -> {
            // Realiza el registro (Log) cuando se hace clic en la imagen
            juegoActividad.selectCardView(v);

        });
        //si no esta seleccionada quitamos el filtro de color
        if (! juegoActividad.selectedCards.contains((Card) holder.imageCard.getTag())) holder.imageCard.clearColorFilter();
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
