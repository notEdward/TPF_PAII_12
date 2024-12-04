package com.example.tpf_paii_android;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tpf_paii_android.modelos.Localidad;
import com.example.tpf_paii_android.modelos.Provincia;
import com.example.tpf_paii_android.repositorios.EmpresaRepository;
import com.example.tpf_paii_android.viewmodels.EmpresaViewModel;

import java.util.List;

public class ModificarEmpresa extends AppCompatActivity {

    private EmpresaViewModel empresaViewModel;

   private EditText txtNombre, txtDescripcion, txtSector, txtEmail, txtTelefono, txtDireccion;
   private Spinner spProvincia, spLocalidad;
   private Button btnModificar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modificar_empresa);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MyApp app = (MyApp) getApplication();
        int idEmpresa = app.getidEspecifico();

        txtNombre = findViewById(R.id.txtNombreEmpMOD);
        txtDescripcion = findViewById(R.id.txtDescripcionEmpMOD);
        txtSector = findViewById(R.id.txtSectorEmpMOD);
        txtEmail = findViewById(R.id.txtEmailEmpMOD);
        txtTelefono = findViewById(R.id.txtTelefonoEmpMOD);
        txtDireccion = findViewById(R.id.txtDireccionEmpMOD);
        spProvincia = findViewById(R.id.spProvinciaEmpMOD);
        spLocalidad = findViewById(R.id.spLocalidadEmpMOD);
        btnModificar = findViewById(R.id.btnModificarEmp);

// Inicializar el ViewModel
        EmpresaRepository empresaRepository = new EmpresaRepository(getApplicationContext());
        EmpresaViewModel.Factory factory = new EmpresaViewModel.Factory(empresaRepository);
        empresaViewModel = new ViewModelProvider(this, factory).get(EmpresaViewModel.class);


// Observar el LiveData de la empresa para obtener los datos
        empresaViewModel.obtenerEmpresa(idEmpresa).observe(this, empresa -> {
            if (empresa != null) {
                // Rellenar los campos con los datos de la empresa
                txtNombre.setText(empresa.getNombre());
                txtDescripcion.setText(empresa.getDescripcion());
                txtSector.setText(empresa.getSector());
                txtEmail.setText(empresa.getEmail());
                txtTelefono.setText(empresa.getTelefono());
                txtDireccion.setText(empresa.getDireccion());
            }
        });

        // Observar los datos de los getProvincias
        empresaViewModel.getProvincias().observe(this, new Observer<List<Provincia>>() {
            @Override
            public void onChanged(List<Provincia> provincias) {
                if (provincias != null) cargarSpinnerProvincia(provincias);
            }
        });

        // Observar las localidades basadas en la provincia seleccionada
        empresaViewModel.getLocalidades().observe(this, new Observer<List<Localidad>>() {
            @Override
            public void onChanged(List<Localidad> localidades) {
                if (localidades != null) {
                    cargarSpinnerLocalidad(localidades);
                }
            }
        });

        // Listener para el spinner de provincias
        spProvincia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Provincia provinciaSeleccionada = (Provincia) parent.getItemAtPosition(position);
                empresaViewModel.setProvinciaSeleccionada(provinciaSeleccionada.getId_provincia());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

    }

    private void cargarSpinnerProvincia(List<Provincia> provincias) {
        ArrayAdapter<Provincia> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provincias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProvincia.setAdapter(adapter);
    }

    private void cargarSpinnerLocalidad(List<Localidad> localidades) {
        ArrayAdapter<Localidad> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, localidades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLocalidad.setAdapter(adapter);
    }

}