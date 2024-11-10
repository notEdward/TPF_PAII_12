package com.example.tpf_paii_android.modelos;

public class Tutor extends Usuario{

    private int idTutor; // AUTOINCREMENT en la DB
    private String dni; // El dni es varchar(20) en la bd
    private String nombre;
    private String apellido;
    private int edad;
    private Genero idGenero;
    private String ocupacion;
    private String pasatiempos;
    private String infoAdicional;

    // Constructor
    public Tutor() {
    }

    // Constructor sin idTutor porque es AUTOINCREMENT en la DB
    public Tutor(String dni, String nombre, String apellido, int edad, Genero idGenero, String ocupacion, String pasatiempos, String infoAdicional) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.idGenero = idGenero;
        this.ocupacion = ocupacion;
        this.pasatiempos = pasatiempos;
        this.infoAdicional = infoAdicional;
    }

    // Constructor que incluye Usuario 
    public Tutor(int idTutor, String dni, String nombre, String apellido, int edad, Genero idGenero, String ocupacion, String pasatiempos, String infoAdicional, Usuario usuario) {
        super(usuario);
        this.idTutor = idTutor;
        this.dni = dni;
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

    public Genero getIdGenero() {
        return idGenero;
    }

    public void setIdGenero(Genero idGenero) {
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


    @Override
    public String toString() {
        return "Tutor{" +
                "idTutor=" + idTutor +
                ", dni='" + dni + '\'' +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", edad=" + edad +
                ", genero=" + idGenero +
                ", ocupacion='" + ocupacion + '\'' +
                ", pasatiempos='" + pasatiempos + '\'' +
                ", infoAdicional='" + infoAdicional + '\'' +
                '}';
    }
}