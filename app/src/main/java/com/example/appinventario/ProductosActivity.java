package com.example.appinventario;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
    ListView lvProductos;
    ArrayList<String> lista;
    ArrayAdapter<String> adapter;
    TextInputEditText etBuscar;
    TextView tvNoEncontrado;
    Spinner spOrdenar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_productos);
        lvProductos = findViewById(R.id.lvProductos);
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
                            Datos.listaProductos.sort(
                                    Comparator.comparing(
                                            Producto::getNombre,
                                            String.CASE_INSENSITIVE_ORDER
                                    )
                            );

                        } else if (position == 1) {

                            // Nombre Z-A
                            Datos.listaProductos.sort(
                                    Comparator.comparing(
                                            Producto::getNombre,
                                            String.CASE_INSENSITIVE_ORDER
                                    ).reversed()
                            );

                        } else if (position == 2) {

                            // Precio menor a mayor
                            Datos.listaProductos.sort(
                                    Comparator.comparingDouble(
                                            Producto::getPrecio
                                    )
                            );

                        } else if (position == 3) {

                            // Precio mayor a menor
                            Datos.listaProductos.sort(
                                    Comparator.comparingDouble(
                                            Producto::getPrecio
                                    ).reversed()
                            );

                        } else if (position == 4) {

                            // Cantidad menor a mayor
                            Datos.listaProductos.sort(
                                    Comparator.comparingInt(
                                            Producto::getCantidad
                                    )
                            );

                        } else if (position == 5) {

                            // Cantidad mayor a menor
                            Datos.listaProductos.sort(
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
                lista.clear();

                for (Producto p : Datos.listaProductos) {
                    if (p.getNombre().toLowerCase().contains(texto) ||
                            p.getCategoria().toLowerCase().contains(texto) ||
                            p.getProveedor().toLowerCase().contains(texto)) {
                        lista.add(
                                "Nombre: " + p.getNombre() +
                                        "\nCategoría: " + p.getCategoria() +
                                        "\nCantidad: " + p.getCantidad() +
                                        "\nPrecio: Q" + p.getPrecio() +
                                        "\nProveedor: " + p.getProveedor()
                        );
                    }
                }
                if (lista.isEmpty() && !texto.isEmpty()) {
                    tvNoEncontrado.setVisibility(View.VISIBLE);
                } else {
                    tvNoEncontrado.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        lvProductos.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(ProductosActivity.this, DetalleProductoActivity.class);//aca cree la instruccion para ir de un Activtz a otro
            intent.putExtra("posicion", position); //enviamos la posicion dl producto
            startActivity(intent);
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
    private void actualizarLista() {

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
    private void filtrarYOrdenar() {

        String texto = etBuscar.getText().toString()
                .trim()
                .toLowerCase();
        ArrayList<Producto> productosFiltrados = new ArrayList<>();

        // 1. FILTRAMOS
        for (Producto p : Datos.listaProductos) {
            if (p.getNombre().toLowerCase().contains(texto) ||
                    p.getCategoria().toLowerCase().contains(texto) ||
                    p.getProveedor().toLowerCase().contains(texto)) {

                productosFiltrados.add(p);
            }
        }

        // 2. ORDENA
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

        // 3. NOS MUESTRA
        lista.clear();

        for (Producto p : productosFiltrados) {

            lista.add(
                    "Nombre: " + p.getNombre() +
                            "\nCategoría: " + p.getCategoria() +
                            "\nCantidad: " + p.getCantidad() +
                            "\nPrecio: Q" + p.getPrecio() +
                            "\nProveedor: " + p.getProveedor()
            );
        }

        // 4. MENSAJE SI NO HAY RESULTADOS
        if (lista.isEmpty() && !texto.isEmpty()) {
            tvNoEncontrado.setVisibility(View.VISIBLE);
        } else {
            tvNoEncontrado.setVisibility(View.GONE);
        }

        // 5. ACTUALIZAR LISTVIEW
        adapter.notifyDataSetChanged();
    }
    }
