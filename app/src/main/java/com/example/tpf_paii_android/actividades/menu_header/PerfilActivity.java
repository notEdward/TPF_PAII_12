package com.example.tpf_paii_android.actividades.menu_header;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpf_paii_android.ModificarContrasena;
import com.example.tpf_paii_android.MyApp;
import com.example.tpf_paii_android.R;

public class PerfilActivity extends AppCompatActivity {

    private EditText etNombreUsuario;
    private Button btnCambiarContrasena, btnGestionarPerfil;

    private String nombreUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        MyApp app = (MyApp) getApplication();
        nombreUsuario = app.getNombreUsuario();

        etNombreUsuario = findViewById(R.id.et_nombre_usuario);

        btnCambiarContrasena = findViewById(R.id.btnCambiarContrasena);
        btnGestionarPerfil = findViewById(R.id.btnGestionarPerfil);

        etNombreUsuario.setText(nombreUsuario);

        btnCambiarContrasena.setOnClickListener(view -> {
            startActivity(new Intent(PerfilActivity.this, ModificarContrasena.class));
        });

    }

}
