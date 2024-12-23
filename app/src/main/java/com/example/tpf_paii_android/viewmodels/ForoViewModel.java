package com.example.tpf_paii_android.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tpf_paii_android.modelos.Foro;
import com.example.tpf_paii_android.modelos.ForoMensaje;
import com.example.tpf_paii_android.modelos.UsuarioInfo;
import com.example.tpf_paii_android.repositorios.ForoRepository;

import java.util.List;

public class ForoViewModel extends AndroidViewModel {

    private ForoRepository foroRepository;
    private MutableLiveData<List<Foro>> foros;
    private MutableLiveData<Boolean> isHiloCreated;
    private LiveData<UsuarioInfo> usuarioInfoLiveData;
    private MutableLiveData<Boolean> respuestaEnviada;
    private MutableLiveData<List<ForoMensaje>> respuestasLiveData = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public ForoViewModel(Application application) {
        super(application);
        foroRepository = new ForoRepository();
        foros = new MutableLiveData<>();
        isHiloCreated = new MutableLiveData<>();
        respuestaEnviada = new MutableLiveData<>();
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

    //rta
    public LiveData<Boolean> enviarRespuesta(int idHilo, String nombreUsuario, String mensaje) {
        foroRepository.agregarRespuesta(idHilo, nombreUsuario, mensaje, new ForoRepository.DataCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                respuestaEnviada.setValue(true);
            }

            @Override
            public void onFailure(Exception e) {
                respuestaEnviada.setValue(false);
            }
        });
        return respuestaEnviada;
    }

    //detalles
    public LiveData<List<ForoMensaje>> getRespuestas(int idHilo) {
        foroRepository.getRespuestas(idHilo, new ForoRepository.DataCallback<List<ForoMensaje>>() {
            @Override
            public void onSuccess(List<ForoMensaje> respuestas) {
                respuestasLiveData.postValue(respuestas);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.postValue("Error al cargar respuestas");
            }
        });
        return respuestasLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        foroRepository.shutdownExecutor();
    }
}
