package com.example.appinventario;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class EditarProductoActivity extends AppCompatActivity {
    TextInputEditText etNombre, etCategoria, etCantidad, etPrecio, etProveedor;
    MaterialButton btnActualizar, btnCancelar;
    int posicion;
    Producto producto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_producto);
        etNombre = findViewById(R.id.etNombre);
        etCategoria = findViewById(R.id.etCategoria);
        etCantidad = findViewById(R.id.etCantidad);
        etPrecio = findViewById(R.id.etPrecio);
        etProveedor = findViewById(R.id.etProveedor);

        btnActualizar = findViewById(R.id.btnActualizar);
        btnCancelar = findViewById(R.id.btnCancelar);

        posicion = getIntent().getIntExtra("posicion", -1);
        producto = Datos.listaProductos.get(posicion);
        etNombre.setText(producto.getNombre());
        etCategoria.setText(producto.getCategoria());
        etCantidad.setText(String.valueOf(producto.getCantidad()));
        etPrecio.setText(String.valueOf(producto.getPrecio()));
        etProveedor.setText(producto.getProveedor());

        btnActualizar.setOnClickListener(v -> {
            producto.setNombre(etNombre.getText().toString());
            producto.setCategoria(etCategoria.getText().toString());
            producto.setCantidad(Integer.parseInt(etCantidad.getText().toString()));
            producto.setPrecio(Double.parseDouble(etPrecio.getText().toString()));
            producto.setProveedor(etProveedor.getText().toString());
            Toast.makeText(this, "Producto actualizado correctamente", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnCancelar.setOnClickListener(v -> {
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}