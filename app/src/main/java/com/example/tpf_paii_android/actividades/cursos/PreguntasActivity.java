package com.example.tpf_paii_android.actividades.cursos;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.adapters.PreguntasAdapter;
import com.example.tpf_paii_android.modelos.Pregunta;
import com.example.tpf_paii_android.viewmodels.CursoViewModel;

import java.util.Date;
import java.util.List;

public class PreguntasActivity extends AppCompatActivity {

    private CursoViewModel cursoViewModel;
    private RecyclerView recyclerView;
    private PreguntasAdapter preguntasAdapter;
    private Button btnEnviar;
    private int idInscripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);

        idInscripcion = getIntent().getIntExtra("idInscripcion", -1);
        int idCurso = getIntent().getIntExtra("idCurso", -1);

        recyclerView = findViewById(R.id.recyclerViewPreguntas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        preguntasAdapter = new PreguntasAdapter();
        recyclerView.setAdapter(preguntasAdapter);

        cursoViewModel = new ViewModelProvider(this).get(CursoViewModel.class);

        // cambios en prg con opciones
        cursoViewModel.getPreguntasConOpciones().observe(this, preguntas -> {
            if (preguntas != null) {
                preguntasAdapter.setPreguntas(preguntas);
            } else {
                Toast.makeText(this, "Error al cargar preguntas", Toast.LENGTH_SHORT).show();
            }
        });

        cursoViewModel.obtenerPreguntasConOpciones(idCurso);

        // btn enviar
        btnEnviar = findViewById(R.id.btnEnviar);
        btnEnviar.setOnClickListener(v -> validarYRegistrarEvaluacion());

        // evaluacion
        cursoViewModel.getEvaluacionExitosa().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(this, "Evaluación registrada con éxito", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al registrar la evaluación", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validarYRegistrarEvaluacion() {
        List<Pregunta> preguntas = preguntasAdapter.getPreguntas();

        boolean todasRespondidas = true;
        int puntosObtenidos = 0;

        for (Pregunta pregunta : preguntas) {
            if (pregunta.getRespuestaSeleccionada() == null) {
                todasRespondidas = false;
                break;
            } else if (pregunta.getRespuestaSeleccionada().isEsCorrecta()) {
                puntosObtenidos += 2;
            }
        }
        if (todasRespondidas) {
            Date fechaFinalizacion = new Date(); // Fecha de finalización actual
            cursoViewModel.registrarEvaluacion(idInscripcion, puntosObtenidos, fechaFinalizacion);
        } else {
            Toast.makeText(this, "Debe responder todas las preguntas antes de enviar", Toast.LENGTH_SHORT).show();
        }
    }
}
