package com.example.rooms.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.rooms.R;
import com.example.rooms.usuario.DetallesEspacioUsuario;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void irLogueo (View view){
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }
}