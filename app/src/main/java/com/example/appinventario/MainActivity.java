package com.example.appinventario;

import android.os.Bundle;
import android.content.Intent;
import com.google.android.material.button.MaterialButton;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
//aca declare los botones
    MaterialButton btnProductos;
    MaterialButton btnAgregar;
    MaterialButton btnEditar;
    MaterialButton btnEliminarProducto;
    MaterialButton btnInfo;
    MaterialButton btnSalir;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        //conecto los botones con el xml
        btnProductos = findViewById(R.id.btnProductos);
        btnAgregar = findViewById(R.id.btnAggProducto);
        btnEditar = findViewById(R.id.btnEditarproducto);
        btnEliminarProducto = findViewById(R.id.btnEliminarproducto);
        btnInfo = findViewById(R.id.btnInfo);
        btnSalir = findViewById(R.id.btnSalir);

        btnProductos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProductosActivity.class);
            startActivity(intent);
        });

        btnAgregar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AgregarProductoActivity.class);
            startActivity(intent);
        });

        btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProductosActivity.class);
            startActivity(intent);
        });

        btnEliminarProducto.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProductosActivity.class);
            startActivity(intent);
        });

        btnInfo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ActivityAcercade.class);
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

        ArrayList<Producto> listaProductos = new ArrayList<>();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}