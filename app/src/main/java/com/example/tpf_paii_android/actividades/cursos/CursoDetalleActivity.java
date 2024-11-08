package com.example.tpf_paii_android.actividades.cursos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.adapters.PreguntasAdapter;
import com.example.tpf_paii_android.viewmodels.CursoViewModel;

public class CursoDetalleActivity extends AppCompatActivity {

    private TextView txtNombreCurso, txtDescripcionCurso;
    private Button btnIniciarCapacitacion;
    private Button btnIniciarCurso;
    private ImageView imgCurso;

    private int idUsuario;
    private String nombreUsuario;
    private int idCurso;
    private CursoViewModel cursoViewModel;

    private RecyclerView recyclerViewPreguntas;
    private PreguntasAdapter preguntasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_curso);

        // Inicializar el ViewModel
        cursoViewModel = new ViewModelProvider(this).get(CursoViewModel.class);

        // Inicializar vistas
        txtNombreCurso = findViewById(R.id.txtNombreCurso);
        txtDescripcionCurso = findViewById(R.id.txtDescripcionCurso);
        btnIniciarCapacitacion = findViewById(R.id.btnIniciarCapacitacion);
        btnIniciarCurso = findViewById(R.id.btnIniciarCurso);
        imgCurso = findViewById(R.id.imgCurso);

        // Configurar RecyclerView para las preguntas
        recyclerViewPreguntas = findViewById(R.id.recyclerViewPreguntas);
        recyclerViewPreguntas.setLayoutManager(new LinearLayoutManager(this));
        preguntasAdapter = new PreguntasAdapter();
        recyclerViewPreguntas.setAdapter(preguntasAdapter);

        // Obtener los datos enviados desde la actividad anterior
        Intent intent = getIntent();
        String nombreCurso = intent.getStringExtra("nombreCurso");
        String descripcionCurso = intent.getStringExtra("descripcionCurso");
        idCurso = intent.getIntExtra("idCurso", 0);

        // Simulación de datos del usuario
        idUsuario = 1; // Para pruebas, obtener id_usuario de la sesión en producción
        nombreUsuario = "prueba";

        // Asignar datos a las vistas
        txtNombreCurso.setText(nombreCurso);
        txtDescripcionCurso.setText(descripcionCurso);

        // Verificar inscripción activa para habilitar/deshabilitar botones
        cursoViewModel.verificarInscripcionActiva(idCurso, idUsuario);
//        cursoViewModel.getInscripcionActiva().observe(this, isActive -> {
//            if (isActive != null && isActive) {
//                btnIniciarCurso.setEnabled(true);
//                btnIniciarCurso.setBackgroundColor(getResources().getColor(R.color.blue));
//                btnIniciarCurso.setText("Iniciar Curso");
//
//                btnIniciarCapacitacion.setEnabled(false);
//                btnIniciarCapacitacion.setBackgroundColor(getResources().getColor(R.color.grey));
//            } else {
//                btnIniciarCurso.setEnabled(false);
//            }
//        });
        cursoViewModel.getInscripcionActiva().observe(this, idInscripcion -> {
            if (idInscripcion != null) {
                // Ya existe inscripción activa, habilitar el botón y pasar el idInscripcion a la actividad de preguntas
                btnIniciarCurso.setEnabled(true);
                btnIniciarCurso.setBackgroundColor(getResources().getColor(R.color.blue));
                btnIniciarCurso.setText("Iniciar Curso");

                btnIniciarCapacitacion.setEnabled(false);
                btnIniciarCapacitacion.setBackgroundColor(getResources().getColor(R.color.grey));

                // Configurar botón "Iniciar Curso" para abrir PreguntasActivity
                btnIniciarCurso.setOnClickListener(v -> {
                    Intent intentPreguntas = new Intent(CursoDetalleActivity.this, PreguntasActivity.class);
                    intentPreguntas.putExtra("idInscripcion", idInscripcion); // Pasar el idInscripcion existente
                    intentPreguntas.putExtra("idCurso", idCurso);
                    startActivity(intentPreguntas);
                });
            } else {
                // Si no existe inscripción activa, solo habilitar el botón "Iniciar Capacitación"
                btnIniciarCurso.setEnabled(false);

                // Acción para registrar inscripción al presionar "Iniciar Capacitación"
                btnIniciarCapacitacion.setOnClickListener(v -> {
                    cursoViewModel.registrarInscripcion(idCurso, idUsuario, "activo");
                });
            }
        });

//        // Registro al curso
//        btnIniciarCapacitacion.setOnClickListener(v -> {
//            cursoViewModel.registrarInscripcion(idCurso, idUsuario, "activo");
//        });

        cursoViewModel.getInscripcionExitosa().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(CursoDetalleActivity.this, "Inscripción realizada con éxito", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CursoDetalleActivity.this, "Error al inscribirse", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
