package com.example.tpf_paii_android.modelos;

public class CategoriaCurso {
    private int idCategoria;
    private String descripcion;

    public CategoriaCurso(){}
    // Constructor
    public CategoriaCurso(int idCategoria, String descripcion) {
        this.idCategoria = idCategoria;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Método opcional para obtener una representación en texto
    @Override
//    public String toString() {
//        return "CategoriaCurso{" +
//                "idCategoria=" + idCategoria +
//                ", descripcion='" + descripcion + '\'' +
//                '}';
//    }
    public String toString() {
        return descripcion;
    }
}
