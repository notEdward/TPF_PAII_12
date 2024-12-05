package com.example.tpf_paii_android.actividades.ofertas;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.tpf_paii_android.MyApp;
import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.viewmodels.OfertaViewModel;
import android.view.View;

public class DetalleEstudianteActivity extends AppCompatActivity {
    private TextView textNombreApellido, textDNI, textEmail, textTelefono, textDireccion, textProvinciaLocalidad, textNivelEducativo, textEstadoNivel;
    private Button btnAceptar, btnRechazar;

    private int idPostulacion;
    private int idUsuario;
    private String estadoPostulacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_estudiante);

        textNombreApellido = findViewById(R.id.textNombreApellido);
        textDNI = findViewById(R.id.textDNI);
        textEmail = findViewById(R.id.textEmail);
        textTelefono = findViewById(R.id.textTelefono);
        textDireccion = findViewById(R.id.textDireccion);
        textProvinciaLocalidad = findViewById(R.id.textProvinciaLocalidad);
        textNivelEducativo = findViewById(R.id.textNivelEducativo);
        textEstadoNivel = findViewById(R.id.textEstadoNivel);

        btnAceptar = findViewById(R.id.btnAceptar);
        btnRechazar = findViewById(R.id.btnRechazar);
        MyApp app = (MyApp) getApplicationContext();


        //recibimos del adapter de la actividad anterior para ver el detalle del estudiante
        idPostulacion = getIntent().getIntExtra("idPostulacion", -1);
        idUsuario = getIntent().getIntExtra("idUsuario", -1);
        estadoPostulacion = getIntent().getStringExtra("estadoPostulacion"); // Recuperar estado

        if ("Aceptado".equalsIgnoreCase(estadoPostulacion) || "Rechazado".equalsIgnoreCase(estadoPostulacion)) {
            btnAceptar.setVisibility(View.GONE);
            btnRechazar.setVisibility(View.GONE);
        }

        OfertaViewModel viewModel = new ViewModelProvider(this).get(OfertaViewModel.class);
        viewModel.obtenerDetalleEstudiante(idUsuario);

        viewModel.getEstudianteLiveData().observe(this, estudiante -> {
            if (estudiante != null) {
                textNombreApellido.setText(estudiante.getNombre() + " " + estudiante.getApellido());
                textDNI.setText("DNI: " + estudiante.getDni());
                textEmail.setText("Email: " + estudiante.getEmail());
                textTelefono.setText("Teléfono: " + estudiante.getTelefono());
                textDireccion.setText("Dirección: " + estudiante.getDireccion());
                textNivelEducativo.setText("Nivel Educativo: " + estudiante.getNivelEducativo().getDescripcion());
                textEstadoNivel.setText("Estado: " + estudiante.getEstadoNivelEducativo().getDescripcion());
                textProvinciaLocalidad.setText("Provincia y localidad: " + estudiante.getLocalidad().getId_provincia().getNombre() + " , " + estudiante.getLocalidad().getNombre());

            }
        });

        if (viewModel.getEstadoPostulacionLiveData() != null) {
            viewModel.getEstadoPostulacionLiveData().observe(this, mensaje -> {
                if (mensaje != null) {
                    Toast.makeText(DetalleEstudianteActivity.this, mensaje, Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }

        btnAceptar.setOnClickListener(v -> {
            viewModel.actualizarEstadoPostulacion(idPostulacion, "Aceptado");
        });
        btnRechazar.setOnClickListener(v -> {
            viewModel.actualizarEstadoPostulacion(idPostulacion, "Rechazado");
        });

  }

}


