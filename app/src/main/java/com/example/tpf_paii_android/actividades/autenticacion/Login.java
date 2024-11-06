package com.example.tpf_paii_android.actividades.autenticacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.tpf_paii_android.R;

public class Login extends AppCompatActivity {

    private EditText txtUsuario, txtContrasena;
    private Button btnIniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializa las vistas
        txtUsuario = findViewById(R.id.txtUsuario);
        txtContrasena = findViewById(R.id.txtContrasena);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);


        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtiene los valores ingresados
                String usuario = txtUsuario.getText().toString().trim();
                String contrasena = txtContrasena.getText().toString().trim();

                // Verifica que los campos no estén vacios
                if(usuario.isEmpty() || contrasena.isEmpty()) {
                    Toast.makeText(Login.this, "Por favor ingrese usuario y contraseña.", Toast.LENGTH_SHORT).show();
                    return;
                }


            }
        });

    }

    // Método para el textView Registrarse
    public void Registrarse(View view){
        Intent registrarse = new Intent(this, IniciarRegistro.class);
        startActivity(registrarse);
    }
}