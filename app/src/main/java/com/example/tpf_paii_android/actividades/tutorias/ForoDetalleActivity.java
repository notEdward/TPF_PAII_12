package com.example.tpf_paii_android.actividades.tutorias;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.modelos.ForoMensaje;
import com.example.tpf_paii_android.viewmodels.ForoViewModel;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ForoDetalleActivity extends AppCompatActivity {

    private TextView textViewTituloForo, textViewNombreUsuario, textViewMensajeOriginal, textViewNumeroReplicas;
    private LinearLayout linearLayoutRespuestas;
    private Button btnResponder;
    private ForoViewModel foroDetalleViewModel;
    private int idHilo;
    private String nombreUsuario;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foro_detalle);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        textViewTituloForo = findViewById(R.id.textViewTituloForo);
        textViewNombreUsuario = findViewById(R.id.textViewNombreUsuario);
        textViewMensajeOriginal = findViewById(R.id.textViewMensajeOriginal);
        linearLayoutRespuestas = findViewById(R.id.linearLayoutRespuestas);
        btnResponder = findViewById(R.id.btnResponder);

        foroDetalleViewModel = new ViewModelProvider(this).get(ForoViewModel.class);
        swipeRefreshLayout.setOnRefreshListener(() -> foroDetalleViewModel.getRespuestas(idHilo));

        idHilo = getIntent().getIntExtra("idHilo", -1);
        String tituloForo = getIntent().getStringExtra("tituloForo");
        String mensajeForo = getIntent().getStringExtra("mensajeForo");
        nombreUsuario = getIntent().getStringExtra("nombreUsuario");

        textViewTituloForo.setText(tituloForo);
        textViewMensajeOriginal.setText(mensajeForo);
        textViewNombreUsuario.setText("Creado por: " + nombreUsuario);

        foroDetalleViewModel.getRespuestas(idHilo).observe(this, respuestas -> {
            // Mostrar las respuestas en el LinearLayout
            linearLayoutRespuestas.removeAllViews();
            for (ForoMensaje respuesta : respuestas) {
                TextView textViewRespuesta = new TextView(ForoDetalleActivity.this);
                textViewRespuesta.setText(respuesta.getMensaje() + " - " + respuesta.getNombreUsuario());
                linearLayoutRespuestas.addView(textViewRespuesta);
            }
            swipeRefreshLayout.setRefreshing(false);
        });

        foroDetalleViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(ForoDetalleActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

        foroDetalleViewModel.getRespuestas(idHilo);

        btnResponder.setOnClickListener(v -> mostrarPantallaRespuesta());
    }

    private void mostrarPantallaRespuesta() {
        Intent intent = new Intent(ForoDetalleActivity.this, ResponderActivity.class);
        intent.putExtra("idHilo", idHilo);
        startActivity(intent);
    }
}

