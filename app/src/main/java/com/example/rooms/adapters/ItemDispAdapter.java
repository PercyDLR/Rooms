package com.example.rooms.adapters;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rooms.R;
import com.example.rooms.admin.CuentaAdminActivity;
import com.example.rooms.dto.HorarioDTO;
import com.example.rooms.login.MainActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.FirebaseDatabase;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ItemDispAdapter extends RecyclerView.Adapter<ItemDispAdapter.ItemViewHolder> {

    SubItemDispAdapter subItemDispAdapter;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    private ArrayList<HorarioDTO> listaHorarios;
    private HashMap<Long,ArrayList<Integer>> listaHorariosSeleccionados;
    private String funcion;
    private String keyEspacio;

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha;
        RecyclerView rvHoras;
        ImageButton btnEliminar;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            rvHoras = itemView.findViewById(R.id.rvHoras);
            btnEliminar = itemView.findViewById(R.id.btnEliminarHorario);
        }
    }


    @NonNull
    @Override
    public ItemDispAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemDispAdapter.ItemViewHolder holder, int position) {

        HorarioDTO horarioDia = listaHorarios.get(position);
        LocalDateTime dia = LocalDateTime.ofInstant(Instant.ofEpochMilli(horarioDia.getFecha()), ZoneId.of("UTC"));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("E d MMM YY",new Locale("es"));
        holder.tvFecha.setText(dia.format(df).toUpperCase());

        // Create layout manager with initial prefetch item count
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.rvHoras.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );

        if(funcion.equals("admin")){
            holder.btnEliminar.setVisibility(View.VISIBLE);

            holder.btnEliminar.setOnClickListener(view -> {
                new MaterialAlertDialogBuilder(view.getContext())
                        .setTitle("Eliminar Horario")
                        .setMessage("¿Estás seguro de querer eliminar el horario del "+dia.format(df)+"?")
                        .setNegativeButton("Cancelar",((dialogInterface, i) -> {
                            dialogInterface.cancel();
                        })).setPositiveButton("Cerrar Sesión", ((dialogInterface, i) -> {
                            FirebaseDatabase.getInstance().getReference("disponibilidad")
                                    .child(keyEspacio).child(horarioDia.getFecha().toString()).removeValue()
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()){

                                            Log.d("detallesEspacio", "Se eliminó el horario del "+dia.format(df));
                                            Toast.makeText(view.getContext(), "Se eliminó el horario del "+dia.format(df), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Log.e("detallesEspacio", task.getException().getMessage());
                                            Toast.makeText(view.getContext(), "Hubo un problema", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        })).show();
            });
        }


        // Create sub item view adapter
        subItemDispAdapter = new SubItemDispAdapter();
        subItemDispAdapter.setListaHoras(horarioDia.getListaHoras());
        subItemDispAdapter.setFuncion(funcion);
        subItemDispAdapter.setDiaReserva(horarioDia.getFecha());
        subItemDispAdapter.setListaHorariosSeleccionados(listaHorariosSeleccionados);

        holder.rvHoras.setLayoutManager(layoutManager);
        holder.rvHoras.setAdapter(subItemDispAdapter);
        holder.rvHoras.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return listaHorarios.size();
    }

    // Getters y setters
    public ArrayList<HorarioDTO> getListaHorarios() {return listaHorarios;}
    public void setListaHorarios(ArrayList<HorarioDTO> listaHorarios) {this.listaHorarios = listaHorarios;}
    public String getFuncion() {return funcion;}
    public void setFuncion(String funcion) {this.funcion = funcion;}
    public HashMap<Long, ArrayList<Integer>> getListaHorariosSeleccionados() {return listaHorariosSeleccionados;}
    public void setListaHorariosSeleccionados(HashMap<Long, ArrayList<Integer>> listaHorariosSeleccionados) {this.listaHorariosSeleccionados = listaHorariosSeleccionados;}
    public String getKeyEspacio() {return keyEspacio;}
    public void setKeyEspacio(String keyEspacio) {this.keyEspacio = keyEspacio;}
}
