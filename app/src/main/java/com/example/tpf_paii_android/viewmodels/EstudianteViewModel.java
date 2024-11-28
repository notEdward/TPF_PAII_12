package com.example.tpf_paii_android.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tpf_paii_android.modelos.EstadoNivelEducativo;
import com.example.tpf_paii_android.modelos.Estudiante;
import com.example.tpf_paii_android.modelos.Genero;
import com.example.tpf_paii_android.modelos.Localidad;
import com.example.tpf_paii_android.modelos.NivelEducativo;
import com.example.tpf_paii_android.modelos.Provincia;
import com.example.tpf_paii_android.repositorios.EstudianteRepository;
import com.example.tpf_paii_android.repositorios.GeneroRepository;

import java.util.List;

public class EstudianteViewModel extends ViewModel {

    private final EstudianteRepository estudianteRepository;
    private LiveData<List<Genero>> generosLiveData;
    private LiveData<List<NivelEducativo>> nivelesEducLiveData;
    private LiveData<List<EstadoNivelEducativo>> estadoNivelesEducLiveData;
    private LiveData<List<Provincia>> ProvinciasLiveData;
    private LiveData<List<Localidad>> LocalidadesLiveData;
    private MutableLiveData<Integer> provinciaIdLiveData = new MutableLiveData<>();

    // Constructor del ViewModel que recibe el repositorio
    public EstudianteViewModel(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
        LocalidadesLiveData = Transformations.switchMap(provinciaIdLiveData, idProvincia ->
                estudianteRepository.obtenerLocalidadesPorProvincia(idProvincia)
        );
    }

    public LiveData<Boolean> registrarEstudiante(Estudiante estudiante, int idUsuario) {
        return estudianteRepository.registrarEstudiante(estudiante, idUsuario);
    }

    public LiveData<List<Genero>> getGeneros() {
        if (generosLiveData == null) {
            generosLiveData = estudianteRepository.obtenerGeneros();
        }
        return generosLiveData;
    }

    public LiveData<List<NivelEducativo>> getNivelesEducativos() {
        if (nivelesEducLiveData == null) {
            nivelesEducLiveData = estudianteRepository.obtenerNivelEducativo();
        }
        return nivelesEducLiveData;
    }

    public LiveData<List<EstadoNivelEducativo>> getEstadoNivelesEducativos() {
        if (estadoNivelesEducLiveData == null) {
            estadoNivelesEducLiveData = estudianteRepository.obtenerEstadoNivelEducativo();
        }
        return estadoNivelesEducLiveData;
    }

   public LiveData<List<Provincia>> getProvincias() {
        if (ProvinciasLiveData == null) {
            ProvinciasLiveData = estudianteRepository.obtenerProvincias();
        }
        return ProvinciasLiveData;
    }

   public LiveData<List<Localidad>> getLocalidades() {
        return LocalidadesLiveData;
    }

    public void setProvinciaSeleccionada(int idProvincia) {
        provinciaIdLiveData.setValue(idProvincia);
    }

// Factory para crear el ViewModel con un repositorio
    public static class Factory implements ViewModelProvider.Factory {
        private final EstudianteRepository estudianteRepository;

        public Factory(EstudianteRepository estudianteRepository) {
            this.estudianteRepository = estudianteRepository;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(EstudianteViewModel.class)) {
                return (T) new EstudianteViewModel(estudianteRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
