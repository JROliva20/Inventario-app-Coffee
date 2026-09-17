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
    TextInputLayout tilUsuario;
    TextInputLayout tilPassword;
    MaterialButton btnIngresar;
    MaterialButton btnRegistro;
    UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        usuarioDAO = new UsuarioDAO(this);

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

            Usuario usuarioEncontrado = usuarioDAO.validarLogin(usuario, password);
            if(usuarioEncontrado != null){
                Toast.makeText(
                        this,
                        "¡Bienvenido " + usuarioEncontrado.getNombre() + "!",
                        Toast.LENGTH_SHORT
                ).show();

                Intent intent = new Intent(
                        Activitylogin.this,
                        MainActivity.class
                );

                intent.putExtra("nombre", usuarioEncontrado.getNombre());
                intent.putExtra("rol", usuarioEncontrado.getRol());

                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Usuario o Contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegistro = findViewById(R.id.btnRegistro);
        btnRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(
                    Activitylogin.this,
                    RegistroUsuarioActivity.class
            );
            startActivity(intent);
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