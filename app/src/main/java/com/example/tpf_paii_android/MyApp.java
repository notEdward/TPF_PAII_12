package com.example.tpf_paii_android;

import android.app.Application;

public class MyApp extends Application {
    private int idUsuario = 4; // Valor predeterminado, puedes cambiarlo
    private String tipoUsuario = "Admin"; // Valor predeterminado, puedes cambiarlo
    private String nombreUsuario = "pepito";

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
