package com.example.tpf_paii_android.actividades.ofertas;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tpf_paii_android.MyApp;
import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.adapters.PostulacionesAdapter;
import com.example.tpf_paii_android.viewmodels.OfertaViewModel;

public class PostulacionesActivity extends AppCompatActivity {
    private OfertaViewModel viewModel;
    private RecyclerView recyclerView;
    private TextView textTituloPostulaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postulaciones);

        textTituloPostulaciones = findViewById(R.id.textTituloPostulaciones);
        recyclerView = findViewById(R.id.recyclerPostulaciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyApp app = (MyApp) getApplicationContext();
        String tipoUsuario = app.getTipoUsuario();
        int idUsuario = app.getIdUsuario();
        viewModel = new ViewModelProvider(this).get(OfertaViewModel.class);

        if (tipoUsuario.equalsIgnoreCase("Estudiante")) {
            textTituloPostulaciones.setText("Mis Postulaciones");
            viewModel.cargarPostulacionesEstudiante(idUsuario);
        } else if (tipoUsuario.equalsIgnoreCase("Empresa")) {
            textTituloPostulaciones.setText("Postulaciones Recibidas");
            viewModel.cargarPostulacionesEmpresa(idUsuario);
        }
        viewModel.getPostulaciones().observe(this, postulaciones -> {
            if (postulaciones == null || postulaciones.isEmpty()) {
                Toast.makeText(PostulacionesActivity.this, "No hay postulaciones", Toast.LENGTH_SHORT).show();
            } else {
                PostulacionesAdapter adapter = new PostulacionesAdapter(postulaciones, tipoUsuario, PostulacionesActivity.this);
                recyclerView.setAdapter(adapter);
            }
        });
    }
}

