package com.example.appinventario;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import android.content.ContentValues;

import java.util.ArrayList;

public class ProductoDbHelper extends SQLiteOpenHelper {

    public ProductoDbHelper(Context context){
        super(context, "apprestaurante.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE productos ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                " categoria TEXT, " +
                " cantidad INTEGER, " +
                "precio REAL, " +
                " proveedor TEXT, " +
                "imagenUri TEXT" +
                ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // insertar datos
    public void insertarProducto(Producto producto) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();

        valores.put("nombre", producto.getNombre());
        valores.put("categoria", producto.getCategoria());
        valores.put("cantidad", producto.getCantidad());
        valores.put("precio", producto.getPrecio());
        valores.put("proveedor", producto.getProveedor());
        valores.put("imagenUri", producto.getImagenUri());

        db.insert("productos", null, valores);

        db.close();
    }

    // obtener los productos de sqllite
    public ArrayList<Producto> obtenerProductos() {

        ArrayList<Producto> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM productos", null
        );

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow("id")
            );
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")
            );
            String categoria = cursor.getString(cursor.getColumnIndexOrThrow("categoria")
            );
            int cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad")
            );
            double precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio")
            );
            String proveedor = cursor.getString(cursor.getColumnIndexOrThrow("proveedor")
            );
            String imagenUri = cursor.getString(cursor.getColumnIndexOrThrow("imagenUri")
            );
            Producto producto = new Producto(
                    nombre,
                    categoria,
                    cantidad,
                    precio,
                    proveedor,
                    imagenUri
            );
            producto.setId(id);
            lista.add(producto);
        }
        cursor.close();db.close();
        return lista;
    }
    public Producto obtenerProductoPorId(long id) { // para obtener producto por id
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM productos WHERE id = ?", new String[]{String.valueOf(id)}
        );

        Producto producto = null;

        if (cursor.moveToFirst()) {

            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre")
            );

            String categoria = cursor.getString(cursor.getColumnIndexOrThrow("categoria")
            );

            int cantidad = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad")
            );

            double precio = cursor.getDouble(cursor.getColumnIndexOrThrow("precio")
            );

            String proveedor = cursor.getString(cursor.getColumnIndexOrThrow("proveedor")
            );

            String imagenUri = cursor.getString(cursor.getColumnIndexOrThrow("imagenUri")
            );

            producto = new Producto(
                    nombre,
                    categoria,
                    cantidad,
                    precio,
                    proveedor,
                    imagenUri
            );
            producto.setId(id);
        }
        cursor.close();
        db.close();

        return producto;
    }
    // eliminar en sqlite
    public void eliminarProducto(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(
                "productos",
                "id = ?",
                new String[]{String.valueOf(id)}
        );
        db.close();
    }
    //editar o actualizar desde sqlite
    public void actualizarProducto(Producto producto) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues valores = new ContentValues();

        valores.put("nombre", producto.getNombre());
        valores.put("categoria", producto.getCategoria());
        valores.put("cantidad", producto.getCantidad());
        valores.put("precio", producto.getPrecio());
        valores.put("proveedor", producto.getProveedor());
        valores.put("imagenUri", producto.getImagenUri());

        db.update(
                "productos",
                valores,
                "id = ?",
                new String[]{String.valueOf(producto.getId())}
        );
        db.close();
    }

}
