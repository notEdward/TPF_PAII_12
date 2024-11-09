package com.example.tpf_paii_android.modelos;

public class EstadoNivelEducativo {

    private int id_estadoNivelEducativo;
    private String descripcion;

    public EstadoNivelEducativo() {
    }

    public int getId_estadoNivelEducativo() {
        return id_estadoNivelEducativo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return "id_estadoNivelEducativo=" + id_estadoNivelEducativo +
                ", descripcion='" + descripcion;
    }
}
