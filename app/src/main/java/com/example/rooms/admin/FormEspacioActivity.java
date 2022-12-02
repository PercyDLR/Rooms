package com.example.rooms.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.rooms.R;
import com.example.rooms.dto.EspacioDTO;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;


public class FormEspacioActivity extends AppCompatActivity {

    private DatabaseReference ref;
    private TextInputLayout inputNombre, inputDescripcion, inputCreditos;
    private ImageButton btnFoto;
    private Button btnEnviarForm;
    private EspacioDTO espacio;
    private Boolean conFoto = false;

    private int PERMISOS_CODE = 100;
    private int GALERIA_CODE = 101;
    private int CAMARA_CODE = 102;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_espacio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Se piden los permisos para la cámara
        if (checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA},PERMISOS_CODE);
        }

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

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent,GALERIA_CODE);
                    dialogInterface.dismiss();

                })).setNeutralButton("Cancelar",((dialogInterface, i) -> {
                    dialogInterface.cancel();
                })).setPositiveButton("Cámara", ((dialogInterface, i) -> {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,CAMARA_CODE);
                    dialogInterface.dismiss();

                })).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALERIA_CODE && data != null){
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),data.getData());
                btnFoto.setImageBitmap(bitmap);
                conFoto = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMARA_CODE && data != null){
            bitmap = (Bitmap) data.getExtras().get("data");
            btnFoto.setImageBitmap(bitmap);
            conFoto = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==PERMISOS_CODE && grantResults.length > 0){
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
                // Se piden los permisos nuevamente
                if (checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.CAMERA},PERMISOS_CODE);
                }
            }
        }
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