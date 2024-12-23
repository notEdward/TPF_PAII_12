package com.example.tpf_paii_android.modelos;

public class Curso {

    private int idCurso;
    private String nombreCurso;
    private String descripcion;
    private int idCategoria;
    private String respuestasCorrectas;
    private int estado;

    public Curso(){}
    // Constructor
    public Curso(int idCurso, String nombreCurso, String descripcion, int idCategoria, String respuestasCorrectas, int estado) {
        this.idCurso = idCurso;
        this.nombreCurso = nombreCurso;
        this.descripcion = descripcion;
        this.idCategoria = idCategoria;
        this.respuestasCorrectas = respuestasCorrectas;
        this.estado = estado;
    }
    // Constructor
    public Curso(String nombreCurso, String descripcion, int idCategoria, String respuestasCorrectas, int estado) {
        this.nombreCurso = nombreCurso;
        this.descripcion = descripcion;
        this.idCategoria = idCategoria;
        this.respuestasCorrectas = respuestasCorrectas;
        this.estado = estado;
    }

    // Compara objetos Curso
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Curso curso = (Curso) obj;
        return idCurso == curso.idCurso && nombreCurso.equals(curso.nombreCurso) &&
                descripcion.equals(curso.descripcion) && idCategoria == curso.idCategoria &&
                respuestasCorrectas.equals(curso.respuestasCorrectas) && estado == curso.estado;
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

    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }
}
