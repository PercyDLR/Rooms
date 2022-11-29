package com.example.rooms.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rooms.R;
import com.example.rooms.admin.CuentaAdminActivity;
import com.example.rooms.admin.ListaEspaciosActivity;
import com.example.rooms.dto.UsuarioDTO;
import com.example.rooms.usuario.CuentaUsuarioActivity;
import com.example.rooms.usuario.DetallesEspacioUsuario;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference ref;
    private ProgressBar progressBar;
    private TextInputLayout inputCorreo, inputPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputCorreo = findViewById(R.id.etLoginCorreo);
        inputPwd = findViewById(R.id.etLoginPswd);

        auth = FirebaseAuth.getInstance();


    }


    public boolean datosValidos (String correo, String pwd){
        if (!correo.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){ inputCorreo.setError("El correo ingresado no es válido"); return false;}
        if (!pwd.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[*.!¡¿?@$%^&~_+-=]).{8,}$")) {
            inputPwd.setError("La contraseña debe tener al menos 8 dígitos, un número y un caracter especial");
            return false;
        }
        return true;
    }

    public void logueo (View view){

        String correo = inputCorreo.getEditText().getText().toString().trim();
        String pwd = inputPwd.getEditText().getText().toString().trim();

        if (datosValidos(correo,pwd)){
            auth.signInWithEmailAndPassword(correo,pwd)
                    .addOnCompleteListener(logueo -> {
                        if (logueo.isSuccessful() ){
                            if (auth.getCurrentUser().isEmailVerified()){

                                // TODO: obtener el rol de la DB y redirigir
                                String rol = "usuario";

                                if (rol.equals("admin")){
                                    startActivity(new Intent(getApplicationContext(), CuentaAdminActivity.class));
                                    finish();
                                } else if (rol.equals("usuario")) {
                                    startActivity(new Intent(getApplicationContext(), CuentaUsuarioActivity.class));
                                    finish();
                                }


                            } else {
                                Toast.makeText(getApplicationContext(),"Debe verificar su correo para poder loguearse",Toast.LENGTH_SHORT).show();
                                Log.d("logueo", "Debe verificar su correo para poder loguearse");
                                inputPwd.getEditText().setText("");
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),logueo.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            Log.e("logueo", logueo.getException().getMessage());
                            inputPwd.getEditText().setText("");
                        }
                    });
        }


        startActivity(new Intent(LoginActivity.this, DetallesEspacioUsuario.class));
    }
}