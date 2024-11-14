package com.example.tpf_paii_android.modelos;

import androidx.annotation.NonNull;

public class Usuario {

    private int id_usuario;
    private String nombreUsuario;
    private String contrasenia;
    private TipoUsuario id_tipoUsuario;

    public Usuario() {
    }

    public Usuario(String nombreUsuario, String contrasenia) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
    }

    // Constructor con tipoUsuario
    public Usuario(String nombreUsuario, String contrasenia, TipoUsuario id_tipoUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.id_tipoUsuario = id_tipoUsuario;
    }

    public TipoUsuario getId_tipoUsuario() {
        return id_tipoUsuario;
    }

    public int getId_usuario() {
        return id_usuario;
    }


    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    @NonNull
    @Override
    public String toString() {
        return "Usuario{" +
                "id_usuario=" + id_usuario +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", contrasenia='" + contrasenia + '\'' +
                ", id_tipoUsuario=" + id_tipoUsuario +
                '}';
    }
}
