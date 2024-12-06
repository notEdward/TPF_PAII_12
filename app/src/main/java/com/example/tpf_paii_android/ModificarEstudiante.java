package com.example.tpf_paii_android;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.tpf_paii_android.adapters.LocalidadAdapter;
import com.example.tpf_paii_android.adapters.ModalidadAdapter;
import com.example.tpf_paii_android.adapters.NivelEducativoAdapter;
import com.example.tpf_paii_android.adapters.ProvinciaAdapter;
import com.example.tpf_paii_android.adapters.TipoEmpleoAdapter;
import com.example.tpf_paii_android.modelos.EstadoNivelEducativo;
import com.example.tpf_paii_android.modelos.Estudiante;
import com.example.tpf_paii_android.modelos.ExperienciaLaboral;
import com.example.tpf_paii_android.modelos.Genero;
import com.example.tpf_paii_android.modelos.Localidad;
import com.example.tpf_paii_android.modelos.Modalidad;
import com.example.tpf_paii_android.modelos.NivelEducativo;
import com.example.tpf_paii_android.modelos.OfertaEmpleo;
import com.example.tpf_paii_android.modelos.Provincia;
import com.example.tpf_paii_android.modelos.TipoEmpleo;
import com.example.tpf_paii_android.viewmodels.ModificarEstudianteViewModel;
import com.example.tpf_paii_android.viewmodels.OfertaViewModel;

public class ModificarEstudiante extends AppCompatActivity {

    private EditText txtNombreEstMOD, txtApellidoEstMOD, txtEmailEstMOD,
            txtTelefonoEstMOD, txtDireccionEstMOD, txtLugarTabajoEstMOD,
            txtCargoEstMOD, txtTareasEstMOD, txtDuracionEstMOD;

    private Spinner spGeneroEstMOD, spProvinciaEstMOD, spLocalidadEstMOD,
            spNivelEducativoEstMOD, spEstadoEducativoEstMOD;

    private Button btnModificarEst;
    private int idUsuario;
    private ModificarEstudianteViewModel viewModel;
    private ProvinciaAdapter provinciaAdapter;
    private LocalidadAdapter localidadAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_estudiante);
        MyApp app = (MyApp) getApplicationContext();
        idUsuario = app.getIdUsuario();

        txtNombreEstMOD = findViewById(R.id.txtNombreEstMOD);
        txtApellidoEstMOD = findViewById(R.id.txtApellidoEstMOD);
        txtEmailEstMOD = findViewById(R.id.txtEmailEstMOD);
        txtTelefonoEstMOD = findViewById(R.id.txtTelefonoEstMOD);
        txtDireccionEstMOD = findViewById(R.id.txtDireccionEstMOD);
        txtLugarTabajoEstMOD = findViewById(R.id.txtLugarTabajoEstMOD);
        txtCargoEstMOD = findViewById(R.id.txtCargoEstMOD);
        txtTareasEstMOD = findViewById(R.id.txtTareasEstMOD);
        txtDuracionEstMOD = findViewById(R.id.txtDuracionEstMOD);

        spGeneroEstMOD = findViewById(R.id.spGeneroEstMOD);
        spProvinciaEstMOD = findViewById(R.id.spProvinciaEstMOD);
        spLocalidadEstMOD = findViewById(R.id.spLocalidadEstMOD);
        spNivelEducativoEstMOD = findViewById(R.id.spNivelEducativoEstMOD);
        spEstadoEducativoEstMOD = findViewById(R.id.spEstadoEducativoEstMOD);

        btnModificarEst = findViewById(R.id.btnModificarEst);

        viewModel = new ViewModelProvider(this).get(ModificarEstudianteViewModel.class);
        viewModel.obtenerDetalleEstudiante(idUsuario);


        viewModel.getEstudianteLiveData().observe(this, estudiante -> {
            if (estudiante != null) {
                txtNombreEstMOD.setText(estudiante.getNombre());
                txtApellidoEstMOD.setText(estudiante.getApellido());
//              textDNI.setText("DNI: " + estudiante.getDni());
                txtEmailEstMOD.setText(estudiante.getEmail());
                txtTelefonoEstMOD.setText(estudiante.getTelefono());
                txtDireccionEstMOD.setText(estudiante.getDireccion());
                txtLugarTabajoEstMOD.setText(estudiante.getExperienciaLaboral().getLugar());
                txtCargoEstMOD.setText(estudiante.getExperienciaLaboral().getCargo());
                txtTareasEstMOD.setText(estudiante.getExperienciaLaboral().getTareas());
                txtDuracionEstMOD.setText(estudiante.getExperienciaLaboral().getDuracion());

                seleccionarNivelEducativoEnSpinner(estudiante.getNivelEducativo());
                seleccionarEstadoNivelEnSpinnerEnSpinner(estudiante.getEstadoNivelEducativo());
                seleccionarGeneroEnSpinner(estudiante.getGenero());
                seleccionarLocalidadEnSpinner(estudiante.getLocalidad());
                seleccionarProvinciaEnSpinner(estudiante.getLocalidad());
            }
        });
        configurarObservadores();
        cargarDatos();

        btnModificarEst.setOnClickListener(v -> {
            if (validarFormulario()) {
                Estudiante estudiante = new Estudiante();
                llenarEstudianteDesdeFormulario(estudiante);
                viewModel.actualizarEstudiante(estudiante);
                Toast.makeText(this, "Formulario válido, procesando datos...", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Por favor corregir los errores antes de continuar.", Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getExitoActualizacion().observe(this, exito -> {
            if (exito != null && exito) {
                Toast.makeText(this, "Actualización exitosa.", Toast.LENGTH_SHORT).show();
                finish();
            } else if (exito != null) {
                Toast.makeText(this, "Error al actualizar. Intente nuevamente.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void llenarEstudianteDesdeFormulario(Estudiante estudiante) {
        estudiante.setIdUsuario(idUsuario);
        // Campos de texto
        estudiante.setNombre(txtNombreEstMOD.getText().toString().trim());
        estudiante.setApellido(txtApellidoEstMOD.getText().toString().trim());
        estudiante.setEmail(txtEmailEstMOD.getText().toString().trim());
        estudiante.setTelefono(txtTelefonoEstMOD.getText().toString().trim());
        estudiante.setDireccion(txtDireccionEstMOD.getText().toString().trim());

        // Experiencia laboral
        ExperienciaLaboral experiencia = new ExperienciaLaboral();
        experiencia.setLugar(txtLugarTabajoEstMOD.getText().toString().trim());
        experiencia.setCargo(txtCargoEstMOD.getText().toString().trim());
        experiencia.setTareas(txtTareasEstMOD.getText().toString().trim());
        experiencia.setDuracion(txtDuracionEstMOD.getText().toString().trim());
        estudiante.setExperienciaLaboral(experiencia);

        // Spinners
        estudiante.setId_genero(((Genero) spGeneroEstMOD.getSelectedItem()).getId_genero());
        estudiante.setId_localidad(((Localidad) spLocalidadEstMOD.getSelectedItem()).getId_localidad());
        estudiante.setId_nivelEducativo(((NivelEducativo) spNivelEducativoEstMOD.getSelectedItem()).getId_nivelEducativo());
        estudiante.setId_estadoNivelEducativo(((EstadoNivelEducativo) spEstadoEducativoEstMOD.getSelectedItem()).getId_estadoNivelEducativo());
    }
    private void configurarObservadores() {
        viewModel.getNivelesEducativosLiveData().observe(this, nivelesEducativos -> {
            NivelEducativoAdapter nivelEducativoAdapter = new NivelEducativoAdapter(this, nivelesEducativos);
            spNivelEducativoEstMOD.setAdapter(nivelEducativoAdapter);
        });
        viewModel.getEstadoNivelEducativoLiveData().observe(this, estadoNivelEducativos -> {
            ArrayAdapter<EstadoNivelEducativo> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, estadoNivelEducativos);
            spEstadoEducativoEstMOD.setAdapter(adapter);
        });
        viewModel.getGeneroLiveData().observe(this, generos -> {
            ArrayAdapter<Genero> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, generos);
            spGeneroEstMOD.setAdapter(adapter);
        });
        viewModel.getProvinciasLiveData().observe(this, provincias -> {
            provinciaAdapter = new ProvinciaAdapter(this, provincias);
            spProvinciaEstMOD.setAdapter(provinciaAdapter);
        });
        viewModel.getLocalidadesLiveData().observe(this, localidades -> {
            ArrayAdapter<Localidad> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, localidades);
            spLocalidadEstMOD.setAdapter(adapter);
        });
        viewModel.getLocalidadesLiveData().observe(this, localidades -> {
            localidadAdapter = new LocalidadAdapter(this, localidades);
            spLocalidadEstMOD.setAdapter(localidadAdapter);
        });

        //cambio automatico de provincia/localidad
        spProvinciaEstMOD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Provincia selectedProvincia = (Provincia) parentView.getItemAtPosition(position);
                viewModel.cargarLocalidadesPorProvincia(selectedProvincia.getId_provincia());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }
    //predeterminar spinners
    private void seleccionarNivelEducativoEnSpinner(NivelEducativo nivelEducativoSeleccionado) {
        if (nivelEducativoSeleccionado != null) {
            viewModel.getNivelesEducativosLiveData().observe(this, nivelEducativo -> {
                int posicion = -1;
                for (int i = 0; i < nivelEducativo.size(); i++) {
                    if (nivelEducativo.get(i).getId_nivelEducativo() == nivelEducativoSeleccionado.getId_nivelEducativo()) {
                        posicion = i;
                        break;
                    }
                }
                if (posicion != -1) {
                    spNivelEducativoEstMOD.setSelection(posicion);
                }
            });
        }
    }
    private void seleccionarEstadoNivelEnSpinnerEnSpinner(EstadoNivelEducativo estadoNivelEducativoSeleccionado) {
        if (estadoNivelEducativoSeleccionado != null) {
            viewModel.getEstadoNivelEducativoLiveData().observe(this, estadoNivelEducativo -> {
                int posicion = -1;
                for (int i = 0; i < estadoNivelEducativo.size(); i++) {
                    if (estadoNivelEducativo.get(i).getId_estadoNivelEducativo() == estadoNivelEducativoSeleccionado.getId_estadoNivelEducativo()) {
                        posicion = i;
                        break;
                    }
                }
                if (posicion != -1) {
                    spEstadoEducativoEstMOD.setSelection(posicion);
                }
            });
        }
    }
    private void seleccionarGeneroEnSpinner(Genero generoSeleccionado) {
        if (generoSeleccionado != null) {
            viewModel.getGeneroLiveData().observe(this, generos -> {
                int posicion = -1;
                for (int i = 0; i < generos.size(); i++) {
                    if (generos.get(i).getId_genero() == generoSeleccionado.getId_genero()) {
                        posicion = i;
                        break;
                    }
                }
                if (posicion != -1) {
                    spGeneroEstMOD.setSelection(posicion);
                }
            });
        }
    }
    private void seleccionarLocalidadEnSpinner(Localidad localidadSeleccionado) {
        if (localidadSeleccionado != null) {
            viewModel.getLocalidadesLiveData().observe(this, localidad -> {
                int posicion = -1;
                for (int i = 0; i < localidad.size(); i++) {
                    if (localidad.get(i).getId_localidad() == localidadSeleccionado.getId_localidad()) {
                        posicion = i;
                        break;
                    }
                }
                if (posicion != -1) {
                    spLocalidadEstMOD.setSelection(posicion);
                }
            });
        }
    }
    private void seleccionarProvinciaEnSpinner(Localidad localidadSeleccionado) {
        if (localidadSeleccionado != null) {
            viewModel.getProvinciasLiveData().observe(this, provincias -> {
                int posicion = -1;
                for (int i = 0; i < provincias.size(); i++) {
                    if (provincias.get(i).getId_provincia() == localidadSeleccionado.getIdprovincia()) {
                        posicion = i;
                        break;
                    }
                }
                if (posicion != -1) {
                    spProvinciaEstMOD.setSelection(posicion);
                }
            });
        }
    }
    private void cargarDatos() {
        viewModel.cargarGeneros();
        viewModel.cargarNivelesEducativos();
        viewModel.cargarEstadoNivelEducativo();
        viewModel.cargarLocalidades();
        viewModel.cargarProvincias();
    }
    //validaciones
    private boolean validarFormulario() {
        return validarCamposTexto() && validarSpinners();
    }
    private boolean validarCamposTexto() {

        if (txtNombreEstMOD.getText().toString().trim().isEmpty() || txtNombreEstMOD.getText().toString().length() > 50) {
            txtNombreEstMOD.setError("El nombre es obligatorio y no debe exceder 50 caracteres.");
            return false;
        }
        if (txtApellidoEstMOD.getText().toString().trim().isEmpty() || txtApellidoEstMOD.getText().toString().length() > 50) {
            txtApellidoEstMOD.setError("El apellido es obligatorio y no debe exceder 50 caracteres.");
            return false;
        }
        if (txtEmailEstMOD.getText().toString().trim().isEmpty() || txtEmailEstMOD.getText().toString().length() > 50) {
            txtEmailEstMOD.setError("El correo electrónico es obligatorio y no debe exceder 50 caracteres.");
            return false;
        }
        if (txtTelefonoEstMOD.getText().toString().trim().isEmpty() || !txtTelefonoEstMOD.getText().toString().matches("\\d{1,15}")) {
            txtTelefonoEstMOD.setError("El teléfono es obligatorio, debe contener solo números y no superar 15 caracteres.");
            return false;
        }
        if (txtDireccionEstMOD.getText().toString().trim().isEmpty() || txtDireccionEstMOD.getText().toString().length() > 100) {
            txtDireccionEstMOD.setError("La dirección es obligatoria y no debe exceder 100 caracteres.");
            return false;
        }
        if (txtLugarTabajoEstMOD.getText().toString().trim().isEmpty() || txtLugarTabajoEstMOD.getText().toString().length() > 150) {
            txtLugarTabajoEstMOD.setError("El lugar de trabajo es obligatorio y no debe exceder 150 caracteres.");
            return false;
        }
        if (txtCargoEstMOD.getText().toString().trim().isEmpty() || txtCargoEstMOD.getText().toString().length() > 150) {
            txtCargoEstMOD.setError("El cargo ocupado es obligatorio y no debe exceder 150 caracteres.");
            return false;
        }
        if (txtTareasEstMOD.getText().toString().trim().isEmpty() || txtTareasEstMOD.getText().toString().length() > 150) {
            txtTareasEstMOD.setError("Las tareas realizadas son obligatorias y no deben exceder 150 caracteres.");
            return false;
        }
        if (txtDuracionEstMOD.getText().toString().trim().isEmpty() || txtDuracionEstMOD.getText().toString().length() > 20) {
            txtDuracionEstMOD.setError("La duración es obligatoria y no debe exceder 20 caracteres.");
            return false;
        }
        return true;
    }
    private boolean validarSpinners() {
        if (spGeneroEstMOD.getSelectedItem() == null || spGeneroEstMOD.getSelectedItem().toString().isEmpty()) {
            Toast.makeText(this, "Debe seleccionar un género.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (spProvinciaEstMOD.getSelectedItem() == null || spProvinciaEstMOD.getSelectedItem().toString().isEmpty()) {
            Toast.makeText(this, "Debe seleccionar una provincia.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (spLocalidadEstMOD.getSelectedItem() == null || spLocalidadEstMOD.getSelectedItem().toString().isEmpty()) {
            Toast.makeText(this, "Debe seleccionar una localidad.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (spNivelEducativoEstMOD.getSelectedItem() == null || spNivelEducativoEstMOD.getSelectedItem().toString().isEmpty()) {
            Toast.makeText(this, "Debe seleccionar un nivel educativo.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (spEstadoEducativoEstMOD.getSelectedItem() == null || spEstadoEducativoEstMOD.getSelectedItem().toString().isEmpty()) {
            Toast.makeText(this, "Debe seleccionar un estado educativo.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
