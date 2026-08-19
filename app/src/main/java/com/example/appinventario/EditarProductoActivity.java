package com.example.appinventario;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class EditarProductoActivity extends AppCompatActivity {

    TextInputEditText etNombre, etCategoria, etCantidad, etPrecio, etProveedor;
    TextInputLayout tilNombre, tilCategoria, tilCantidad, tilPrecio, tilProveedor;
    MaterialButton btnActualizar, btnCancelar;
    MaterialButton btnGaleria, btnCamara;
    ImageView imgProducto;

    int posicion;
    Producto producto;
    Uri imagenSeleccionada;


    // permisos para la camara
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
    // para la toma de la foto
    ActivityResultLauncher<Uri> tomarFoto =
            registerForActivityResult(
                    new ActivityResultContracts.TakePicture(),
                    resultado -> {
                        if (resultado) {
                            imgProducto.setImageURI(imagenSeleccionada);
                        }
                    });
    // permisos para la galeria
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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_producto);
        // RECIBIR LA POSICIÓN DEL PRODUCTO
        posicion = getIntent().getIntExtra("posicion", -1);
        if (posicion == -1) {
            finish();
            return;
        }
        // creamos el producto
        producto = Datos.listaProductos.get(posicion);
        // conectamos xml con java
        etNombre = findViewById(R.id.etNombre);
        etCategoria = findViewById(R.id.etCategoria);
        etCantidad = findViewById(R.id.etCantidad);
        etPrecio = findViewById(R.id.etPrecio);
        etProveedor = findViewById(R.id.etProveedor);
        // validaciones
        tilNombre = findViewById(R.id.tilNombre);
        tilCategoria = findViewById(R.id.tilCategoria);
        tilCantidad = findViewById(R.id.tilCantidad);
        tilPrecio = findViewById(R.id.tilPrecio);
        tilProveedor = findViewById(R.id.tilProveedor);
        // conexicn de botones
        btnActualizar = findViewById(R.id.btnActualizar);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnGaleria = findViewById(R.id.btnGaleria);
        btnCamara = findViewById(R.id.btnCamara);
        imgProducto = findViewById(R.id.imgProducto);
        // cargaos los datos existentes
        etNombre.setText(producto.getNombre());

        etCategoria.setText(producto.getCategoria());

        etCantidad.setText(
                String.valueOf(producto.getCantidad())
        );
        etPrecio.setText(
                String.valueOf(producto.getPrecio())
        );
        etProveedor.setText(producto.getProveedor());
        // cargar la imagen existente
        if (producto.getImagenUri() != null &&
                !producto.getImagenUri().isEmpty()) {
            Uri uriImagen = Uri.parse(producto.getImagenUri());
            try {
                InputStream entrada =
                        getContentResolver().openInputStream(uriImagen);
                Bitmap imagen =
                        BitmapFactory.decodeStream(entrada);
                if (entrada != null) {
                    entrada.close();
                }
                if (imagen != null) {imgProducto.setImageBitmap(imagen);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "No se pudo cargar la imagen",
                        Toast.LENGTH_SHORT).show();
            }
        }
        // boton galeria
        btnGaleria.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            seleccionarImagen.launch(intent);
        });
        // botonde la camara
        btnCamara.setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                pedirPermisoCamara.launch(
                        Manifest.permission.CAMERA
                );
            } else {
                abrirCamara();
            }
        });
        // actualizar producto
        btnActualizar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String categoria = etCategoria.getText().toString().trim();
            String txtCantidad = etCantidad.getText().toString().trim();
            String txtPrecio = etPrecio.getText().toString().trim();
            String proveedor = etProveedor.getText().toString().trim();
            // validaciones
            if (nombre.isEmpty()) {tilNombre.setError(
                        "Ingrese el nombre del producto"
                );
                etNombre.requestFocus();
                return;
            }

            if (categoria.isEmpty()) {
                tilCategoria.setError(
                        "Ingrese la categoría"
                );
                etCategoria.requestFocus();
                return;
            }

            if (txtCantidad.isEmpty()) {
                tilCantidad.setError(
                        "Ingrese la cantidad"
                );
                etCantidad.requestFocus();
                return;
            }

            if (txtPrecio.isEmpty()) {
                tilPrecio.setError(
                        "Ingrese el precio"
                );
                etPrecio.requestFocus();
                return;
            }

            if (proveedor.isEmpty()) {
                tilProveedor.setError(
                        "Ingrese el proveedor"
                );
                etProveedor.requestFocus();
                return;
            }

            int cantidad =
                    Integer.parseInt(txtCantidad);
            double precio =
                    Double.parseDouble(txtPrecio);

            if (cantidad <= 0) {
                tilCantidad.setError(
                        "La cantidad debe ser mayor que 0"
                );
                etCantidad.requestFocus();
                return;
            }

            if (precio <= 0) {
                tilPrecio.setError(
                        "El precio debe ser mayor que 0"
                );
                etPrecio.requestFocus();
                return;
            }
            // actualizar el producto
            producto.setNombre(nombre);
            producto.setCategoria(categoria);
            producto.setCantidad(cantidad);
            producto.setPrecio(precio);
            producto.setProveedor(proveedor);

            // si selecionamos una nueva imagen; remplazamos la anterior
            if (imagenSeleccionada != null) {
                producto.setImagenUri(
                        imagenSeleccionada.toString()
                );
            }
            Toast.makeText(
                    this,
                    "Producto actualizado correctamente",
                    Toast.LENGTH_SHORT
            ).show();
            finish();
        });
        // regresar
        btnCancelar.setOnClickListener(v -> {
            finish();
        });
        // teclado y barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {

                    Insets systemBars =
                            insets.getInsets(
                                    WindowInsetsCompat.Type.systemBars()
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
    // abrir la camara
    private void abrirCamara() {
        ContentValues valores =
                new ContentValues();
        valores.put(
                MediaStore.Images.Media.DISPLAY_NAME,
                "producto_" +
                        System.currentTimeMillis() + ".jpg"
        );
        valores.put(
                MediaStore.Images.Media.MIME_TYPE, "image/jpeg"
        );
        imagenSeleccionada =
                getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        valores
                );
        tomarFoto.launch(imagenSeleccionada);
    }
    // copiar imagen de la galeria
    private Uri copiarImagen(Uri uriOriginal) {
        try {
            InputStream entrada =
                    getContentResolver()
                            .openInputStream(uriOriginal);
            File archivo =
                    new File(
                            getFilesDir(),
                            "producto_" +
                                    System.currentTimeMillis() + ".jpg"
                    );
            FileOutputStream salida =
                    new FileOutputStream(archivo);
            byte[] buffer = new byte[1024];
            int cantidad;
            while ((cantidad =
                    entrada.read(buffer)) != -1) {
                salida.write(
                        buffer,
                        0,
                        cantidad
                );
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