package com.example.tpf_paii_android.actividades.tutorias;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.modelos.ForoMensaje;
import com.example.tpf_paii_android.repositorios.ForoRepository;

import java.util.List;

public class ForoDetalleActivity extends AppCompatActivity {

    private TextView textViewTituloForo, textViewNombreUsuario, textViewMensajeOriginal, textViewNumeroReplicas;
    private LinearLayout linearLayoutRespuestas;
    private Button btnResponder;
    private int idHilo;
    private String nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foro_detalle);

        textViewTituloForo = findViewById(R.id.textViewTituloForo);
        textViewNombreUsuario = findViewById(R.id.textViewNombreUsuario);
        textViewMensajeOriginal = findViewById(R.id.textViewMensajeOriginal);
        linearLayoutRespuestas = findViewById(R.id.linearLayoutRespuestas);
        btnResponder = findViewById(R.id.btnResponder);

        // intent
        idHilo = getIntent().getIntExtra("idHilo", -1);
        String tituloForo = getIntent().getStringExtra("tituloForo");
        String mensajeForo = getIntent().getStringExtra("mensajeForo");
        nombreUsuario = getIntent().getStringExtra("nombreUsuario");

        // Mostrar datos del foro
        textViewTituloForo.setText(tituloForo);
        textViewMensajeOriginal.setText(mensajeForo);
        textViewNombreUsuario.setText("Creado por: " + nombreUsuario);

        cargarRespuestas();

        btnResponder.setOnClickListener(v -> mostrarPantallaRespuesta());
    }

    private void cargarRespuestas() {
        ForoRepository foroRepository = new ForoRepository();
        foroRepository.getRespuestas(idHilo, new ForoRepository.DataCallback<List<ForoMensaje>>() {
            @Override
            public void onSuccess(List<ForoMensaje> respuestas) {
                // Mostrar las respuestas en el LinearLayout
                for (ForoMensaje respuesta : respuestas) {
                    TextView textViewRespuesta = new TextView(ForoDetalleActivity.this);
                    textViewRespuesta.setText(respuesta.getMensaje() + " - " + respuesta.getNombreUsuario());
                    linearLayoutRespuestas.addView(textViewRespuesta);
                }
            }

            @Override
            public void onFailure(Exception e) {
                // Manejar error
                Toast.makeText(ForoDetalleActivity.this, "Error al cargar respuestas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarPantallaRespuesta() {
        // Abrir una pantalla para responder al hilo
        Intent intent = new Intent(ForoDetalleActivity.this, ResponderActivity.class);
        intent.putExtra("idHilo", idHilo);
        startActivity(intent);
    }
}
