package com.example.rooms.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rooms.R;

import java.util.ArrayList;

public class ItemDispAdapter extends RecyclerView.Adapter<ItemDispAdapter.ViewHolder> {

    SubItemDispAdapter subItemDispAdapter;
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha;
        RecyclerView rvHoras;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            rvHoras = itemView.findViewById(R.id.rvHoras);
        }
    }


    @NonNull
    @Override
    public ItemDispAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemDispAdapter.ViewHolder holder, int position) {

        holder.tvFecha.setText("Lunes 21 de Julio");

        // Create layout manager with initial prefetch item count
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.rvHoras.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        //item.getSubItemList().size() --> Cantidad de subitems (Horas)
        layoutManager.setInitialPrefetchItemCount(6);

        // Create sub item view adapter
        subItemDispAdapter = new SubItemDispAdapter();

        holder.rvHoras.setLayoutManager(layoutManager);
        holder.rvHoras.setAdapter(subItemDispAdapter);
        holder.rvHoras.setRecycledViewPool(viewPool);
    }

    @Override
    //listItems.size()
    public int getItemCount() {
        return 10;
    }
}
