package com.example.tpf_paii_android.modelos;

public class ExperienciaLaboral {

    private int id_experienciaLaboral;
    private Estudiante dniEstudiante;
    private String lugar;
    private String cargo;
    private String tareas;
    private String duracion;

    public ExperienciaLaboral() {
    }

    public ExperienciaLaboral(int id_experienciaLaboral, Estudiante dniEstudiante, String lugar, String cargo, String tareas, String duracion) {
        this.id_experienciaLaboral = id_experienciaLaboral;
        this.dniEstudiante = dniEstudiante;
        this.lugar = lugar;
        this.cargo = cargo;
        this.tareas = tareas;
        this.duracion = duracion;
    }

    public int getId_experienciaLaboral() {
        return id_experienciaLaboral;
    }

    public Estudiante getDniEstudiante() {
        return dniEstudiante;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getTareas() {
        return tareas;
    }

    public void setTareas(String tareas) {
        this.tareas = tareas;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    @Override
    public String toString() {
        return "ExperienciaLaboral{" +
                "id_experienciaLaboral=" + id_experienciaLaboral +
                ", dniEstudiante=" + dniEstudiante +
                ", lugar='" + lugar + '\'' +
                ", cargo='" + cargo + '\'' +
                ", tareas='" + tareas + '\'' +
                ", duracion='" + duracion + '\'' +
                '}';
    }
}
