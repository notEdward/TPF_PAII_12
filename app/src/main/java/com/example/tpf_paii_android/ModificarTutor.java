package com.example.tpf_paii_android;

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

import com.example.tpf_paii_android.modelos.Genero;
import com.example.tpf_paii_android.modelos.Tutor;
import com.example.tpf_paii_android.viewmodels.TutorViewModel;
import com.example.tpf_paii_android.viewmodels.TutorViewModelFactory;

public class ModificarTutor extends AppCompatActivity {

    private TutorViewModel tutorViewModel;
    private EditText txtNombre, txtApellido, txtEdad, txtOcupacion, txtPasatiempos, txtMasInfo;
    private Spinner spGenero;
    private Button btnModificar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modificar_tutor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spGenero = findViewById(R.id.spGeneroTutorMOD);
        txtNombre = findViewById(R.id.txtNombreTutorMOD);
        txtApellido = findViewById(R.id.txtApellidoTutorMOD);
        txtEdad = findViewById(R.id.txtEdadTutorMOD);
        txtOcupacion = findViewById(R.id.txtOcupacionTutorMOD);
        txtPasatiempos = findViewById(R.id.txtPasaTiemposTutorMOD);
        txtMasInfo = findViewById(R.id.txtMasSobreTutorMOD);
        btnModificar = findViewById(R.id.btnModificarTutor);

        tutorViewModel = new ViewModelProvider(this, new TutorViewModelFactory(this)).get(TutorViewModel.class);

        // Obtener el ID del tutor
        MyApp app = (MyApp) getApplication();
        int idTutor = app.getidEspecifico();

        tutorViewModel.getTutorLiveData().observe(this, tutor -> {
            if (tutor != null) {
                txtNombre.setText(tutor.getNombre());
                txtApellido.setText(tutor.getApellido());
                txtEdad.setText(String.valueOf(tutor.getEdad()));
                txtOcupacion.setText(tutor.getOcupacion());
                txtPasatiempos.setText(tutor.getPasatiempos());
                txtMasInfo.setText(tutor.getInfoAdicional());

               tutorViewModel.getGeneros().observe(this, generos -> {
                    if (generos != null && !generos.isEmpty()) {
                        ArrayAdapter<Genero> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, generos);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spGenero.setAdapter(adapter);

                        // carga genero  segun el idGenero del tutor
                        for (int i = 0; i < generos.size(); i++) {
                            if (generos.get(i).getId_genero() == tutor.getIdGenero()) {
                                spGenero.setSelection(i);
                                break;
                            }
                        }
                    } else {
                        Toast.makeText(this, "No hay géneros disponibles", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        tutorViewModel.getActualizacionExitosaLiveData().observe(this, isSuccessful -> {
            if (isSuccessful) {
                Toast.makeText(this, "Tutor actualizado exitosamente", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al actualizar el tutor", Toast.LENGTH_SHORT).show();
            }
        });

        tutorViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        tutorViewModel.cargarTutor(idTutor);

        btnModificar.setOnClickListener(v -> {
            String nombre = txtNombre.getText().toString();
            String apellido = txtApellido.getText().toString();
            String edad = txtEdad.getText().toString();
            String ocupacion = txtOcupacion.getText().toString();
            String pasatiempos = txtPasatiempos.getText().toString();
            String masInfo = txtMasInfo.getText().toString();

            int edadInt = Integer.parseInt(edad);
            int idGenero = ((Genero) spGenero.getSelectedItem()).getId_genero();

            if (nombre.isEmpty() || apellido.isEmpty() || edad.isEmpty() || ocupacion.isEmpty() ||
                pasatiempos.isEmpty() || masInfo.isEmpty()) {
                Toast.makeText(ModificarTutor.this, "Todos los campos deben ser completados.", Toast.LENGTH_SHORT).show();
                return;
            }

            Tutor tutorModificado = new Tutor(nombre, apellido, edadInt, idGenero, ocupacion, pasatiempos, masInfo);
            tutorModificado.setIdTutor(idTutor);

            tutorViewModel.actualizarTutor(tutorModificado);
        });
    }

}