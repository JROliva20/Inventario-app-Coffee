package com.example.appinventario;

import android.os.Bundle;

import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public class AgregarProductoActivity extends AppCompatActivity {

    TextInputEditText etNombre;
    TextInputEditText etCategoria;
    TextInputEditText etCantidad;
    TextInputEditText etPrecio;
    TextInputEditText etProveedor;
    MaterialButton btnGuardar;
    MaterialButton btnCancelar;

    TextInputLayout tilNombre;
    TextInputLayout tilCategoria;
    TextInputLayout tilCantidad;
    TextInputLayout tilPrecio;
    TextInputLayout tilProveedor;

    ImageView imgProducto;
    MaterialButton btnGaleria;
    MaterialButton btnCamara;
    Uri imagenSeleccionada;

    ActivityResultLauncher<String> pedirPermisoCamara =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    concedido -> {
                        if (concedido) {
                            abrirCamara();
                        } else {
                            Toast.makeText(this,
                                    "Permiso de cámara necesario",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
    ActivityResultLauncher<Uri> tomarFoto =
            registerForActivityResult(
                    new ActivityResultContracts.TakePicture(),
                    resultado -> {
                        if (resultado) {
                            imgProducto.setImageURI(imagenSeleccionada);
                        }
                    });

    ActivityResultLauncher<Intent> seleccionarImagen =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK &&
                                result.getData() != null) {
                            Uri uriOriginal = result.getData().getData();
                            imagenSeleccionada = copiarImagen(uriOriginal);
                            if (imagenSeleccionada != null) {
                                imgProducto.setImageURI(imagenSeleccionada);
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            int teclado = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom;
            v.setPadding(
                    v.getPaddingLeft(),
                    v.getPaddingTop(),
                    v.getPaddingRight(),
                    teclado
            );
            return insets;
        });

        etNombre = findViewById(R.id.etNombre);
        etCategoria = findViewById(R.id.etCategoria);
        etCantidad = findViewById(R.id.etCantidad);
        etPrecio = findViewById(R.id.etPrecio);
        etProveedor = findViewById(R.id.etProveedor);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);
        tilNombre = findViewById(R.id.tilNombre);
        tilCategoria = findViewById(R.id.tilCategoria);
        tilCantidad = findViewById(R.id.tilCantidad);
        tilPrecio = findViewById(R.id.tilPrecio);
        tilProveedor = findViewById(R.id.tilProveedor);

        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String categoria = etCategoria.getText().toString().trim();
            String txtCantidad = etCantidad.getText().toString().trim();
            String txtPrecio = etPrecio.getText().toString().trim();
            String proveedor = etProveedor.getText().toString().trim();

            if (nombre.isEmpty()) {
                tilNombre.setError("Ingrese el nombre del producto");
                etNombre.requestFocus();
                return;
            }
            if (categoria.isEmpty()) {
                tilCategoria.setError("Ingrese la categoría");
                etCategoria.requestFocus();
                return;
            }
            if (txtCantidad.isEmpty()) {
                tilCantidad.setError("Ingrese la cantidad");
                etCantidad.requestFocus();
                return;
            }
            if (txtPrecio.isEmpty()) {
                tilPrecio.setError("Ingrese el precio");
                etPrecio.requestFocus();
                return;
            }
            if (proveedor.isEmpty()) {
                tilProveedor.setError("Ingrese el proveedor");
                etProveedor.requestFocus();
                return;
            }
            int cantidad = Integer.parseInt(txtCantidad);
            double precio = Double.parseDouble(txtPrecio);

            if (cantidad <= 0) {
                tilCantidad.setError("La cantidad debe ser mayor que 0");
                etCantidad.requestFocus();
                return;
            }

            if (precio <= 0) {
                tilPrecio.setError("El precio debe ser mayor que 0");
                etPrecio.requestFocus();
                return;
            }

            String imagenUri = "";

            if(imagenSeleccionada != null ){
                imagenUri = imagenSeleccionada.toString();
            }

            Producto producto = new Producto(
                    nombre,
                    categoria,
                    cantidad,
                    precio,
                    proveedor,
                    imagenUri
            );
            Datos.listaProductos.add(producto);

            etNombre.setText("");
            etCategoria.setText("");
            etCantidad.setText("");
            etPrecio.setText("");
            etProveedor.setText("");
            imagenSeleccionada = null;
            imgProducto.setImageResource(R.drawable.photo);

            etNombre.requestFocus();

            Toast.makeText(this,
                    "Producto agregado correctamente",
                    Toast.LENGTH_SHORT).show();
        });

        btnCancelar.setOnClickListener(v -> {
            finish();
        });

        imgProducto = findViewById(R.id.imgProducto);
        btnGaleria = findViewById(R.id.btnGaleria);
        btnCamara = findViewById(R.id.btnCamara);

        btnGaleria.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            seleccionarImagen.launch(intent);

        });

        btnCamara.setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                pedirPermisoCamara.launch(Manifest.permission.CAMERA);
            } else {
                abrirCamara();
            }
        });
    }
        private void abrirCamara() {
            ContentValues valores = new ContentValues();
            valores.put(MediaStore.Images.Media.DISPLAY_NAME,
                    "producto_" + System.currentTimeMillis() + ".jpg");
            valores.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            imagenSeleccionada = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    valores
            );
            tomarFoto.launch(imagenSeleccionada);
        }
    private Uri copiarImagen(Uri uriOriginal) {
        try {
            InputStream entrada = getContentResolver().openInputStream(uriOriginal);
            File archivo = new File(
                    getFilesDir(),
                    "producto_" + System.currentTimeMillis() + ".jpg"
            );
            FileOutputStream salida = new FileOutputStream(archivo);
            byte[] buffer = new byte[1024];
            int cantidad;

            while ((cantidad = entrada.read(buffer)) != -1) {
                salida.write(buffer, 0, cantidad);
            }
            entrada.close();
            salida.close();
            return Uri.fromFile(archivo);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    }