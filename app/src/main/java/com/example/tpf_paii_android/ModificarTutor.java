package com.example.tpf_paii_android;

import android.os.Bundle;
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


        tutorViewModel = new ViewModelProvider(this, new TutorViewModelFactory(this)).get(TutorViewModel.class);

        // Observar los datos del tutor
        tutorViewModel.getTutorLiveData().observe(this, tutor -> {
            if (tutor != null) {
                txtNombre.setText(tutor.getNombre());
                txtApellido.setText(tutor.getApellido());
                txtEdad.setText(String.valueOf(tutor.getEdad()));
                txtOcupacion.setText(tutor.getOcupacion());
                txtPasatiempos.setText(tutor.getPasatiempos());
                txtMasInfo.setText(tutor.getInfoAdicional());
            }
        });


        // Observar errores
        tutorViewModel.getErrorLiveData().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });


        MyApp app = (MyApp) getApplication();
        int idTutor = app.getidEspecifico();
        // Llamar al método para cargar el tutor (por ejemplo, con idTutor = 1)
        tutorViewModel.cargarTutor(idTutor);


    }
}