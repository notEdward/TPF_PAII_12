package com.example.tpf_paii_android.modelos;

public class OfertaDetalle {

    private int id_oferta_empleo; // ID de la oferta
    private String titulo;
    private String descripcion;
    private String direccion;
    private String nombreCurso;
    private String localidad;
    private String provincia;
    private String modalidad;
    private String tipoEmpleo;
    private String nivelEducativo;
    private String otrosRequisitos;
    private String nombreEmpresa;

    public OfertaDetalle(int id_oferta_empleo, String titulo, String descripcion, String direccion,
                         String nombreCurso, String localidad, String provincia,
                         String modalidad, String tipoEmpleo, String otrosRequisitos) {  // Constructor corregido a OfertaDetalle
        this.id_oferta_empleo = id_oferta_empleo;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.nombreCurso = nombreCurso;
        this.localidad = localidad;
        this.provincia = provincia;
        this.modalidad = modalidad;
        this.tipoEmpleo = tipoEmpleo;
        this.otrosRequisitos = otrosRequisitos;
    }

    public OfertaDetalle(int id_oferta_empleo, String nombreEmpresa, String titulo, String descripcion, String direccion,
                         String nombreCurso, String localidad, String provincia,
                         String modalidad, String tipoEmpleo, String nivelEducativo, String otrosRequisitos) {  // Constructor corregido a OfertaDetalle
        this.id_oferta_empleo = id_oferta_empleo;
        this.nombreEmpresa = nombreEmpresa;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.nombreCurso = nombreCurso;
        this.localidad = localidad;
        this.provincia = provincia;
        this.modalidad = modalidad;
        this.tipoEmpleo = tipoEmpleo;
        this.nivelEducativo = nivelEducativo;
        this.otrosRequisitos = otrosRequisitos;
    }

    // Getters y setters
    public int getId_oferta_empleo() {
        return id_oferta_empleo;
    }

    public void setId_oferta_empleo(int id_oferta_empleo) {
        this.id_oferta_empleo = id_oferta_empleo;
    }
    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getTipoEmpleo() {
        return tipoEmpleo;
    }

    public void setTipoEmpleo(String tipoEmpleo) {
        this.tipoEmpleo = tipoEmpleo;
    }

    public String getNivelEducativo() {
        return nivelEducativo;
    }

    public void setNivelEducativo(String nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }

    public String getOtrosRequisitos() {
        return otrosRequisitos;
    }

    public void setOtrosRequisitos(String otrosRequisitos) {
        this.otrosRequisitos = otrosRequisitos;
    }
}

