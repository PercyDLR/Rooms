package com.example.rooms.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rooms.R;
import com.example.rooms.dto.EspacioDTO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.UUID;


public class FormEspacioActivity extends AppCompatActivity {

    private StorageReference imgRef;

    private TextInputLayout inputNombre, inputDescripcion, inputCreditos;
    private ImageView btnFoto;
    private Button btnEnviarForm;
    private EspacioDTO espacio;
    private Boolean conFoto = false;

    private final int PERMISOS_CODE = 100;
    private final int GALERIA_CODE = 101;
    private final int CAMARA_CODE = 102;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_espacio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Agregar Espacio");

        espacio = (EspacioDTO) getIntent().getSerializableExtra("espacio");


        // Se piden los permisos para la cámara
        if (checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA},PERMISOS_CODE);
        }

        inputNombre = findViewById(R.id.etNombreFormEspacio);
        inputDescripcion = findViewById(R.id.etDescripcionFormEspacio);
        inputCreditos = findViewById(R.id.etCreditosFormEspacio);
        btnFoto = findViewById(R.id.iBtnFotoFormEspacio);
        btnEnviarForm = findViewById(R.id.btnEnviarFormEspacio);

        // Si se manda un espacio para editarlo, se llenan los campos
        if (espacio != null){
            setTitle("Editar Espacio");
            inputNombre.getEditText().setInputType(InputType.TYPE_NULL);
            inputNombre.getEditText().setText(espacio.getNombre());
            inputDescripcion.getEditText().setText(espacio.getDescripcion());
            inputCreditos.getEditText().setText(espacio.getCreditosPorHora().toString());
            Picasso.get().load(espacio.getImgUrl()).into(btnFoto);
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
                btnFoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
                conFoto = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMARA_CODE && data != null){
            bitmap = (Bitmap) data.getExtras().get("data");
            btnFoto.setImageBitmap(bitmap);
            btnFoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
            conFoto = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==PERMISOS_CODE && grantResults.length > 0){
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
                // Se piden los permisos para la cámara
                if (checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.CAMERA},PERMISOS_CODE);
                    Toast.makeText(FormEspacioActivity.this,"Debe aceptar los permisos para continuar",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FormEspacioActivity.this,DetallesEspacioActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("espacio",espacio);
                    startActivity(intent);
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
                    intent.putExtra("funcion","admin");
                    startActivity(intent);
                } else {
                    startActivity(new Intent(FormEspacioActivity.this,ListaEspaciosActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void enviarForm(View view) throws IOException {

        boolean datosValidos = true;

        String nombre = inputNombre.getEditText().getText().toString().trim();
        String descripcion = inputDescripcion.getEditText().getText().toString().trim();
        String creditos = inputCreditos.getEditText().getText().toString().trim();

        // Limpia los errores previos
        inputNombre.setError(null);
        inputDescripcion.setError(null);
        inputCreditos.setError(null);

        // Analiza los errores
        if(nombre.equals("")){
            inputNombre.setError("Debe ingresar un nombre");
            datosValidos = false;
        } if (descripcion.equals("")){
            datosValidos = false;
            inputDescripcion.setError("Debe ingresar una descripcion");
        } if (creditos.equals("")){
            datosValidos = false;
            inputCreditos.setError("Debe ingresar el costo en créditos");
        }

        if (datosValidos){

            // Se verifica que se agregara una foto
            if (!conFoto){
                Toast.makeText(FormEspacioActivity.this, "Debe ingresar una imagen", Toast.LENGTH_SHORT).show();
                return;
            }

            Bitmap bitmapFoto = ((BitmapDrawable) btnFoto.getDrawable()).getBitmap();
            EspacioDTO newEspacio = new EspacioDTO(nombre,descripcion,Integer.parseInt(creditos),
                    espacio != null ? espacio.getHorariosDisponibles() : 0,true);

            // Si no se cambió ningun campo al editar
            if (espacio != null && nombre.equals(espacio.getNombre()) && descripcion.equals(espacio.getDescripcion())
            && Integer.parseInt(creditos) == espacio.getCreditosPorHora() && bitmapFoto.getByteCount() == espacio.getImgSize() ){
                Toast.makeText(FormEspacioActivity.this, "Debe editar al menos 1 campo", Toast.LENGTH_SHORT).show();
                Log.d("formEspacio", "Tamaño Imagen: "+bitmapFoto.getByteCount());
            }
            // Si es un nuevo espacio o se cambió la foto, esta se subirá
            else if (espacio == null || bitmapFoto.getByteCount() != espacio.getImgSize()){

                 if (espacio==null){
                    String key = FirebaseDatabase.getInstance().getReference("espacios").push().getKey();
                    imgRef = FirebaseStorage.getInstance().getReference("img/"+key+".webp");
                    newEspacio.setKey(key);
                } else {
                    imgRef = FirebaseStorage.getInstance().getReference("img/"+espacio.getKey()+".webp");
                    newEspacio.setKey(espacio.getKey());

                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmapFoto.compress(Bitmap.CompressFormat.WEBP,50,baos);

                imgRef.putBytes(baos.toByteArray()).continueWithTask(task -> {
                    if  (!task.isSuccessful()) {
                        Log.e("formEspacio", task.getException().getMessage());
                        Toast.makeText(FormEspacioActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    return imgRef.getDownloadUrl();

                }).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        newEspacio.setImgSize(bitmapFoto.getByteCount());
                        newEspacio.setImgUrl(task.getResult().toString());
                        mandarAPI(newEspacio);
                    }
                    else {
                        Toast.makeText(FormEspacioActivity.this, "Hubo un problema al subir la imagen", Toast.LENGTH_SHORT).show();
                        Log.e("formEspacio", task.getException().getMessage());
                    }
                });
            }
            // Caso donde la imagen no se cambia
            else {
                newEspacio.setKey(espacio.getKey());
                newEspacio.setImgUrl(espacio.getImgUrl());
                newEspacio.setImgSize(espacio.getImgSize());
                mandarAPI(newEspacio);
            }
        }

    }

    public void mandarAPI(EspacioDTO nuevoEspacio){

        Log.d("formEspacio", "key a almacenar: " + nuevoEspacio.getKey());

        FirebaseFunctions.getInstance().getHttpsCallable("guardarEspacio").call(nuevoEspacio.toMap())
                .addOnSuccessListener(new OnSuccessListener<HttpsCallableResult>() {

            @Override
            public void onSuccess(HttpsCallableResult httpsCallableResult) {
                Toast.makeText(FormEspacioActivity.this, espacio!=null? "Espacio modificado Exitosamente":"Espacio agregado exitosamente", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(FormEspacioActivity.this, DetallesEspacioActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("espacio", nuevoEspacio);
                intent.putExtra("funcion","admin");
                startActivity(intent);
            }
        }).addOnFailureListener(e -> {
                Log.e("formEspacio",e.getMessage());
                Toast.makeText(FormEspacioActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}