package com.example.tpf_paii_android.actividades.menu_header;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpf_paii_android.ModificarContrasena;
import com.example.tpf_paii_android.ModificarEmpresa;
import com.example.tpf_paii_android.ModificarEstudiante;
import com.example.tpf_paii_android.ModificarTutor;
import com.example.tpf_paii_android.MyApp;
import com.example.tpf_paii_android.R;

public class PerfilActivity extends AppCompatActivity {

    private TextView tvNombreUser;
    private Button btnCambiarContrasena, btnGestionarPerfil;

    private String nombreUsuario, tipoUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        MyApp app = (MyApp) getApplication();
        nombreUsuario = app.getNombreUsuario();
        tipoUser = app.getTipoUsuario();

        tvNombreUser = findViewById(R.id.tvNombreUserMOD);

        btnCambiarContrasena = findViewById(R.id.btnCambiarContrasena);
        btnGestionarPerfil = findViewById(R.id.btnGestionarPerfil);

        tvNombreUser.append(nombreUsuario);

        btnCambiarContrasena.setOnClickListener(view -> {
            startActivity(new Intent(PerfilActivity.this, ModificarContrasena.class));
        });

        btnGestionarPerfil.setOnClickListener(view -> {
            // Redirigir según el tipo de usuario
            switch (tipoUser) {
                case "Estudiante":
                    startActivity(new Intent(PerfilActivity.this, ModificarEstudiante.class));
                    break;
                case "Empresa":
                    startActivity(new Intent(PerfilActivity.this, ModificarEmpresa.class));
                    break;
                case "Tutor":
                    startActivity(new Intent(PerfilActivity.this, ModificarTutor.class));
                    break;
                default:
                    break;
            };
        });

    }

}
