package com.example.rooms.usuario;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.EditorInfo;

import com.example.rooms.R;
import com.example.rooms.adapters.ListaEspaciosAdapter;
import com.example.rooms.admin.CuentaAdminActivity;
import com.example.rooms.admin.ListaUsuariosActivity;
import com.example.rooms.dto.EspacioDTO;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListaEspaciosUsuarioActivity extends AppCompatActivity {

    private DatabaseReference ref;

    BottomNavigationView bottomNavigationView;
    TextInputLayout buscadorEspacios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_espacios_usuario);

        ref = FirebaseDatabase.getInstance().getReference("espacios");
        configurarNavBar();
        buscadorEspacios = findViewById(R.id.etBusquedaEspaciosUsuario);

        // Setea los valores iniciales de la lista en la pantalla
        listarEspacios("");

        // Realiza la busqueda si se presiona el botón del teclado
        buscadorEspacios.getEditText().setOnEditorActionListener(((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                listarEspacios(buscadorEspacios.getEditText().getText().toString().trim());
                return true;
            }
            return false;
        }));
    }

    public void listarEspacios(String busqueda){
        ListaEspaciosAdapter adapter = new ListaEspaciosAdapter();
        ArrayList<EspacioDTO> listaEspacios = new ArrayList<>();

        adapter.setListaEspacios(listaEspacios);
        adapter.setContext(this);
        adapter.setFuncion("usuario");

        ref.orderByChild("nombre").startAt(busqueda).endAt(busqueda+"\uf8ff")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        EspacioDTO espacio = snapshot.getValue(EspacioDTO.class);
                        espacio.setKey(snapshot.getKey());

                        if(espacio.isActivo()){
                            listaEspacios.add(espacio);
                            Log.d("listaEspacios", "Se agregó espacio de key: "+snapshot.getKey());
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        for (int ii=0; ii<listaEspacios.size(); ii++){
                            if (snapshot.getKey().equals(listaEspacios.get(ii).getKey())){
                                EspacioDTO espacio = snapshot.getValue(EspacioDTO.class);
                                espacio.setKey(snapshot.getKey());

                                if(espacio.isActivo()){
                                    listaEspacios.set(ii,espacio);
                                    Log.d("listaEspacios", "Se modificó el espacio de key: "+snapshot.getKey());
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {}

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("listaEspacios", error.getMessage());
                    }
                });

        RecyclerView recyclerView = findViewById(R.id.rvListaEspaciosUsuario);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    public void configurarNavBar(){
        bottomNavigationView = findViewById(R.id.nvEspaciosUsuario);
        bottomNavigationView.setSelectedItemId(R.id.navigation_inicio);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.navigation_inicio:
                    return true;
                case R.id.navigation_reservas:
//                    startActivity(new Intent(getApplicationContext(), ListaUsuariosActivity.class)
//                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                    overridePendingTransition(0,0);
                    return true;
                case R.id.navigation_cuenta:
                    startActivity(new Intent(getApplicationContext(), CuentaUsuarioActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    overridePendingTransition(0,0);
                    return true;
            }
            return false;
        });
    }
}