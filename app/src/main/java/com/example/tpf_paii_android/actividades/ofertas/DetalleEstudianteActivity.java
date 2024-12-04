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

public class DetalleEstudianteActivity extends AppCompatActivity {
    private TextView textNombreApellido, textDNI, textEmail, textTelefono, textDireccion, textNivelEducativo;
    private Button btnAceptar, btnRechazar;

    private int idPostulacion;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_estudiante);

        textNombreApellido = findViewById(R.id.textNombreApellido);
        textDNI = findViewById(R.id.textDNI);
        textEmail = findViewById(R.id.textEmail);
        textTelefono = findViewById(R.id.textTelefono);
        textDireccion = findViewById(R.id.textDireccion);
        textNivelEducativo = findViewById(R.id.textNivelEducativo);
        btnAceptar = findViewById(R.id.btnAceptar);
        btnRechazar = findViewById(R.id.btnRechazar);
        MyApp app = (MyApp) getApplicationContext();


        //recibimos del adapter de la actividad anterior para ver el detalle del estudiante
        idPostulacion = getIntent().getIntExtra("idPostulacion", -1);
        idUsuario = getIntent().getIntExtra("idUsuario", -1);

        OfertaViewModel viewModel = new ViewModelProvider(this).get(OfertaViewModel.class);
        viewModel.obtenerDetalleEstudiante(idUsuario);

        viewModel.getEstudianteLiveData().observe(this, estudiante -> {
            if (estudiante != null) {
                textNombreApellido.setText(estudiante.getNombre() + " " + estudiante.getApellido());
                textDNI.setText("DNI: " + estudiante.getDni());
                textEmail.setText("Email: " + estudiante.getEmail());
                textTelefono.setText("Teléfono: " + estudiante.getTelefono());
                textDireccion.setText("Dirección: " + estudiante.getDireccion());
                // textNivelEducativo.setText("Nivel Educativo: " + estudiante.getNivelEducativoDescripcion());
            }
        });

//        btnAceptar.setOnClickListener(v -> actualizarEstadoPostulacion(viewModel, "Aceptado"));
//        btnRechazar.setOnClickListener(v -> actualizarEstadoPostulacion(viewModel, "Rechazado"));
    }

//    private void actualizarEstadoPostulacion(OfertaViewModel viewModel, String estado) {
//        viewModel.actualizarEstadoPostulacion(idPostulacion, estado, success -> {
//            if (success) {
//                Toast.makeText(this, "Estado actualizado a " + estado, Toast.LENGTH_SHORT).show();
//                finish();
//            } else {
//                Toast.makeText(this, "Error al actualizar estado", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}


