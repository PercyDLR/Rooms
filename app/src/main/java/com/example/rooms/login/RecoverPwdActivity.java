package com.example.rooms.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.rooms.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class RecoverPwdActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private TextInputLayout inputCorreo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_pwd);

        inputCorreo = findViewById(R.id.etRecoverPwdCorreo);
        auth = FirebaseAuth.getInstance();
    }

    public void mandarCorreoRecuperacion(View view){

        String correo = inputCorreo.getEditText().getText().toString().trim();

        if (correo.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z.]+")){
            auth.sendPasswordResetEmail(correo)
                    .addOnCompleteListener( task -> {
                        if(task.isSuccessful()){
                            Toast.makeText(RecoverPwdActivity.this, "Se ha enviado un enlace de recuperación a su correo", Toast.LENGTH_SHORT).show();
                            Log.d("pwdRecover", "Envío de enlace de recuperación exitoso");
                            startActivity(new Intent(RecoverPwdActivity.this,MainActivity.class));
                            finish();
                        }
                        else {
                            Toast.makeText(RecoverPwdActivity.this, "No existe ningun usuario registrado con este correo", Toast.LENGTH_SHORT).show();
                            Log.e("pwdRecover", task.getException().getMessage());
                        }
                    });
        } else {
            inputCorreo.setError("El correo no es válido");
        }
    }
}