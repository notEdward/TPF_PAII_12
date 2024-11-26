package com.example.tpf_paii_android.actividades.registracion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.actividades.autenticacion.Login;
import com.example.tpf_paii_android.modelos.Empresa;
import com.example.tpf_paii_android.modelos.EstadoNivelEducativo;
import com.example.tpf_paii_android.modelos.Estudiante;
import com.example.tpf_paii_android.modelos.ExperienciaLaboral;
import com.example.tpf_paii_android.modelos.Genero;
import com.example.tpf_paii_android.modelos.Localidad;
import com.example.tpf_paii_android.modelos.NivelEducativo;
import com.example.tpf_paii_android.modelos.Provincia;
import com.example.tpf_paii_android.modelos.Usuario;
import com.example.tpf_paii_android.repositorios.EmpresaRepository;
import com.example.tpf_paii_android.repositorios.EstadoEducativoRepository;
import com.example.tpf_paii_android.repositorios.EstudianteRepository;
import com.example.tpf_paii_android.repositorios.ExpLaboralRepository;
import com.example.tpf_paii_android.repositorios.GeneroRepository;
import com.example.tpf_paii_android.repositorios.LocalidadRepository;
import com.example.tpf_paii_android.repositorios.NivelEducativoRepository;
import com.example.tpf_paii_android.repositorios.ProvinciaRepository;
import com.example.tpf_paii_android.repositorios.UsuarioRepository;
import com.example.tpf_paii_android.repositorios.generosRepository;
import com.example.tpf_paii_android.viewmodels.EstudianteViewModel;
import com.example.tpf_paii_android.viewmodels.TutorViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegistrarEstudiante extends AppCompatActivity {

    private EstudianteViewModel estudianteViewModel;

    private EditText txtNombre, txtApellido, txtDni, txtNombreUser, txtContrasena, txtRepetirContrasena, txtEmail,
                     txtTelefono, txtDireccion, txtLugar, txtCargo, txtTareas, txtDuracion;
    private Spinner  spGenero, spProvincia, spLocalidad, spNivelEducativo, spEstado;
    private Button btnRegistrarse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_estudiante);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtNombre = findViewById(R.id.txtNombreEst);
        txtApellido = findViewById(R.id.txtApellidoEst);
        txtDni = findViewById(R.id.txtDniEst);
        txtNombreUser = findViewById(R.id.txtNombreUserEst);
        txtContrasena = findViewById(R.id.txtContrasenaEst);
        txtRepetirContrasena = findViewById(R.id.txtRepetirContrasenaEst);
        txtEmail = findViewById(R.id.txtEmailEst);
        txtTelefono = findViewById(R.id.txtTelefonoEst);
        txtDireccion = findViewById(R.id.txtDireccionEst);
        txtLugar = findViewById(R.id.txtLugarTabajoEst);
        txtCargo = findViewById(R.id.txtCargoEst);
        txtTareas = findViewById(R.id.txtTareasEst);
        txtDuracion = findViewById(R.id.txtDuracionEst);

        spProvincia = findViewById(R.id.spProvinciaEst);
        spLocalidad = findViewById(R.id.spLocalidadEst);
        spGenero = findViewById(R.id.spGeneroEst);
        spNivelEducativo = findViewById(R.id.spNivelEducativoEst);
        spEstado = findViewById(R.id.spEstadoEducativoEst);
        btnRegistrarse = findViewById(R.id.btnRegistrarseEst);


        // Configurar el ViewModel
        EstudianteRepository estudianteRepository = new EstudianteRepository(this);
        EstudianteViewModel.Factory factory = new EstudianteViewModel.Factory(estudianteRepository);
        estudianteViewModel = new ViewModelProvider(this, factory).get(EstudianteViewModel.class);

        // Observar los datos de los géneros
        estudianteViewModel.getGeneros().observe(this, generos -> {
            if (generos != null) {
                cargarSpinnerGenero(generos);
            }
        });

        // Observar los datos de los getNivelesEducativos
        estudianteViewModel.getNivelesEducativos().observe(this, nivelEducativos -> {
            if (nivelEducativos != null) {
                cargarSpinnerNivelesEduc(nivelEducativos);
            }
        });

        // Observar los datos de los getEstadoNivelesEducativos
        estudianteViewModel.getEstadoNivelesEducativos().observe(this, estadoNivelEducativos -> {
            if (estadoNivelEducativos != null) {
                cargarSpinnerEstadoNivelesEduc(estadoNivelEducativos);
            }
        });

        // Observar los datos de los getProvincias
        estudianteViewModel.getProvincias().observe(this, new Observer<List<Provincia>>() {
            @Override
            public void onChanged(List<Provincia> provincias) {
                if (provincias != null) cargarSpinnerProvincia(provincias);
            }
        });

        // Observar las localidades basadas en la provincia seleccionada
        estudianteViewModel.getLocalidades().observe(this, new Observer<List<Localidad>>() {
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
                if (provinciaSeleccionada.getId_provincia() != 0) {
                    // Cambiar las localidades cuando se selecciona una provincia válida
                    estudianteViewModel.setProvinciaSeleccionada(provinciaSeleccionada.getId_provincia());
                } else {
                    // Limpiar el Spinner de localidades si no hay provincia válida
                    cargarSpinnerLocalidad(new ArrayList<>());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Opcional: manejar si no se selecciona nada
            }
        });


        btnRegistrarse.setOnClickListener(v -> registrarEstudiante());
    }




    // Método para cargar los géneros en el Spinner
    private void cargarSpinnerGenero(List<Genero> generos) {
        // Crear un adaptador para el Spinner
        ArrayAdapter<Genero> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, generos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGenero.setAdapter(adapter);
    }

    // Método para cargar los nivelesEducativos en el Spinner
    private void cargarSpinnerNivelesEduc(List<NivelEducativo> nivelEducativos) {
        // Crear un adaptador para el Spinner
        ArrayAdapter<NivelEducativo> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nivelEducativos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNivelEducativo.setAdapter(adapter);
    }

    // Método para cargar los nivelesEducativos en el Spinner
    private void cargarSpinnerEstadoNivelesEduc(List<EstadoNivelEducativo> estadoNivelEducativos) {
        // Crear un adaptador para el Spinner
        ArrayAdapter<EstadoNivelEducativo> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, estadoNivelEducativos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEstado.setAdapter(adapter);
    }

    // Método para cargar los nivelesEducativos en el Spinner
    private void cargarSpinnerProvincia(List<Provincia> provincias) {
        // Crear un adaptador para el Spinner
        ArrayAdapter<Provincia> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, provincias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProvincia.setAdapter(adapter);
    }

    // Método para cargar los nivelesEducativos en el Spinner
    private void cargarSpinnerLocalidad(List<Localidad> localidades) {
        // Crear un adaptador para el Spinner
        ArrayAdapter<Localidad> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, localidades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLocalidad.setAdapter(adapter);
    }


    public void registrarEstudiante(){

        String Dni = txtDni.getText().toString();
        String nombre = txtNombre.getText().toString();
        String apellido = txtApellido.getText().toString();
        String nombreUsuario = txtNombreUser.getText().toString();
        String contrasena = txtContrasena.getText().toString();
        String repetirContrasena = txtRepetirContrasena.getText().toString();
        String email = txtEmail.getText().toString();
        String telefono = txtTelefono.getText().toString();
        String direccion = txtDireccion.getText().toString();

        String lugar = txtLugar.getText().toString();
        String cargo = txtCargo.getText().toString();
        String tarea = txtTareas.getText().toString();
        String duracion = txtDuracion.getText().toString();



        if (nombre.isEmpty() || apellido.isEmpty() || Dni.isEmpty() || nombreUsuario.isEmpty() || contrasena.isEmpty() ||
           repetirContrasena.isEmpty() || email.isEmpty() || telefono.isEmpty() || direccion.isEmpty() ||
           lugar.isEmpty() || cargo.isEmpty() || tarea.isEmpty() || duracion.isEmpty() ||
            (spLocalidad.getSelectedItem()==null) || (spProvincia.getSelectedItemPosition()==0) || (spGenero.getSelectedItemPosition()==0) ||
            (spNivelEducativo.getSelectedItemPosition()==0) || (spEstado.getSelectedItemPosition()==0) ) {

            Toast.makeText(RegistrarEstudiante.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!contrasena.equals(repetirContrasena)) {
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            txtContrasena.requestFocus();
            return;
        }

        Genero genero = (Genero) spGenero.getSelectedItem();
        int idGenero = genero.getId_genero();
        Localidad Loc = (Localidad) spLocalidad.getSelectedItem();
        int idLocalidad = Loc.getId_localidad();
        NivelEducativo nivelEdu = (NivelEducativo) spNivelEducativo.getSelectedItem();
        int idNivelEdu = nivelEdu.getId_nivelEducativo();
        EstadoNivelEducativo estado = (EstadoNivelEducativo) spEstado.getSelectedItem();
        int idEstadoEdu = estado.getId_estadoNivelEducativo();

        Usuario user = new Usuario(nombreUsuario,contrasena,1);
        Estudiante est = new Estudiante(Dni, user, nombre, apellido, idGenero, email, telefono,
                                       direccion, idLocalidad, idNivelEdu, idEstadoEdu);

        // Registrar el usuario primero
        UsuarioRepository ur = new UsuarioRepository();
        int idUsuario = ur.registrarUsuario(user);

        if (idUsuario != -1) {
            if (idUsuario == 0) {
                Toast.makeText(this, "El usuario ya existe!", Toast.LENGTH_SHORT).show();
                return;
            }

            ExperienciaLaboral expLaboral = new ExperienciaLaboral(user, lugar, cargo, tarea, duracion);
            ExpLaboralRepository expLaboralRepository = new ExpLaboralRepository();

            if (expLaboralRepository.registrarExpLaboral(expLaboral, idUsuario)) {
                // Usar el ViewModel para registrar al estudiante
                estudianteViewModel.registrarEstudiante(est, idUsuario).observe(this, registrado -> {
                    if (registrado) {
                        Toast.makeText(this, "Estudiante registrado con éxito.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, Login.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Error al registrar el estudiante.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Error al registrar la experiencia laboral.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Error al registrar el usuario.", Toast.LENGTH_SHORT).show();
        }
    }
}