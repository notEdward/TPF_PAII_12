package com.example.tpf_paii_android.modelos;

public class EstadoNivelEducativo {

    private int id_estadoNivelEducativo;
    private String descripcion;

    public EstadoNivelEducativo() {
    }

    public EstadoNivelEducativo(int id_estadoNivelEducativo, String descripcion) {
        this.id_estadoNivelEducativo = id_estadoNivelEducativo;
        this.descripcion = descripcion;
    }

    public int getId_estadoNivelEducativo() {
        return id_estadoNivelEducativo;
    }

    public void setId_estadoNivelEducativo(int id_estadoNivelEducativo) {
        this.id_estadoNivelEducativo = id_estadoNivelEducativo;
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
