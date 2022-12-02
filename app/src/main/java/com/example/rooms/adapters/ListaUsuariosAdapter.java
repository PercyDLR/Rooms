package com.example.rooms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rooms.R;
import com.example.rooms.dto.UsuarioDTO;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListaUsuariosAdapter extends RecyclerView.Adapter<ListaUsuariosAdapter.UsuarioViewHolder> {

    private ArrayList<UsuarioDTO> listaUsuarios;
    private Context context;

    public ArrayList<UsuarioDTO> getListaUsuarios() {return listaUsuarios;}
    public void setListaUsuarios(ArrayList<UsuarioDTO> listaUsuarios) {this.listaUsuarios = listaUsuarios;}
    public Context getContext() {return context;}
    public void setContext(Context context) {this.context = context;}

    public class UsuarioViewHolder extends RecyclerView.ViewHolder {
        UsuarioDTO usuario;
        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.elemento_usuario_rv,parent,false);
        return new UsuarioViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position) {
        UsuarioDTO user = listaUsuarios.get(position);
        holder.usuario = user;

        // Se mapean los elementos del fragmento
        TextView encabezado = holder.itemView.findViewById(R.id.rvEncabezado);
        TextView cuerpo = holder.itemView.findViewById(R.id.rvCuerpo);
        ImageButton boton = holder.itemView.findViewById(R.id.rvBoton);

        // Se asignan los valores
        encabezado.setText(user.getNombre());
        cuerpo.setText(user.getCorreo());

        // Permite banear o desbanear usuarios
        boton.setImageResource(user.isActivo() ? R.drawable.ic_report : R.drawable.ic_unreport);

        boton.setOnClickListener(view -> {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("usuarios");

            new MaterialAlertDialogBuilder(context)
                    .setTitle(user.isActivo() ? "Banear Usuario" : "Desbanear Usuario")
                    .setMessage(user.isActivo() ? "¿Estas seguro de querer banear a este usuario?" : "¿Estas seguro de querer desbanear a este usuario?")
                    .setNegativeButton("Cancelar",((dialogInterface, i) -> {
                        dialogInterface.cancel();
                    })).setPositiveButton(user.isActivo() ? "Banear" : "Desbanear", ((dialogInterface, i) -> {
                        Map<String,Object> updates = new HashMap<>();
                        updates.put("activo",!user.isActivo());
                        ref.child(user.getUid()).updateChildren(updates);
                        dialogInterface.dismiss();
                    })).show();
        });

    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }
}