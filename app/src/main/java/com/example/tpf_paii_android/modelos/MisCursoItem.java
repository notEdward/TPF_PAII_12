package com.example.tpf_paii_android.modelos;

import java.util.Date;
public class MisCursoItem {

    private String nombreCurso;
    private String estado;
    private boolean esEvaluacion;
    private Double notaObtenida;
    private Date fechaFinalizacion;

    // Constructor para Inscripciones (sin evaluación)
    public MisCursoItem(String nombreCurso, String estado, boolean esEvaluacion) {
        this.nombreCurso = nombreCurso;
        this.estado = estado;
        this.esEvaluacion = esEvaluacion;
    }

    // Constructor para Evaluaciones (con nota y fecha)
    public MisCursoItem(String nombreCurso, String estado, Double notaObtenida, Date fechaFinalizacion) {
        this.nombreCurso = nombreCurso;
        this.estado = estado;
        this.esEvaluacion = true; // Es una evaluación
        this.notaObtenida = notaObtenida;
        this.fechaFinalizacion = fechaFinalizacion;
    }

    // Getters y setters
    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isEsEvaluacion() {
        return esEvaluacion;
    }

    public void setEsEvaluacion(boolean esEvaluacion) {
        this.esEvaluacion = esEvaluacion;
    }

    public Double getNotaObtenida() {
        return notaObtenida;
    }

    public void setNotaObtenida(Double notaObtenida) {
        this.notaObtenida = notaObtenida;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }
}

//public class MisCursoItem {
//    private String nombreCurso;
//    private String estado;
//    private Double notaObtenida;
//    private Date fechaFinalizacion;
//
//    // Constructor para inscripción
//    public MisCursoItem(String nombreCurso, String estado) {
//        this.nombreCurso = nombreCurso;
//        this.estado = estado;
//    }
//
//    // Constructor para evaluación
//    public MisCursoItem(String nombreCurso, String estado, Double notaObtenida, Date fechaFinalizacion) {
//        this.nombreCurso = nombreCurso;
//        this.estado = estado;
//        this.notaObtenida = notaObtenida;
//        this.fechaFinalizacion = fechaFinalizacion;
//    }
//
//    public String getNombreCurso() {
//        return nombreCurso;
//    }
//
//    public String getEstado() {
//        return estado;
//    }
//
//    public Double getNotaObtenida() {
//        return notaObtenida;
//    }
//
//    public Date getFechaFinalizacion() {
//        return fechaFinalizacion;
//    }
//
//    // Método para determinar si es una evaluación
//    public boolean isEsEvaluacion() {
//        return notaObtenida != null && fechaFinalizacion != null;
//    }
//}



//public abstract class MisCursosItem {
//    private String nombreCurso;
//    private String estado;
//
//    // Constructor
//    public MisCursosItem(String nombreCurso, String estado) {
//        this.nombreCurso = nombreCurso;
//        this.estado = estado;
//    }
//
//    // Getters
//    public String getNombreCurso() {
//        return nombreCurso;
//    }
//
//    public String getEstado() {
//        return estado;
//    }
//}
