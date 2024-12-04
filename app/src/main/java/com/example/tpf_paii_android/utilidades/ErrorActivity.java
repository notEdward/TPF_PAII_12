package com.example.tpf_paii_android.utilidades;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.actividades.autenticacion.Login;

public class ErrorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        Button btnReiniciar = findViewById(R.id.btnReiniciar);
        btnReiniciar.setOnClickListener(v -> {
            Intent intent = new Intent(ErrorActivity.this, Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
