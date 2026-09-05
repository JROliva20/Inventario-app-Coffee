package com.example.appinventario;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.net.Uri;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class DetalleProductoActivity extends AppCompatActivity {

    TextView tvNombre, tvCategoria, tvCantidad, tvPrecio, tvProveedor;
    ImageView imgProducto;

    MaterialButton btnEditar;
    MaterialButton btnEliminar;
    MaterialButton btnRegresar;

    // base de datos
    ProductoDbHelper dbHelper;

    //id
    long idProducto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle_producto);

        //conexion con sqlite
        dbHelper = new ProductoDbHelper(this);

        // recibir el id del producto
        idProducto = getIntent().getLongExtra("id", -1);

        if (idProducto == -1) {
            finish();
            return;
        }

        tvNombre = findViewById(R.id.tvNombre);
        tvCategoria = findViewById(R.id.tvCategoria);
        tvCantidad = findViewById(R.id.tvCantidad);
        tvPrecio = findViewById(R.id.tvPrecio);
        tvProveedor = findViewById(R.id.tvProveedor);
        imgProducto = findViewById(R.id.imgProducto);

        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnRegresar = findViewById(R.id.btnRegresar);

        // mostramos producto
        mostrarProducto();

        // btn editar
        btnEditar.setOnClickListener(v -> {

            Intent intent = new Intent(
                    DetalleProductoActivity.this, EditarProductoActivity.class
            );

            // enviamos el id a editar
            intent.putExtra("id", idProducto);
            startActivity(intent);
        });

        // BOTON ELIMINAR
        btnEliminar.setOnClickListener(v -> {

            new AlertDialog.Builder(this)
                    .setTitle("Eliminar Producto")
                    .setMessage("¿Está seguro de eliminar este producto?")
                    .setNegativeButton("Cancelar", null)
                    .setPositiveButton("Eliminar", (dialog, which) -> {

                        // eliminamos ddesde sqlite
                        dbHelper.eliminarProducto(idProducto);
                        finish();
                    })
                    .show();
        });

        btnRegresar.setOnClickListener(v -> finish());

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

    // mostramos el producto desde sqlite
    private void mostrarProducto() {

        Producto producto = dbHelper.obtenerProductoPorId(idProducto);
        if (producto == null) {
            finish();
            return;
        }

        tvNombre.setText(producto.getNombre());
        tvCategoria.setText("Categoría: " + producto.getCategoria());
        tvCantidad.setText("Cantidad: " + producto.getCantidad());
        tvPrecio.setText("Precio: Q" + producto.getPrecio());
        tvProveedor.setText("Proveedor: " + producto.getProveedor());

        if (producto.getImagenUri() != null &&
                !producto.getImagenUri().isEmpty()) {
            Uri uriImagen = Uri.parse(producto.getImagenUri());
            try {
                imgProducto.setImageURI(uriImagen);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // se ejecutar al volver ejecutar
    @Override
    protected void onResume() {
        super.onResume();

        if (dbHelper != null) {
            mostrarProducto();
        }
    }
}