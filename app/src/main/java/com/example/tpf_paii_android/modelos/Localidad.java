package com.example.tpf_paii_android.modelos;

public class Localidad {

    private int id_localidad;
    private Provincia id_provincia;
    private String nombre;

    public Localidad() {
    }

    public int getId_localidad() {
        return id_localidad;
    }

    public Provincia getId_provincia() {
        return id_provincia;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "Localidad{" +
                "id_localidad=" + id_localidad +
                ", id_provincia=" + id_provincia +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
