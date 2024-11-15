package com.example.tpf_paii_android.modelos;

public class UsuarioInfo {
    private String tipoUsuario;
    private String nombre;
    private String apellido;
    private String infoAdicional;
    private int imagenResId;

    // Constructor
    public UsuarioInfo(String tipoUsuario, String nombre, String apellido, String infoAdicional, int imagenResId) {
        this.tipoUsuario = tipoUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.infoAdicional = infoAdicional;
        this.imagenResId = imagenResId;
    }

    // Getters
    public String getTipoUsuario() { return tipoUsuario; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getInfoAdicional() { return infoAdicional; }
    public int getImagenResId() { return imagenResId; }
}
