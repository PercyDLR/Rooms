package com.example.rooms.usuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rooms.R;
import com.example.rooms.admin.CuentaAdminActivity;
import com.example.rooms.dto.UsuarioDTO;
import com.example.rooms.login.MainActivity;
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

public class CuentaUsuarioActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference ref;

    BottomNavigationView bottomNavigationView;
    TextView tvNombreCompleto, tvCorreo, tvTI, tvCreditos;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta_usuario);

        // Se setea la progressbar
        progressBar = findViewById(R.id.pbCuentaUsuario);
        progressBar.setVisibility(View.VISIBLE);
        // Se setea la navbar
        configurarNavBar();

        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("usuarios").child(auth.getCurrentUser().getUid());

        tvNombreCompleto = findViewById(R.id.tvNombreCuentaUsuario);
        tvCorreo = findViewById(R.id.tvCorreoCuentaUsuario);
        tvTI = findViewById(R.id.tvTICuentaUsuario);
        tvCreditos = findViewById(R.id.tvCreditosCuentaUsuario);

        // Se llena la información del usuario
        ref.get().addOnCompleteListener(task -> {
           if (task.isSuccessful() && task.getResult().exists()){
               UsuarioDTO user = task.getResult().getValue(UsuarioDTO.class);

               tvNombreCompleto.setText(user.getNombre());
               tvCorreo.setText(user.getCorreo());
               tvTI.setText(user.getTI());

               // Si ya pasó la fecha de la recarga y no se tienen los créditos completos
               Log.d("cuenta", "Prox recarga: " + user.getTimestampSiguienteRecarga() + " , Ahora: " + Instant.now().getEpochSecond());
               if(user.getTimestampSiguienteRecarga() < Instant.now().getEpochSecond()){
                   tvCreditos.setText("100");

                   // Este es el timestamp del próximo lunes a las 00:00
                   Long timestampProxLunes = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atStartOfDay(ZoneId.systemDefault()).toInstant().getEpochSecond();

                   // Se actualizan los creditos y el timestamp en la db
                   Map<String,Object> updates = new HashMap<>();
                   updates.put("timestampSiguienteRecarga",timestampProxLunes);
                   updates.put("creditos",100);
                   ref.updateChildren(updates);
               }
               else {tvCreditos.setText(user.getCreditos().toString());}
           }
        });

        progressBar.setVisibility(View.GONE);
    }

    public void cambiarContrasena(View view){
        startActivity(new Intent(getApplicationContext(),CambiarPwdUsuarioActivity.class));
    }

    public void logout(View view){
        new MaterialAlertDialogBuilder(view.getContext())
                .setTitle("Cerrar Sesión")
                .setMessage("¿Estas seguro de querer cerrar tu sesión actual?")
                .setNegativeButton("Cancelar",((dialogInterface, i) -> {
                    dialogInterface.cancel();
                })).setPositiveButton("Cerrar Sesión", ((dialogInterface, i) -> {
                    auth.signOut();
                    startActivity(new Intent(CuentaUsuarioActivity.this, MainActivity.class));
                    dialogInterface.dismiss();
                    finish();
                })).show();
    }

    public void configurarNavBar(){
        bottomNavigationView = findViewById(R.id.nvCuentaUsuario);
        bottomNavigationView.setSelectedItemId(R.id.navigation_cuenta);

        // TODO: hacer las otras vistas y vincular
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.navigation_inicio:
                    startActivity(new Intent(getApplicationContext(),ListaEspaciosUsuarioActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.navigation_reservas:
//                    startActivity(new Intent(getApplicationContext(),).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                    overridePendingTransition(0,0);
                    return true;
                case R.id.navigation_cuenta:
                    return true;
            }
            return false;
        });
    }
}