package com.example.rooms.usuario;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.rooms.R;
import com.example.rooms.adapters.ReservasAdapter;
import com.example.rooms.dto.ReservaDTO;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

public class ListaReservasActivity extends AppCompatActivity {

    private DatabaseReference ref;
    private FirebaseAuth auth;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_reservas);

        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("reservas").child(auth.getCurrentUser().getUid());

        configurarNavBar();
        listarReservas();
    }

    public void listarReservas() {
        String todayinMiliseconds = String.valueOf(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
        ArrayList<ReservaDTO> listaReservas = new ArrayList<>();

        ReservasAdapter adapter = new ReservasAdapter();
        adapter.setListaReservas(listaReservas);

        ref.orderByChild("dia").startAt(todayinMiliseconds).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ReservaDTO reserva = snapshot.getValue(ReservaDTO.class);
                Log.d("listaReservas", "Se agregó reserva de key: "+snapshot.getKey());
                listaReservas.add(reserva);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        RecyclerView recyclerView = findViewById(R.id.rvListaReservasUsuario);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }


    public void configurarNavBar(){
        bottomNavigationView = findViewById(R.id.nvReservasUsuario);
        bottomNavigationView.setSelectedItemId(R.id.navigation_reservas);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.navigation_inicio:
                    startActivity(new Intent(getApplicationContext(),ListaEspaciosUsuarioActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    overridePendingTransition(0,0);
                    return true;
                case R.id.navigation_reservas:
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