package com.example.tpf_paii_android.modelos;

public class Estudiante {

    private String dni;
    private Usuario id_usuario;
    private String nombre;
    private String apellido;
    private int id_genero;
    private String email;
    private String telefono;
    private String direccion;
    private int id_localidad;
    private int id_nivelEducativo;
    private int id_estadoNivelEducativo;

    private Localidad localidad;
    private EstadoNivelEducativo estadoNivelEducativo;
    private NivelEducativo nivelEducativo;


    public Estudiante() {
    }

    public Estudiante(String DNI, Usuario id_usuario, String nombre, String apellido, int id_genero, String email,
                      String telefono, String direccion, int id_localidad, int id_nivelEducativo,
                      int id_estadoNivelEducativo) {
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

    public Estudiante(String DNI, Usuario id_usuario, String nombre, String apellido, int id_genero, String email,
                      String telefono, String direccion, int id_localidad, EstadoNivelEducativo estadoNivelEducativo,
                      NivelEducativo nivelEducativo) {
        this.dni = DNI;
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.id_genero = id_genero;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.id_localidad = id_localidad;
        this.estadoNivelEducativo = estadoNivelEducativo;
        this.nivelEducativo = nivelEducativo;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Usuario getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Usuario id_usuario) {
        this.id_usuario = id_usuario;
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

    public int getId_genero() {
        return id_genero;
    }

    public void setId_genero(int id_genero) {
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

    public int getId_localidad() {
        return id_localidad;
    }

    public void setId_localidad(int id_localidad) {
        this.id_localidad = id_localidad;
    }

    public int getId_nivelEducativo() {
        return id_nivelEducativo;
    }

    public void setId_nivelEducativo(int id_nivelEducativo) {
        this.id_nivelEducativo = id_nivelEducativo;
    }

    public int getId_estadoNivelEducativo() {
        return id_estadoNivelEducativo;
    }

    public void setId_estadoNivelEducativo(int id_estadoNivelEducativo) {
        this.id_estadoNivelEducativo = id_estadoNivelEducativo;
    }

    public NivelEducativo getNivelEducativo() {
        return nivelEducativo;
    }

    public void setNivelEducativo(NivelEducativo nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }

    public EstadoNivelEducativo getEstadoNivelEducativo() {
        return estadoNivelEducativo;
    }

    public void setEstadoNivelEducativo(EstadoNivelEducativo estadoNivelEducativo) {
        this.estadoNivelEducativo = estadoNivelEducativo;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
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
