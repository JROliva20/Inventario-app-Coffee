package com.example.appinventario;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecetaExternaAdapter extends RecyclerView.Adapter<RecetaExternaAdapter.RecetaViewHolder> {

    private List<RecetaExterna> listaRecetas;

    public RecetaExternaAdapter(List<RecetaExterna> listaRecetas) {
        this.listaRecetas = listaRecetas;
    }

    @NonNull
    @Override
    public RecetaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_receta, parent, false);
        return new RecetaViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull RecetaViewHolder holder,int position) {

        RecetaExterna receta = listaRecetas.get(position);

        holder.tvNombreReceta.setText(receta.getNombre());
        holder.tvCategoriaReceta.setText(receta.getCategoria());
        holder.tvOrigenReceta.setText("" + receta.getOrigen());
        Glide.with(holder.itemView.getContext())
                .load(receta.getImagenUrl())
                .into(holder.imgReceta);
    }

    @Override
    public int getItemCount() {
        return listaRecetas.size();
    }

    public static class RecetaViewHolder extends RecyclerView.ViewHolder {

        ImageView imgReceta;
        TextView tvNombreReceta;
        TextView tvCategoriaReceta;
        TextView tvOrigenReceta;

        public RecetaViewHolder(@NonNull View itemView) {
            super(itemView);
            imgReceta = itemView.findViewById(R.id.imgReceta);
            tvNombreReceta = itemView.findViewById(R.id.tvNombreReceta);
            tvCategoriaReceta = itemView.findViewById(R.id.tvCategoriaReceta);
            tvOrigenReceta = itemView.findViewById(R.id.tvOrigenReceta);
        }
    }
}