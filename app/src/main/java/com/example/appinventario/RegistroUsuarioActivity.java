package com.example.appinventario;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegistroUsuarioActivity extends AppCompatActivity {

    TextInputEditText etNombre, etUsuario, etNuevaPassword, etConfirmarPassword;
    TextInputLayout tilNombre, tilUsuario, tilNuevaPassword, tilConfirmarPassword, tilRol;
    AutoCompleteTextView actvRol;
    MaterialButton btnRegistrarUsuario, btnVolverLogin;
    UsuarioDAO usuarioDAO;
    long usuarioId = -1;
    boolean modoEdicion = false;
    boolean desdeUsuarios = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_usuario);

        usuarioDAO = new UsuarioDAO(this);
        usuarioId = getIntent().getLongExtra("usuario_id", -1);
        modoEdicion = usuarioId != -1;
        desdeUsuarios = getIntent().getBooleanExtra("desde_usuarios", false);

        etNombre = findViewById(R.id.etNombre);
        etUsuario = findViewById(R.id.etUsuario);
        etNuevaPassword = findViewById(R.id.etNuevaPassword);
        etConfirmarPassword = findViewById(R.id.etConfirmarPassword);

        tilNombre = findViewById(R.id.tilNombre);
        tilUsuario = findViewById(R.id.tilUsuario);
        tilNuevaPassword = findViewById(R.id.tilNuevaPassword);
        tilConfirmarPassword = findViewById(R.id.tilConfirmarPassword);
        tilRol = findViewById(R.id.tilRol);

        actvRol = findViewById(R.id.actvRol);

        String[] roles = {
                "DEVELOPER", "ADMINISTRADOR", "RRHH",
                "AUDITOR", "COCINA", "CAJERO", "MESERO"
        };

        ArrayAdapter<String> adapterRoles = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                roles
        );

        actvRol.setAdapter(adapterRoles);

        if (modoEdicion) {
            Usuario usuario = usuarioDAO.obtenerUsuarioPorId(usuarioId);

            if (usuario != null) {
                etNombre.setText(usuario.getNombre());
                etUsuario.setText(usuario.getUsuario());
                actvRol.setText(usuario.getRol(), false);
            }
        }

        btnRegistrarUsuario = findViewById(R.id.btnRegistrarUsuario);
        btnRegistrarUsuario.setText(
                modoEdicion ? "Actualizar usuario" : "Registrar usuario"
        );
        btnRegistrarUsuario.setOnClickListener(v -> guardarUsuario());

        btnVolverLogin = findViewById(R.id.btnVolverLogin);

        if (desdeUsuarios || modoEdicion) {
            btnVolverLogin.setText("Volver a usuarios");
            btnVolverLogin.setOnClickListener(v -> finish());
        } else {
            btnVolverLogin.setText("Volver al login");
            btnVolverLogin.setOnClickListener(v -> {
                Intent intent = new Intent(
                        RegistroUsuarioActivity.this,
                        Activitylogin.class
                );
                startActivity(intent);
                finish();
            });
        }

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {
                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );
                    Insets teclado = insets.getInsets(
                            WindowInsetsCompat.Type.ime()
                    );

                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            teclado.bottom
                    );
                    return insets;
                }
        );
    }

    private void guardarUsuario() {

        String nombre = etNombre.getText().toString().trim();
        String usuario = etUsuario.getText().toString().trim();
        String password = etNuevaPassword.getText().toString().trim();
        String confirmarPassword = etConfirmarPassword.getText().toString().trim();
        String rol = actvRol.getText().toString().trim();

        tilNombre.setError(null);
        tilUsuario.setError(null);
        tilNuevaPassword.setError(null);
        tilConfirmarPassword.setError(null);
        tilRol.setError(null);

        if (nombre.isEmpty()) {
            tilNombre.setError("Ingrese el nombre");
            etNombre.requestFocus();
            return;
        }

        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            tilNombre.setError("El nombre solo puede contener letras");
            etNombre.requestFocus();
            return;
        }

        if (usuario.isEmpty()) {
            tilUsuario.setError("Ingrese un usuario");
            etUsuario.requestFocus();
            return;
        }

        if (!usuario.matches("[a-zA-Z0-9_.]+")) {
            tilUsuario.setError("Solo letras, números, punto y guion bajo");
            etUsuario.requestFocus();
            return;
        }

        if (usuario.length() < 4 || usuario.length() > 20) {
            tilUsuario.setError("El usuario debe tener entre 4 y 20 caracteres");
            etUsuario.requestFocus();
            return;
        }

        Usuario usuarioActual = modoEdicion
                ? usuarioDAO.obtenerUsuarioPorId(usuarioId)
                : null;

        if (usuarioDAO.existeUsuario(usuario) &&
                (!modoEdicion ||
                        usuarioActual == null ||
                        !usuario.equals(usuarioActual.getUsuario()))) {

            tilUsuario.setError("Este usuario ya existe");
            etUsuario.requestFocus();
            return;
        }

        if (!modoEdicion || !password.isEmpty()) {

            if (password.length() < 8) {
                tilNuevaPassword.setError("Mínimo 8 caracteres");
                etNuevaPassword.requestFocus();
                return;
            }

            if (!password.matches(".*[A-Z].*")) {
                tilNuevaPassword.setError("Debe contener una mayúscula");
                etNuevaPassword.requestFocus();
                return;
            }

            if (!password.matches(".*[a-z].*")) {
                tilNuevaPassword.setError("Debe contener una minúscula");
                etNuevaPassword.requestFocus();
                return;
            }

            if (!password.matches(".*[0-9].*")) {
                tilNuevaPassword.setError("Debe contener un número");
                etNuevaPassword.requestFocus();
                return;
            }

            if (!password.matches(".*[^a-zA-Z0-9].*")) {
                tilNuevaPassword.setError("Debe contener un carácter especial");
                etNuevaPassword.requestFocus();
                return;
            }

            if (!password.equals(confirmarPassword)) {
                tilConfirmarPassword.setError("Las contraseñas no coinciden");
                etConfirmarPassword.requestFocus();
                return;
            }
        }

        if (rol.isEmpty()) {
            tilRol.setError("Seleccione un rol");
            actvRol.requestFocus();
            return;
        }

        if (modoEdicion) {

            if (password.isEmpty()) {
                password = usuarioActual.getPassword();
            }

            Usuario usuarioEditado = new Usuario(
                    usuarioId,
                    nombre,
                    usuario,
                    password,
                    rol,
                    "ACTIVO"
            );

            usuarioDAO.actualizarUsuario(usuarioEditado);

            Toast.makeText(
                    this, "Usuario actualizado correctamente", Toast.LENGTH_SHORT
            ).show();

        } else {

            Usuario nuevoUsuario = new Usuario(
                    nombre,
                    usuario,
                    password,
                    rol,
                    "ACTIVO"
            );

            long id = usuarioDAO.insertarUsuario(nuevoUsuario);

            if (id == -1) {
                Toast.makeText(
                        this, "No se pudo registrar el usuario", Toast.LENGTH_SHORT
                ).show();
                return;
            }

            Toast.makeText(
                    this, "Usuario registrado correctamente", Toast.LENGTH_SHORT
            ).show();
        }

        finish();
    }
}