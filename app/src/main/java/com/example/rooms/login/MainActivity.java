package com.example.rooms.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rooms.R;
import com.example.rooms.admin.CuentaAdminActivity;
import com.example.rooms.dto.UsuarioDTO;
import com.example.rooms.usuario.CuentaUsuarioActivity;
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

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference ref;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("usuarios");
        progressBar = findViewById(R.id.pbMain);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Si ya existe una cuenta verificada guardada, se saltea esta vista
        if (auth.getCurrentUser() != null && auth.getCurrentUser().isEmailVerified()){

            progressBar.setVisibility(View.VISIBLE);

            // Se busca la cuenta en Realtime DB
            ref.child(auth.getCurrentUser().getUid()).get()
                    .addOnCompleteListener(task -> {

                        progressBar.setVisibility(View.GONE);
                        // Si se encuentra algo en la búsqueda
                        if (task.isSuccessful() && task.getResult().exists()){

                            UsuarioDTO user = task.getResult().getValue(UsuarioDTO.class);

                            // Se redirige por rol
                            if (user.getRol().equals("admin")){
                                Log.d("main-logueo", "Logueo Exitoso: admin");
                                startActivity(new Intent(getApplicationContext(), CuentaAdminActivity.class));
                                finish();
                            } else if (user.getRol().equals("usuario")) {
                                Log.d("main-logueo", "Logueo Exitoso: usuario");
                                recargarCreditos(user);
                                startActivity(new Intent(getApplicationContext(), CuentaUsuarioActivity.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),"No se pudo obtener el rol",Toast.LENGTH_SHORT).show();
                                Log.e("main-logueo", "No se pudo obtener el rol");
                            }

                        }
                    });
        }
    }

    public void irLogueo (View view){
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }
    public void irRegistro (View view){
        startActivity(new Intent(MainActivity.this, RegistroActivity.class));
    }

    public void recargarCreditos(UsuarioDTO user){
        // Si ya pasó la fecha de la recarga y no se tienen los créditos completos
        Log.d("cuenta", "Prox recarga: " + user.getTimestampSiguienteRecarga() + " , Ahora: " + Instant.now().getEpochSecond());
        if(user.getTimestampSiguienteRecarga() < Instant.now().getEpochSecond()){

            // Este es el timestamp del próximo lunes a las 00:00
            Long timestampProxLunes = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atStartOfDay(ZoneId.systemDefault()).toInstant().getEpochSecond();

            // Se actualizan los creditos y el timestamp en la db
            Map<String,Object> updates = new HashMap<>();
            updates.put("timestampSiguienteRecarga",timestampProxLunes);
            updates.put("creditos",100);
            ref.updateChildren(updates);
        }
    }
}