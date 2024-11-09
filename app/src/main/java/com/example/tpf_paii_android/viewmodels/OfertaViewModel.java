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

    public OfertaViewModel() {
        this.ofertaRepository = new OfertaRepository();
    }

    public LiveData<List<OfertaEmpleo>> getOfertasLiveData() {
        return ofertasLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void loadOfertas() {
        ofertaRepository.getAllOfertas(new OfertaRepository.DataCallback<ArrayList<OfertaEmpleo>>() {
            @Override
            public void onSuccess(ArrayList<OfertaEmpleo> result) {
                ofertasLiveData.setValue(result);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue("Error al cargar ofertas: " + e.getMessage());
            }
        });
    }
}
