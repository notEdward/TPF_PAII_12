package com.example.tpf_paii_android.modelos;

public class Curso {

    private int idCurso;
    private String nombreCurso;
    private String descripcion;
    private int idCategoria;
    private String respuestasCorrectas;
    private String estado;

    // Constructor
    public Curso(int idCurso, String nombreCurso, String descripcion, int idCategoria, String respuestasCorrectas, String estado) {
        this.idCurso = idCurso;
        this.nombreCurso = nombreCurso;
        this.descripcion = descripcion;
        this.idCategoria = idCategoria;
        this.respuestasCorrectas = respuestasCorrectas;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdCurso() { return idCurso; }
    public void setIdCurso(int idCurso) { this.idCurso = idCurso; }

    public String getNombreCurso() { return nombreCurso; }
    public void setNombreCurso(String nombreCurso) { this.nombreCurso = nombreCurso; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public String getRespuestasCorrectas() { return respuestasCorrectas; }
    public void setRespuestasCorrectas(String respuestasCorrectas) { this.respuestasCorrectas = respuestasCorrectas; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
