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

    //setters
    public void setId_tipoEmpleo(int id_tipoEmpleo) {
        this.id_tipoEmpleo = id_tipoEmpleo;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "TipoEmpleo{" +
                "id_tipoEmpleo=" + id_tipoEmpleo +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
