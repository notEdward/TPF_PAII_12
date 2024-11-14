package com.example.tpf_paii_android.modelos;

public class Foro {
    private int idHilo;
    private int idUsuarioCreador;  // id_usuario_creador
    private String titulo;
    private String descripcionMensaje;
    private String fechaCreacion;
    private String nombreUsuario;  // nombreUsuario
    private int numeroReplicas;

    // Constructor
    public Foro(int idHilo, int idUsuarioCreador, String nombreUsuario, String titulo, String descripcionMensaje, String fechaCreacion) {
        this.idHilo = idHilo;
        this.idUsuarioCreador = idUsuarioCreador;
        this.nombreUsuario = nombreUsuario;
        this.titulo = titulo;
        this.descripcionMensaje = descripcionMensaje;
        this.fechaCreacion = fechaCreacion;

    }
    public Foro(int idHilo, int idUsuarioCreador, String nombreUsuario, String titulo, String descripcionMensaje, String fechaCreacion, int numeroReplicas) {
        this.idHilo = idHilo;
        this.idUsuarioCreador = idUsuarioCreador;
        this.nombreUsuario = nombreUsuario;
        this.titulo = titulo;
        this.descripcionMensaje = descripcionMensaje;
        this.fechaCreacion = fechaCreacion;
        this.numeroReplicas = numeroReplicas;

    }

    public int getNumeroReplicas() {
        return numeroReplicas;
    }
    // Getters y setters
    public int getIdHilo() {
        return idHilo;
    }

    public void setIdHilo(int idHilo) {
        this.idHilo = idHilo;
    }

    public int getIdUsuarioCreador() {
        return idUsuarioCreador;
    }

    public void setIdUsuarioCreador(int idUsuarioCreador) {
        this.idUsuarioCreador = idUsuarioCreador;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcionMensaje() {
        return descripcionMensaje;
    }

    public void setDescripcionMensaje(String descripcionMensaje) {
        this.descripcionMensaje = descripcionMensaje;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}
