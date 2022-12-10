package com.example.rooms.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rooms.R;
import com.example.rooms.dto.ReservaDTO;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class ReservasAdapter extends RecyclerView.Adapter<ReservasAdapter.ReservaViewHolder> {

    private ArrayList<ReservaDTO> listaReservas;

    public ArrayList<ReservaDTO> getListaReservas() {return listaReservas;}
    public void setListaReservas(ArrayList<ReservaDTO> listaReservas) {this.listaReservas = listaReservas;}


    public class ReservaViewHolder extends RecyclerView.ViewHolder {
        ReservaDTO reserva;
        public ReservaViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public ReservaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_reservas,parent,false);
        return new ReservaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservaViewHolder holder, int position) {
        ReservaDTO reservaDTO = listaReservas.get(position);
        holder.reserva = reservaDTO;

        // Se mapean los elementos del fragmento
        TextView nombreEspacio = holder.itemView.findViewById(R.id.rvNombreEspacioReserva);
        TextView fecha = holder.itemView.findViewById(R.id.rvFechaReserva);
        TextView horaInicio = holder.itemView.findViewById(R.id.rvHoraInicioReserva);
        TextView horaFin = holder.itemView.findViewById(R.id.rvHoraFinReserva);

        // Se asignan los valores
        nombreEspacio.setText(reservaDTO.getNombreEspacio());

        LocalDateTime dia = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(reservaDTO.getDia())), ZoneId.of("UTC"));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("EE d MMM",new Locale("es"));
        fecha.setText(dia.format(df));

        horaInicio.setText(reservaDTO.getHoraInicio()+":00");
        horaFin.setText(reservaDTO.getHoraFin()+":00");

    }

    @Override
    public int getItemCount() {
        return listaReservas.size();
    }
}
