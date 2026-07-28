package com.example.appinventario;

import android.os.Bundle;

import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public class AgregarProductoActivity extends AppCompatActivity {

    TextInputEditText etNombre;
    TextInputEditText etCategoria;
    TextInputEditText etCantidad;
    TextInputEditText etPrecio;
    TextInputEditText etProveedor;
    MaterialButton btnGuardar;
    MaterialButton btnCancelar;

    ImageView imgProducto;
    MaterialButton btnGaleria;
    MaterialButton btnCamara;
    Uri imagenSeleccionada;

    ActivityResultLauncher<Intent> seleccionarImagen =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK &&
                                result.getData() != null) {
                            imagenSeleccionada = result.getData().getData();
                            imgProducto.setImageURI(imagenSeleccionada);
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_producto);

        etNombre = findViewById(R.id.etNombre);
        etCategoria = findViewById(R.id.etCategoria);
        etCantidad = findViewById(R.id.etCantidad);
        etPrecio = findViewById(R.id.etPrecio);
        etProveedor = findViewById(R.id.etProveedor);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String categoria = etCategoria.getText().toString().trim();
            String txtCantidad = etCantidad.getText().toString().trim();
            String txtPrecio = etPrecio.getText().toString().trim();
            String proveedor = etProveedor.getText().toString().trim();

            if (nombre.isEmpty()) {
                etNombre.setError("Ingrese el nombre del producto");
                etNombre.requestFocus();
                return;
            }
            if (categoria.isEmpty()) {
                etCategoria.setError("Ingrese la categoría");
                etCategoria.requestFocus();
                return;
            }
            if (txtCantidad.isEmpty()) {
                etCantidad.setError("Ingrese la cantidad");
                etCantidad.requestFocus();
                return;
            }
            if (txtPrecio.isEmpty()) {
                etPrecio.setError("Ingrese el precio");
                etPrecio.requestFocus();
                return;
            }
            if (proveedor.isEmpty()) {
                etProveedor.setError("Ingrese el proveedor");
                etProveedor.requestFocus();
                return;
            }
            int cantidad = Integer.parseInt(txtCantidad);
            double precio = Double.parseDouble(txtPrecio);

            Producto producto = new Producto(
                    nombre,
                    categoria,
                    cantidad,
                    precio,
                    proveedor
            );
            Datos.listaProductos.add(producto);

            etNombre.setText("");
            etCategoria.setText("");
            etCantidad.setText("");
            etPrecio.setText("");
            etProveedor.setText("");

            etNombre.requestFocus();

            Toast.makeText(this,
                    "Producto agregado correctamente",
                    Toast.LENGTH_SHORT).show();
        });

        btnCancelar.setOnClickListener(v -> {
            finish();
        });

        imgProducto = findViewById(R.id.imgProducto);
        btnGaleria = findViewById(R.id.btnGaleria);
        btnCamara = findViewById(R.id.btnCamara);

        btnGaleria.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            seleccionarImagen.launch(intent);

        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}