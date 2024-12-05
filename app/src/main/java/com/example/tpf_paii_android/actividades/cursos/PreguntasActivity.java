package com.example.tpf_paii_android.actividades.cursos;

import android.content.Intent;
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
    private int idInscripcion,idCurso, puntosObtenidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);

        idInscripcion = getIntent().getIntExtra("idInscripcion", -1);
        idCurso = getIntent().getIntExtra("idCurso", -1);

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
        puntosObtenidos = 0;

        for (Pregunta pregunta : preguntas) {
            if (pregunta.getRespuestaSeleccionada() == null) {
                todasRespondidas = false;
                break;
            } else if (pregunta.getRespuestaSeleccionada().isEsCorrecta()) {
                puntosObtenidos += 1;
            }
        }
        if (!todasRespondidas) {
            Toast.makeText(this, "Debe responder todas las preguntas antes de enviar", Toast.LENGTH_SHORT).show();
            return;
        }
        cursoViewModel.obtenerRespuestasCorrectas(idCurso);
        cursoViewModel.getRespuestasCorrectasLiveData().observe(this, respuestasCorrectas -> {
            if (respuestasCorrectas == null) {
                Toast.makeText(this, "Error al validar respuestas requeridas", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar si las respuestas correctas obtenidas son suficientes
            if (puntosObtenidos >= respuestasCorrectas) {
                int cantidadDePreguntas = preguntas.size();
                double porcentaje = (cantidadDePreguntas > 0) ? (puntosObtenidos * 100.0 / cantidadDePreguntas) : 0.0;
                int nota = (int) Math.round(porcentaje / 10.0);

                // Si aprobó pero las preguntas son pocas y da menos de 6 entonces ponemos
                // la nota por defecto.
                if (nota < 6) {
                    nota = 6;
                }
                Date fechaFinalizacion = new Date(); // Fecha de finalización actual
                cursoViewModel.registrarEvaluacion(idInscripcion, nota, fechaFinalizacion);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("cuestionarioFinalizado", true);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "No alcanzó las respuestas correctas requeridas. Intente nuevamente.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

//    private void validarYRegistrarEvaluacion() {
//        List<Pregunta> preguntas = preguntasAdapter.getPreguntas();
//
//        boolean todasRespondidas = true;
//        int puntosObtenidos = 0;
//
//        for (Pregunta pregunta : preguntas) {
//            if (pregunta.getRespuestaSeleccionada() == null) {
//                todasRespondidas = false;
//                break;
//            } else if (pregunta.getRespuestaSeleccionada().isEsCorrecta()) {
//                puntosObtenidos += 1;
//            }
//        }
//        if (todasRespondidas) {
//
//            cursoViewModel.obtenerRespuestasCorrectas(idCurso);
//
//            // Calculo de nota obtenid
//            int cantidadDePreguntas = preguntas.size();
//            double porcentaje = (cantidadDePreguntas > 0) ? (puntosObtenidos * 100.0 / cantidadDePreguntas) : 0.0;
//            int nota = (int) Math.round(porcentaje / 10.0);
//            if (nota < 1) {
//                nota = 1;
//            } else if (nota > 10) {
//                nota = 10;
//            }
//            Date fechaFinalizacion = new Date(); // Fecha de finalización actual
//            cursoViewModel.registrarEvaluacion(idInscripcion, nota, fechaFinalizacion);
//
//            // Aviso que finalice con exito
//            Intent resultIntent = new Intent();
//            resultIntent.putExtra("cuestionarioFinalizado", true);
//            setResult(RESULT_OK, resultIntent);
//            finish();
//        } else {
//            Toast.makeText(this, "Debe responder todas las preguntas antes de enviar", Toast.LENGTH_SHORT).show();
//        }
//    }
}
