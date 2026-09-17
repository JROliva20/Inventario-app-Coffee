package com.example.appinventario;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    TextView tvSaludo;
    TextView tvRol;
    MaterialButton btnPedidos;
    MaterialButton btnVentas;
    MaterialButton btnMesas;
    MaterialButton btnInventario;
    MaterialButton btnReportes;
    MaterialButton btnUsuarios;
    MaterialButton btnRecetario;
    MaterialButton btnAcercaDe;
    MaterialButton btnSalir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        tvSaludo = findViewById(R.id.tvSaludo);
        tvRol = findViewById(R.id.tvRol);

        String nombre = getIntent().getStringExtra("nombre");
        String rol = getIntent().getStringExtra("rol");

        if (nombre != null) {
            tvSaludo.setText("¡Hola, " + nombre + "!");
        }

        if (rol != null) {
            tvRol.setText(rol);
        }

        btnPedidos = findViewById(R.id.btnPedidos);
        btnVentas = findViewById(R.id.btnVentas);
        btnMesas = findViewById(R.id.btnMesas);
        btnInventario = findViewById(R.id.btnInventario);
        btnReportes = findViewById(R.id.btnReportes);
        btnRecetario = findViewById(R.id.btnRecetario);
        btnUsuarios = findViewById(R.id.btnUsuarios);
        btnAcercaDe = findViewById(R.id.btnAcercaDe);
        btnSalir = findViewById(R.id.btnSalir);
        btnInventario.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    ProductosActivity.class
            );
            startActivity(intent);
        });

        btnUsuarios.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    UsuariosActivity.class
            );
            startActivity(intent);
        });

        btnRecetario.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    RecetarioActivity.class
            );
            startActivity(intent);
        });

        btnAcercaDe.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    ActivityAcercade.class
            );
            startActivity(intent);
        });

        btnSalir.setOnClickListener(v -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Salir")
                    .setMessage("¿Desea salir de la aplicación?")
                    .setPositiveButton("Sí", (dialog, which) -> finish())
                    .setNegativeButton("No", null)
                    .show();
        });

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
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
}