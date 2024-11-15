package com.example.tpf_paii_android.viewmodels;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tpf_paii_android.modelos.UsuarioLogin;
import com.example.tpf_paii_android.repositorios.LoginRepository;


public class LoginViewModel extends AndroidViewModel {
    private final LoginRepository loginRepository;
    private final MutableLiveData<UsuarioLogin> usuarioLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LoginViewModel(Application application) {
        super(application);
        loginRepository = new LoginRepository();
    }

    public LiveData<UsuarioLogin> getUsuarioLiveData() {
        return usuarioLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void authenticate(String username, String password) {
        loginRepository.authenticateUser(username, password, new LoginRepository.DataCallback<UsuarioLogin>() {
            @Override
            public void onSuccess(UsuarioLogin result) {
                usuarioLiveData.setValue(result);
            }

            @Override
            public void onFailure(Exception e) {
                errorMessage.setValue(e.getMessage());
            }
        });
    }
}
