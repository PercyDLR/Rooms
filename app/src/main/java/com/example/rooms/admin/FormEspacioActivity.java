package com.example.rooms.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.rooms.R;
import com.example.rooms.dto.EspacioDTO;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;


public class FormEspacioActivity extends AppCompatActivity {

    private DatabaseReference ref;
    private TextInputLayout inputNombre, inputDescripcion, inputCreditos;
    private ImageButton btnFoto;
    private Button btnEnviarForm;
    private EspacioDTO espacio;
    private Boolean conFoto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_espacio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        inputNombre = findViewById(R.id.etNombreFormEspacio);
        inputDescripcion = findViewById(R.id.etDescripcionFormEspacio);
        inputCreditos = findViewById(R.id.etCreditosFormEspacio);
        btnFoto = findViewById(R.id.iBtnFotoFormEspacio);
        btnEnviarForm = findViewById(R.id.btnEnviarFormEspacio);

        espacio = (EspacioDTO) getIntent().getSerializableExtra("espacio");

        // Si se manda un espacio para editarlo, se llenan los campos
        if (espacio != null){
            inputNombre.getEditText().setText(espacio.getNombre());
            inputDescripcion.getEditText().setText(espacio.getDescripción());
            inputCreditos.getEditText().setText(espacio.getCreditosPorHora());
            btnEnviarForm.setText("Actualizar Espacio");
            conFoto = true;
        }

    }

    public void obtenerImagen(View view){
        new MaterialAlertDialogBuilder(FormEspacioActivity.this)
                .setTitle("Obtener Imagen")
                .setMessage("Elija de donde quiere obtener su imagen")
                .setNegativeButton("Galeria",((dialogInterface, i) -> {
                    obtenerImgGaleria();
                    dialogInterface.dismiss();
                })).setNeutralButton("Cancelar",((dialogInterface, i) -> {
                    dialogInterface.cancel();
                })).setPositiveButton("Cámara", ((dialogInterface, i) -> {
                    obtenerImgCamara();
                    dialogInterface.dismiss();
                })).show();
    }

    // TODO: Implementar obtener imagen de cámara y de galería
    private void obtenerImgGaleria(){

    }

    private void obtenerImgCamara() {

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                if (espacio != null) {
                    Intent intent = new Intent(FormEspacioActivity.this,DetallesEspacioActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("espacio",espacio);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(FormEspacioActivity.this,ListaEspaciosActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}