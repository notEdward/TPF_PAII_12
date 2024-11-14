package com.example.tpf_paii_android.modelos;

public class ForoMensaje {
    private int idHilo;
    private String nombreUsuario;
    private String mensaje;
    private String fechaMensaje;

    public ForoMensaje(int idHilo, String nombreUsuario, String mensaje, String fechaMensaje) {
        this.idHilo = idHilo;
        this.nombreUsuario = nombreUsuario;
        this.mensaje = mensaje;
        this.fechaMensaje = fechaMensaje;
    }

    public int getIdHilo() {
        return idHilo;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getFechaMensaje() {
        return fechaMensaje;
    }
}
