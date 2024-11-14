package com.example.tpf_paii_android.modelos;

public class NivelEducativo {

    private int id_nivelEducativo;
    private String descripcion;

    public NivelEducativo() {
    }

    public int getId_nivelEducativo() {
        return id_nivelEducativo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return "NivelEducativo{" +
                "id_nivelEducativo=" + id_nivelEducativo +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }

    public void setId_nivelEducativo(int idNivelEducativo) {
        this.id_nivelEducativo = idNivelEducativo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
