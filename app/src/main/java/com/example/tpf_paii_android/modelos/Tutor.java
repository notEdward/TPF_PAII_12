package com.example.tpf_paii_android.modelos;

import androidx.annotation.NonNull;

public class Tutor extends Usuario{

    private int idTutor; // AUTOINCREMENT en la DB
    private String dni; // El dni es varchar(20) en la bd
    private String nombre;
    private String apellido;
    private int edad;
    private int idGenero;
    private String ocupacion;
    private String pasatiempos;
    private String infoAdicional;

    // Constructor
    public Tutor() {
    }


    // Constructor que incluye Usuario
    public Tutor(String dni, String nombre, String apellido, int edad, int idGenero, String ocupacion, String pasatiempos, String infoAdicional, Usuario usuario) {
        super(usuario.getNombreUsuario(), usuario.getContrasenia(), usuario.getId_tipoUsuario());
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.idGenero = idGenero;
        this.ocupacion = ocupacion;
        this.pasatiempos = pasatiempos;
        this.infoAdicional = infoAdicional;
    }

    public Tutor(String nombre, String apellido, int edad, int idGenero, String ocupacion, String pasatiempos, String infoAdicional) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.idGenero = idGenero;
        this.ocupacion = ocupacion;
        this.pasatiempos = pasatiempos;
        this.infoAdicional = infoAdicional;

    }


    // Getters y setters

    public int getIdTutor() {
        return idTutor;
    }

    public void setIdTutor(int idTutor) {
        this.idTutor = idTutor;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
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

    public int getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(int idGenero) {
        this.idGenero = idGenero;
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


    // Método toString


    @NonNull
    @Override
    public String toString() {
        return "Tutor{" +
                "idTutor=" + idTutor +
                ", dni='" + dni + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", edad=" + edad +
                ", idGenero=" + idGenero +
                ", ocupacion='" + ocupacion + '\'' +
                ", pasatiempos='" + pasatiempos + '\'' +
                ", infoAdicional='" + infoAdicional + '\'' +
                '}';
    }
}