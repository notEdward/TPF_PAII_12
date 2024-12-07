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
import com.example.tpf_paii_android.modelos.Tutor;
import com.example.tpf_paii_android.repositorios.EmpresaRepository;
import com.example.tpf_paii_android.repositorios.EstudianteRepository;
import com.example.tpf_paii_android.repositorios.ModificarEstudianteRepository;

import java.util.List;


public class EmpresaViewModel extends ViewModel {

    private final EmpresaRepository empresaRepository;

    private LiveData<List<Provincia>> ProvinciasLiveData;
    private LiveData<List<Localidad>> LocalidadesLiveData;
    private MutableLiveData<Integer> provinciaIdLiveData = new MutableLiveData<>();

    private MutableLiveData<Empresa> empresaLiveData = new MutableLiveData<>();

    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> registroExitosoLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> actualizacionExitosaLiveData = new MutableLiveData<>();

    // Constructor del ViewModel que recibe el repositorio
    public EmpresaViewModel(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
        LocalidadesLiveData = Transformations.switchMap(provinciaIdLiveData, idProvincia ->
                empresaRepository.obtenerLocalidadesPorProvincia(idProvincia)
        );

        // Asigna un valor inicial, como 0 o una provincia predeterminada
        provinciaIdLiveData.setValue(1); // O reemplaza 0 por un valor válido si lo conoces
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

    public int getProvinciaSeleccionada() {
        Integer id = provinciaIdLiveData.getValue();
        return id != null ? id : -1;
    }


    // Getter para el LiveData de errores
    public LiveData<String> getError() {
        return errorLiveData;
    }

    // Obtener la respuesta de si el registro fue exitoso
    public LiveData<Boolean> getRegistroExitoso() {
        return registroExitosoLiveData;
    }

    // Observador del estado de la actualización
    public LiveData<Boolean> getActualizacionExitosaLiveData() {
        return actualizacionExitosaLiveData;
    }



    public LiveData<Empresa> cargarEmpresa(int idEmpresa) {
        // Usamos un nuevo hilo para obtener la empresa
        new Thread(() -> {
            Empresa empresa = empresaRepository.obtenerEmpresa(idEmpresa);
            empresaLiveData.postValue(empresa); // Postear los datos en el LiveData
        }).start();
        return empresaLiveData;
    }

    // Método para actualizar un tutor
    public void actualizarEmpresa(Empresa empresa) {
        new Thread(() -> {
            boolean resultado = empresaRepository.modificarEmpresa(empresa);
            if (resultado) {
                actualizacionExitosaLiveData.postValue(true);
            } else {
                actualizacionExitosaLiveData.postValue(false);
                errorLiveData.postValue("Error al actualizar los datos de la empresa.");
            }
        }).start();
    }



    public void cargarLocalidadesPorProvincia(int idProvincia) {
        LocalidadesLiveData =  empresaRepository.obtenerLocalidadesPorProvincia(idProvincia);
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
