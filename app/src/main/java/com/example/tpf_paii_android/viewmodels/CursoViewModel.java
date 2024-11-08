package com.example.tpf_paii_android.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.tpf_paii_android.modelos.Curso;
import com.example.tpf_paii_android.modelos.Evaluacion;
import com.example.tpf_paii_android.modelos.Inscripcion;
import com.example.tpf_paii_android.modelos.Pregunta;
import com.example.tpf_paii_android.repositorios.CursoRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CursoViewModel extends ViewModel {
    private CursoRepository cursoRepository;
    private MutableLiveData<List<Curso>> cursosLiveData;
    private MutableLiveData<List<Curso>> cursosFiltradosLiveData;
    private MutableLiveData<Exception> errorLiveData;
    private final MutableLiveData<Boolean> inscripcionExitosa = new MutableLiveData<>();
//    private MutableLiveData<Boolean> inscripcionActiva = new MutableLiveData<>();
private MutableLiveData<Integer> inscripcionActiva = new MutableLiveData<>();
    private final MutableLiveData<List<Pregunta>> preguntasConOpciones = new MutableLiveData<>();
    private MutableLiveData<Boolean> evaluacionExitosa = new MutableLiveData<>();

    public CursoViewModel() {
        cursoRepository = new CursoRepository();
        cursosLiveData = new MutableLiveData<>();
        cursosFiltradosLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
    }
    public LiveData<List<Curso>> getCursosFiltrados() {
        return cursosFiltradosLiveData;
    }

    public LiveData<Exception> getError() {
        return errorLiveData;
    }

    public LiveData<Boolean> getInscripcionExitosa() {
        return inscripcionExitosa;
    }
//    public LiveData<Boolean> getInscripcionActiva() {
//        return inscripcionActiva;
//    }
public LiveData<Integer> getInscripcionActiva() {
    return inscripcionActiva;
}
    public LiveData<List<Pregunta>> getPreguntasConOpciones() {
        return preguntasConOpciones;
    }
    public LiveData<Boolean> getEvaluacionExitosa() {
        return evaluacionExitosa;
    }

    public void cargarCursos() {
        cursoRepository.getAllCursos(new CursoRepository.DataCallback<ArrayList<Curso>>() {
            @Override
            public void onSuccess(ArrayList<Curso> cursos) {
                cursosLiveData.setValue(cursos);
                cursosFiltradosLiveData.setValue(cursos);  // Inicializamos con todos los cursos
            }

            @Override
            public void onFailure(Exception e) {
                errorLiveData.setValue(e);
            }
        });
    }
    //filtro por campo (nombre)
    public void filtrarCursos(String query) {
        List<Curso> cursosOriginales = cursosLiveData.getValue();
        if (cursosOriginales != null) {
            List<Curso> cursosFiltrados = new ArrayList<>();
            for (Curso curso : cursosOriginales) {
                if (curso.getNombreCurso().toLowerCase().contains(query.toLowerCase())) {
                    cursosFiltrados.add(curso);
                }
            }
            cursosFiltradosLiveData.setValue(cursosFiltrados);
        }
    }

    public void filtrarPorCategorias(List<Integer> categoriasSeleccionadas) {
        List<Curso> cursosOriginales = cursosLiveData.getValue();
        if (cursosOriginales != null && categoriasSeleccionadas != null && !categoriasSeleccionadas.isEmpty()) {
            List<Curso> cursosFiltrados = new ArrayList<>();
            for (Curso curso : cursosOriginales) {
                if (categoriasSeleccionadas.contains(curso.getIdCategoria())) {
                    cursosFiltrados.add(curso);
                }
            }
            cursosFiltradosLiveData.setValue(cursosFiltrados);
        } else {
            // por defecto todos ( si no hay cursos )
            cursosFiltradosLiveData.setValue(cursosOriginales);
        }
    }

    public void registrarInscripcion(int idCurso, int idUsuario, String estado) {
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setIdCurso(idCurso);
        inscripcion.setIdUsuario(idUsuario);
        inscripcion.setFechaInscripcion(new Date()); // Fecha de inscripción actual
        inscripcion.setEstadoInscripcion(estado);

        cursoRepository.registrarInscripcion(inscripcion, new CursoRepository.DataCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean success) {
                // Actualiza el LiveData con el éxito de la inscripción
                inscripcionExitosa.postValue(success);
            }

            @Override
            public void onFailure(Exception e) {
                // Actualiza el LiveData con false si ocurre un error
                inscripcionExitosa.postValue(false);
            }
        });
    }

//    public void verificarInscripcionActiva(int idCurso, int idUsuario) {
//        cursoRepository.verificarInscripcionActiva(idCurso, idUsuario, new CursoRepository.DataCallback<Boolean>() {
//            @Override
//            public void onSuccess(Boolean isActive) {
//                inscripcionActiva.postValue(isActive);
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                inscripcionActiva.postValue(false);  // Manejar error
//            }
//        });
//    }
public void verificarInscripcionActiva(int idCurso, int idUsuario) {
    cursoRepository.verificarInscripcionActiva(idCurso, idUsuario, new CursoRepository.DataCallback<Integer>() {
        @Override
        public void onSuccess(Integer idInscripcion) {
            if (idInscripcion != null) {
                // Si existe una inscripción activa, guardamos el idInscripcion
                inscripcionActiva.postValue(idInscripcion);
            } else {
                // Si no existe inscripción activa, pasamos null
                inscripcionActiva.postValue(null);
            }
        }

        @Override
        public void onFailure(Exception e) {
            // Manejo de error
            inscripcionActiva.postValue(null);
        }
    });
}

    public void obtenerPreguntasConOpciones(int idCurso) {
        cursoRepository.obtenerPreguntasConOpciones(idCurso, new CursoRepository.DataCallback<List<Pregunta>>() {
            @Override
            public void onSuccess(List<Pregunta> result) {
                preguntasConOpciones.postValue(result);  // Publicamos las preguntas obtenidas
            }

            @Override
            public void onFailure(Exception e) {
                // En caso de error, podemos manejarlo aquí o mostrar un mensaje al usuario en la actividad
                preguntasConOpciones.postValue(null);
            }
        });
    }
    public void registrarEvaluacion(int idInscripcion, int notaObtenida, Date fechaFinalizacion) {
        Evaluacion evaluacion = new Evaluacion(idInscripcion, notaObtenida, fechaFinalizacion);

        cursoRepository.registrarEvaluacion(evaluacion, new CursoRepository.DataCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                // Notificar a la actividad que la evaluación se registró exitosamente
                evaluacionExitosa.postValue(true);
            }

            @Override
            public void onFailure(Exception e) {
                // Notificar a la actividad que hubo un fallo al registrar la evaluación
                evaluacionExitosa.postValue(false);
            }
        });
    }
}
