package com.example.tpf_paii_android.actividades.tutorias;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpf_paii_android.MyApp;
import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.repositorios.ForoRepository;

public class ResponderActivity extends AppCompatActivity {

    private EditText editTextMensajeRespuesta;
    private Button btnEnviarRespuesta;
    private int idHilo;
    private String nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responder);

        MyApp app = (MyApp) getApplication();
        int idUsuario = app.getIdUsuario();
        String tipo_usuario = app.getTipoUsuario();
         nombreUsuario = app.getNombreUsuario();

        editTextMensajeRespuesta = findViewById(R.id.editTextMensajeRespuesta);
        btnEnviarRespuesta = findViewById(R.id.btnEnviarRespuesta);

        // Obtener el ID del hilo
        idHilo = getIntent().getIntExtra("idHilo", -1);

        btnEnviarRespuesta.setOnClickListener(v -> enviarRespuesta());
    }

    private void enviarRespuesta() {
        String mensaje = editTextMensajeRespuesta.getText().toString();
        if (mensaje.isEmpty()) {
            Toast.makeText(this, "El mensaje no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        // Llamar al repositorio para guardar la respuesta
        ForoRepository foroRepository = new ForoRepository();
        foroRepository.agregarRespuesta(idHilo, nombreUsuario, mensaje, new ForoRepository.DataCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                Toast.makeText(ResponderActivity.this, "Respuesta enviada", Toast.LENGTH_SHORT).show();
                finish(); // Regresar a la actividad de foro
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(ResponderActivity.this, "Error al enviar respuesta", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
