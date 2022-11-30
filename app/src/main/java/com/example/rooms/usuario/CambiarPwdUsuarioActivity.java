package com.example.rooms.usuario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.rooms.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CambiarPwdUsuarioActivity extends AppCompatActivity {

    private TextInputLayout inputPwdAntiguo, inputPwdNuevo, inputRePwdNuevo;
     private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_pwd_usuario);

        inputPwdAntiguo = findViewById(R.id.etPwdAntiguoCuentaUsuario);
        inputPwdNuevo = findViewById(R.id.etPwdNuevoCuentaUsuario);
        inputRePwdNuevo = findViewById(R.id.etRePwdNuevoCuentaUsuario);

        // Se quitan los iconos de advertencia que bloquean la opción de ver contraseña
        inputPwdAntiguo.setErrorIconDrawable(null);
        inputPwdNuevo.setErrorIconDrawable(null);
        inputRePwdNuevo.setErrorIconDrawable(null);

        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void cambiarContrasena(View view){

        String pwdAntiguo = inputPwdAntiguo.getEditText().getText().toString().trim();
        String pwdNuevo = inputPwdNuevo.getEditText().getText().toString().trim();
        String rePwdNuevo = inputRePwdNuevo.getEditText().getText().toString().trim();

        if (validarForm(pwdAntiguo,pwdNuevo,rePwdNuevo)){

            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), pwdAntiguo);

            // Se reautentica al usuario antes de cambiar la contraseña
            user.reauthenticate(credential)
                    .addOnCompleteListener(reautenticacion -> {
                        if (reautenticacion.isSuccessful()) {
                            user.updatePassword(pwdNuevo).addOnCompleteListener(actualizacion -> {
                                if (actualizacion.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),"Contraseña actualizada con éxito",Toast.LENGTH_SHORT).show();
                                    Log.d("cambioPwd", "Contraseña actualizada con éxito");
                                    startActivity(new Intent(getApplicationContext(),CuentaUsuarioActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(),actualizacion.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    Log.e("cambioPwd", actualizacion.getException().getMessage());
                                }
                            });
                        } else {
                            Log.e("cambioPwd", reautenticacion.getException().getMessage());
                            inputPwdAntiguo.setError("La contraseña ingresada es incorrecta");
                        }
                    });
        }
    }


    public boolean validarForm(String antiguoPwd, String nuevoPwd, String nuevoPwdRep){
        boolean valido = true;

        // Se limpian las alertas anteriores
        inputPwdAntiguo.setError(null);
        inputPwdNuevo.setError(null);
        inputRePwdNuevo.setError(null);

        // Se valida la nueva contraseña
        if (!nuevoPwd.equals(nuevoPwdRep)){
            inputPwdNuevo.setError("Las contraseñas deben ser iguales");
            inputRePwdNuevo.setError("Las contraseñas deben ser iguales");
            valido = false;
        } else if (antiguoPwd.equals(nuevoPwd)){
            inputPwdNuevo.setError("La contraseña nueva no puede ser igual a la antigua");
            inputRePwdNuevo.setError("La contraseña nueva no puede ser igual a la antigua");
            valido = false;
        } else if (!nuevoPwd.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[*.!¡¿?@$%^&~_+-=]).{8,}$")){
            inputPwdNuevo.setError("La contraseña debe tener al menos 8 dígitos, un número y un caracter especial");
            inputRePwdNuevo.setError("La contraseña debe tener al menos 8 dígitos, un número y un caracter especial");
            valido = false;
        }

        return valido;
    }
}