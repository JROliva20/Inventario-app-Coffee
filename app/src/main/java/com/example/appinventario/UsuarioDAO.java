package com.example.appinventario;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class UsuarioDAO {

    private ProductoDbHelper dbHelper;

    public UsuarioDAO(Context context) {
        dbHelper = new ProductoDbHelper(context);
    }

    public long insertarUsuario(Usuario usuario) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombre", usuario.getNombre());
        valores.put("usuario", usuario.getUsuario());
        valores.put("password", usuario.getPassword());
        valores.put("rol", usuario.getRol());
        valores.put("estado", usuario.getEstado());
        long id = db.insert("usuarios", null, valores);
        db.close();
        return id;
    }

    // para comprobar si ya existe un nombre de usuario
    public boolean existeUsuario(String usuario) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id FROM usuarios WHERE usuario = ?",
                new String[]{usuario}
        );
        boolean existe = cursor.moveToFirst();
        cursor.close();
        db.close();

        return existe;
    }

    //para obtener todos los usuarios
    public ArrayList<Usuario> obtenerUsuarios() {
        ArrayList<Usuario> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM usuarios",
                null
        );
        while (cursor.moveToNext()) {

            long id = cursor.getLong(
                    cursor.getColumnIndexOrThrow("id")
            );
            String nombre = cursor.getString(
                    cursor.getColumnIndexOrThrow("nombre")
            );
            String usuario = cursor.getString(
                    cursor.getColumnIndexOrThrow("usuario")
            );
            String password = cursor.getString(
                    cursor.getColumnIndexOrThrow("password")
            );
            String rol = cursor.getString(
                    cursor.getColumnIndexOrThrow("rol")
            );
            String estado = cursor.getString(
                    cursor.getColumnIndexOrThrow("estado")
            );
            Usuario nuevoUsuario = new Usuario(
                    id,
                    nombre,
                    usuario,
                    password,
                    rol,
                    estado
            );
            lista.add(nuevoUsuario);
        }
        cursor.close();
        db.close();

        return lista;
    }

    //obtenemos el usuario por su id
    public Usuario obtenerUsuarioPorId(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM usuarios WHERE id = ?",
                new String[]{String.valueOf(id)}
        );

        Usuario usuario = null;
        if (cursor.moveToFirst()) {
            long idUsuario = cursor.getLong(
                    cursor.getColumnIndexOrThrow("id")
            );
            String nombre = cursor.getString(
                    cursor.getColumnIndexOrThrow("nombre")
            );
            String nombreUsuario = cursor.getString(
                    cursor.getColumnIndexOrThrow("usuario")
            );
            String password = cursor.getString(
                    cursor.getColumnIndexOrThrow("password")
            );
            String rol = cursor.getString(
                    cursor.getColumnIndexOrThrow("rol")
            );
            String estado = cursor.getString(
                    cursor.getColumnIndexOrThrow("estado")
            );

            usuario = new Usuario(
                    idUsuario,
                    nombre,
                    nombreUsuario,
                    password,
                    rol,
                    estado
            );
        }

        cursor.close();
        db.close();

        return usuario;
    }
    // actualizar un usuario
    public void actualizarUsuario(Usuario usuario) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombre", usuario.getNombre());
        valores.put("usuario", usuario.getUsuario());
        valores.put("password", usuario.getPassword());
        valores.put("rol", usuario.getRol());
        valores.put("estado", usuario.getEstado());
        db.update(
                "usuarios", valores, "id = ?",
                new String[]{String.valueOf(usuario.getId())}
        );
        db.close();
    }
    //eliminar por id
    public void eliminarUsuario(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(
                "usuarios", "id = ?",
                new String[]{String.valueOf(id)}
        );
        db.close();
    }
    // VALIDACION
    public Usuario validarLogin(String usuario, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM usuarios WHERE usuario = ? AND password = ? AND estado = ? ",
                new String[]{usuario, password, "ACTIVO"}
        );

        Usuario resultado = null;
        if (cursor.moveToFirst()) {
            long id = cursor.getLong(
                    cursor.getColumnIndexOrThrow("id")
            );
            String nombre = cursor.getString(
                    cursor.getColumnIndexOrThrow("nombre")
            );
            String nombreUsuario = cursor.getString(
                    cursor.getColumnIndexOrThrow("usuario")
            );
            String clave = cursor.getString(
                    cursor.getColumnIndexOrThrow("password")
            );
            String rol = cursor.getString(
                    cursor.getColumnIndexOrThrow("rol")
            );
            String estado = cursor.getString(
                    cursor.getColumnIndexOrThrow("estado")
            );

            resultado = new Usuario(
                    id,
                    nombre,
                    nombreUsuario,
                    clave,
                    rol,
                    estado
            );
        }
        cursor.close();
        db.close();

        return resultado;
    }
}
