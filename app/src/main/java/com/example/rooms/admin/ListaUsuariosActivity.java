package com.example.rooms.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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
    TextInputLayout buscadorUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuarios);

        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("usuarios");
        configurarNavBar();
        buscadorUsuarios = findViewById(R.id.etBusquedaUsuariosAdmin);

        listarUsuarios("");

        buscadorUsuarios.getEditText().setOnEditorActionListener(((textView, actionId, keyEvent) -> {

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                listarUsuarios(buscadorUsuarios.getEditText().getText().toString().trim());
                return true;
            }
            return false;
        }));
    }

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

    public void listarUsuarios (String busqueda){

        ListaUsuariosAdapter adapter = new ListaUsuariosAdapter();
        ArrayList<UsuarioDTO> listaUsuarios = new ArrayList<>();

        adapter.setListaUsuarios(listaUsuarios);
        adapter.setContext(getApplicationContext());

        RecyclerView recyclerView = findViewById(R.id.rvListaUsuariosAdmin);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        ref.orderByChild("nombre").startAt(busqueda).endAt(busqueda+"\uf8ff").limitToFirst(20)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        UsuarioDTO user = snapshot.getValue(UsuarioDTO.class);
                        user.setUid(previousChildName);

                        if (user.getRol().equals("usuario")){
                            listaUsuarios.add(user);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        for (int ii=0; ii<listaUsuarios.size(); ii++){
                            if (previousChildName.equals(listaUsuarios.get(ii).getUid())){
                                listaUsuarios.set(ii,snapshot.getValue(UsuarioDTO.class));
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

    }
}