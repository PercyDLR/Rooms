package com.example.rooms.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.rooms.R;
import com.example.rooms.usuario.DetallesEspacioUsuario;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextInputLayout etCorreo, etPsw;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etCorreo = findViewById(R.id.etLoginCorreo);
        etPsw = findViewById(R.id.etLoginPswd);
        btnLogin = findViewById(R.id.btnIniciarSesion);

        auth = FirebaseAuth.getInstance();


    }



    public void irUsuario (View view){
        startActivity(new Intent(LoginActivity.this, DetallesEspacioUsuario.class));
    }
}