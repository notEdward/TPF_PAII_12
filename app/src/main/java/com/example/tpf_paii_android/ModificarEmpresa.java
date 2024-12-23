package com.example.tpf_paii_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tpf_paii_android.actividades.autenticacion.Login;
import com.example.tpf_paii_android.modelos.Empresa;
import com.example.tpf_paii_android.modelos.Localidad;
import com.example.tpf_paii_android.modelos.Provincia;
import com.example.tpf_paii_android.modelos.Tutor;
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
        empresaViewModel.cargarEmpresa(idEmpresa).observe(this, empresa -> {
            if (empresa != null) {
                // Rellenar los campos con los datos de la empresa
                txtNombre.setText(empresa.getNombre());
                txtDescripcion.setText(empresa.getDescripcion());
                txtSector.setText(empresa.getSector());
                txtEmail.setText(empresa.getEmail());
                txtTelefono.setText(empresa.getTelefono());
                txtDireccion.setText(empresa.getDireccion());
                empresaViewModel.setProvinciaSeleccionada(empresa.getLocalidad().getIdprovincia());
                seleccionarProvinciaEnSpinner(empresa.getLocalidad().getIdprovincia());
                seleccionarLocalidadEnSpinner(empresa.getId_localidad());
            }
        });

        empresaViewModel.getProvincias().observe(this, new Observer<List<Provincia>>() {
            @Override
            public void onChanged(List<Provincia> provincias) {
                if (provincias != null) cargarSpinnerProvincia(provincias);
            }
        });

       // Observar los datos de los getProvincias
        empresaViewModel.getProvincias().observe(this, provincias -> {
            if (provincias != null && !provincias.isEmpty()) {
                cargarSpinnerProvincia(provincias);
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


        empresaViewModel.getActualizacionExitosaLiveData().observe(this, isSuccessful -> {
            if (isSuccessful) {
                Toast.makeText(this, "Empresa actualizado exitosamente", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al actualizar el Empresa", Toast.LENGTH_SHORT).show();
            }
        });

        empresaViewModel.getError().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        empresaViewModel.cargarEmpresa(idEmpresa);

        btnModificar.setOnClickListener(view -> {
            if (spLocalidad.getAdapter() == null || spLocalidad.getAdapter().isEmpty()) {
                Toast.makeText(ModificarEmpresa.this, "Espere a que las localidades carguen antes de modificar.", Toast.LENGTH_SHORT).show();
                return;
            }
            String nombre = txtNombre.getText().toString();
            String descripcion = txtDescripcion.getText().toString();
            String sector = txtSector.getText().toString();
            String email = txtEmail.getText().toString();
            String telefono = txtTelefono.getText().toString();
            String direccion = txtDireccion.getText().toString();
            int idLocalidad = ((Localidad) spLocalidad.getSelectedItem()).getId_localidad();
//validaciones
            if (nombre.isEmpty()) {
                Toast.makeText(ModificarEmpresa.this, "El nombre no puede estar vacío.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (sector.isEmpty()) {
                Toast.makeText(ModificarEmpresa.this, "El sector no puede estar vacío.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (email.isEmpty()) {
                Toast.makeText(ModificarEmpresa.this, "El email no puede estar vacío.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (descripcion.isEmpty()) {
                Toast.makeText(ModificarEmpresa.this, "La descripción no puede estar vacía.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (direccion.isEmpty()) {
                Toast.makeText(ModificarEmpresa.this, "La dirección no puede estar vacía.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (telefono.isEmpty()) {
                Toast.makeText(ModificarEmpresa.this, "El teléfono no puede estar vacío.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (nombre.length() > 50) {
                Toast.makeText(ModificarEmpresa.this, "El nombre no puede superar los 50 caracteres.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (sector.length() > 50) {
                Toast.makeText(ModificarEmpresa.this, "El sector no puede superar los 50 caracteres.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (email.length() > 50 || !email.endsWith(".com")) {
                Toast.makeText(ModificarEmpresa.this, "El email debe tener un máximo de 50 caracteres y terminar en .com.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (descripcion.length() > 200) {
                Toast.makeText(ModificarEmpresa.this, "La descripción no puede superar los 200 caracteres.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (direccion.length() > 150) {
                Toast.makeText(ModificarEmpresa.this, "La dirección no puede superar los 150 caracteres.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (telefono.length() > 20) {
                Toast.makeText(ModificarEmpresa.this, "El teléfono no puede superar los 20 caracteres.", Toast.LENGTH_SHORT).show();
                return;
            }
            //
            Localidad localidadSeleccionada = (Localidad) spLocalidad.getSelectedItem();
            if (localidadSeleccionada == null) {
                Toast.makeText(ModificarEmpresa.this, "Seleccione una localidad válida.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (spProvincia.getSelectedItem() == null || spProvincia.getSelectedItemPosition() == 0) {
                Toast.makeText(this, "Debe seleccionar una provincia", Toast.LENGTH_SHORT).show();
                return;
            }

            if(nombre.isEmpty() || descripcion.isEmpty() || sector.isEmpty() ||
                email.isEmpty() || telefono.isEmpty() || direccion.isEmpty()){
                Toast.makeText(ModificarEmpresa.this, "Todos los campos deben ser completados.", Toast.LENGTH_SHORT).show();
                return;
            }


            Empresa empresaModificada = new Empresa( nombre, descripcion, sector, email, telefono, direccion, idLocalidad);
            empresaModificada.setId_empresa(idEmpresa);
            empresaViewModel.actualizarEmpresa(empresaModificada);
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
    private void seleccionarLocalidadEnSpinner(int localidadSeleccionado) {
        if (localidadSeleccionado != -1) {
            empresaViewModel.getLocalidades().observe(this, localidad -> {
                int posicion = -1;
                for (int i = 0; i < localidad.size(); i++) {
                    if (localidad.get(i).getId_localidad() == localidadSeleccionado) {
                        posicion = i;
                        break;
                    }
                }
                if (posicion != -1) {
                    spLocalidad.setSelection(posicion);
                }
            });
        }
    }
    private void seleccionarProvinciaEnSpinner(int idProvinciaSeleccionada) {
        if (idProvinciaSeleccionada != -1) {
            empresaViewModel.getProvincias().observe(this, provincias -> {
                int posicion = -1;
                for (int i = 0; i < provincias.size(); i++) {
                    if (provincias.get(i).getId_provincia() == idProvinciaSeleccionada) {
                        posicion = i;
                        break;
                    }
                }
                if (posicion != -1) {
                    spProvincia.setSelection(posicion);
                }
            });
        }
    }
}