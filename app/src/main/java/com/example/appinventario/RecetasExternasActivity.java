package com.example.appinventario;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class RecetasExternasActivity extends AppCompatActivity {

    RecyclerView rvRecetas;
    RecetaExternaAdapter adapter;
    ArrayList<RecetaExterna> listaRecetas;
    TextInputEditText etBuscarReceta;
    MaterialButton btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recetas_externas);

        // Botón regresar
        btnRegresar = findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(v -> finish());

        // RecyclerView
        rvRecetas = findViewById(R.id.rvRecetas);
        rvRecetas.setLayoutManager(new LinearLayoutManager(this));
        listaRecetas = new ArrayList<>();
        adapter = new RecetaExternaAdapter(listaRecetas);
        rvRecetas.setAdapter(adapter);

        // Campo de búsqueda
        etBuscarReceta = findViewById(R.id.etBuscarReceta);

        etBuscarReceta.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String texto = etBuscarReceta.getText().toString().trim();

                if (!texto.isEmpty()) {
                    cargarRecetas(texto);
                }
                return true;
            }
            return false;
        });

        // Limpiar resultados al borrar la búsqueda
        etBuscarReceta.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    listaRecetas.clear();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void cargarRecetas(String nombre) {
        new Thread(() -> {
            ServicioRecetasAPI servicio = new ServicioRecetasAPI();
            String respuesta = servicio.buscarRecetas(nombre);
            ArrayList<RecetaExterna> recetas = servicio.convertirJSON(respuesta);

            runOnUiThread(() -> {
                listaRecetas.clear();

                if (recetas.isEmpty()) {
                    adapter.notifyDataSetChanged();
                    Toast.makeText(
                            RecetasExternasActivity.this,
                            "No se encontraron recetas",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                listaRecetas.addAll(recetas);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }
}