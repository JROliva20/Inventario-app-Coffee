package com.example.appinventario;

import android.content.Intent;
import android.os.Bundle;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class ProductosActivity extends AppCompatActivity {

    MaterialButton btnEditar;
    MaterialButton btnEliminar;
    MaterialButton btnCancelar;
    MaterialButton btnAgregar;
    ListView lvProductos;
    ArrayList<String> lista;
    ArrayAdapter<String> adapter;
//para selecionar un item
    int posicionSeleccionada = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_productos);
        lvProductos = findViewById(R.id.lvProductos);
        lista = new ArrayList<>();
        for (Producto p : Datos.listaProductos) {
            lista.add(
                    "Nombre: " + p.getNombre() +
                            "\nCategoría: " + p.getCategoria() +
                            "\nCantidad: " + p.getCantidad() +
                            "\nPrecio: Q" + p.getPrecio() +
                            "\nProveedor: " + p.getProveedor()
            );
        }
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                lista
        );
        lvProductos.setAdapter(adapter);

        lvProductos.setOnItemClickListener((parent, view, position, id) -> {
            posicionSeleccionada = position; //Guarda la posición del producto.
        });
        btnEditar = findViewById(R.id.btnEditar);
        btnEditar.setOnClickListener(v -> {
            if (posicionSeleccionada == -1) {
                Toast.makeText(this, "Seleccione un producto", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(ProductosActivity.this, EditarProductoActivity.class);
            intent.putExtra("posicion", posicionSeleccionada); //manda información a la otra Activity.
            startActivity(intent);
        });

        btnEliminar = findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(v -> {
            if (posicionSeleccionada == -1) {
                Toast.makeText(this, "Seleccione un producto", Toast.LENGTH_SHORT).show();
                return;
            }
            new AlertDialog.Builder(ProductosActivity.this)
                    .setTitle("Salir")
                    .setMessage("¿Desea Eliminar este producto?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        Datos.listaProductos.remove(posicionSeleccionada);
                        lista.clear();
                        for (Producto p : Datos.listaProductos) {
                            lista.add(
                                    "Nombre: " + p.getNombre() +
                                            "\nCategoría: " + p.getCategoria() +
                                            "\nCantidad: " + p.getCantidad() +
                                            "\nPrecio: Q" + p.getPrecio() +
                                            "\nProveedor: " + p.getProveedor()
                            );
                        }
                        adapter.notifyDataSetChanged();
                        posicionSeleccionada = -1;
                        Toast.makeText(ProductosActivity.this,
                                "Producto eliminado correctamente",
                                Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
            });
        btnCancelar = findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(view -> {
            finish();
        });

        btnAgregar = findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(ProductosActivity.this, AgregarProductoActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
        @Override
        protected void onResume() {
            super.onResume();
            lista.clear();
            for (Producto p : Datos.listaProductos) {
                lista.add(
                        "Nombre: " + p.getNombre() +
                                "\nCategoría: " + p.getCategoria() +
                                "\nCantidad: " + p.getCantidad() +
                                "\nPrecio: Q" + p.getPrecio() +
                                "\nProveedor: " + p.getProveedor()
                );
            }
            adapter.notifyDataSetChanged();
        }
    }
