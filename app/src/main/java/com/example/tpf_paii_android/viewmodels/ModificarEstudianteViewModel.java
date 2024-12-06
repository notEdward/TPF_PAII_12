package com.example.tpf_paii_android.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tpf_paii_android.modelos.EstadoNivelEducativo;
import com.example.tpf_paii_android.modelos.Estudiante;
import com.example.tpf_paii_android.modelos.Genero;
import com.example.tpf_paii_android.modelos.Localidad;
import com.example.tpf_paii_android.modelos.NivelEducativo;
import com.example.tpf_paii_android.modelos.OfertaEmpleo;
import com.example.tpf_paii_android.modelos.Provincia;
import com.example.tpf_paii_android.repositorios.ModificarEstudianteRepository;
import com.example.tpf_paii_android.repositorios.OfertaRepository;

import java.util.List;

public class ModificarEstudianteViewModel extends ViewModel {

    private final ModificarEstudianteRepository modificarEstudianteRepository;
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Estudiante> estudianteLiveData = new MutableLiveData<>();
    private MutableLiveData<List<NivelEducativo>> nivelesEducativosLiveData = new MutableLiveData<>();
    private MutableLiveData<List<EstadoNivelEducativo>> estadoNivelEducativoLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Genero>> generoLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Localidad>> localidadesLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Provincia>> provinciasLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> exitoActualizacion = new MutableLiveData<>();


    public LiveData<List<NivelEducativo>> getNivelesEducativosLiveData() { return nivelesEducativosLiveData; }
    public LiveData<List<EstadoNivelEducativo>> getEstadoNivelEducativoLiveData() { return estadoNivelEducativoLiveData; }
    public LiveData<List<Genero>> getGeneroLiveData() { return generoLiveData; }
    public LiveData<Estudiante> getEstudianteLiveData() { return estudianteLiveData; }
    public LiveData<List<Localidad>> getLocalidadesLiveData() { return localidadesLiveData; }
    public LiveData<List<Provincia>> getProvinciasLiveData() { return provinciasLiveData; }
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    public LiveData<Boolean> getExitoActualizacion() {
        return exitoActualizacion;
    }

    public ModificarEstudianteViewModel() {
        this.modificarEstudianteRepository= new ModificarEstudianteRepository();
    }

    public void obtenerDetalleEstudiante(int idUsuario) {
        modificarEstudianteRepository.obtenerDetalleEstudiante(idUsuario, new ModificarEstudianteRepository.DataCallback<Estudiante>() {
            @Override
            public void onSuccess(Estudiante result) {
                estudianteLiveData.postValue(result);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    // Cargar niveles educativos
    public void cargarNivelesEducativos() {
        modificarEstudianteRepository.obtenerNivelesEducativos(new ModificarEstudianteRepository.DataCallback<List<NivelEducativo>>() {
            @Override
            public void onSuccess(List<NivelEducativo> nivelesEducativos) {
                nivelesEducativosLiveData.setValue(nivelesEducativos);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Error al cargar niveles educativos: " + e.getMessage());
            }
        });
    }

    public void cargarEstadoNivelEducativo() {
        modificarEstudianteRepository.obtenerEstadoNivelEducativo(new ModificarEstudianteRepository.DataCallback<List<EstadoNivelEducativo>>() {
            @Override
            public void onSuccess(List<EstadoNivelEducativo> estadoNivelEducativo) {
                estadoNivelEducativoLiveData.setValue(estadoNivelEducativo);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Error al cargar estados deducativos: " + e.getMessage());
            }
        });
    }
    //generos
    public void cargarGeneros() {
        modificarEstudianteRepository.obtenerGeneros(new ModificarEstudianteRepository.DataCallback<List<Genero>>() {
            @Override
            public void onSuccess(List<Genero> generos) {
                generoLiveData.setValue(generos);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Error al cargar generos: " + e.getMessage());
            }
        });
    }
    // Cargar localidades
    public void cargarLocalidades() {
        modificarEstudianteRepository.obtenerLocalidades(new ModificarEstudianteRepository.DataCallback<List<Localidad>>() {
            @Override
            public void onSuccess(List<Localidad> localidades) {
                localidadesLiveData.setValue(localidades);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Error al cargar localidades: " + e.getMessage());
            }
        });
    }
    public void cargarProvincias() {
        modificarEstudianteRepository.obtenerProvincias(new ModificarEstudianteRepository.DataCallback<List<Provincia>>() {
            @Override
            public void onSuccess(List<Provincia> provincias) {
                provinciasLiveData.setValue(provincias);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Error al cargar provincias: " + e.getMessage());
            }
        });
    }
    public void cargarLocalidadesPorProvincia(int idProvincia) {
        modificarEstudianteRepository.obtenerLocalidadesPorProvincia(idProvincia, new ModificarEstudianteRepository.DataCallback<List<Localidad>>() {
            @Override
            public void onSuccess(List<Localidad> localidades) {
                localidadesLiveData.setValue(localidades);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Error al cargar localidades: " + e.getMessage());
            }
        });
    }

    public void actualizarEstudiante(Estudiante estudianteActualizado) {
        modificarEstudianteRepository.actualizarEstudiante(estudianteActualizado, new ModificarEstudianteRepository.DataCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean exito) {
                exitoActualizacion.setValue(exito);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue(e.getMessage());
            }
        });
    }
}
