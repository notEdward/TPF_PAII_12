package com.example.tpf_paii_android.actividades.cursos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.modelos.CategoriaCurso;
import com.example.tpf_paii_android.modelos.Curso;
import com.example.tpf_paii_android.modelos.Opcion;
import com.example.tpf_paii_android.modelos.Pregunta;
import com.example.tpf_paii_android.viewmodels.CursoViewModel;

import java.util.ArrayList;
import java.util.List;

public class CrearCursoActivity extends AppCompatActivity {

    private EditText etNombreCurso, etDescripcion, etRespuestasCorrectas;
    private Spinner spinnerCategoria, spinnerEstado;
    private LinearLayout contenedorPreguntas;
    private Button btnGuardarCurso, btnAñadirPregunta;
    private CursoViewModel cursoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_curso);

        etNombreCurso = findViewById(R.id.etNombreCurso);
        etDescripcion = findViewById(R.id.etDescripcionCurso);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        etRespuestasCorrectas = findViewById(R.id.etRespuestasCorrectas);
        contenedorPreguntas = findViewById(R.id.contenedorPreguntas);
        btnGuardarCurso = findViewById(R.id.btnGuardarCurso);
        btnAñadirPregunta = findViewById(R.id.btnAñadirPregunta);
        spinnerEstado = findViewById(R.id.spinnerEstado);

        cursoViewModel = new ViewModelProvider(this).get(CursoViewModel.class);

        // Spinner de cursos
        String[] estados = {"Activo", "Inactivo"};
        ArrayAdapter<String> adapterEstado = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, estados);
        adapterEstado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapterEstado);

        // spinner cat (cambios)
        cursoViewModel.getCategoriasLiveData().observe(this, categorias -> {
            ArrayAdapter<CategoriaCurso> categoriaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
            categoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategoria.setAdapter(categoriaAdapter);
        });


        // cargo cats
        cursoViewModel.obtenerCategorias();

        btnAñadirPregunta.setOnClickListener(v -> añadirPregunta());

        //crear curso
        btnGuardarCurso.setOnClickListener(v -> {
            String nombreCurso = etNombreCurso.getText().toString();
            String descripcionCurso = etDescripcion.getText().toString();
            int idCategoria = ((CategoriaCurso) spinnerCategoria.getSelectedItem()).getIdCategoria();
            String estadoCurso = spinnerEstado.getSelectedItem().toString();
            String respuestasCorrectas = etRespuestasCorrectas.getText().toString();

            // Convertir el estado de String a Integer
            int estadoCursoInt = estadoCurso.equals("Activo") ? 1 : 0;

            // Crear el curso
            Curso curso = new Curso(nombreCurso, descripcionCurso, idCategoria, respuestasCorrectas, estadoCursoInt);

            // Recoger las preguntas y respuestas
            List<Pregunta> preguntas = new ArrayList<>();
            List<Opcion> opciones = new ArrayList<>();

            for (int i = 0; i < contenedorPreguntas.getChildCount(); i++) {
                LinearLayout preguntaLayout = (LinearLayout) contenedorPreguntas.getChildAt(i);
                EditText etPregunta = (EditText) preguntaLayout.getChildAt(0);
                String textoPregunta = etPregunta.getText().toString();

                // Guardar la pregunta
                Pregunta pregunta = new Pregunta(curso.getIdCurso(), textoPregunta, "radio");
                preguntas.add(pregunta);

                // Guardar las respuestas (suponiendo que se guardan 2 opciones por pregunta)
                EditText etRespuesta1 = (EditText) preguntaLayout.getChildAt(1);
                EditText etRespuesta2 = (EditText) preguntaLayout.getChildAt(2);

                opciones.add(new Opcion(pregunta.getIdPregunta(), etRespuesta1.getText().toString(), true));
                opciones.add(new Opcion(pregunta.getIdPregunta(), etRespuesta2.getText().toString(), false));
            }

            // crear curso
            cursoViewModel.guardarCurso(curso, preguntas, opciones);
        });

        cursoViewModel.getCursoGuardadoLiveData().observe(this, guardadoExitoso -> {
            if (guardadoExitoso) {
                Toast.makeText(this, "Curso guardado correctamente", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CrearCursoActivity.this, CursoActivity.class);
                startActivity(intent);
                finish(); // Opcional para cerrar la actividad actual
            } else {
                Toast.makeText(this, "Error al guardar el curso", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void añadirPregunta() {
        LinearLayout preguntaLayout = new LinearLayout(this);
        preguntaLayout.setOrientation(LinearLayout.VERTICAL);
        preguntaLayout.setPadding(8, 8, 8, 8);

        EditText etPregunta = new EditText(this);
        etPregunta.setHint("Pregunta");
        preguntaLayout.addView(etPregunta);

        EditText etRespuesta1 = new EditText(this);
        etRespuesta1.setHint("Respuesta 1");
        preguntaLayout.addView(etRespuesta1);

        EditText etRespuesta2 = new EditText(this);
        etRespuesta2.setHint("Respuesta 2");
        preguntaLayout.addView(etRespuesta2);

        contenedorPreguntas.addView(preguntaLayout);
    }
}

