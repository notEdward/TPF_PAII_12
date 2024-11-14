package com.example.tpf_paii_android.modelos;

import androidx.annotation.NonNull;

public class TipoUsuario {

    private int id_tipoUsuario;
    private String descripcion;

    // Tipos de usuario predefinidos
    public static final TipoUsuario TUTOR = new TipoUsuario(3, "Tutor");
    public static final TipoUsuario ESTUDIANTE = new TipoUsuario(2, "Estudiante");
    public static final TipoUsuario ADMINISTRADOR = new TipoUsuario(1, "Administrador");


    public TipoUsuario() {
    }

    // Constructor con parametros para crear el TipoUsuario
    public TipoUsuario(int id_tipoUsuario, String descripcion) {
        this.id_tipoUsuario = id_tipoUsuario;
        this.descripcion = descripcion;
    }

    public int getId_tipoUsuario() {
        return id_tipoUsuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @NonNull
    @Override
    public String toString() {
        return "TipoUsuario{" +
                "id_tipoUsuario=" + id_tipoUsuario +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
