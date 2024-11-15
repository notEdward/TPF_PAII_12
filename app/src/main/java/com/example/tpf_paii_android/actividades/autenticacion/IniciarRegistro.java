package com.example.tpf_paii_android.actividades.autenticacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.actividades.registracion.RegistrarEmpresa;
import com.example.tpf_paii_android.actividades.registracion.RegistrarEstudiante;
import com.example.tpf_paii_android.actividades.registracion.RegistrarTutor;
import com.example.tpf_paii_android.actividades.tutores.TutorActivity;

public class IniciarRegistro extends AppCompatActivity {

    private RadioGroup radioGroup; // Declaracion del RadioGroup para opciones de registro


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_iniciar_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializa las vistas
        radioGroup = findViewById(R.id.radioGroup);

        // Convierte el boton en una variable local
        Button btnIniciarRegistro = findViewById(R.id.btnIniciarRegistro);

        // Config evento OnClick del botón de registro
        btnIniciarRegistro.setOnClickListener(v -> {
            int seleccionId = radioGroup.getCheckedRadioButtonId();

            if(seleccionId == R.id.rbEstudiante){
                // Dirige a RegistrarEstudiante Activity
                Intent intent = new Intent(IniciarRegistro.this, RegistrarEstudiante.class);
                startActivity(intent);
            } else if (seleccionId == R.id.rbEmpresa){
                // Dirige a RegistrarEmpresa Activity
                Intent intent = new Intent(IniciarRegistro.this, RegistrarEmpresa.class);
                startActivity(intent);
            } else if (seleccionId == R.id.rbTutor){
                // Dirige a RegistrarTutor Activity
                 Intent intent = new Intent(IniciarRegistro.this, TutorActivity.class);
                 startActivity(intent);
            } else {
                Toast.makeText(this, "Por favor, selecciona una opción.", Toast.LENGTH_SHORT).show();
            }
        });
    }



}