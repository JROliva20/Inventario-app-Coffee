package com.example.appinventario;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder> {

    private List<Usuario> listaUsuarios;

    public UsuarioAdapter(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public void actualizarLista(List<Usuario> nuevaLista) {
        listaUsuarios = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UsuarioViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usuario, parent, false);

        return new UsuarioViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(
            @NonNull UsuarioViewHolder holder, int position) {

        Usuario usuario = listaUsuarios.get(position);

        holder.tvNombre.setText(usuario.getNombre());
        holder.tvUsuario.setText("@" + usuario.getUsuario());
        holder.tvRol.setText(usuario.getRol());
        holder.tvEstado.setText("● " + usuario.getEstado());

        holder.btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(
                    v.getContext(),
                    RegistroUsuarioActivity.class
            );
            intent.putExtra("usuario_id", usuario.getId());
            intent.putExtra("desde_usuarios", true);
            v.getContext().startActivity(intent);
        });

        holder.btnEliminar.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Eliminar usuario")
                    .setMessage("¿Desea eliminar a " + usuario.getNombre() + "?")
                    .setNegativeButton("Cancelar", null)
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        UsuarioDAO usuarioDAO = new UsuarioDAO(v.getContext());
                        usuarioDAO.eliminarUsuario(usuario.getId());
                        listaUsuarios.remove(usuario);
                        notifyDataSetChanged();
                    })
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return listaUsuarios.size();
    }

    public static class UsuarioViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombre, tvUsuario, tvRol, tvEstado;
        MaterialButton btnEditar, btnEliminar;

        public UsuarioViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvUsuario = itemView.findViewById(R.id.tvUsuario);
            tvRol = itemView.findViewById(R.id.tvRol);
            tvEstado = itemView.findViewById(R.id.tvEstado);

            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}