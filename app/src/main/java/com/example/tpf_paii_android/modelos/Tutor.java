package com.example.tpf_paii_android.modelos;

public class Tutor {

    private int dni;
    private Usuario id_usuario;
    private String nombre;
    private String apellido;
    private int edad;
    private Genero id_genero;
    private String ocupacion;
    private String pasatiempos;
    private String infoAdicional;

    public Tutor() {
    }

    public Tutor(int dni, Usuario id_usuario, String nombre, String apellido, int edad, Genero id_genero, String ocupacion, String pasatiempos, String infoAdicional) {
        this.dni = dni;
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.id_genero = id_genero;
        this.ocupacion = ocupacion;
        this.pasatiempos = pasatiempos;
        this.infoAdicional = infoAdicional;
    }

    public int getDni() {
        return dni;
    }

    public Usuario getId_usuario() {
        return id_usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public Genero getId_genero() {
        return id_genero;
    }

    public void setId_genero(Genero id_genero) {
        this.id_genero = id_genero;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public String getPasatiempos() {
        return pasatiempos;
    }

    public void setPasatiempos(String pasatiempos) {
        this.pasatiempos = pasatiempos;
    }

    public String getInfoAdicional() {
        return infoAdicional;
    }

    public void setInfoAdicional(String infoAdicional) {
        this.infoAdicional = infoAdicional;
    }

    @Override
    public String toString() {
        return "Tutor{" +
                "dni=" + dni +
                ", id_usuario=" + id_usuario +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", edad=" + edad +
                ", id_genero=" + id_genero +
                ", ocupacion='" + ocupacion + '\'' +
                ", pasatiempos='" + pasatiempos + '\'' +
                ", infoAdicional='" + infoAdicional + '\'' +
                '}';
    }
}
