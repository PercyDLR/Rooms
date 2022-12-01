package com.example.rooms.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.rooms.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class ListaEspaciosActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference ref;

    RecyclerView rvListaUsuarios;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_espacios);

        configurarNavBar();
    }

    public void configurarNavBar(){
        bottomNavigationView = findViewById(R.id.nvEspaciosAdmin);
        bottomNavigationView.setSelectedItemId(R.id.navigation_espacios);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.navigation_espacios:
                    return true;
                case R.id.navigation_usuarios:
                    startActivity(new Intent(getApplicationContext(),ListaUsuariosActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.navigation_cuenta_admin:
                    startActivity(new Intent(getApplicationContext(),CuentaAdminActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });
    }

    public void listarUsuarios(){

    }
}