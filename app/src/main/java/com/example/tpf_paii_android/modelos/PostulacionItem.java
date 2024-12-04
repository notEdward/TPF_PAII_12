package com.example.tpf_paii_android.modelos;

import java.util.Date;

public class PostulacionItem {
    private int idPostulacion;
    private String titulo;
    private String nombreUsuario;
    private Date fechaPostulacion;
    private String estadoPostulacion;
    private int idUsuario;  // Nuevo campo para el id del usuario

    // Getters y Setters
    public int getIdPostulacion() {
        return idPostulacion;
    }

    public void setIdPostulacion(int idPostulacion) {
        this.idPostulacion = idPostulacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public Date getFechaPostulacion() {
        return fechaPostulacion;
    }

    public void setFechaPostulacion(Date fechaPostulacion) {
        this.fechaPostulacion = fechaPostulacion;
    }

    public String getEstadoPostulacion() {
        return estadoPostulacion;
    }

    public void setEstadoPostulacion(String estadoPostulacion) {
        this.estadoPostulacion = estadoPostulacion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}


