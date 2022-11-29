package com.example.rooms.usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rooms.R;
import com.example.rooms.login.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CuentaUsuarioActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference ref;

    TextView tvNombreCompleto, tvCorreo, tvTI;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta_usuario);

        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("usuarios").child(auth.getCurrentUser().getUid());

        progressBar = findViewById(R.id.pbCuentaUsuario);
        tvNombreCompleto = findViewById(R.id.tvNombreCuentaUsuario);
        tvCorreo = findViewById(R.id.tvCorreoCuentaUsuario);
        tvTI = findViewById(R.id.tvTICuentaUsuario);

        // TODO: consultar la DB y obtener esos datos

        progressBar.setVisibility(View.GONE);
    }

    public void cambiarContrasena(View view){

    }

    public void logout(View view){
        auth.signOut();
        startActivity(new Intent(CuentaUsuarioActivity.this, MainActivity.class));
        finish();
    }

    // TODO: implemetar lógica del bottomNavBar
}