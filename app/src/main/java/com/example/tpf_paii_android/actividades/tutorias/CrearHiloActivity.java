package com.example.tpf_paii_android.actividades.tutorias;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.tpf_paii_android.MyApp;
import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.modelos.Foro;
import com.example.tpf_paii_android.viewmodels.ForoViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CrearHiloActivity extends AppCompatActivity {

    private ForoViewModel foroViewModel;
    private EditText editTextTitulo, editTextMensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_hilo);

        MyApp app = (MyApp) getApplication();
        int idUsuario = app.getIdUsuario();
        String tipo_usuario = app.getTipoUsuario();
        String nombreUsuario = app.getNombreUsuario();

        // Inicialización del ViewModel
        foroViewModel = new ViewModelProvider(this).get(ForoViewModel.class);

        // Inicializar campos de texto
        editTextTitulo = findViewById(R.id.editTextTitulo);
        editTextMensaje = findViewById(R.id.editTextMensaje);

        // Configuración del botón para crear el hilo
        findViewById(R.id.buttonCrearHilo).setOnClickListener(v -> {
            String titulo = editTextTitulo.getText().toString();
            String mensaje = editTextMensaje.getText().toString();

            if (titulo.isEmpty() || mensaje.isEmpty()) {
                Toast.makeText(CrearHiloActivity.this, "Por favor ingrese un título y un mensaje", Toast.LENGTH_SHORT).show();
            } else {
                // Crear nuevo foro
                Foro nuevoForo = new Foro(1, idUsuario, nombreUsuario, titulo, mensaje, new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                foroViewModel.createHilo(nuevoForo).observe(this, isCreated -> {
                    if (isCreated) {
                        Toast.makeText(CrearHiloActivity.this, "Hilo creado con éxito", Toast.LENGTH_SHORT).show();
                        finish(); // Volver a la actividad anterior
                    } else {
                        Toast.makeText(CrearHiloActivity.this, "Error al crear el hilo", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
