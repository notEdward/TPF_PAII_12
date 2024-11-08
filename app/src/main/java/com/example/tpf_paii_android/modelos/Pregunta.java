package com.example.tpf_paii_android.modelos;

import java.util.List;

public class Pregunta {
    private int idPregunta;
    private int idCurso;
    private String pregunta;
    private String tipoPregunta;
    private List<Opcion> opciones;
    private Opcion respuestaSeleccionada;  // Nueva variable para la respuesta seleccionada

    // Constructor y otros métodos

    // Constructor
    public Pregunta(int idCurso, String pregunta, String tipoPregunta) {
        this.idCurso = idCurso;
        this.pregunta = pregunta;
        this.tipoPregunta = tipoPregunta;
    }

    public int getIdPregunta() {
        return idPregunta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public List<Opcion> getOpciones() {
        return opciones;
    }

    // Obtener la respuesta seleccionada
    public Opcion getRespuestaSeleccionada() {
        return respuestaSeleccionada;
    }

    // Establecer la respuesta seleccionada
    public void setRespuestaSeleccionada(Opcion respuestaSeleccionada) {
        this.respuestaSeleccionada = respuestaSeleccionada;
    }
    public void setOpciones(List<Opcion> opciones) { this.opciones = opciones; }

}
