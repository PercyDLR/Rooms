package com.example.rooms.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rooms.R;
import com.example.rooms.dto.UsuarioDTO;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;

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
        inputPwd.setErrorIconDrawable(null);
        inputRepPwd.setErrorIconDrawable(null);

        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("usuarios");

        progressBar.setVisibility(View.GONE);
    }

    public void registrarse(View view) {

        String nombre = inputNombre.getEditText().getText().toString().trim();
        String apellidos = inputApellidos.getEditText().getText().toString().trim();
        String ti = inputTI.getEditText().getText().toString().trim();
        String correo = inputCorreo.getEditText().getText().toString().trim();
        String pwd = inputPwd.getEditText().getText().toString().trim();
        String rePwd = inputRepPwd.getEditText().getText().toString().trim();

        if (verificarCampos(nombre,apellidos,ti,correo,pwd,rePwd)){

            // Se analiza si el TI ya está registrado
            ref.orderByChild("ti").equalTo(ti).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult().exists()){
                            progressBar.setVisibility(View.GONE);
                            inputTI.setError("El TI ya se encuentra registrado");
                            Log.e("registro", "El TI ya se encuentra registrado");
                        }

                        // Si no lo está, se procede con el registro
                        else {
                            // Aqui te registras con Firebase Auth
                            auth.createUserWithEmailAndPassword(correo,pwd)
                                    .addOnCompleteListener( registro -> {
                                        if (registro.isSuccessful()){

                                            // Este es el timestamp del próximo lunes a las 00:00
                                            Long timestampProxLunes = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atStartOfDay(ZoneId.systemDefault()).toInstant().getEpochSecond();

                                            // Si el registro es exitoso te registras en la DB
                                            UsuarioDTO user = new UsuarioDTO(nombre+" "+apellidos,correo,ti,"usuario",100, timestampProxLunes,true);

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

                                        } // Esto aparece si el correo ya estaba registrado
                                        else {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(RegistroActivity.this, "El correo ya se encuentra en uso", Toast.LENGTH_SHORT).show();
                                            inputCorreo.setError("El correo ya se encuentra en uso");
                                            Log.e("registro", registro.getException().getMessage());
                                        }
                                    });
                        }
                    });
        }
        else {progressBar.setVisibility(View.GONE);}
    }

    private boolean verificarCampos(String nombre, String apellidos, String ti, String correo, String pwd, String rePwd){

        boolean valido = true;

        // Se limipian los mensajes de error anteriores
        inputNombre.setError(null);
        inputApellidos.setError(null);
        inputTI.setError(null);
        inputCorreo.setError(null);
        inputPwd.setError(null);
        inputRepPwd.setError(null);

        progressBar.setVisibility(View.VISIBLE);
        // Se verifican los campos y se ponen alertas

        if (nombre.equals("")){ inputNombre.setError("El nombre no puede estar vacío"); valido = false;}
        if (apellidos.equals("")){ inputApellidos.setError("El apellido no puede estar vacío"); valido = false;}

        if (!ti.matches("^[0-9]{8}$")){
            inputTI.setError("El TI debe ser un número de 8 dígitos");
            valido = false;
        }
        if (!correo.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z.]+")){
            inputCorreo.setError("El correo no es válido");
            valido = false;
        }
        if (!pwd.equals(rePwd)){
            inputPwd.setError("Las contraseñas deben ser iguales");
            inputRepPwd.setError("Las contraseñas deben ser iguales");
            valido = false;
        } else if (!pwd.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[*.!¡¿?@$%^&~_+-=]).{8,}$")){
            inputPwd.setError("La contraseña debe tener al menos 8 dígitos, un número y un caracter especial");
            inputRepPwd.setError("La contraseña debe tener al menos 8 dígitos, un número y un caracter especial");
            valido = false;
        }
        return valido;
    }
}