package com.example.tpf_paii_android.actividades.cursos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.adapters.PreguntasAdapter;
import com.example.tpf_paii_android.utilidades.PDFGenerator;
import com.example.tpf_paii_android.viewmodels.CursoViewModel;

public class CursoDetalleActivity extends AppCompatActivity {

    private TextView txtNombreCurso, txtDescripcionCurso;
    private Button btnIniciarCapacitacion;
    private Button btnIniciarCurso;
    private Button btnDescargarCertificado;
    private static final int REQUEST_CODE_PREGUNTAS = 1;

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
        btnDescargarCertificado = findViewById(R.id.btnDescargarCertificado);
        imgCurso = findViewById(R.id.imgCurso);
        ProgressBar progressBarLoading = findViewById(R.id.progressBarLoading);
        progressBarLoading.setVisibility(View.VISIBLE);

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
        idUsuario = 1; // Para pruebas
        nombreUsuario = "prueba";

        // Asignar datos a las vistas
        txtNombreCurso.setText(nombreCurso);
        txtDescripcionCurso.setText(descripcionCurso);

        cursoViewModel.verificarInscripcionEstado(idCurso, idUsuario);
        cursoViewModel.getInscripcionEstado().observe(this, inscripcionEstado -> {
            progressBarLoading.setVisibility(View.GONE);
            if (inscripcionEstado != null) {
                // Aquí obtienes el idInscripcion y el estado
                int idInscripcion = inscripcionEstado.getIdInscripcion();
                String estadoInscripcion = inscripcionEstado.getEstadoInscripcion();

                switch (estadoInscripcion) {
                    case "activo":
                        // Si la inscripción está activa
                        btnIniciarCurso.setEnabled(true);
                        btnIniciarCurso.setText("Iniciar Curso");

                        // Configurar botón "Iniciar Curso" para abrir PreguntasActivity
                        btnIniciarCurso.setOnClickListener(v -> {
                            Intent intentPreguntas = new Intent(CursoDetalleActivity.this, PreguntasActivity.class);
                            intentPreguntas.putExtra("idInscripcion", idInscripcion); // Pasar el idInscripcion
                            intentPreguntas.putExtra("idCurso", idCurso);
                            startActivityForResult(intentPreguntas, REQUEST_CODE_PREGUNTAS);
                        });
                        break;

                    case "finalizado":
                        // descarga del certificado
                        btnDescargarCertificado.setEnabled(true);
                        btnDescargarCertificado.setOnClickListener(v -> {
                            //pdf
                            generarPDFCertificado(nombreCurso, nombreUsuario, "10/11/2024"); // Fecha de ejemplo
                            Toast.makeText(CursoDetalleActivity.this, "Descargando certificado...", Toast.LENGTH_SHORT).show();
                        });
                        break;

                    default:
                        // Si no existe inscripción activa o finalizada
                        btnIniciarCapacitacion.setEnabled(true);
                        btnIniciarCapacitacion.setOnClickListener(v -> {
                            progressBarLoading.setVisibility(View.VISIBLE);
                            cursoViewModel.registrarInscripcion(idCurso, idUsuario, "activo");
                        });
                        break;
                }
            } else {
                // Si no existe inscripción
                btnIniciarCapacitacion.setEnabled(true);

                btnIniciarCapacitacion.setOnClickListener(v -> {
                    progressBarLoading.setVisibility(View.VISIBLE);
                    cursoViewModel.registrarInscripcion(idCurso, idUsuario, "activo");
                });
            }
        });

        cursoViewModel.getInscripcionExitosa().observe(this, success -> {
            if (success != null && success) {
                Toast.makeText(CursoDetalleActivity.this, "Inscripción realizada con éxito", Toast.LENGTH_SHORT).show();
                btnIniciarCapacitacion.setEnabled(false);
                cursoViewModel.verificarInscripcionEstado(idCurso, idUsuario);
                progressBarLoading.setVisibility(View.GONE);
            } else {
                progressBarLoading.setVisibility(View.GONE);
                Toast.makeText(CursoDetalleActivity.this, "Error al inscribirse", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PREGUNTAS && resultCode == RESULT_OK) {
            boolean cuestionarioFinalizado = data.getBooleanExtra("cuestionarioFinalizado", false);

            if (cuestionarioFinalizado) {
                // Deshabilitar el botón de iniciar curso y habilitar el de certificado
                btnIniciarCurso.setEnabled(false);
                btnDescargarCertificado.setEnabled(true);
            }
        }
    }

    // Función para generar el certificado
    private void generarPDFCertificado(String nombreCurso, String nombreUsuario, String fechaFinalizacion) {
        PDFGenerator pdfGenerator = new PDFGenerator(this);
        pdfGenerator.crearCertificadoPDF(nombreCurso, nombreUsuario, fechaFinalizacion);
    }

}
