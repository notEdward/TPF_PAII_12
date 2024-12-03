package com.example.tpf_paii_android.viewmodels;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tpf_paii_android.modelos.Genero;
import com.example.tpf_paii_android.modelos.Tutor;
import com.example.tpf_paii_android.repositorios.GeneroRepository;
import com.example.tpf_paii_android.repositorios.TutorRepository;
import com.example.tpf_paii_android.repositorios.UsuarioRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TutorViewModel extends ViewModel {

    private final TutorRepository tutorRepository;
    private final GeneroRepository generoRepository;
    private final UsuarioRepository usuarioRepository;

    private final MutableLiveData<List<Genero>> generosLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Tutor>> tutoresLiveData = new MutableLiveData<>(); // Permite almacenar y observar los datos del objeto tipo Tutor
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> registroExitosoLiveData = new MutableLiveData<>();

    private final MutableLiveData<Tutor> tutorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> actualizacionExitosaLiveData = new MutableLiveData<>();


    // Ejecutor
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // Constructor que recibe los repositorios
    public TutorViewModel(Context context){
        this.tutorRepository = new TutorRepository(context);
        this.generoRepository = new GeneroRepository(context);
        this.usuarioRepository = new UsuarioRepository(context);
        cargarGenerosDesdeRepositorio(); // Carga los generos al inicializar el ViewModel
    }

    // Obtener lista de generos
    public LiveData<List<Genero>> getGeneros() {
        return generosLiveData;
    }

    // Obtener lista de tutores
    public LiveData<List<Tutor>> getTutores() {
        return tutoresLiveData;
    }

    // Obtener la respuesta del error
    public LiveData<String> getError() {
        return errorLiveData;
    }

    // Getter para el LiveData de errores
    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    // Obtener la respuesta de si el registro fue exitoso
    public LiveData<Boolean> getRegistroExitoso() {
        return registroExitosoLiveData;
    }

    // obtener el tutor
    public LiveData<Tutor> getTutorLiveData() {
        return tutorLiveData;
    }

    // Observador del estado de la actualización
    public LiveData<Boolean> getActualizacionExitosaLiveData() {
        return actualizacionExitosaLiveData;
    }


    // Metodo cargarGeneros() - carga todos los generos en una lista
    public void cargarGenerosDesdeRepositorio(){
        generoRepository.obtenerTodos().observeForever(generos -> {
            if (generos != null) {
                generosLiveData.setValue(generos); // Actualiza el LiveData
            } else {
                errorLiveData.setValue("No se pudieron cargar los generos.");
            }
        });
    }

    // Metodo para agregar un tutor
    public void registrarTutor(Tutor tutor) {
        tutorRepository.registrarTutor(tutor, new TutorRepository.DataCallBack<Boolean>() {
            @Override
            public void onSuccess(Boolean isSuccess) {
                // Si es exitoso, actualizamos el LiveData
                if (isSuccess) {
                    registroExitosoLiveData.setValue(true);
                } else {
                    // Si no es exitoso, actualizamos el error
                    errorLiveData.setValue("Error al registrar tutor.");
                }
            }

            @Override
            public void onFailure(Exception e) {
                // Si falla, actualizamos el LiveData con el error
                errorLiveData.setValue("Error al registrar tutor: " + e.getMessage());
            }
        });
    }


    // Método para cargar los datos del tutor
    public void cargarTutor(int idTutor) {
        new Thread(() -> {
            Tutor tutor = tutorRepository.obtenerTutor(idTutor);
            if (tutor != null) {
                tutorLiveData.postValue(tutor);  // Actualiza el LiveData con los datos del tutor
            } else {
                errorLiveData.postValue("No se pudo obtener los datos del tutor");
            }
        }).start();
    }

    // Método para actualizar un tutor
    public void actualizarTutor(Tutor tutor) {
        new Thread(() -> {
            boolean resultado = tutorRepository.actualizarTutor(tutor);
            if (resultado) {
                actualizacionExitosaLiveData.postValue(true);
            } else {
                actualizacionExitosaLiveData.postValue(false);
                errorLiveData.postValue("Error al actualizar los datos del tutor.");
            }
        }).start();
    }

}