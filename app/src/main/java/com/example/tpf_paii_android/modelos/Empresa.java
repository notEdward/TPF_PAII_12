package com.example.tpf_paii_android.modelos;

public class Empresa {

    private int id_empresa;
    private String nombre;
    private String descripcion;
    private String sector;
    private String n_identificacionFiscal;
    private String email;
    private String telefono;
    private String direccion;
    private int id_localidad;
    private Usuario id_usuario;


    public Empresa() {
    }

    public Empresa(String nombre, String descripcion, String sector, String n_identificacionFiscal, String email, String telefono, String direccion, int id_localidad, Usuario id_usuario) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.sector = sector;
        this.n_identificacionFiscal = n_identificacionFiscal;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.id_localidad = id_localidad;
        this.id_usuario = id_usuario;
    }

    public Empresa(String nombre, String descripcion, String sector, String email, String telefono, String direccion, int idLoc ){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.sector = sector;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.id_localidad = idLoc;
    }

    public int getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(int id_empresa) {
        this.id_empresa = id_empresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getN_identificacionFiscal() {
        return n_identificacionFiscal;
    }

    public void setN_identificacionFiscal(String n_identificacionFiscal) {
        this.n_identificacionFiscal = n_identificacionFiscal;
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

    public Usuario getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Usuario id_usuario) {
        this.id_usuario = id_usuario;
    }

    @Override
    public String toString() {
        return "Empresa{" +
                "id_empresa=" + id_empresa +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", sector='" + sector + '\'' +
                ", n_identificacionFiscal='" + n_identificacionFiscal + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", direccion='" + direccion + '\'' +
                ", id_localidad=" + id_localidad +
                ", id_usuario=" + id_usuario +
                '}';
    }
}
