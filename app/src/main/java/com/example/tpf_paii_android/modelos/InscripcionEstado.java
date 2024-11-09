package com.example.tpf_paii_android.modelos;

public class InscripcionEstado {
    private int idInscripcion;
    private String estadoInscripcion;

    public InscripcionEstado(int idInscripcion, String estadoInscripcion) {
        this.idInscripcion = idInscripcion;
        this.estadoInscripcion = estadoInscripcion;
    }

    public int getIdInscripcion() {
        return idInscripcion;
    }

    public String getEstadoInscripcion() {
        return estadoInscripcion;
    }
}
