package com.example.tpf_paii_android.modelos;

public class Genero {

    private int id_genero;
    private String descripcion;

    public Genero() {
    }

    public int getId_genero() {
        return id_genero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return "Genero{" +
                "id_genero=" + id_genero +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
