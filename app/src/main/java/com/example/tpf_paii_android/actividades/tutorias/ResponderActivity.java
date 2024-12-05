package com.example.tpf_paii_android.actividades.tutorias;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.tpf_paii_android.MyApp;
import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.viewmodels.ForoViewModel;

public class ResponderActivity extends AppCompatActivity {

    private EditText editTextMensajeRespuesta;
    private Button btnEnviarRespuesta;
    private int idHilo;
    private String nombreUsuario;
    private ForoViewModel foroViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_responder);

        MyApp app = (MyApp) getApplication();
        int idUsuario = app.getIdUsuario();
        nombreUsuario = app.getNombreUsuario();

        foroViewModel = new ViewModelProvider(this).get(ForoViewModel.class);

        editTextMensajeRespuesta = findViewById(R.id.editTextMensajeRespuesta);
        btnEnviarRespuesta = findViewById(R.id.btnEnviarRespuesta);

        idHilo = getIntent().getIntExtra("idHilo", -1);

        btnEnviarRespuesta.setOnClickListener(v -> enviarRespuesta());
    }

    private void enviarRespuesta() {
        String mensaje = editTextMensajeRespuesta.getText().toString();
        if (mensaje.isEmpty()) {
            Toast.makeText(this, "El mensaje no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mensaje.length() > 150) {
            Toast.makeText(this, "El mensaje no puede tener más de 150 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }
        foroViewModel.enviarRespuesta(idHilo, nombreUsuario, mensaje).observe(this, success -> {
            if (success) {
                Toast.makeText(ResponderActivity.this, "Respuesta enviada", Toast.LENGTH_SHORT).show();
           finish();
            } else {
                Toast.makeText(ResponderActivity.this, "Error al enviar respuesta", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

