package com.example.appinventario;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    private List<Producto>listaProductos;

    //interfa para detectar selecion
    public interface OnProductoClickListener {
        void onProductoClick(Producto producto);
    }
    private OnProductoClickListener listener;
    public ProductoAdapter(List<Producto> listaProductos,
                           OnProductoClickListener listener){

        this.listaProductos = listaProductos;
        this.listener = listener;
    }
    public void actualizarLista(List<Producto> nuevaLista) {listaProductos = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductoAdapter.ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoAdapter.ProductoViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);

        holder.tvNombre.setText(producto.getNombre());
        holder.tvCategoria.setText("Categoria: " + producto.getCategoria());
        holder.tvCantidad.setText("Cantidad: " + producto.getCantidad());
        holder.tvPrecio.setText("Precio: Q" + producto.getPrecio());
        holder.tvProveedor.setText("Proveedor: " + producto.getProveedor());
        if (producto.getImagenUri() != null &&
                !producto.getImagenUri().isEmpty()) {
            holder.imgProducto.setImageURI(
                    Uri.parse(producto.getImagenUri())
            );
        } else {
            holder.imgProducto.setImageResource(R.drawable.photo);
        }
        // se detecta cuando se seleciona la cardview
        holder.itemView.setOnClickListener(v -> {
            listener.onProductoClick(producto);
        });

    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public static class ProductoViewHolder extends RecyclerView.ViewHolder{

        TextView tvNombre;
        TextView tvCategoria;
        TextView tvCantidad;
        TextView tvPrecio;
        TextView tvProveedor;
        ImageView imgProducto;
        public ProductoViewHolder(@NonNull  View itemView) {
            super(itemView);

            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvProveedor = itemView.findViewById(R.id.tvProveedor);
            imgProducto = itemView.findViewById(R.id.imgProducto);
        }
    }
}
