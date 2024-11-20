package com.example.tpf_paii_android.repositorios;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TutoriasRepository {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    // private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface DataCallback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }

}
