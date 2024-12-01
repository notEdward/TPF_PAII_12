package com.example.tpf_paii_android;

import android.app.Application;

public class MyApp extends Application {
    private int idUsuario = 4; //predet admin
    private String tipoUsuario = "Admin";
    private String nombreUsuario = "pepito";
    private int idEspecifico;


    //para obeter el id del usuario ya sea tutor/estudiante/mempresa
    //esto lo validamos en el repo al hacer login
    public int getidEspecifico() { return idEspecifico; }
    public void setidEspecifico(int idEspecifico) { this.idEspecifico = idEspecifico; }


    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}
