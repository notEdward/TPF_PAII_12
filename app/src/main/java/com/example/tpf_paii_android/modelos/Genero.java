package com.example.tpf_paii_android.modelos;

import androidx.annotation.NonNull;

public class Genero {

    private int id_genero;
    private String descripcion;

    public Genero() {
    }

    public Genero (int id_genero, String descripcion) {
        this.id_genero = id_genero;
        this.descripcion = descripcion;
    }

    public int getId_genero() {
        return id_genero;
    }

    public void setId_genero(int id_genero) {
        this.id_genero = id_genero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
