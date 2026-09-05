package com.example.appinventario;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

//para el buscador
import android.text.Editable;
import android.text.TextWatcher;

import java.util.Comparator;

import java.util.ArrayList;

public class ProductosActivity extends AppCompatActivity {

    MaterialButton btnAgregar;
    MaterialButton btnRegresar;

    //NUEVAS VARIABLES
    RecyclerView rvProductos;
    ProductoAdapter adapter;
    ArrayList<Producto> productosMostrados;

    ProductoDbHelper dbHelper;
    TextInputEditText etBuscar;
    TextView tvNoEncontrado;
    Spinner spOrdenar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_productos);

        //IMPLEMENTAMOS RECYCLERVIEW
        rvProductos = findViewById(R.id.rvProductos);
        rvProductos.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new ProductoDbHelper(this);
        productosMostrados = dbHelper.obtenerProductos();

        //creamos el adapter y definimos la accion seleccionada
        adapter = new ProductoAdapter(productosMostrados, producto -> {
            // se obtiene el id q sql le asigno al producto
            long id = producto.getId();
            Intent intent = new Intent(
                    ProductosActivity.this,
                    DetalleProductoActivity.class
            );
            // enviar id al detalle
            intent.putExtra("id", id);

            startActivity(intent);
        });

        rvProductos.setAdapter(adapter);

        etBuscar = findViewById(R.id.etBuscar);
        spOrdenar = findViewById(R.id.spOrdenar);
        String[] opcionesOrden = {
                "Nombre A-Z",
                "Nombre Z-A",
                "Precio menor a mayor",
                "Precio mayor a menor",
                "Cantidad menor a mayor",
                "Cantidad mayor a menor"
        };
        ArrayAdapter<String> adapterOrden =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        opcionesOrden
                );
        adapterOrden.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spOrdenar.setAdapter(adapterOrden);
        spOrdenar.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id) {

                        if (position == 0) {

                            // Nombre A-Z
                            productosMostrados.sort(
                                    Comparator.comparing(
                                            Producto::getNombre,
                                            String.CASE_INSENSITIVE_ORDER
                                    )
                            );

                        } else if (position == 1) {

                            // Nombre Z-A
                            productosMostrados.sort(
                                    Comparator.comparing(
                                            Producto::getNombre,
                                            String.CASE_INSENSITIVE_ORDER
                                    ).reversed()
                            );

                        } else if (position == 2) {

                            // Precio menor a mayor
                            productosMostrados.sort(
                                    Comparator.comparingDouble(
                                            Producto::getPrecio
                                    )
                            );

                        } else if (position == 3) {

                            // Precio mayor a menor
                            productosMostrados.sort(
                                    Comparator.comparingDouble(
                                            Producto::getPrecio
                                    ).reversed()
                            );

                        } else if (position == 4) {

                            // Cantidad menor a mayor
                            productosMostrados.sort(
                                    Comparator.comparingInt(
                                            Producto::getCantidad
                                    )
                            );

                        } else if (position == 5) {

                            // Cantidad mayor a menor
                            productosMostrados.sort(
                                    Comparator.comparingInt(
                                            Producto::getCantidad
                                    ).reversed()
                            );
                        }

                        filtrarYOrdenar();
                    }

                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent) {
                    }
                }
        );

        tvNoEncontrado = findViewById(R.id.tvNoEncontrado);

        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    CharSequence s,
                    int start,
                    int count,
                    int after) {
            }
            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count) {
                String texto = s.toString()
                        .trim()
                        .toLowerCase();
                ArrayList<Producto> productosFiltrados = new ArrayList<>();
                for (Producto p : productosMostrados) {
                    if (p.getNombre().toLowerCase().contains(texto) ||
                            p.getCategoria().toLowerCase().contains(texto) ||
                            p.getProveedor().toLowerCase().contains(texto)) {
                        productosFiltrados.add(p);
                    }
                }
                if (productosFiltrados.isEmpty() && !texto.isEmpty()) {
                    tvNoEncontrado.setVisibility(View.VISIBLE);
                } else {
                    tvNoEncontrado.setVisibility(View.GONE);
                }
                adapter.actualizarLista(productosFiltrados);
            }
                @Override
                public void afterTextChanged (Editable s){
                }
            });

        btnAgregar = findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(ProductosActivity.this, AgregarProductoActivity.class);
            startActivity(intent);
        });

        btnRegresar = findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(v -> {
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    //MAS LIMPIO
    @Override
    protected void onResume() {
        super.onResume();
        productosMostrados = dbHelper.obtenerProductos();
        adapter.actualizarLista(productosMostrados);
        filtrarYOrdenar();
    }
    private void filtrarYOrdenar() {

        String texto = etBuscar.getText().toString()
                .trim()
                .toLowerCase();

        //EL BUSCADOR CREA UNA LISTA DE OBJETOS
        ArrayList<Producto> productosFiltrados = new ArrayList<>();

        // 1. FILTRAMOS (comprobamos si el texto coicide con nombre,categoria o provedor)
        for (Producto p : productosMostrados) {
            if (p.getNombre().toLowerCase().contains(texto) ||
                    p.getCategoria().toLowerCase().contains(texto) ||
                    p.getProveedor().toLowerCase().contains(texto)) {

                //AGREGAMOS EL PRODUCTO ENCONTRADO
                productosFiltrados.add(p);
            }
        }

        // 2. ORDENA(ordenamos la lista filtrada segun la opcion selecionada en el spinner)
        int opcion = spOrdenar.getSelectedItemPosition();
        if (opcion == 0) {

            // Nombre A-Z
            productosFiltrados.sort(
                    Comparator.comparing(
                            Producto::getNombre,
                            String.CASE_INSENSITIVE_ORDER
                    )
            );
        } else if (opcion == 1) {

            // Nombre Z-A
            productosFiltrados.sort(
                    Comparator.comparing(
                            Producto::getNombre,
                            String.CASE_INSENSITIVE_ORDER
                    ).reversed()
            );
        } else if (opcion == 2) {

            // Precio menor a mayor
            productosFiltrados.sort(
                    Comparator.comparingDouble(
                            Producto::getPrecio
                    )
            );
        } else if (opcion == 3) {

            // Precio mayor a menor
            productosFiltrados.sort(
                    Comparator.comparingDouble(
                            Producto::getPrecio
                    ).reversed()
            );
        } else if (opcion == 4) {

            // Cantidad menor a mayor
            productosFiltrados.sort(
                    Comparator.comparingInt(
                            Producto::getCantidad
                    )
            );

        } else if (opcion == 5) {

            // Cantidad mayor a menor
            productosFiltrados.sort(
                    Comparator.comparingInt(
                            Producto::getCantidad
                    ).reversed()
            );
        }

        // 3. ACTUALIZAMOS LA LISTA
        adapter.actualizarLista(productosFiltrados);


        // 4. MENSAJE SI NO HAY RESULTADOS
        if (productosFiltrados.isEmpty() && !texto.isEmpty()) {
            tvNoEncontrado.setVisibility(View.VISIBLE);
        } else {
            tvNoEncontrado.setVisibility(View.GONE);
        }
    }
    }
