package com.example.tpf_paii_android.modelos;

import java.util.Date;

public class Inscripcion {
    private int idInscripcion;
    private int idCurso;
    private int idUsuario;
    private Date fechaInscripcion;
    private String estadoInscripcion;

    public Inscripcion(int idCurso, int idUsuario, Date fechaInscripcion, String estadoInscripcion) {
        this.idCurso = idCurso;
        this.idUsuario = idUsuario;
        this.fechaInscripcion = fechaInscripcion;
        this.estadoInscripcion = estadoInscripcion;
    }
    public Inscripcion() {}

    public int getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(int idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public String getEstadoInscripcion() {
        return estadoInscripcion;
    }

    public void setEstadoInscripcion(String estadoInscripcion) {
        this.estadoInscripcion = estadoInscripcion;
    }
}
