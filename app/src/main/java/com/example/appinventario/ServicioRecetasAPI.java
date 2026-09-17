package com.example.appinventario;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ServicioRecetasAPI {

    public String buscarRecetas(String nombre) {

        try {

            String direccion =
                    "https://www.themealdb.com/api/json/v1/1/search.php?s=" + nombre;

            URL url = new URL(direccion);

            HttpURLConnection conexion =
                    (HttpURLConnection) url.openConnection();

            conexion.setRequestMethod("GET");

            BufferedReader lector = new BufferedReader(
                    new InputStreamReader(
                            conexion.getInputStream()
                    )
            );

            StringBuilder respuesta = new StringBuilder();

            String linea;

            while ((linea = lector.readLine()) != null) {
                respuesta.append(linea);
            }

            lector.close();
            conexion.disconnect();

            return respuesta.toString();

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public ArrayList<RecetaExterna> convertirJSON(String json) {

        ArrayList<RecetaExterna> listaRecetas = new ArrayList<>();

        try {
            JSONObject objetoPrincipal = new JSONObject(json);
            JSONArray comidas = objetoPrincipal.getJSONArray("meals");

            for (int i = 0; i < comidas.length(); i++) {

                JSONObject comida = comidas.getJSONObject(i);

                String nombre =
                        comida.getString("strMeal");

                String categoria =
                        comida.getString("strCategory");

                String origen =
                        comida.getString("strArea");

                String imagenUrl =
                        comida.getString("strMealThumb");

                String instrucciones =
                        comida.getString("strInstructions");

                RecetaExterna receta = new RecetaExterna(
                        nombre,
                        categoria,
                        origen,
                        imagenUrl,
                        instrucciones
                );

                listaRecetas.add(receta);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return listaRecetas;
    }
}