package com.example.tpf_paii_android.actividades.menu_header;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpf_paii_android.MyApp;
import com.example.tpf_paii_android.R;

public class PerfilActivity extends AppCompatActivity {

    private EditText etNombreUsuario, etCorreoUsuario;
    private String nombreUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        MyApp app = (MyApp) getApplication();
        nombreUsuario = app.getNombreUsuario();

        etNombreUsuario = findViewById(R.id.et_nombre_usuario);
        etCorreoUsuario = findViewById(R.id.et_correo_usuario);

        cargarDatosUsuario();
    }

    private void cargarDatosUsuario() {

        String correo = "usuario@ejemplo.com";

        etNombreUsuario.setText(nombreUsuario);
        etCorreoUsuario.setText(correo);
    }
}
