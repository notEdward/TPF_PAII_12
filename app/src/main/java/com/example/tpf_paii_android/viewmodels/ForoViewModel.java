package com.example.tpf_paii_android.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tpf_paii_android.modelos.Foro;
import com.example.tpf_paii_android.modelos.UsuarioInfo;
import com.example.tpf_paii_android.repositorios.ForoRepository;

import java.util.List;

public class ForoViewModel extends AndroidViewModel {

    private ForoRepository foroRepository;
    private MutableLiveData<List<Foro>> foros;
    private MutableLiveData<Boolean> isHiloCreated;
    private LiveData<UsuarioInfo> usuarioInfoLiveData;

    public ForoViewModel(Application application) {
        super(application);
        foroRepository = new ForoRepository();
        foros = new MutableLiveData<>();
        isHiloCreated = new MutableLiveData<>();
    }

    // Obtener los foros
    public LiveData<List<Foro>> getForos() {
        foroRepository.getAllForos(new ForoRepository.DataCallback<List<Foro>>() {
            @Override
            public void onSuccess(List<Foro> result) {
                foros.setValue(result);
            }

            @Override
            public void onFailure(Exception e) {
                // Manejar el error (si es necesario)
            }
        });
        return foros;
    }

    // Crear un nuevo hilo
    public LiveData<Boolean> createHilo(Foro foro) {
        foroRepository.createHilo(foro, new ForoRepository.DataCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                isHiloCreated.setValue(result);
            }

            @Override
            public void onFailure(Exception e) {
                isHiloCreated.setValue(false);
            }
        });
        return isHiloCreated;
    }

    public LiveData<UsuarioInfo> obtenerUsuarioInfo(int idUsuario, String tipoUsuario) {
        if (usuarioInfoLiveData == null) {
            usuarioInfoLiveData = foroRepository.obtenerUsuarioInfo(idUsuario, tipoUsuario);
        }
        return usuarioInfoLiveData;
    }
}
