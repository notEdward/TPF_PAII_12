package com.example.tpf_paii_android.entidades;

public class TipoUsuario {

    private int id_tipoUsuario;
    private String descripcion;

    public TipoUsuario() {
    }

    public int getId_tipoUsuario() {
        return id_tipoUsuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return "TipoUsuario{" +
                "id_tipoUsuario=" + id_tipoUsuario +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
