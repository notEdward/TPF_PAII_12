package com.example.tpf_paii_android.actividades.tutorias;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.lifecycle.ViewModelProvider;

import com.example.tpf_paii_android.MyApp;
import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.actividades.menu_header.MenuHamburguesaActivity;
import com.example.tpf_paii_android.viewmodels.TutoriasViewModel;

public class SolicitarTutoriaActivity extends MenuHamburguesaActivity {

    private TutoriasViewModel tutoriasViewModel;

    private EditText etTema, etComentarios;
    private Button btnSolicitar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_solicitar_tutoria);

        etTema = findViewById(R.id.etTema);
        etComentarios = findViewById(R.id.etComentarios);
        btnSolicitar = findViewById(R.id.btnSolicitarTutoria);

        tutoriasViewModel = new ViewModelProvider(this).get(TutoriasViewModel.class);

        // MyApp para obtener datos de usuario
        MyApp app = (MyApp)getApplication();
        int idUsuario = app.getIdUsuario();

        tutoriasViewModel.setUsuarioLogueado(idUsuario);

        // Obtiene ID de curso desde el Intent
        int idCurso = getIntent().getIntExtra("idCurso", -1);
        if (idCurso == -1) {
            Toast.makeText(this, "ID de curso no válido.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Observadores de exito y error
        tutoriasViewModel.getSuccessLiveData().observe(this, successMessage -> {
            Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show();
            finish();
        });

        tutoriasViewModel.getErrorLiveData().observe(this, errorMessage -> {
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        });

        btnSolicitar.setOnClickListener(v -> {
            String tema = etTema.getText().toString().trim();
            String comentarios = etComentarios.getText().toString().trim();

            if (tema.isEmpty() || comentarios.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            tutoriasViewModel.solicitarTutoria(idCurso, tema, comentarios, new java.sql.Date(System.currentTimeMillis()));

        });
    }

}