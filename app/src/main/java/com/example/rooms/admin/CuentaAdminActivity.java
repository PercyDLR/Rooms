package com.example.rooms.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.rooms.R;
import com.example.rooms.dto.UsuarioDTO;
import com.example.rooms.login.MainActivity;
import com.example.rooms.usuario.CuentaUsuarioActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

public class CuentaAdminActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference ref;

    BottomNavigationView bottomNavigationView;
    TextView tvNombreCompleto, tvCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta_admin);

        // Se setea la navbar
        configurarNavBar();

        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("usuarios").child(auth.getCurrentUser().getUid());

        tvNombreCompleto = findViewById(R.id.tvNombreCuentaAdmin);
        tvCorreo = findViewById(R.id.tvCorreoCuentaAdmin);

        // Se llena la información del usuario
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()){
                UsuarioDTO user = task.getResult().getValue(UsuarioDTO.class);

                tvNombreCompleto.setText(user.getNombre());
                tvCorreo.setText(user.getCorreo());
            }
        });

    }

    public void configurarNavBar(){
        bottomNavigationView = findViewById(R.id.nvCuentaAdmin);
        bottomNavigationView.setSelectedItemId(R.id.navigation_cuenta_admin);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.navigation_espacios:
                    startActivity(new Intent(getApplicationContext(),ListaEspaciosActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.navigation_usuarios:
                    startActivity(new Intent(getApplicationContext(),ListaUsuariosActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.navigation_cuenta_admin:
                    return true;
            }
            return false;
        });
    }

    public void logout(View view){
        new MaterialAlertDialogBuilder(view.getContext())
                .setTitle("Cerrar Sesión")
                .setMessage("¿Estas seguro de querer cerrar tu sesión actual?")
                .setNegativeButton("Cancelar",((dialogInterface, i) -> {
                    dialogInterface.cancel();
                })).setPositiveButton("Cerrar Sesión", ((dialogInterface, i) -> {
                    auth.signOut();
                    startActivity(new Intent(CuentaAdminActivity.this, MainActivity.class));
                    dialogInterface.dismiss();
                    finish();
                })).show();
    }
}