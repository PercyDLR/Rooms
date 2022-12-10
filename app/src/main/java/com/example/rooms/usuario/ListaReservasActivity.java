package com.example.rooms.usuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.rooms.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListaReservasActivity extends AppCompatActivity {

    private DatabaseReference ref;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_reservas);

        ref = FirebaseDatabase.getInstance().getReference("espacios");

        configurarNavBar();
    }




    public void configurarNavBar(){
        bottomNavigationView = findViewById(R.id.nvReservasUsuario);
        bottomNavigationView.setSelectedItemId(R.id.navigation_reservas);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.navigation_inicio:
                    startActivity(new Intent(getApplicationContext(),ListaEspaciosUsuarioActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.navigation_reservas:
                    return true;
                case R.id.navigation_cuenta:
                    startActivity(new Intent(getApplicationContext(), CuentaUsuarioActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });
    }
}