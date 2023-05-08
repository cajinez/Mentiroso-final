package com.example.mentiroso_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnPlay = findViewById(R.id.btnPlay);

        btnPlay.setOnClickListener(v -> {
            //Create the Intent
            Intent intent = new Intent(v.getContext(), GameActivity.class);
            //Añadimos al intent la información a pasar entre actividades
            //intent.putExtra("NOMBRE", txtNombre.text.toString())
            //Iniciamos la nueva actividad
            startActivity(intent);
        });

    }
}