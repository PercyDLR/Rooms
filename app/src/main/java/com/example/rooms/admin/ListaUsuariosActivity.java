package com.example.rooms.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.example.rooms.R;
import com.example.rooms.adapters.ListaUsuariosAdapter;
import com.example.rooms.dto.UsuarioDTO;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class ListaUsuariosActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference ref;

    BottomNavigationView bottomNavigationView;
    TextInputLayout buscadorUsuarios, filtroUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuarios);

        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("usuarios");
        configurarNavBar();
        buscadorUsuarios = findViewById(R.id.etBusquedaUsuariosAdmin);
        filtroUsuarios = findViewById(R.id.etFiltroUsuariosAdmin);

        // Setea los valores iniciales de la lista en la pantalla
        listarUsuarios("", filtroUsuarios.getEditText().getText().toString());

        // Realiza la bpusquedasi se presiona el botón del teclado
        buscadorUsuarios.getEditText().setOnEditorActionListener(((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                listarUsuarios(buscadorUsuarios.getEditText().getText().toString().trim(),filtroUsuarios.getEditText().getText().toString());
                return true;
            }
            return false;
        }));

        // Realiza la búsqueda si se selecciona un filtro distinto
        filtroUsuarios.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                listarUsuarios(buscadorUsuarios.getEditText().getText().toString().trim(),filtroUsuarios.getEditText().getText().toString());
            }
        });
    }

    // Lógica para las listas
    public void listarUsuarios (String busqueda, String filtro){

        ListaUsuariosAdapter adapter = new ListaUsuariosAdapter();
        ArrayList<UsuarioDTO> listaUsuarios = new ArrayList<>();

        adapter.setListaUsuarios(listaUsuarios);
        adapter.setContext(this);

        ref.orderByChild(filtro.toLowerCase()).startAt(busqueda).endAt(busqueda+"\uf8ff").limitToFirst(20)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        UsuarioDTO user = snapshot.getValue(UsuarioDTO.class);

                        if (user.getRol().equals("usuario")){
                            user.setUid(snapshot.getKey());
                            listaUsuarios.add(user);
                            Log.d("listaUsuario", "Se agregó usuario de UID: "+snapshot.getKey());
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        for (int ii=0; ii<listaUsuarios.size(); ii++){
                            if (snapshot.getKey().equals(listaUsuarios.get(ii).getUid())){
                                UsuarioDTO user = snapshot.getValue(UsuarioDTO.class);
                                user.setUid(snapshot.getKey());
                                listaUsuarios.set(ii,user);
                                Log.d("listaUsuario", "Se modificó usuario de UID: "+snapshot.getKey());
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("listaUsuarios", error.getMessage());
                        Toast.makeText(ListaUsuariosActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RecyclerView recyclerView = findViewById(R.id.rvListaUsuariosAdmin);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    // Opciones de navegación
    public void configurarNavBar(){
        bottomNavigationView = findViewById(R.id.nvUsuariosAdmin);
        bottomNavigationView.setSelectedItemId(R.id.navigation_usuarios);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.navigation_espacios:
                    startActivity(new Intent(getApplicationContext(),ListaEspaciosActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.navigation_usuarios:
                    return true;
                case R.id.navigation_cuenta_admin:
                    startActivity(new Intent(getApplicationContext(),CuentaAdminActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });
    }
}