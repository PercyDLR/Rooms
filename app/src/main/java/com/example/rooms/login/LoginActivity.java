package com.example.rooms.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rooms.R;
import com.example.rooms.admin.CuentaAdminActivity;
import com.example.rooms.admin.ListaEspaciosActivity;
import com.example.rooms.dto.UsuarioDTO;
import com.example.rooms.usuario.CuentaUsuarioActivity;
import com.example.rooms.usuario.ListaEspaciosUsuarioActivity;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference ref;
    private ProgressBar progressBar;
    private TextInputLayout inputCorreo, inputPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = findViewById(R.id.pbLogin);
        inputCorreo = findViewById(R.id.etLoginCorreo);
        inputPwd = findViewById(R.id.etLoginPswd);
        inputPwd.setErrorIconDrawable(null);

        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("usuarios");

        progressBar.setVisibility(View.GONE);
    }

    public void logueo (View view){

        String correo = inputCorreo.getEditText().getText().toString().trim();
        String pwd = inputPwd.getEditText().getText().toString().trim();

        if (datosValidos(correo,pwd)){
            // Se logueo con Firebase Auth
            auth.signInWithEmailAndPassword(correo,pwd)
                    .addOnCompleteListener(logueo -> {
                        if (logueo.isSuccessful() ){
                            if (auth.getCurrentUser().isEmailVerified()){

                                // Se busca la cuenta en Realtime DB
                                ref.child(auth.getCurrentUser().getUid()).get()
                                        .addOnCompleteListener(task -> {

                                            progressBar.setVisibility(View.GONE);
                                            // Si se encuentra algo en la b??squeda
                                            if (task.isSuccessful() && task.getResult().exists()){

                                                UsuarioDTO user = task.getResult().getValue(UsuarioDTO.class);

                                                // Si el usuario no fue baneado, se redirige por rol
                                                if(user.isActivo()){
                                                    if (user.getRol().equals("admin")){
                                                        Log.d("logueo", "Logueo Exitoso: admin");
                                                        startActivity(new Intent(getApplicationContext(), ListaEspaciosActivity.class));
                                                        finish();
                                                    } else if (user.getRol().equals("usuario")) {
                                                        Log.d("logueo", "Logueo Exitoso: usuario");
                                                        startActivity(new Intent(getApplicationContext(), ListaEspaciosUsuarioActivity.class));
                                                        finish();
                                                    } else {
                                                        Toast.makeText(getApplicationContext(),"No se pudo obtener el rol",Toast.LENGTH_SHORT).show();
                                                        Log.e("logueo", "No se pudo obtener el rol");
                                                    }
                                                } else {
                                                    Toast.makeText(getApplicationContext(),"Su cuenta se encuentra bloqueada",Toast.LENGTH_SHORT).show();
                                                    Log.e("logueo", "El usuario se encuentra baneado");
                                                }
                                            } else {
                                                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                Log.e("logueo", task.getException().getMessage());
                                            }
                                        });
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Debe verificar su correo para poder loguearse",Toast.LENGTH_SHORT).show();
                                Log.d("logueo", "Debe verificar su correo para poder loguearse");
                                inputPwd.getEditText().setText("");
                                auth.getCurrentUser().sendEmailVerification();
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Log.e("logueo", logueo.getException().getMessage());
                            inputPwd.getEditText().setText("");
                            inputPwd.setError("Correo o Contrase??a inv??lidos");
                            inputCorreo.setError("Correo o Contrase??a inv??lidos");
                        }
                    });
        } else {progressBar.setVisibility(View.GONE);}
    }

    public boolean datosValidos (String correo, String pwd){
        progressBar.setVisibility(View.VISIBLE);
        boolean valido = true;

        // Se quitan los mensajes previos
        inputCorreo.setError(null);
        inputPwd.setError(null);

        if (!correo.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z.]+")){
            inputCorreo.setError("El correo ingresado no es v??lido");
            valido = false;
        }
        if (!pwd.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[*.!?????@$%^&~_+-=]).{8,}$")) {
            inputPwd.setError("La contrase??a debe tener al menos 8 d??gitos, un n??mero y un caracter especial");
            valido = false;
        }
        return valido;
    }

    public void recoverPwd(View view){
        startActivity(new Intent(getApplicationContext(),RecoverPwdActivity.class));
    }
}