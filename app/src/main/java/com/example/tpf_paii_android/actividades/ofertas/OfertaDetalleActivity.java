package com.example.tpf_paii_android.actividades.ofertas;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.example.tpf_paii_android.MyApp;
import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.viewmodels.OfertaViewModel;

public class OfertaDetalleActivity extends AppCompatActivity {

    private int idOfertaEmpleo, imageResId; // ID de la oferta a consultar
    private TextView tvNombreEmpresa, tvTitulo, tvDescripcion, tvDireccion, tvCurso, tvLocalidad, tvModalidad, tvTipoEmpleo, tvNivelEducativo, tvOtrosRequisitos;
    private ImageView ivImagenOferta;
    private OfertaViewModel ofertaViewModel;
    private int idUsuario;
    private String nombreUsuario;
    private Button btnPostularse;
    private String tipoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_oferta);

        MyApp app = (MyApp) getApplication();
        idUsuario = app.getIdUsuario();
        tipoUsuario = app.getTipoUsuario();
        nombreUsuario = app.getNombreUsuario();

        idOfertaEmpleo = getIntent().getIntExtra("id_oferta_empleo", -1);
        imageResId = getIntent().getIntExtra("imageResId", R.drawable.img1_tpf);

        //vinculo
        tvNombreEmpresa = findViewById(R.id.tvNombreEmpresa);
        tvTitulo = findViewById(R.id.tvTitulo);
        ivImagenOferta = findViewById(R.id.ivImagenOferta);
        tvDescripcion = findViewById(R.id.tvDescripcion);
        tvDireccion = findViewById(R.id.tvDireccion);
        tvCurso = findViewById(R.id.tvCurso);
        tvLocalidad = findViewById(R.id.tvLocalidad);
        tvModalidad = findViewById(R.id.tvModalidad);
        tvTipoEmpleo = findViewById(R.id.tvTipoEmpleo);
        tvNivelEducativo = findViewById(R.id.tvNivelEducativo);
        tvOtrosRequisitos = findViewById(R.id.tvOtrosRequisitos);

        ofertaViewModel = new ViewModelProvider(this).get(OfertaViewModel.class);

        ofertaViewModel.getOfertaDetalleLiveData().observe(this, ofertaDetalle -> {
            if (ofertaDetalle != null) {
                tvNombreEmpresa.setText("Empresa: " + ofertaDetalle.getNombreEmpresa());
                tvTitulo.setText(ofertaDetalle.getTitulo());
                tvDescripcion.setText(ofertaDetalle.getDescripcion());
                tvDireccion.setText("Dirección: " + ofertaDetalle.getDireccion());
                tvCurso.setText("Curso requerido: " + ofertaDetalle.getNombreCurso());
                tvLocalidad.setText("Localidad: " + ofertaDetalle.getLocalidad());
                tvModalidad.setText("Modalidad: " + ofertaDetalle.getModalidad());
                tvTipoEmpleo.setText("Tipo de empleo: " + ofertaDetalle.getTipoEmpleo());
                tvNivelEducativo.setText("Nivel educativo deseable: " + ofertaDetalle.getNivelEducativo());
                tvOtrosRequisitos.setText("Otros requisitos: " + ofertaDetalle.getOtrosRequisitos());
                ivImagenOferta.setImageResource(imageResId);
            }
        });

        //postulaciones
        ofertaViewModel.getPostularMensaje().observe(this, mensaje -> {
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        });

        btnPostularse = findViewById(R.id.btnPostularse);

        if (!"Estudiante".equalsIgnoreCase(tipoUsuario)) {
            btnPostularse.setVisibility(View.GONE);
        } else {
            btnPostularse.setOnClickListener(v -> {
                ofertaViewModel.postularUsuario(idOfertaEmpleo, idUsuario);
            });
        }

        ofertaViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        ofertaViewModel.obtenerDetallesOferta(idOfertaEmpleo);
    }
}

