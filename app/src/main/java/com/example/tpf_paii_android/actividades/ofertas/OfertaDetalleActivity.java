package com.example.tpf_paii_android.actividades.ofertas;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.viewmodels.OfertaViewModel;

public class OfertaDetalleActivity extends AppCompatActivity {

    private int idOfertaEmpleo; // ID de la oferta a consultar
    private TextView tvTitulo, tvDescripcion, tvDireccion, tvCurso, tvLocalidad, tvModalidad, tvTipoEmpleo;
    private ImageView ivImagenOferta;
    private OfertaViewModel ofertaViewModel;
    private int idUsuario;
    private String nombreUsuario;
    private Button btnPostularse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_oferta);

        //simulo recibir el usuario
        idUsuario = 1; // Para pruebas
        nombreUsuario = "prueba";

        idOfertaEmpleo = getIntent().getIntExtra("id_oferta_empleo", -1);
        //vinculo
        tvTitulo = findViewById(R.id.tvTitulo);
        ivImagenOferta = findViewById(R.id.ivImagenOferta);
        tvDescripcion = findViewById(R.id.tvDescripcion);
        tvDireccion = findViewById(R.id.tvDireccion);
        tvCurso = findViewById(R.id.tvCurso);
        tvLocalidad = findViewById(R.id.tvLocalidad);
        tvModalidad = findViewById(R.id.tvModalidad);
        tvTipoEmpleo = findViewById(R.id.tvTipoEmpleo);

        // Inicializa el ViewModel
        ofertaViewModel = new ViewModelProvider(this).get(OfertaViewModel.class);

        // Observar los cambios en los detalles de la oferta
        ofertaViewModel.getOfertaDetalleLiveData().observe(this, ofertaDetalle -> {
            if (ofertaDetalle != null) {
                // Actualiza la UI con los datos de la oferta
                tvTitulo.setText(ofertaDetalle.getTitulo());
                tvDescripcion.setText(ofertaDetalle.getDescripcion());
                tvDireccion.setText("Dirección: " + ofertaDetalle.getDireccion());
                tvCurso.setText("Curso requerido: " + ofertaDetalle.getNombreCurso());
                tvLocalidad.setText("Localidad: " + ofertaDetalle.getLocalidad());
                tvModalidad.setText("Modalidad: " + ofertaDetalle.getModalidad());
                tvTipoEmpleo.setText("Tipo de empleo: " + ofertaDetalle.getTipoEmpleo());

                // Puedes cargar la imagen de la oferta, por ejemplo, usando una librería como Glide
//                Glide.with(this)
//                        .load(ofertaDetalle.getImagenOfertaUrl()) // Suponiendo que tienes la URL de la imagen
//                        .into(ivImagenOferta);
            }
        });

        //postulaciones
        // Observa el mensaje de postulación
        ofertaViewModel.getPostularMensaje().observe(this, mensaje -> {
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        });

        // Configura el botón de postulación
        btnPostularse = findViewById(R.id.btnPostularse);
        btnPostularse.setOnClickListener(v -> {
            ofertaViewModel.postularUsuario(idOfertaEmpleo, idUsuario);
        });
        //


        // Observar mensajes de error
        ofertaViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        // Cargar los detalles de la oferta
        ofertaViewModel.obtenerDetallesOferta(idOfertaEmpleo);
    }
}

