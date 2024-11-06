package com.example.tpf_paii_android.modelos;

public class CategoriaCurso {

    private int id_categoria;
    private String descripcion;

    public CategoriaCurso() {
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return "CategoriaCurso{" +
                "id_categoria=" + id_categoria +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
