package com.example.tpf_paii_android.actividades.tutores;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.actividades.autenticacion.Login;
import com.example.tpf_paii_android.modelos.Genero;
import com.example.tpf_paii_android.modelos.TipoUsuario;
import com.example.tpf_paii_android.modelos.Tutor;
import com.example.tpf_paii_android.modelos.Usuario;
import com.example.tpf_paii_android.viewmodels.TutorViewModel;
import com.example.tpf_paii_android.viewmodels.TutorViewModelFactory;

public class TutorActivity extends AppCompatActivity {

    private TutorViewModel tutorViewModel;

    private Spinner spinnerGenero;
    private EditText txtNombreTutor, txtApellidoTutor, txtDniTutor, txtEdadTutor, txtNombreUsuarioTutor,
            txtContrasenaTutor, txtRepetirContrasenaTutor, txtOcupacionTutor, txtPasaTiemposTutor, txtMasSobreTutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_tutor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Inicializa vistas
        spinnerGenero = findViewById(R.id.spGeneroTutor);
        txtNombreTutor = findViewById(R.id.txtNombreTutor);
        txtApellidoTutor = findViewById(R.id.txtApellidoTutor);
        txtDniTutor = findViewById(R.id.txtDniTutor);
        txtEdadTutor = findViewById(R.id.txtEdadTutor);
        txtNombreUsuarioTutor = findViewById(R.id.txtNombreUsuarioTutor);
        txtContrasenaTutor = findViewById(R.id.txtContrasenaTutor);
        txtRepetirContrasenaTutor = findViewById(R.id.txtRepetirContrasenaTutor);
        txtOcupacionTutor = findViewById(R.id.txtOcupacionTutor);
        txtPasaTiemposTutor = findViewById(R.id.txtPasaTiemposTutor);
        txtMasSobreTutor = findViewById(R.id.txtMasSobreTutor);

        Button btnRegistrarseTutor = findViewById(R.id.btnRegistrarseTutor);

        tutorViewModel = new ViewModelProvider(this, new TutorViewModelFactory(this)).get(TutorViewModel.class);


        // Observa la lista de generos para actualizar el spinner cuando los datos cambien
        tutorViewModel.getGeneros().observe(this, generos -> {
            if (generos != null && !generos.isEmpty()) {
                ArrayAdapter<Genero> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, generos);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerGenero.setAdapter(adapter);
            } else {
                Toast.makeText(this, "No hay géneros disponibles", Toast.LENGTH_SHORT).show();
            }
        });

        // Observa el estado de exito del registro
        tutorViewModel.getRegistroExitoso().observe(this, isSuccess -> {
            if (isSuccess) {
                Toast.makeText(this, "Tutor registrado exitosamente", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        // Observa errores
        tutorViewModel.getError().observe(this, errorMessage -> {
            // Mostrar mensaje de error
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        });

        // CONFIG boton para registrar Tutor
        btnRegistrarseTutor.setOnClickListener(v -> {
            if (validarCampos()) {
                Tutor tutor = crearTutorDesdeCampos();
                registrarTutor(tutor);
            }
        });
    }



    // Metodo validarCampos()
    private boolean validarCampos() {
        if (    txtDniTutor.getText().toString().isEmpty() ||
                txtNombreTutor.getText().toString().isEmpty() ||
                txtApellidoTutor.getText().toString().isEmpty() ||
                txtEdadTutor.getText().toString().isEmpty() ||
                txtOcupacionTutor.getText().toString().isEmpty() ||
                txtPasaTiemposTutor.getText().toString().isEmpty() ||
                txtMasSobreTutor.getText().toString().isEmpty() ||
                txtNombreUsuarioTutor.getText().toString().isEmpty() ||
                txtContrasenaTutor.getText().toString().isEmpty() ||
                txtRepetirContrasenaTutor.getText().toString().isEmpty()) {
            Toast.makeText(this, "Complete todos los campos.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // valida contraseñas iguales
        if (!txtContrasenaTutor.getText().toString().equals(txtRepetirContrasenaTutor.getText().toString())) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    // Crea objeto Tutor
    private Tutor crearTutorDesdeCampos() {
        String dni = txtDniTutor.getText().toString();
        String nombre = txtNombreTutor.getText().toString();
        String apellido = txtApellidoTutor.getText().toString();

        int edad = Integer.parseInt(txtEdadTutor.getText().toString());

        // Obtiene ID genero
        Genero genero = (Genero) spinnerGenero.getSelectedItem();
        int idGenero = genero.getId_genero();

        String ocupacion = txtOcupacionTutor.getText().toString();
        String pasatiempos = txtPasaTiemposTutor.getText().toString();
        String masSobre = txtMasSobreTutor.getText().toString();

        // Datos para el Usuario
        String nombreUsuario = txtNombreUsuarioTutor.getText().toString();
        String contrasena = txtContrasenaTutor.getText().toString();

        // Crea objeto Usuario con el tipo TUTOR
        Usuario usuario = new Usuario(nombreUsuario,contrasena, TipoUsuario.TUTOR);

        // Crea objeto Tutor relacionado con el Usuario
        return new Tutor(dni, nombre, apellido, edad, idGenero, ocupacion, pasatiempos, masSobre, usuario);
    }

    // Registra el tutor
    private void registrarTutor(Tutor tutor) {
        if (tutor != null) {
            tutorViewModel.registrarTutor(tutor);
        }
    }

}