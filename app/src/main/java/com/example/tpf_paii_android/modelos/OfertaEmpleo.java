package com.example.tpf_paii_android.modelos;

public class OfertaEmpleo {

    public int getIdEmpresa;
    private int id_ofertaEmpleo;
    private Empresa id_empresa;
    private String titulo;
    private String descripcion;
    private TipoEmpleo id_tipoEmpleo;
    private Modalidad id_modalidad;
    private NivelEducativo id_nivelEducativo;
    private Curso id_curso;
    private String otrosRequisitos;
    private String direccion;
    private Localidad id_localidad;

    //Para constructoro simple
    private int idEmpresa;
    private int idTipoEmpleo;
    private int idTipoModalidad;
    private int idNivelEducativo;
    private int idCurso;
    private int idLocalidad;
    private int idCategoria;

    public OfertaEmpleo() {
    }

    public OfertaEmpleo(int id_ofertaEmpleo, Empresa id_empresa, String titulo, String descripcion, TipoEmpleo id_tipoEmpleo, Modalidad id_modalidad, NivelEducativo id_nivelEducativo, Curso id_curso, String otrosRequisitos, String direccion, Localidad id_localidad) {
        this.id_ofertaEmpleo = id_ofertaEmpleo;
        this.id_empresa = id_empresa;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.id_tipoEmpleo = id_tipoEmpleo;
        this.id_modalidad = id_modalidad;
        this.id_nivelEducativo = id_nivelEducativo;
        this.id_curso = id_curso;
        this.otrosRequisitos = otrosRequisitos;
        this.direccion = direccion;
        this.id_localidad = id_localidad;
    }

    // Constructor Simple para ver ofertas
    public OfertaEmpleo(int id_ofertaEmpleo, int idEmpresa, String titulo, String descripcion, int idTipoEmpleo,
                        int idTipoModalidad, int idNivelEducativo, int idCurso, String otrosRequisitos,
                        String direccion, int idLocalidad) {
        this.id_ofertaEmpleo = id_ofertaEmpleo;
        this.idEmpresa = idEmpresa;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.idTipoEmpleo = idTipoEmpleo;
        this.idTipoModalidad = idTipoModalidad;
        this.idNivelEducativo = idNivelEducativo;
        this.idCurso = idCurso;
        this.otrosRequisitos = otrosRequisitos;
        this.direccion = direccion;
        this.idLocalidad = idLocalidad;
    }

    // Constructor Simple para ver ofertas
    public OfertaEmpleo(int idEmpresa, String titulo, String descripcion, int idTipoEmpleo,
                        int idTipoModalidad, int idNivelEducativo, int idCurso, String otrosRequisitos,
                        String direccion, int idLocalidad) {
        this.idEmpresa = idEmpresa;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.idTipoEmpleo = idTipoEmpleo;
        this.idTipoModalidad = idTipoModalidad;
        this.idNivelEducativo = idNivelEducativo;
        this.idCurso = idCurso;
        this.otrosRequisitos = otrosRequisitos;
        this.direccion = direccion;
        this.idLocalidad = idLocalidad;
    }

    // Constructor para traer ademas imagenes por cat
    public OfertaEmpleo(int id_ofertaEmpleo, int idEmpresa, String titulo, String descripcion, int idTipoEmpleo,
                        int idTipoModalidad, int idNivelEducativo, int idCurso, String otrosRequisitos,
                        String direccion, int idLocalidad, int idCategoria) {
        this.id_ofertaEmpleo = id_ofertaEmpleo;
        this.idEmpresa = idEmpresa;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.idTipoEmpleo = idTipoEmpleo;
        this.idTipoModalidad = idTipoModalidad;
        this.idNivelEducativo = idNivelEducativo;
        this.idCurso = idCurso;
        this.otrosRequisitos = otrosRequisitos;
        this.direccion = direccion;
        this.idLocalidad = idLocalidad;
        this.idCategoria = idCategoria;
    }

    public int getIdCategoria() {
        return idCategoria;
    }
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public int getId_ofertaEmpleo() {
        return id_ofertaEmpleo;
    }

    public Empresa getId_empresa() {
        return id_empresa;
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

    public TipoEmpleo getId_tipoEmpleo() {
        return id_tipoEmpleo;
    }

    public void setId_tipoEmpleo(TipoEmpleo id_tipoEmpleo) {
        this.id_tipoEmpleo = id_tipoEmpleo;
    }

    public Modalidad getId_modalidad() {
        return id_modalidad;
    }

    public void setId_modalidad(Modalidad id_modalidad) {
        this.id_modalidad = id_modalidad;
    }

    public NivelEducativo getId_nivelEducativo() {
        return id_nivelEducativo;
    }

    public void setId_nivelEducativo(NivelEducativo id_nivelEducativo) {
        this.id_nivelEducativo = id_nivelEducativo;
    }

    public Curso getId_curso() {
        return id_curso;
    }

    public void setId_curso(Curso id_curso) {
        this.id_curso = id_curso;
    }

    public String getOtrosRequisitos() {
        return otrosRequisitos;
    }

    public void setOtrosRequisitos(String otrosRequisitos) {
        this.otrosRequisitos = otrosRequisitos;
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

    @Override
    public String toString() {
        return "OfertaEmpleo{" +
                "id_ofertaEmpleo=" + id_ofertaEmpleo +
                ", id_empresa=" + id_empresa +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", id_tipoEmpleo=" + id_tipoEmpleo +
                ", id_modalidad=" + id_modalidad +
                ", id_nivelEducativo=" + id_nivelEducativo +
                ", id_curso=" + id_curso +
                ", otrosRequisitos='" + otrosRequisitos + '\'' +
                ", direccion='" + direccion + '\'' +
                ", id_localidad=" + id_localidad +
                '}';
    }

    //Setters simplificados.
    public int setId_ofertaEmpleo(int idOfertaEmpleo) {
        return this.id_ofertaEmpleo = idOfertaEmpleo;
    }

    public int setId_empresa(int idEmpresa) {
        return this.idEmpresa = idEmpresa;
    }

    public int setId_tipoModalidad(int idTipoModalidad) {
        return this.idTipoModalidad = idTipoModalidad;
    }

    public int setId_localidad(int id_localidad) {
        return this.idLocalidad = id_localidad;
    }

    public int setId_curso(int id_curso) {
        return this.idCurso = id_curso;
    }

    public int setId_nivelEducativo(int id_nivelEducativo) {
        return this.idNivelEducativo = id_nivelEducativo;
    }
    public int setId_tipoEmpleo(int id_tipoEmpleo) {
        return this.idTipoEmpleo = id_tipoEmpleo;
    }

    public int getIdTipoEmpleo() {
        return idTipoEmpleo;
    }

    public void setIdTipoEmpleo(int idTipoEmpleo) {
        this.idTipoEmpleo = idTipoEmpleo;
    }

    public int getIdModalidad() {
        return idTipoModalidad;
    }

    public void setIdModalidad(int idModalidad) {
        this.idTipoModalidad = idModalidad;
    }

    public int getIdNivelEducativo() {
        return idNivelEducativo;
    }

    public void setIdNivelEducativo(int idNivelEducativo) {
        this.idNivelEducativo = idNivelEducativo;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }

    public int getIdLocalidad() {
        return idLocalidad;
    }

    public void setIdLocalidad(int idLocalidad) {
        this.idLocalidad = idLocalidad;
    }
}
