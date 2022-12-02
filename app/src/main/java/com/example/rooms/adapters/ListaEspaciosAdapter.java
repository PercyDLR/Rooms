package com.example.rooms.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rooms.R;
import com.example.rooms.admin.DetallesEspacioActivity;
import com.example.rooms.dto.EspacioDTO;

import java.util.ArrayList;

public class ListaEspaciosAdapter extends RecyclerView.Adapter<ListaEspaciosAdapter.EspacioViewHolder> {

    private ArrayList<EspacioDTO> listaEspacios;
    private Context context;

    public ArrayList<EspacioDTO> getListaEspacios() {return listaEspacios;}
    public void setListaEspacios(ArrayList<EspacioDTO> listaEspacios) {this.listaEspacios = listaEspacios;}
    public Context getContext() {return context;}
    public void setContext(Context context) {this.context = context;}

    public class EspacioViewHolder extends RecyclerView.ViewHolder {
        EspacioDTO espacio;

        public EspacioViewHolder(@NonNull View itemView) {super(itemView);}
    }

    @NonNull
    @Override
    public EspacioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.elemento_espacio,parent,false);
        return new EspacioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EspacioViewHolder holder, int position) {
        EspacioDTO espacio = listaEspacios.get(position);
        holder.espacio =  espacio;

        // Se mapean los elementos del fragmento
        TextView encabezado = holder.itemView.findViewById(R.id.rvEncabezadoEspacio);
        TextView contador = holder.itemView.findViewById(R.id.rvContadorEspacio);
        ImageButton boton = holder.itemView.findViewById(R.id.rvBotonDetallesEspacio);

        encabezado.setText(espacio.getNombre());
        contador.setText(espacio.getHorariosDisponibles());

        boton.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetallesEspacioActivity.class);
            intent.putExtra("espacio",espacio);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return listaEspacios.size();
    }
}
