package com.example.tpf_paii_android.modelos;

public class Modalidad {

    private int id_modalidad;
    private String descripcion;

    public Modalidad() {
    }

    public int getId_modalidad() {
        return id_modalidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return "Modalidad{" +
                "id_modalidad=" + id_modalidad +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}