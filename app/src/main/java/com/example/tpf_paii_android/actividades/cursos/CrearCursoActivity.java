package com.example.tpf_paii_android.actividades.cursos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

            int estadoCursoInt = estadoCurso.equals("Activo") ? 1 : 0;

            Curso curso = new Curso(nombreCurso, descripcionCurso, idCategoria, respuestasCorrectas, estadoCursoInt);

            List<Pregunta> preguntas = new ArrayList<>();
            List<Opcion> opciones = new ArrayList<>();

            for (int i = 0; i < contenedorPreguntas.getChildCount(); i++) {
                LinearLayout preguntaLayout = (LinearLayout) contenedorPreguntas.getChildAt(i);
                EditText etPregunta = (EditText) preguntaLayout.getChildAt(0);
                RadioGroup radioGroup = (RadioGroup) preguntaLayout.getChildAt(1);

                String textoPregunta = etPregunta.getText().toString();
                Pregunta pregunta = new Pregunta(curso.getIdCurso(), textoPregunta, "radio");
                preguntas.add(pregunta);

                for (int j = 0; j < radioGroup.getChildCount(); j++) {
                    LinearLayout opcionLayout = (LinearLayout) radioGroup.getChildAt(j);
                    EditText etOpcion = (EditText) opcionLayout.getChildAt(0);
                    RadioButton rbOpcion = (RadioButton) opcionLayout.getChildAt(1);

                    boolean esCorrecta = rbOpcion.isChecked();
                    opciones.add(new Opcion(pregunta.getIdPregunta(), etOpcion.getText().toString(), esCorrecta));
                }
            }

            cursoViewModel.guardarCurso(curso, preguntas, opciones);
        });


        cursoViewModel.getCursoGuardadoLiveData().observe(this, guardadoExitoso -> {
            if (guardadoExitoso) {
                Toast.makeText(this, "Curso guardado correctamente", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CrearCursoActivity.this, CursoActivity.class);
                startActivity(intent);
                finish();
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

        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(RadioGroup.VERTICAL);

        LinearLayout opcion1Layout = new LinearLayout(this);
        opcion1Layout.setOrientation(LinearLayout.HORIZONTAL);
        EditText etRespuesta1 = new EditText(this);
        etRespuesta1.setHint("Respuesta 1");
        opcion1Layout.addView(etRespuesta1);
        RadioButton rbOpcion1 = new RadioButton(this);
        opcion1Layout.addView(rbOpcion1);
        radioGroup.addView(opcion1Layout);

        LinearLayout opcion2Layout = new LinearLayout(this);
        opcion2Layout.setOrientation(LinearLayout.HORIZONTAL);
        EditText etRespuesta2 = new EditText(this);
        etRespuesta2.setHint("Respuesta 2");
        opcion2Layout.addView(etRespuesta2);
        RadioButton rbOpcion2 = new RadioButton(this);
        opcion2Layout.addView(rbOpcion2);
        radioGroup.addView(opcion2Layout);

        preguntaLayout.addView(radioGroup);
        contenedorPreguntas.addView(preguntaLayout);
    }

}

