package com.example.rooms.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rooms.R;

public class SubItemDispAdapter extends RecyclerView.Adapter<SubItemDispAdapter.ViewHolder> {


    @NonNull
    @Override
    public SubItemDispAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_sub_item,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubItemDispAdapter.ViewHolder holder, int position) {
        holder.etHora.setText("10:00");

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button etHora;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            etHora = itemView.findViewById(R.id.btnHoraDisp);
        }
    }

    @Override
    //list.size()
    public int getItemCount() {
        return 6;
    }


}
