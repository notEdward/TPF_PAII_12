package com.example.tpf_paii_android.entidades;

public class Provincia {

    private int id_provincia;
    private String nombre;

    public Provincia() {
    }

    public String getNombreProvincia() {
        return nombre;
    }

    public int getId_provincia() {
        return id_provincia;
    }


    @Override
    public String toString() {
        return "id_provincia=" + id_provincia +", nombre='" + nombre;
    }
}
