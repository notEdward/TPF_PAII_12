package com.example.tpf_paii_android.viewmodels;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tpf_paii_android.actividades.ofertas.AltaOfertaActivity;
import com.example.tpf_paii_android.modelos.Curso;
import com.example.tpf_paii_android.modelos.Estudiante;
import com.example.tpf_paii_android.modelos.Localidad;
import com.example.tpf_paii_android.modelos.Modalidad;
import com.example.tpf_paii_android.modelos.NivelEducativo;
import com.example.tpf_paii_android.modelos.OfertaDetalle;
import com.example.tpf_paii_android.modelos.OfertaEmpleo;
import com.example.tpf_paii_android.modelos.PostulacionItem;
import com.example.tpf_paii_android.modelos.Provincia;
import com.example.tpf_paii_android.modelos.TipoEmpleo;
import com.example.tpf_paii_android.repositorios.CursoRepository;
import com.example.tpf_paii_android.repositorios.OfertaRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OfertaViewModel extends ViewModel {
    private final OfertaRepository ofertaRepository;
    private final MutableLiveData<List<OfertaEmpleo>> ofertasLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<List<OfertaEmpleo>> ofertasFiltradasLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> postularMensaje = new MutableLiveData<>();
    //empresa
    private MutableLiveData<List<TipoEmpleo>> tiposEmpleoLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Modalidad>> modalidadesLiveData = new MutableLiveData<>();
    private MutableLiveData<List<NivelEducativo>> nivelesEducativosLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Curso>> cursosLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Localidad>> localidadesLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Provincia>> provinciasLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> ofertaCreadaExitosamente = new MutableLiveData<>();
    //baja
    private MutableLiveData<Boolean> bajaOfertaLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<PostulacionItem>> postulaciones = new MutableLiveData<>();
    ////
    private MutableLiveData<Estudiante> estudianteLiveData = new MutableLiveData<>();
    private final MutableLiveData<OfertaEmpleo> ofertaLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> exitoActualizacion = new MutableLiveData<>();

    public LiveData<String> getPostularMensaje() {
        return postularMensaje;
    }
    //oferta detalle
    private final MutableLiveData<OfertaDetalle> ofertaDetalleLiveData = new MutableLiveData<>();
    private MutableLiveData<String> estadoPostulacionLiveData = new MutableLiveData<>();

    public LiveData<OfertaDetalle> getOfertaDetalleLiveData() {
        return ofertaDetalleLiveData;
    }

    public OfertaViewModel() {
        this.ofertaRepository = new OfertaRepository();
    }

    public LiveData<List<OfertaEmpleo>> getOfertasLiveData() {
        return ofertasLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<List<OfertaEmpleo>> getOfertasFiltradas() {
        return ofertasFiltradasLiveData;
    }
    public LiveData<List<PostulacionItem>> getPostulaciones() {
        return postulaciones;
    }
    //empresa
    public LiveData<List<TipoEmpleo>> getTiposEmpleoLiveData() { return tiposEmpleoLiveData; }
    public LiveData<List<Modalidad>> getModalidadesLiveData() { return modalidadesLiveData; }
    public LiveData<List<NivelEducativo>> getNivelesEducativosLiveData() { return nivelesEducativosLiveData; }
    public LiveData<List<Curso>> getCursosLiveData() { return cursosLiveData; }
    public LiveData<List<Localidad>> getLocalidadesLiveData() { return localidadesLiveData; }
    public LiveData<List<Provincia>> getProvinciasLiveData() { return provinciasLiveData; }
    public LiveData<Boolean> getOfertaCreadaExitosamente() {
        return ofertaCreadaExitosamente;
    }
    //baja
    public LiveData<Boolean> getBajaOfertaLiveData() {
        return bajaOfertaLiveData;
    }
    ////
    //detalle postuliacion est
    public LiveData<Estudiante> getEstudianteLiveData() {
        return estudianteLiveData;
    }
    public LiveData<String> getEstadoPostulacionLiveData() {
        return estadoPostulacionLiveData;
    }
    //para modificacion
    public LiveData<OfertaEmpleo> getOfertaLiveData() {
        return ofertaLiveData;
    }
    public LiveData<Boolean> getExitoActualizacion() {
        return exitoActualizacion;
    }

    public void loadOfertas() {
        ofertaRepository.getAllOfertas(new OfertaRepository.DataCallback<ArrayList<OfertaEmpleo>>() {
            @Override
            public void onSuccess(ArrayList<OfertaEmpleo> result) {
                ofertasLiveData.setValue(result);
                // Actualizamos el LiveData filtrado con todos los elementos inicialmente
                ofertasFiltradasLiveData.setValue(result);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Error al cargar ofertas: " + e.getMessage());
            }
        });
    }


    public void filtrarOfertas(String query) {
        List<OfertaEmpleo> ofertasOriginales = ofertasLiveData.getValue();
        if (ofertasOriginales != null) {
            List<OfertaEmpleo> ofertasFiltradas = new ArrayList<>();
            for (OfertaEmpleo oferta : ofertasOriginales) {
                if (oferta.getTitulo().toLowerCase().contains(query.toLowerCase())) {
                    ofertasFiltradas.add(oferta);
                }
            }
            ofertasFiltradasLiveData.setValue(ofertasFiltradas);
        }
    }

    public void obtenerOfertasConFiltros(ArrayList<Integer> modalidades, ArrayList<Integer> tiposEmpleo, ArrayList<Integer> cursos) {
        ofertaRepository.obtenerOfertasConFiltros(modalidades, tiposEmpleo, cursos, new OfertaRepository.DataCallback<List<OfertaEmpleo>>() {
            @Override
            public void onSuccess(List<OfertaEmpleo> ofertas) {

                ofertasFiltradasLiveData.setValue(ofertas);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Error al cargar ofertas con filtros: " + e.getMessage());
            }
        });
    }
    public void obtenerDetallesOferta(int idOfertaEmpleo) {
        ofertaRepository.obtenerDetallesOferta(idOfertaEmpleo, new OfertaRepository.DataCallback<OfertaDetalle>() {
            @Override
            public void onSuccess(OfertaDetalle result) {
                ofertaDetalleLiveData.setValue(result);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Error al cargar los detalles de la oferta: " + e.getMessage());
            }
        });
    }

    public void postularUsuario(int idOfertaEmpleo, int idUsuario) {
        ofertaRepository.registrarPostulacion(idOfertaEmpleo, idUsuario, new OfertaRepository.DataCallback<String>() {
            @Override
            public void onSuccess(String mensaje) {
                postularMensaje.setValue(mensaje);
            }

            @Override
            public void onFailure(Exception e) {
                postularMensaje.setValue("Error al realizar la postulación: " + e.getMessage());
            }
        });
    }

    //empresa carga para alta
    public void cargarTiposEmpleo() {
        ofertaRepository.obtenerTiposEmpleo(new OfertaRepository.DataCallback<List<TipoEmpleo>>() {
            @Override
            public void onSuccess(List<TipoEmpleo> tiposEmpleo) {
                tiposEmpleoLiveData.setValue(tiposEmpleo);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Error al cargar tipos de empleo: " + e.getMessage());
            }
        });
    }

    // Cargar modalidades
    public void cargarModalidades() {
        ofertaRepository.obtenerModalidades(new OfertaRepository.DataCallback<List<Modalidad>>() {
            @Override
            public void onSuccess(List<Modalidad> modalidades) {
                modalidadesLiveData.setValue(modalidades);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Error al cargar modalidades: " + e.getMessage());
            }
        });
    }

    // Cargar niveles educativos
    public void cargarNivelesEducativos() {
        ofertaRepository.obtenerNivelesEducativos(new OfertaRepository.DataCallback<List<NivelEducativo>>() {
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

    // Cargar cursos
    public void cargarCursos() {
        ofertaRepository.obtenerCursos(new OfertaRepository.DataCallback<List<Curso>>() {
            @Override
            public void onSuccess(List<Curso> cursos) {
                cursosLiveData.setValue(cursos);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Error al cargar cursos: " + e.getMessage());
            }
        });
    }

    // Cargar localidades
    public void cargarLocalidades() {
        ofertaRepository.obtenerLocalidades(new OfertaRepository.DataCallback<List<Localidad>>() {
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
        ofertaRepository.obtenerProvincias(new OfertaRepository.DataCallback<List<Provincia>>() {
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
        ofertaRepository.obtenerLocalidadesPorProvincia(idProvincia, new OfertaRepository.DataCallback<List<Localidad>>() {
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
    // En OfertaViewModel
    public void guardarOferta(OfertaEmpleo oferta) {
        ofertaRepository.guardarOferta(oferta, new OfertaRepository.DataCallback<String>() {
            @Override
            public void onSuccess(String result) {
                ofertaCreadaExitosamente.setValue(true);
            }

            @Override
            public void onFailure(Exception e) {
                ofertaCreadaExitosamente.setValue(false);
            }
        });
    }
    // En OfertaViewModel
    // Método para obtener el ID de la empresa por el ID de usuario
    public void obtenerIdEmpresaPorUsuario(int idUsuario, AltaOfertaActivity.DataCallback<Integer> callback) {
        ofertaRepository.obtenerIdEmpresaPorUsuario(idUsuario, new OfertaRepository.DataCallback<Integer>() {
            @Override
            public void onSuccess(Integer idEmpresa) {
                callback.onSuccess(idEmpresa);  // Pasa el idEmpresa obtenido al callback
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Error al obtener el ID de la empresa: " + e.getMessage());
                callback.onFailure(e);  // Pasa el error al callback
            }
        });
    }

    //baja
    public void actualizarEstadoOferta(int idOferta, int estado) {
        ofertaRepository.actualizarEstadoOferta(idOferta, estado, new OfertaRepository.DataCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                bajaOfertaLiveData.setValue(result);
            }

            @Override
            public void onFailure(Exception e) {
                bajaOfertaLiveData.setValue(false);
            }
        });
    }
//postulaciones
public void cargarPostulacionesEstudiante(int idUsuario) {
    ofertaRepository.obtenerPostulacionesEstudiante(idUsuario, new OfertaRepository.DataCallback<List<PostulacionItem>>() {
        @Override
        public void onSuccess(List<PostulacionItem> result) {
            postulaciones.postValue(result);
        }

        @Override
        public void onFailure(Exception e) {
            postulaciones.postValue(Collections.emptyList());
        }
    });
}

    public void cargarPostulacionesEmpresa(int idUsuario) {
        ofertaRepository.obtenerPostulacionesEmpresa(idUsuario, new OfertaRepository.DataCallback<List<PostulacionItem>>() {
            @Override
            public void onSuccess(List<PostulacionItem> result) {
                postulaciones.postValue(result);
            }

            @Override
            public void onFailure(Exception e) {
                postulaciones.postValue(Collections.emptyList());
            }
        });
    }

    public void obtenerDetalleEstudiante(int idUsuario) {
        ofertaRepository.obtenerDetalleEstudiante(idUsuario, new OfertaRepository.DataCallback<Estudiante>() {
            @Override
            public void onSuccess(Estudiante result) {
                estudianteLiveData.postValue(result);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    public void actualizarEstadoPostulacion(int idPostulacion, String nuevoEstado) {
        ofertaRepository.actualizarEstadoPostulacion(idPostulacion, nuevoEstado, new OfertaRepository.DataCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    estadoPostulacionLiveData.postValue(nuevoEstado + " con éxito");
                } else {
                    estadoPostulacionLiveData.postValue("Error al actualizar el estado");
                }
            }

            @Override
            public void onFailure(Exception e) {
                estadoPostulacionLiveData.postValue("Error al actualizar el estado");
            }
        });
    }

    public void cargarOferta(int idOfertaEmpleo) {
        ofertaRepository.cargarOferta(idOfertaEmpleo, new OfertaRepository.DataCallback<OfertaEmpleo>() {
            @Override
            public void onSuccess(OfertaEmpleo result) {
                ofertaLiveData.setValue(result);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Error al cargar los detalles de la oferta: " + e.getMessage());
            }
        });
    }
    public void actualizarOferta(OfertaEmpleo ofertaActualizada) {
        ofertaRepository.actualizarOferta(ofertaActualizada, new OfertaRepository.DataCallback<Boolean>() {
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

    @Override
    protected void onCleared() {
        super.onCleared();
        ofertaRepository.shutdownExecutor();
    }
}
