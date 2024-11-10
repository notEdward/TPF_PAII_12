package com.example.tpf_paii_android.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tpf_paii_android.modelos.Tutor;
import com.example.tpf_paii_android.repositorios.TutorRepository;

public class TutorViewModel extends ViewModel {

    private MutableLiveData<Tutor> tutorLiveData; // Permite almacenar y observar los datos del objeto tipo Tutor
    private TutorRepository tutorRepository;

    public TutorViewModel(){
        tutorRepository = new TutorRepository();
        tutorLiveData = new MutableLiveData<>();
    }

    // Método para obtener el tutor desde el repositorio
    public void cargarTutorPorUsuario(int idUsuario){
        Tutor tutor = tutorRepository.obtenerTutorPorIdUsuario(idUsuario);
        tutorLiveData.setValue(tutor);
    }

    // Getter LiveData

    public LiveData<Tutor> getTutor(){
        return tutorLiveData;
    }
}
