package com.example.tpf_paii_android.modelos;

public class Opcion {
    private int idOpcion;
    private int idPregunta;
    private String opcionTexto;
    private boolean esCorrecta;

    // Constructor
    public Opcion(int idOpcion, int idPregunta, String opcionTexto, boolean esCorrecta) {
        this.idOpcion = idOpcion;
        this.idPregunta = idPregunta;
        this.opcionTexto = opcionTexto;
        this.esCorrecta = esCorrecta;
    }

    // Getters y Setters
    public int getIdOpcion() {
        return idOpcion;
    }

    public void setIdOpcion(int idOpcion) {
        this.idOpcion = idOpcion;
    }

    public int getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getOpcionTexto() {
        return opcionTexto;
    }

    public void setOpcionTexto(String opcionTexto) {
        this.opcionTexto = opcionTexto;
    }

    public boolean isEsCorrecta() {
        return esCorrecta;
    }

    public void setEsCorrecta(boolean esCorrecta) {
        this.esCorrecta = esCorrecta;
    }
}
