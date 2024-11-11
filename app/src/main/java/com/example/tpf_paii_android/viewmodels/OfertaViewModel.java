package com.example.tpf_paii_android.viewmodels;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tpf_paii_android.modelos.OfertaEmpleo;
import com.example.tpf_paii_android.repositorios.OfertaRepository;

import java.util.ArrayList;
import java.util.List;

public class OfertaViewModel extends ViewModel {
    private final OfertaRepository ofertaRepository;
    private final MutableLiveData<List<OfertaEmpleo>> ofertasLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<List<OfertaEmpleo>> ofertasFiltradasLiveData = new MutableLiveData<>();

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
}
