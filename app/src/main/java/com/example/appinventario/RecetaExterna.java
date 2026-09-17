package com.example.appinventario;

public class RecetaExterna {
    private String nombre;
    private String categoria;
    private String origen;
    private String imagenUrl;
    private String instrucciones;

    public RecetaExterna (){
    }
    public RecetaExterna(String nombre, String categoria, String origen, String imagenUrl, String instrucciones) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.origen = origen;
        this.imagenUrl = imagenUrl;
        this.instrucciones = instrucciones;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }
}
