package com.example.appinventario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import java.util.ArrayList;

import android.text.Editable;
import android.text.TextWatcher;

public class UsuariosActivity extends AppCompatActivity {
    RecyclerView rvUsuarios;
    UsuarioAdapter adapter;
    ArrayList<Usuario> usuariosMostrados;
    UsuarioDAO usuarioDAO;
    TextInputEditText etBuscar;
    TextView tvNoEncontrado;
    MaterialButton btnNuevoUsuario;
    MaterialButton btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_usuarios);


        rvUsuarios = findViewById(R.id.rvUsuarios);
        rvUsuarios.setLayoutManager(new LinearLayoutManager(this));
        usuarioDAO = new UsuarioDAO(this);

        usuariosMostrados = usuarioDAO.obtenerUsuarios();

        // Creamos el Adapter
        adapter = new UsuarioAdapter(usuariosMostrados);

        // Conectamos el Adapter con el RecyclerView
        rvUsuarios.setAdapter(adapter);

        etBuscar = findViewById(R.id.etBuscar);
        tvNoEncontrado = findViewById(R.id.tvNoEncontrado);
        btnNuevoUsuario = findViewById(R.id.btnNuevoUsuario);
        btnVolver = findViewById(R.id.btnVolver);

        btnNuevoUsuario.setOnClickListener(v -> {
            Intent intent = new Intent(
                    UsuariosActivity.this,
                    RegistroUsuarioActivity.class
            );
            intent.putExtra("desde_usuarios", true);
            startActivity(intent);
        });

        btnVolver.setOnClickListener(v -> {
            Intent intent = new Intent(
                    UsuariosActivity.this,
                    MainActivity.class
            );
            startActivity(intent);
        });
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

                filtrarUsuarios();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Ajuste de pantalla
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()
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

    @Override
    protected void onResume() {
        super.onResume();

        usuariosMostrados = usuarioDAO.obtenerUsuarios();
        adapter.actualizarLista(usuariosMostrados);

        filtrarUsuarios();
    }

    // Filtrar usuarios por nombre o usuario
    private void filtrarUsuarios() {

        String texto = etBuscar.getText().toString().trim().toLowerCase();

        ArrayList<Usuario> usuariosFiltrados = new ArrayList<>();

        for (Usuario u : usuariosMostrados) {
            if (u.getNombre().toLowerCase().contains(texto) ||
                    u.getUsuario().toLowerCase().contains(texto)) {

                usuariosFiltrados.add(u);
            }
        }

        // Actualizamos el RecyclerView
        adapter.actualizarLista(usuariosFiltrados);

        // Mensaje si no encontramos resultados
        if (usuariosFiltrados.isEmpty() && !texto.isEmpty()) {
            tvNoEncontrado.setVisibility(View.VISIBLE);

        } else {
            tvNoEncontrado.setVisibility(View.GONE);
        }
    }
}