package com.example.tpf_paii_android.modelos;

public class Estudiante {

    private int dni;
    private Usuario id_usuario;
    private String nombre;
    private String apellido;
    private Genero id_genero;
    private String email;
    private String telefono;
    private String direccion;
    private Localidad id_localidad;
    private NivelEducativo id_nivelEducativo;
    private EstadoNivelEducativo id_estadoNivelEducativo;


    public Estudiante() {
    }

    public Estudiante(int DNI, Usuario id_usuario, String nombre, String apellido, Genero id_genero, String email, String telefono, String direccion, Localidad id_localidad, NivelEducativo id_nivelEducativo, EstadoNivelEducativo id_estadoNivelEducativo) {
        this.dni = DNI;
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.id_genero = id_genero;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.id_localidad = id_localidad;
        this.id_nivelEducativo = id_nivelEducativo;
        this.id_estadoNivelEducativo = id_estadoNivelEducativo;
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

    public Genero getId_genero() {
        return id_genero;
    }

    public void setId_genero(Genero id_genero) {
        this.id_genero = id_genero;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Localidad getId_localidad() {
        return id_localidad;
    }

    public void setId_localidad(Localidad id_localidad) {
        this.id_localidad = id_localidad;
    }

    public NivelEducativo getId_nivelEducativo() {
        return id_nivelEducativo;
    }

    public void setId_nivelEducativo(NivelEducativo id_nivelEducativo) {
        this.id_nivelEducativo = id_nivelEducativo;
    }

    public EstadoNivelEducativo getId_estadoNivelEducativo() {
        return id_estadoNivelEducativo;
    }

    public void setId_estadoNivelEducativo(EstadoNivelEducativo id_estadoNivelEducativo) {
        this.id_estadoNivelEducativo = id_estadoNivelEducativo;
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "DNI=" + dni +
                ", id_usuario=" + id_usuario +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", id_genero=" + id_genero +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", direccion='" + direccion + '\'' +
                ", id_localidad=" + id_localidad +
                ", id_nivelEducativo=" + id_nivelEducativo +
                ", id_estadoNivelEducativo=" + id_estadoNivelEducativo +
                '}';
    }
}
