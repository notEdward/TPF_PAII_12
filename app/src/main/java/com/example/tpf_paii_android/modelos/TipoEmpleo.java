package com.example.tpf_paii_android.modelos;

public class TipoEmpleo {

    private int id_tipoEmpleo;
    private String descripcion;

    public TipoEmpleo() {
    }

    public int getId_tipoEmpleo() {
        return id_tipoEmpleo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return "TipoEmpleo{" +
                "id_tipoEmpleo=" + id_tipoEmpleo +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
