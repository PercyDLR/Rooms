package com.example.rooms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.rooms.R;
import com.example.rooms.dto.EspacioDTO;
import com.example.rooms.dto.UsuarioDTO;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

public class DetallesEspacioActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference ref;

    private TextView tvNombre, tvDescripcion, tvCreditosRequeridos, tvCreditosRestantes;
    private LinearLayout llCreditosRestantes;
    private ImageButton btnAgregarDisponibilidad;
    private ImageView ivFoto;
    private EspacioDTO espacio;
    private String funcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_espacio);

        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();

        espacio = (EspacioDTO) getIntent().getSerializableExtra("espacio");
        funcion = getIntent().getStringExtra("funcion");
        funcion = funcion==null ? "" : funcion;

        tvNombre = findViewById(R.id.tvNombreEspacioDetalles);
        tvDescripcion = findViewById(R.id.tvDescripcionEspacioDetalles);
        tvCreditosRequeridos = findViewById(R.id.tvCreditosRequeridosEspacioDetalles);
        tvCreditosRestantes = findViewById(R.id.tvCreditosRestantesEspacioDetalles);
        btnAgregarDisponibilidad = findViewById(R.id.btnAgregarDisponibilidad);

        ivFoto = findViewById(R.id.ivFotoEspacioDetalles);
        llCreditosRestantes = findViewById(R.id.llCreditosRestantesEspacioDetalles);

        Picasso.get().load(espacio.getImgUrl()).into(ivFoto);
        tvNombre.setText(espacio.getNombre());
        tvDescripcion.setText(espacio.getDescripcion());
        tvCreditosRequeridos.setText(espacio.getCreditosPorHora()+ " / HORA");

        manejarVistas();
    }

    public void manejarVistas(){
        switch (funcion){
            // Caso de ver una reserva ya hecha
            case "reserva":
                break;
            // Caso de ver detalles desde admin
            case "admin":

                break;
            default:
                llCreditosRestantes.setVisibility(View.VISIBLE);

                // Se llena la información del usuario
                ref.child("usuarios/"+auth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()){
                        UsuarioDTO user = task.getResult().getValue(UsuarioDTO.class);

                        // Si ya pasó la fecha de la recarga y no se tienen los créditos completos
                        Log.d("cuenta", "Prox recarga: " + user.getTimestampSiguienteRecarga() + " , Ahora: " + Instant.now().getEpochSecond());
                        if(user.getTimestampSiguienteRecarga() < Instant.now().getEpochSecond()){
                            tvCreditosRestantes.setText("100");

                            // Este es el timestamp del próximo lunes a las 00:00
                            Long timestampProxLunes = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atStartOfDay(ZoneId.systemDefault()).toInstant().getEpochSecond();

                            // Se actualizan los creditos y el timestamp en la db
                            Map<String,Object> updates = new HashMap<>();
                            updates.put("timestampSiguienteRecarga",timestampProxLunes);
                            updates.put("creditos",100);
                            ref.child("usuarios/"+auth.getCurrentUser().getUid()).updateChildren(updates);
                        }
                        else {tvCreditosRestantes.setText(user.getCreditos().toString());}
                    }
                });
        }
        // TODO: Disponibilidad y Hora
    }


    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        if (funcion.equals("admin")) {
            getMenuInflater().inflate(R.menu.opt_espacio_menu, menu);
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.opt_editar:
                Intent intent = new Intent(DetallesEspacioActivity.this,FormEspacioActivity.class);
                intent.putExtra("espacio",espacio);
                startActivity(intent);
                return true;
            case R.id.opt_eliminar:
                new MaterialAlertDialogBuilder(DetallesEspacioActivity.this)
                        .setTitle("Eliminar Espacio")
                        .setMessage("¿Estas seguro de querer eleiminar este espacio? Esta acción no podrá ser revertida")
                        .setNegativeButton("Cancelar",((dialogInterface, i) -> {
                            dialogInterface.cancel();
                        })).setPositiveButton("Eliminar", ((dialogInterface, i) -> {

                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("usuarios").child(espacio.getKey());
                            Map<String,Object> updates = new HashMap<>();
                            updates.put("activo",false);
                            ref.updateChildren(updates);
                            dialogInterface.dismiss();
                            startActivity(new Intent(getApplicationContext(),CuentaAdminActivity.class));
                            finish();

                        })).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void agregarDisponibilidad (View view){
        Intent intent = new Intent(DetallesEspacioActivity.this,FormEspacioActivity.class);
        intent.putExtra("espacio",espacio);
        startActivity(intent);
    }
}