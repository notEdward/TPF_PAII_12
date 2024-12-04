package com.example.tpf_paii_android.utilidades;

import android.content.Context;
import android.content.Intent;

// como dato esta clase extiende de Thread.UncaughtExceptionHandler para
//capturar excepciones que pueden no estar manejadas
public class CustomExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Context context;

    public CustomExceptionHandler(Context context) {
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Intent intent = new Intent(context, ErrorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        //mato el proceso actual para iniciar de nuevo
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
