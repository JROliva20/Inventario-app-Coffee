package com.example.appinventario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class RecetarioActivity extends AppCompatActivity {
    LinearLayout btnCocina;
    TextView btnExplorarRecetas;
    LinearLayout btnBebidas;
    LinearLayout btnPostres;

    LinearLayout opcionesCocina;
    LinearLayout opcionesBebidas;
    LinearLayout opcionesPostres;

    TextView flechaCocina;
    TextView flechaBebidas;
    TextView flechaPostres;

    MaterialButton btnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recetario);

        btnCocina = findViewById(R.id.btnCocina);
        btnExplorarRecetas =findViewById(R.id.btnExplorarRecetas);
        btnBebidas = findViewById(R.id.btnBebidas);
        btnPostres = findViewById(R.id.btnPostres);

        opcionesCocina = findViewById(R.id.opcionesCocina);
        opcionesBebidas = findViewById(R.id.opcionesBebidas);
        opcionesPostres = findViewById(R.id.opcionesPostres);

        flechaCocina = findViewById(R.id.flechaCocina);
        flechaBebidas = findViewById(R.id.flechaBebidas);
        flechaPostres = findViewById(R.id.flechaPostres);

        //cocina
        btnCocina.setOnClickListener(v -> {
            boolean abierto = opcionesCocina.getVisibility() == View.VISIBLE;
            cerrarOpciones();
            if (!abierto) {
                opcionesCocina.setVisibility(View.VISIBLE);
                flechaCocina.setText("^");
            }
        });

        btnExplorarRecetas.setOnClickListener(v -> {
            Intent intent = new Intent(
                    RecetarioActivity.this,
                    RecetasExternasActivity.class
            );
            startActivity(intent);
        });

        //Bebidas
        btnBebidas.setOnClickListener(v -> {
            boolean abierto = opcionesBebidas.getVisibility() == View.VISIBLE;
            cerrarOpciones();
            if (!abierto) {
                opcionesBebidas.setVisibility(View.VISIBLE);
                flechaBebidas.setText("^");
            }
        });
        //Postres
        btnPostres.setOnClickListener(v -> {
            boolean abierto = opcionesPostres.getVisibility()  == View.VISIBLE;
            cerrarOpciones();
            if (!abierto) {
                opcionesPostres.setVisibility(View.VISIBLE);
                flechaPostres.setText("^");
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnRegresar = findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(v -> finish());
    }

    private void cerrarOpciones(){
        opcionesCocina.setVisibility(View.GONE);
        opcionesBebidas.setVisibility(View.GONE);
        opcionesPostres.setVisibility(View.GONE);

        flechaCocina.setText("∨");
        flechaBebidas.setText("∨");
        flechaPostres.setText("∨");
    }
}

