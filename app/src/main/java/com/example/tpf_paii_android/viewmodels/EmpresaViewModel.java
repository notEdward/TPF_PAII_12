package com.example.tpf_paii_android.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tpf_paii_android.modelos.Empresa;
import com.example.tpf_paii_android.modelos.Estudiante;
import com.example.tpf_paii_android.modelos.Localidad;
import com.example.tpf_paii_android.modelos.Provincia;
import com.example.tpf_paii_android.repositorios.EmpresaRepository;
import com.example.tpf_paii_android.repositorios.EstudianteRepository;

import java.util.List;


public class EmpresaViewModel extends ViewModel {

    private final EmpresaRepository empresaRepository;
    private LiveData<List<Provincia>> ProvinciasLiveData;
    private LiveData<List<Localidad>> LocalidadesLiveData;
    private MutableLiveData<Integer> provinciaIdLiveData = new MutableLiveData<>();

    // Constructor del ViewModel que recibe el repositorio
    public EmpresaViewModel(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
        LocalidadesLiveData = Transformations.switchMap(provinciaIdLiveData, idProvincia ->
                empresaRepository.obtenerLocalidadesPorProvincia(idProvincia)
        );
    }

    public LiveData<Boolean> registrarEmpresa(Empresa empresa, int idUsuario) {
        return empresaRepository.registrarEmpresa(empresa, idUsuario);
    }

    public LiveData<List<Provincia>> getProvincias() {
        if (ProvinciasLiveData == null) {
            ProvinciasLiveData = empresaRepository.obtenerProvincias();
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
        private final EmpresaRepository empresaRepository;

        public Factory(EmpresaRepository empresaRepository) {
            this.empresaRepository = empresaRepository;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(EmpresaViewModel.class)) {
                return (T) new EmpresaViewModel(empresaRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
