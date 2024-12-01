package com.example.tpf_paii_android.modelos;

public class UsuarioLogin {
    private int idUsuario;
    private String nombreUsuario;
    private int tipoUsuario;
    private int idEspecifico;

    public UsuarioLogin(int idUsuario, String nombreUsuario, int tipoUsuario) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.tipoUsuario = tipoUsuario;
    }

    public UsuarioLogin(int idUsuario, String nombreUsuario, int tipoUsuario, int idEspecifico) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.tipoUsuario = tipoUsuario;
        this.idEspecifico = idEspecifico;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public int getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(int tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public int getIdEspecifico() {      return idEspecifico;    }

    public void setIdEspecifico(int idEspecifico) {   this.idEspecifico = idEspecifico;    }
}
