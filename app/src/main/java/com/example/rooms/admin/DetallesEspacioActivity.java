package com.example.rooms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rooms.R;
import com.example.rooms.dto.EspacioDTO;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DetallesEspacioActivity extends AppCompatActivity {

    private TextView etNombre, etDescripcion, etCreditosRequeridos;
    private ImageView ivFoto;
    EspacioDTO espacio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_espacio);

        etNombre = findViewById(R.id.tvNombreEspacioDetallesAdmin);
        ivFoto = findViewById(R.id.ivFotoEspacioDetallesAdmin);
        etDescripcion = findViewById(R.id.tvDescripcionEspacioDetallesAdmin);
        etCreditosRequeridos = findViewById(R.id.tvCreditosEspacioDetallesAdmin);

        espacio = (EspacioDTO) getIntent().getSerializableExtra("espacio");

        etNombre.setText(espacio.getNombre());
        etDescripcion.setText(espacio.getDescripción());
        etCreditosRequeridos.setText(espacio.getCreditosPorHora()+ "/ HORA");

        // TODO: Disponibilidad y Hora
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.opt_espacio_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.opt_editar:
                Intent intent = new Intent(DetallesEspacioActivity.this,FormEspacioActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
}