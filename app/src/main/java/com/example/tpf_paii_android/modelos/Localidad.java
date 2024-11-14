package com.example.tpf_paii_android.modelos;

public class Localidad {

    private int id_localidad;
    private Provincia id_provincia;
    private String nombre;

    private int idProvincia;

    public Localidad() {
    }

    public int getId_localidad() {
        return id_localidad;
    }

    public void setId_localidad(int id_localidad) {
        this.id_localidad = id_localidad;
    }

    public Provincia getId_provincia() {
        return id_provincia;
    }

    public void setId_provincia(Provincia id_provincia) {
        this.id_provincia = id_provincia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return  nombre;
    }

    public void setId_provincia(int idProvincia) {
        this.idProvincia = idProvincia;
    }
}

