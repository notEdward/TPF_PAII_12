package com.example.tpf_paii_android.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tpf_paii_android.modelos.Curso;
import com.example.tpf_paii_android.repositorios.TutoriasRepository;

import java.util.List;

public class TutoriasViewModel extends ViewModel {

    private TutoriasRepository tutoriasRepository;
    private final MutableLiveData<List<Curso>> cursosLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public TutoriasViewModel() {
        this(new TutoriasRepository());
    }

    public TutoriasViewModel(TutoriasRepository repository) {
        this.tutoriasRepository = repository;
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

    // LiveData cursos
    public LiveData<List<Curso>> getCursosLiveData() {
        return cursosLiveData;
    }

    // LiveData errores
    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

}
