package com.example.tpf_paii_android.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tpf_paii_android.modelos.Curso;
import com.example.tpf_paii_android.repositorios.CursoRepository;

import java.util.ArrayList;
import java.util.List;
public class CursoViewModel extends ViewModel {
    private CursoRepository cursoRepository;
    private MutableLiveData<List<Curso>> cursosLiveData;
    private MutableLiveData<List<Curso>> cursosFiltradosLiveData;
    private MutableLiveData<Exception> errorLiveData;

    public CursoViewModel() {
        cursoRepository = new CursoRepository();
        cursosLiveData = new MutableLiveData<>();
        cursosFiltradosLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Curso>> getCursos() {
        return cursosLiveData;
    }

    public LiveData<List<Curso>> getCursosFiltrados() {
        return cursosFiltradosLiveData;
    }

    public LiveData<Exception> getError() {
        return errorLiveData;
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
}
