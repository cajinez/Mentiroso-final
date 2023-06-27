package com.example.mentiroso_final;

import androidx.appcompat.app.AppCompatActivity;
import com.example.mentiroso_final.game.Card;
import com.example.mentiroso_final.game.Game;
import com.example.mentiroso_final.game.Player;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {
    Game gameState;
    Button bttEnd;
    TextView textEnd;
    ImageView imageViewEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        bttEnd = findViewById(R.id.bttEnd);
        textEnd = findViewById(R.id.textEnd);
        imageViewEnd = findViewById(R.id.imageViewEnd);
        Intent intent = getIntent();
        int winner = intent.getIntExtra("winner", 0);

        if (winner != 1){
            imageViewEnd.setImageResource(R.drawable.derrota);
            textEnd.setText("El jugador "+winner+" ha ganado.");
        }
        else {
            imageViewEnd.setImageResource(R.drawable.victoria);
            textEnd.setText("Enhorabuena, has ganado!");
        }

        bttEnd.setOnClickListener(v -> {
            Intent intent2 = new Intent(v.getContext(), MainActivity.class);
            startActivity(intent2);
        });
    }


}