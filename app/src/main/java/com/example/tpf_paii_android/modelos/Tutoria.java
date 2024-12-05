package com.example.tpf_paii_android.modelos;

import java.util.Date;
import java.util.Objects;

public class Tutoria {

    private int id_tutoria;
    private Tutor id_tutor;
    private String id_estudiante; // DNI del estudiante (PK en la tabla Estudiante)
    private Curso id_curso;
    private Date fecha;
    private String tema;
    private String comentarios;

    // Constructor
    public Tutoria() {
    }

    // Constructor
    public Tutoria(int id_tutoria, Tutor id_tutor, String id_estudiante, Curso id_curso, Date fecha, String tema, String comentarios) {
        this.id_tutoria = id_tutoria;
        this.id_tutor = id_tutor;
        this.id_estudiante = id_estudiante;
        this.id_curso = id_curso;
        this.fecha = fecha;
        this.tema = tema;
        this.comentarios = comentarios;
    }

    // Constructor sin Tutor Completado
    public Tutoria(int id_tutoria, String id_estudiante, Curso id_curso, Date fecha, String tema, String comentarios) {
        this.id_tutoria = id_tutoria;
        this.id_estudiante = id_estudiante;
        this.id_curso = id_curso;
        this.fecha = fecha;
        this.tema = tema;
        this.comentarios = comentarios;
    }

    // Getters y setters
    public int getId_tutoria() {
        return id_tutoria;
    }

    public void setId_tutoria(int id_tutoria) {
        this.id_tutoria = id_tutoria;
    }

    public Tutor getId_tutor() {
        return id_tutor;
    }

    public void setId_tutor(Tutor id_tutor) {
        this.id_tutor = id_tutor;
    }

    public String getId_estudiante() {
        return id_estudiante;
    }

    public void setId_estudiante(String id_estudiante) {
        this.id_estudiante = id_estudiante;
    }

    public Curso getId_curso() {
        return id_curso;
    }

    public void setId_curso(Curso id_curso) {
        this.id_curso = id_curso;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    // Método toString
    @Override
    public String toString() {
        return "Tutoria{" +
                "id_tutoria=" + id_tutoria +
                ", id_tutor=" + id_tutor +
                ", id_estudiante='" + id_estudiante + '\'' +
                ", id_curso=" + id_curso +
                ", fecha=" + fecha +
                ", tema='" + tema + '\'' +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tutoria tutoria = (Tutoria) o;
        return id_tutoria == tutoria.id_tutoria;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_tutoria);
    }

}