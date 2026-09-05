package com.example.appinventario;

import android.app.Activity;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import android.content.Intent;
import android.widget.Toast;

public class Activitylogin extends AppCompatActivity {

    TextInputEditText etUsuario;
    TextInputEditText etPassword;
    MaterialButton btnIngresar;
    TextInputLayout tilUsuario;
    TextInputLayout tilPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        tilUsuario = findViewById(R.id.tilUsuario);
        tilPassword = findViewById(R.id.tilPassword);
        btnIngresar = findViewById(R.id.btnIngresar);

        btnIngresar.setOnClickListener(v -> {
            String usuario = etUsuario.getText().toString().trim();
            String password =etPassword.getText().toString().trim();

            tilUsuario.setError(null);
            tilPassword.setError(null);

            if(usuario.isEmpty()){
                tilUsuario.setError("Ingrese su usuario");
                etUsuario.requestFocus();
                return;
            }

            if(password.isEmpty()){
                tilPassword.setError("Ingrese su contraseña");
                etPassword.requestFocus();
                return;
            }

            if(usuario.equals("admin") && password.equals("1234")) {
                Toast.makeText(this,"Inicio de seción correcto \n ¡ Binvenido"  + usuario + "¡",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(
                        Activitylogin.this,
                        MainActivity.class
                );

                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Usuario o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            int teclado = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;
            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    teclado
            );
            return insets;
        });
    }
}