package com.example.appinventario;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.InputStream;


public class DetalleProductoActivity extends AppCompatActivity {

    TextView tvNombre, tvCategoria, tvCantidad, tvPrecio, tvProveedor;
    ImageView imgProducto;

    MaterialButton btnEditar;
    MaterialButton btnEliminar;
    MaterialButton btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle_producto);

        int posicion = getIntent().getIntExtra("posicion", -1);

        if (posicion == -1) {
            finish();
            return;
        }

        Producto producto = Datos.listaProductos.get(posicion);

        tvNombre = findViewById(R.id.tvNombre);
        tvCategoria = findViewById(R.id.tvCategoria);
        tvCantidad = findViewById(R.id.tvCantidad);
        tvPrecio = findViewById(R.id.tvPrecio);
        tvProveedor = findViewById(R.id.tvProveedor);
        imgProducto = findViewById(R.id.imgProducto);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnRegresar= findViewById(R.id.btnRegresar);

        btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(
                    DetalleProductoActivity.this, EditarProductoActivity.class
            );

            intent.putExtra("posicion",posicion);
            startActivity(intent);
        });

        btnEliminar.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Eliminar Producto")
                    .setMessage("¿Está seguro de eliminar este producto?")
                    .setNegativeButton("Cancelar", null)
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        Datos.listaProductos.remove(posicion);
                        finish();
                    })
                    .show();
        });

        btnRegresar.setOnClickListener(v -> finish());

        tvNombre.setText(producto.getNombre());
        tvCategoria.setText("Categoría: " + producto.getCategoria());
        tvCantidad.setText("Cantidad: " + producto.getCantidad());
        tvPrecio.setText("Precio: Q" + producto.getPrecio());
        tvProveedor.setText("Proveedor: " + producto.getProveedor());

        if (producto.getImagenUri() != null &&
                !producto.getImagenUri().isEmpty()) {
            Uri uriImagen = Uri.parse(producto.getImagenUri());
            Bitmap imagen = BitmapFactory.decodeFile(uriImagen.getPath());
            if (imagen != null) {
                imgProducto.setImageBitmap(imagen);
            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {
                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );
                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );
                    return insets;
                }
        );
    }
    //Para refrescar la información del detalle cada vez que la Activity vuelve a estar activa después de editar un producto.
    @Override
    protected void onResume() {
        super.onResume();
        int posicion = getIntent().getIntExtra("posicion", -1);
        if (posicion == -1) {
            finish();
            return;
        }
        Producto producto = Datos.listaProductos.get(posicion);
        tvNombre.setText(producto.getNombre());
        tvCategoria.setText("Categoría: " + producto.getCategoria());
        tvCantidad.setText("Cantidad: " + producto.getCantidad());
        tvPrecio.setText("Precio: Q" + producto.getPrecio());
        tvProveedor.setText("Proveedor: " + producto.getProveedor());
        if (producto.getImagenUri() != null &&
                !producto.getImagenUri().isEmpty()) {
            Uri uriImagen = Uri.parse(producto.getImagenUri());
            try {
                InputStream entrada =
                        getContentResolver().openInputStream(uriImagen);
                Bitmap imagen =
                        BitmapFactory.decodeStream(entrada);
                if (entrada != null) {
                    entrada.close();
                }
                if (imagen != null) {
                    imgProducto.setImageBitmap(imagen);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}