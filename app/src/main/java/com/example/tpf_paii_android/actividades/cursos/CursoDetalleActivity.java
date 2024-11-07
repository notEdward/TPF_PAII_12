package com.example.tpf_paii_android.actividades.cursos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tpf_paii_android.R;

public class CursoDetalleActivity extends AppCompatActivity {

    private TextView txtNombreCurso, txtDescripcionCurso;
    private Button btnIniciarCapacitacion;
    private ImageView imgCurso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_curso);

        // Inicializar los componentes
        txtNombreCurso = findViewById(R.id.txtNombreCurso);
        txtDescripcionCurso = findViewById(R.id.txtDescripcionCurso);
        btnIniciarCapacitacion = findViewById(R.id.btnIniciarCapacitacion);
        imgCurso = findViewById(R.id.imgCurso);

        // Obtener los datos enviados desde la actividad anterior
        Intent intent = getIntent();
        String nombreCurso = intent.getStringExtra("nombreCurso");
        String descripcionCurso = intent.getStringExtra("descripcionCurso");

        // Asignar los valores a las vistas
        txtNombreCurso.setText(nombreCurso);
        txtDescripcionCurso.setText(descripcionCurso);

        // Aquí puedes cargar la imagen del curso si la tienes

        // Acción para iniciar la capacitación
        btnIniciarCapacitacion.setOnClickListener(v -> {
            // Aquí iría el código para iniciar la capacitación
            // Ejemplo: iniciar un nuevo fragmento o actividad para el curso.
        });
    }
}
