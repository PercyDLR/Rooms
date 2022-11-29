package com.example.rooms.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rooms.R;
import com.example.rooms.dto.UsuarioDTO;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistroActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference ref;
    private ProgressBar progressBar;
    private TextInputLayout inputNombre, inputApellidos, inputTI, inputCorreo, inputPwd, inputRepPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        progressBar = findViewById(R.id.pbRegister);
        inputNombre = findViewById(R.id.etNombreRegistro);
        inputApellidos = findViewById(R.id.etApellidosRegistro);
        inputTI = findViewById(R.id.etTIRegistro);
        inputCorreo = findViewById(R.id.etCorreoRegistro);
        inputPwd = findViewById(R.id.etPwdRegistro);
        inputRepPwd = findViewById(R.id.etRepetirPwdRegistro);

        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("usuarios");

        progressBar.setVisibility(View.GONE);
    }

    public void registrarse(View view) {

        String nombre = inputCorreo.getEditText().getText().toString().trim();
        String apellidos = inputCorreo.getEditText().getText().toString().trim();
        String ti = inputCorreo.getEditText().getText().toString().trim();
        String correo = inputCorreo.getEditText().getText().toString().trim();
        String pwd = inputPwd.getEditText().getText().toString().trim();
        String rePwd = inputPwd.getEditText().getText().toString().trim();

        if (verificarCampos(nombre,apellidos,ti,correo,pwd,rePwd)){

            // Aqui te registras con Firebase Auth
            auth.createUserWithEmailAndPassword(correo,pwd)
                    .addOnCompleteListener( registro -> {
                        if (registro.isSuccessful()){

                            // Si el registro es exitoso te registras en la DB
                            UsuarioDTO user = new UsuarioDTO(nombre,apellidos,ti,correo,"usuario");

                            ref.child(auth.getCurrentUser().getUid()).setValue(user)
                                    .addOnCompleteListener(guardado -> {

                                        progressBar.setVisibility(View.GONE);
                                        if (guardado.isSuccessful()){
                                            auth.getCurrentUser().sendEmailVerification();
                                            Toast.makeText(RegistroActivity.this, "Registro Exitoso. Proceda a verificar su cuenta", Toast.LENGTH_SHORT).show();
                                            Log.d("registro", "Registro Exitoso. Proceda a verificar su cuenta");
                                            startActivity(new Intent(getApplicationContext(),MainActivity.class));

                                        } else {
                                            Toast.makeText(RegistroActivity.this, guardado.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            Log.e("registro", guardado.getException().getMessage());
                                        }
                                    });

                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegistroActivity.this, registro.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("registro", registro.getException().getMessage());
                        }
                    });
        }
        else {progressBar.setVisibility(View.GONE);}
    }

    private boolean verificarCampos(String nombre, String apellidos, String ti, String correo, String pwd, String rePwd){

        progressBar.setVisibility(View.VISIBLE);
        // Se verifican los campos y se ponen alertas

        if (nombre.equals("")){ inputNombre.setError("El nombre no puede estar vacío"); return false;}
        if (apellidos.equals("")){ inputApellidos.setError("El apellido no puede estar vacío"); return false;}
        if (!ti.matches("^[0-9]{8}$")){ inputTI.setError("El TI debe ser un número de 8 dígitos"); return false;}
        if (!correo.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){ inputCorreo.setError("El correo ingresado no es válido"); return false;}
        if (!pwd.equals(rePwd)){
            inputPwd.setError("Las contraseñas deben ser iguales");
            inputRepPwd.setError("Las contraseñas deben ser iguales");
            return false;
        } else if (!pwd.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[*.!¡¿?@$%^&~_+-=]).{8,}$")){
            inputPwd.setError("La contraseña debe tener al menos 8 dígitos, un número y un caracter especial");
            inputRepPwd.setError("La contraseña debe tener al menos 8 dígitos, un número y un caracter especial");
            return false;
        }

        return true;
    }
}