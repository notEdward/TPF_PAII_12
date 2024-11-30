package com.example.tpf_paii_android.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tpf_paii_android.repositorios.UsuarioRepository;

import java.util.concurrent.Executors;

public class UsuarioViewModel extends ViewModel {

    private final UsuarioRepository usuarioRepository;
    private final MutableLiveData<Boolean> verificarContrasenaLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> actualizarContrasenaLiveData = new MutableLiveData<>();

    public UsuarioViewModel() {
        usuarioRepository = new UsuarioRepository();
    }

    public LiveData<Boolean> getVerificarContrasenaLiveData() {
        return verificarContrasenaLiveData;
    }

    public LiveData<Boolean> getActualizarContrasenaLiveData() {
        return actualizarContrasenaLiveData;
    }

    public void verificarContrasenaActual(String nombreUsuario, String contrasenaActual) {
        Executors.newSingleThreadExecutor().execute(() -> {
            boolean resultado = usuarioRepository.verificarContrasenaActual(nombreUsuario, contrasenaActual);
            verificarContrasenaLiveData.postValue(resultado);
        });
    }

    public void actualizarContrasena(String nombreUsuario, String nuevaContrasena) {
        Executors.newSingleThreadExecutor().execute(() -> {
            boolean resultado = usuarioRepository.modificarContrasena(nombreUsuario, nuevaContrasena);
            actualizarContrasenaLiveData.postValue(resultado);
        });
    }







}
