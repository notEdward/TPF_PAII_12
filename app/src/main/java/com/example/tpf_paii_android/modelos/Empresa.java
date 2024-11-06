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
    private Localidad id_localidad;
    private Provincia id_provincia;
    private Usuario id_usuario;
    private boolean estado;


    public Empresa() {
    }

    public Empresa(int id_empresa, String nombre, String descripcion, String sector, String n_identificacionFiscal, String email, String telefono, String direccion, Localidad id_localidad, Provincia id_provincia, Usuario id_usuario, boolean estado) {
        this.id_empresa = id_empresa;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.sector = sector;
        this.n_identificacionFiscal = n_identificacionFiscal;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.id_localidad = id_localidad;
        this.id_provincia = id_provincia;
        this.id_usuario = id_usuario;
        this.estado = estado;
    }

    public int getId_empresa() {
        return id_empresa;
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

    public Localidad getId_localidad() {
        return id_localidad;
    }

    public void setId_localidad(Localidad id_localidad) {
        this.id_localidad = id_localidad;
    }

    public Provincia getId_provincia() {
        return id_provincia;
    }

    public void setId_provincia(Provincia id_provincia) {
        this.id_provincia = id_provincia;
    }

    public Usuario getId_usuario() {
        return id_usuario;
    }


    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
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
                ", id_provincia=" + id_provincia +
                ", id_usuario=" + id_usuario +
                ", estado=" + estado +
                '}';
    }
}
