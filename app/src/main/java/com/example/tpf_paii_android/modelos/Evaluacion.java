package com.example.tpf_paii_android.modelos;

import java.util.Date;

public class Evaluacion {
    private int idEvaluacion;
    private int idInscripcion;
    private int notaObtenida;
    private Date fechaFinalizacion;

    public Evaluacion(int idInscripcion, int notaObtenida, Date fechaFinalizacion) {
        this.idInscripcion = idInscripcion;
        this.notaObtenida = notaObtenida;
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public int getIdEvaluacion() {
        return idEvaluacion;
    }

    public void setIdEvaluacion(int idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }

    public int getIdInscripcion() {
        return idInscripcion;
    }

    public void setIdInscripcion(int idInscripcion) {
        this.idInscripcion = idInscripcion;
    }

    public int getNotaObtenida() {
        return notaObtenida;
    }

    public void setNotaObtenida(int notaObtenida) {
        this.notaObtenida = notaObtenida;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }
}
