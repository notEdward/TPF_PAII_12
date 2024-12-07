package com.example.tpf_paii_android.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tpf_paii_android.modelos.Curso;
import com.example.tpf_paii_android.modelos.Tutoria;
import com.example.tpf_paii_android.repositorios.TutoriasRepository;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TutoriasViewModel extends ViewModel {

    private TutoriasRepository tutoriasRepository;
    private final MutableLiveData<List<Curso>> cursosLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Tutoria>> tutoriasAsignadasLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> successLiveData = new MutableLiveData<>();

    private MutableLiveData<Boolean> tutoriaFinalizadaLiveData = new MutableLiveData<>();

    private final MutableLiveData<Map<String, String>> estudiantesMap = new MutableLiveData<>(); // LiveData para el mapa de estudiante

    private int idUsuario;

    // Correct:
    /*
    public TutoriasViewModel() {
        this.tutoriasRepository = new TutoriasRepository();
        obtenerEstudiantesMap();
    }
    */

    public TutoriasViewModel() {
        this(new TutoriasRepository());
    }

    public TutoriasViewModel(TutoriasRepository repository) {
        this.tutoriasRepository = repository;
        obtenerEstudiantesMap();
    }

    public LiveData<Boolean> getTutoriaFinalizadaLiveData() {
        return tutoriaFinalizadaLiveData;
    }


    // ELIMINAR TUTORIA
    public void eliminarTutoria(int idTutoria, int idUsuario) {
        tutoriasRepository.finalizarTutoria(idTutoria, new TutoriasRepository.DataCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    successLiveData.postValue("Tutoria eliminada exitosamente.");

                    // Obtener el ID del tutor async
                    tutoriasRepository.getIdTutorPorIdUsuario(idUsuario, new TutoriasRepository.DataCallback<Integer>() {
                        @Override
                        public void onSuccess(Integer idTutor) {
                            // Una vez obtenido id_tutor, carga las tutorías
                            cargarTutoriasPorTutor(idTutor);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            errorLiveData.postValue("Error al obtener el ID del tutor: " + e.getMessage());
                        }
                    });
                } else {
                    errorLiveData.postValue("Error al eliminar la tutoría.");
                }
            }

            @Override
            public void onFailure(Exception e) {
                errorLiveData.postValue("Error al eliminar la tutoría: " + e.getMessage());
            }
        });
    }

    // método para obtener el id_tutor del Tutor usando el idUsuario
    public void obtenerIdTutor(int idUsuario, TutoriasRepository.DataCallback<Integer> callback) {
        tutoriasRepository.getIdTutorPorIdUsuario(idUsuario, callback);
    }

    // Método para obtener el dni del estudiante usando el idUsuario
    public void obtenerDniEstudiante(int idUsuario, TutoriasRepository.DataCallback<String> callback) {
        tutoriasRepository.getDniEstudiantePorIdUsuario(idUsuario, callback);
    }

    // Obtiene los cursos por estudiante
    public void cargarCursosPorEstudiante(int idUsuario) {
        tutoriasRepository.getCursosPorEstudiante(idUsuario, new TutoriasRepository.DataCallback<List<Curso>>() {
            @Override
            public void onSuccess(List<Curso> result) {
                cursosLiveData.postValue(result);
            }

            @Override
            public void onFailure(Exception e) {
                errorLiveData.postValue(e.getMessage());
            }
        });
    }

    // Obtiene el mapa de estudiantes
    public void obtenerEstudiantesMap() {
        tutoriasRepository.getEstudiantesMap(new TutoriasRepository.DataCallback<Map<String, String>>() {
            @Override
            public void onSuccess(Map<String, String> result) {
                estudiantesMap.postValue(result);
            }

            @Override
            public void onFailure(Exception e) {
                errorLiveData.postValue(e.getMessage());
            }
        });
    }

    // Obtiene las tutorías asignadas por tutor
    public void cargarTutoriasPorTutor(int idUsuario) {
        tutoriasRepository.getTutoriasPorTutor(idUsuario, new TutoriasRepository.DataCallback<List<Tutoria>>() {
            @Override
            public void onSuccess(List<Tutoria> result) {
                tutoriasAsignadasLiveData.postValue(result);
            }

            @Override
            public void onFailure(Exception e) {
                errorLiveData.postValue(e.getMessage());
            }
        });
    }

    // Metodo para config el usuario logueado
    public void setUsuarioLogueado(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    // Metodo para solicitar tutoría
    public void solicitarTutoria(int idCurso, String tema, String comentarios, Date fecha) {
        // Obtiene los tutores disponibles
        tutoriasRepository.obtenerTutores(new TutoriasRepository.DataCallback<List<Integer>>() {
            @Override
            public void onSuccess(List<Integer> tutores) {
                if (tutores.isEmpty()) {
                    errorLiveData.postValue("No hay tutores disponibles.");
                    return;
                }

                // Selecciona tutor aleatorio
                Random random = new Random();
                int idTutor = tutores.get(random.nextInt(tutores.size()));

                // Obtiene el DNI del estudiante logueado
                obtenerDniEstudiante(idUsuario, new TutoriasRepository.DataCallback<String>() {
                    @Override
                    public void onSuccess(String dniEstudiante) {
                        if (dniEstudiante != null) {
                            // Registra la tutoría en el repositorio
                            tutoriasRepository.crearTutoria(
                                    idTutor,
                                    dniEstudiante,
                                    idCurso,
                                    fecha,
                                    tema,
                                    comentarios,
                                    new TutoriasRepository.DataCallback<Boolean>() {
                                        @Override
                                        public void onSuccess(Boolean result) {
                                            if (result) {
                                                successLiveData.postValue("Tutoria registrada exitosamente.");
                                            } else {
                                                errorLiveData.postValue("Error al registrar la tutoria.");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Exception e) {
                                            errorLiveData.postValue("Error al registrar la tutoria: " + e.getMessage());
                                        }
                                    }
                            );
                        } else {
                            errorLiveData.postValue("No se encontro el DNI del estudiante.");
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        errorLiveData.postValue("Error al obtener el DNI del estudiante: " + e.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                errorLiveData.postValue("Error al obtener tutores: " + e.getMessage());
            }
        });
    }



    // LiveData cursos
    public LiveData<List<Curso>> getCursosLiveData() {
        return cursosLiveData;
    }

    // LiveData tutorías asignadas
    public LiveData<List<Tutoria>> getTutoriasAsignadasLiveData() {
        return tutoriasAsignadasLiveData;
    }

    // LiveData errores
    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    // LiveData mapa de estudiantes
    public LiveData<Map<String, String>> getEstudiantesMapLiveData() {
        return estudiantesMap;
    }

    // LiveData para mensajes de exito
    public LiveData<String> getSuccessLiveData() {
        return successLiveData;
    }
}