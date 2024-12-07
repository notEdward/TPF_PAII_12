package com.example.tpf_paii_android;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.tpf_paii_android.viewmodels.UsuarioViewModel;

public class ModificarContrasena extends AppCompatActivity {

    private EditText txtContrasenaActual, txtNuevaContrasena, txtRepetirContrasena;
    private Button btnModificar;

    private UsuarioViewModel usuarioViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modificar_contrasena);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        txtContrasenaActual = findViewById(R.id.txtContrasenaActualMOD);
        txtNuevaContrasena = findViewById(R.id.txtNuevaContrasenaMOD);
        txtRepetirContrasena = findViewById(R.id.txtRepetirContrasenaMOD);

        btnModificar = findViewById(R.id.btnModificarPassMOD);

        usuarioViewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);

        observarViewModel();

        btnModificar.setOnClickListener(view -> validarYactualizarContrasena());

    }

    private void observarViewModel() {
        usuarioViewModel.getVerificarContrasenaLiveData().observe(this, esCorrecta -> {
            if (esCorrecta) {
                actualizarContrasena();
            } else {
                Toast.makeText(this, "La contraseña actual es incorrecta.", Toast.LENGTH_SHORT).show();
            }
        });

        usuarioViewModel.getActualizarContrasenaLiveData().observe(this, actualizada -> {
            if (actualizada) {
                Toast.makeText(this, "Contraseña actualizada correctamente.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al actualizar la contraseña.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validarYactualizarContrasena() {
        String contrasenaActual = txtContrasenaActual.getText().toString().trim();
        String nuevaContrasena = txtNuevaContrasena.getText().toString().trim();
        String repetirNuevaContrasena = txtRepetirContrasena.getText().toString().trim();

        if (contrasenaActual.isEmpty() || nuevaContrasena.isEmpty() || repetirNuevaContrasena.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (contrasenaActual.length() > 40) {
            Toast.makeText(this, "La contraseña actual no puede superar los 40 caracteres.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!nuevaContrasena.equals(repetirNuevaContrasena)) {
            Toast.makeText(this, "Las contraseñas nuevas no coinciden.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el nombre del usuario desde la aplicación
        MyApp app = (MyApp) getApplication();
        String nombreUsuario = app.getNombreUsuario();

        // Verificar la contraseña actual
        usuarioViewModel.verificarContrasenaActual(nombreUsuario, contrasenaActual);
    }

    private void actualizarContrasena() {
        String nuevaContrasena = txtNuevaContrasena.getText().toString().trim();
        MyApp app = (MyApp) getApplication();
        String nombreUsuario = app.getNombreUsuario();

        usuarioViewModel.actualizarContrasena(nombreUsuario, nuevaContrasena);
    }
}