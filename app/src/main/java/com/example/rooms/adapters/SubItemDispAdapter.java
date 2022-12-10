package com.example.rooms.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rooms.R;
import com.example.rooms.dto.HorarioDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class SubItemDispAdapter extends RecyclerView.Adapter<SubItemDispAdapter.SubItemViewHolder> {

    private HashMap<String,Boolean> listaHoras;
    private String funcion;
    private HashMap<Long,ArrayList<Integer>> listaHorariosSeleccionados;
    private Long diaReserva;

    @NonNull
    @Override
    public SubItemDispAdapter.SubItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_sub_item,parent, false);
        return new SubItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubItemDispAdapter.SubItemViewHolder holder, int position) {

        ArrayList<String> listaKeys = new ArrayList<>(listaHoras.keySet());
        Collections.sort(listaKeys);

        if (listaHoras.get(listaKeys.get(position)) != null && listaHoras.get(listaKeys.get(position))){
            holder.btnHora.setText(listaKeys.get(position)+":00");

            if(funcion.equals("usuario")) {

                holder.btnHora.setOnClickListener(view -> {
                    listaHorariosSeleccionados.computeIfAbsent(diaReserva, k -> new ArrayList<Integer>());
                    Button btn = (Button) view;

                    // Se agrega un elemento a la lista
                    if (btn.getCurrentTextColor() == view.getContext().getColor(R.color.rojo)) {

                        listaHorariosSeleccionados.get(diaReserva).add(Integer.parseInt(listaKeys.get(position)));
                        listaHorariosSeleccionados.get(diaReserva).sort(Comparator.naturalOrder());
                        btn.setBackgroundColor(view.getContext().getColor(R.color.rojo));
                        btn.setTextColor(view.getContext().getColor(R.color.negroFondo));

                        Log.d("detallesEspacio", "Agregada la hora " + listaKeys.get(position) + ":00");
                    } else {
                        listaHorariosSeleccionados.get(diaReserva).remove((Object) Integer.parseInt(listaKeys.get(position)));
                        btn.setBackgroundColor(view.getContext().getColor(R.color.negroFondo));
                        btn.setTextColor(view.getContext().getColor(R.color.rojo));

                        if (listaHorariosSeleccionados.get(diaReserva).size() == 0){
                            listaHorariosSeleccionados.remove(diaReserva);
                        }

                        Log.d("detallesEspacio", "Quitada la hora " + listaKeys.get(position) + ":00");
                    }

                });
            }
        }
    }

    public class SubItemViewHolder extends RecyclerView.ViewHolder {
        Button btnHora;
        public SubItemViewHolder(@NonNull View itemView) {
            super(itemView);
            btnHora = itemView.findViewById(R.id.btnHoraDisp);
        }
    }

    @Override
    public int getItemCount() {
        return listaHoras.size();
    }

    // Getters y Setters
    public HashMap<String,Boolean> getListaHoras() {return listaHoras;}
    public void setListaHoras(HashMap<String,Boolean> listaHoras) {this.listaHoras = listaHoras;}
    public String getFuncion() {return funcion;}
    public void setFuncion(String funcion) {this.funcion = funcion;}
    public HashMap<Long, ArrayList<Integer>> getListaHorariosSeleccionados() {return listaHorariosSeleccionados;}
    public void setListaHorariosSeleccionados(HashMap<Long, ArrayList<Integer>> listaHorariosSeleccionados) {this.listaHorariosSeleccionados = listaHorariosSeleccionados;}
    public Long getDiaReserva() {return diaReserva;}
    public void setDiaReserva(Long diaReserva) {this.diaReserva = diaReserva;}
}
