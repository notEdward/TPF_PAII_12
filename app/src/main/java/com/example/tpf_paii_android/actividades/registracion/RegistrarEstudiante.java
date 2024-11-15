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
import com.example.tpf_paii_android.viewmodels.TutorViewModel;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegistrarEstudiante extends AppCompatActivity {

    private EditText txtNombre, txtApellido, txtDni, txtNombreUser, txtContrasena, txtRepetirContrasena, txtEmail,
                     txtTelefono, txtDireccion, txtLugar, txtCargo, txtTareas, txtDuracion;
    private Spinner spGenero, spProvincia, spLocalidad, spNivelEducativo, spEstado;
    private Button btnRegistrarse;

    private ArrayAdapter<Provincia> provinciaAdapter;
    private ArrayAdapter<Localidad> localidadAdapter;
    private ArrayAdapter<Genero> generoAdapter;
    private ArrayAdapter<NivelEducativo> NivelAdapter;
    private ArrayAdapter<EstadoNivelEducativo> estadoAdapter;

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

        fetchProvincias();
        fetchGeneros();
        fetchNivelEducativo();
        fetchEstadoNivelEducativo();

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { registrarEstudiante();
            }
        });

        // Listener para el spinner de provincias
        spProvincia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position > 0) { // Verifica que no sea el primer ítem
                    Provincia provinciaSeleccionada = (Provincia) spProvincia.getSelectedItem();
                    if (provinciaSeleccionada != null) {
                        fetchLocalidadesByProvincia(provinciaSeleccionada.getId_provincia());
                    }
                } else {
                    spLocalidad.setAdapter(null); // Limpiar localidades si no se selecciona una provincia válida
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No hacer nada si no se selecciona nada
            }
        });
    }

    public void fetchGeneros() {
        // Ejecutar la consulta en un hilo secundario
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            generosRepository genero = new generosRepository();
            ArrayList<Genero> listaGeneros = genero.fetchGeneros();

            // Agregar el ítem inicial
            Genero placeholder = new Genero(0, "Seleccione Género");
            listaGeneros.add(0, placeholder);

            // Actualizar la UI en el hilo principal
            new Handler(Looper.getMainLooper()).post(() -> {
                generoAdapter = new ArrayAdapter<>(RegistrarEstudiante.this, android.R.layout.simple_spinner_item, listaGeneros);
                generoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spGenero.setAdapter(generoAdapter);
                spGenero.setSelection(0); // Seleccionar por defecto el primer ítem
            });
        });
    }

    public void fetchNivelEducativo() {
        // Ejecutar la consulta en un hilo secundario
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            NivelEducativoRepository nivel = new NivelEducativoRepository();
            ArrayList<NivelEducativo> listaNiveles = nivel.fetchNivelesEducativos();

            // Agregar el ítem inicial
            NivelEducativo placeholder = new NivelEducativo(0, "Seleccione Nivel");
            listaNiveles.add(0, placeholder);

            // Actualizar la UI en el hilo principal
            new Handler(Looper.getMainLooper()).post(() -> {
                NivelAdapter = new ArrayAdapter<>(RegistrarEstudiante.this, android.R.layout.simple_spinner_item, listaNiveles);
                NivelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spNivelEducativo.setAdapter(NivelAdapter);
                spNivelEducativo.setSelection(0); // Seleccionar por defecto el primer ítem
            });
        });
    }

    public void fetchEstadoNivelEducativo() {
        // Ejecutar la consulta en un hilo secundario
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            EstadoEducativoRepository estadoNivel = new EstadoEducativoRepository();
            ArrayList<EstadoNivelEducativo> listaEstados = estadoNivel.fetchEstadoNivelesEducativos();

            // Agregar el ítem inicial
            EstadoNivelEducativo placeholder = new EstadoNivelEducativo(0, "Seleccione Estado");
            listaEstados.add(0, placeholder);

            // Actualizar la UI en el hilo principal
            new Handler(Looper.getMainLooper()).post(() -> {
                estadoAdapter = new ArrayAdapter<>(RegistrarEstudiante.this, android.R.layout.simple_spinner_item, listaEstados);
                estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spEstado.setAdapter(estadoAdapter);
                spEstado.setSelection(0); // Seleccionar por defecto el primer ítem
            });
        });
    }


    public void fetchProvincias() {
        // Ejecutar la consulta en un hilo secundario
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            ProvinciaRepository provinciaRepository = new ProvinciaRepository();
            ArrayList<Provincia> listaProvincias = provinciaRepository.fetchProvincias();

            // Agregar el ítem inicial
            Provincia placeholder = new Provincia(0, "Seleccione Provincia");
            listaProvincias.add(0, placeholder);

            // Actualizar la UI en el hilo principal
            new Handler(Looper.getMainLooper()).post(() -> {
                provinciaAdapter = new ArrayAdapter<>(RegistrarEstudiante.this, android.R.layout.simple_spinner_item, listaProvincias);
                provinciaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spProvincia.setAdapter(provinciaAdapter);
                spProvincia.setSelection(0); // "Elegí provincia" seleccionado por defecto
            });
        });
    }

    public void fetchLocalidadesByProvincia(int provinciaId) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            LocalidadRepository localidadRepository = new LocalidadRepository();
            ArrayList<Localidad> listaLocalidades = localidadRepository.fetchLocalidadesByProvincia(provinciaId);

            // Actualizar la UI en el hilo principal
            new Handler(Looper.getMainLooper()).post(() -> {
                localidadAdapter = new ArrayAdapter<>(RegistrarEstudiante.this, android.R.layout.simple_spinner_item, listaLocalidades);
                localidadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spLocalidad.setAdapter(localidadAdapter);
            });
        });
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

        Genero genero = (Genero) spGenero.getSelectedItem();
        int idGenero = genero.getId_genero();
        Localidad Loc = (Localidad) spLocalidad.getSelectedItem();
        int idLocalidad = Loc.getId_localidad();
        NivelEducativo nivelEdu = (NivelEducativo) spNivelEducativo.getSelectedItem();
        int idNivelEdu = nivelEdu.getId_nivelEducativo();
        EstadoNivelEducativo estado = (EstadoNivelEducativo) spEstado.getSelectedItem();
        int idEstadoEdu = estado.getId_estadoNivelEducativo();

        if (nombre.isEmpty() || apellido.isEmpty() || Dni.isEmpty() || nombreUsuario.isEmpty() || contrasena.isEmpty() ||
           repetirContrasena.isEmpty() || email.isEmpty() || telefono.isEmpty() || direccion.isEmpty() ||
           lugar.isEmpty() || cargo.isEmpty() || tarea.isEmpty() || duracion.isEmpty() ||
            (spLocalidad == null) || (spProvincia.getSelectedItemPosition()==0) || (spGenero.getSelectedItemPosition()==0) ||
            (spNivelEducativo.getSelectedItemPosition()==0) || (spEstado.getSelectedItemPosition()==0) ) {

            Toast.makeText(RegistrarEstudiante.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!contrasena.equals(repetirContrasena)) {
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario user = new Usuario(nombreUsuario,contrasena,1);
        Estudiante est = new Estudiante(Dni, user, nombre, apellido, idGenero, email, telefono,
                                       direccion, idLocalidad, idNivelEdu, idEstadoEdu);

        EstudianteRepository er = new EstudianteRepository();
        UsuarioRepository ur = new UsuarioRepository();
        ExpLaboralRepository elr = new ExpLaboralRepository();

        int idUsuario = ur.registrarUsuario(user);
        if (idUsuario != -1) {
            ExperienciaLaboral expLaboral = new ExperienciaLaboral(user,lugar,cargo,tarea,duracion);
                if(elr.registrarExpLaboral(expLaboral,idUsuario)){
                    if (er.registrarEstudiante(est,idUsuario)) {
                        Toast.makeText(this, "Estudiante registrado con éxito.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, Login.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Error al registrar la Estudiante", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Error al registrar la Exp Laboral", Toast.LENGTH_SHORT).show();
                }
        } else {
            Toast.makeText(this, "Error al registrar el usuario.", Toast.LENGTH_SHORT).show();
        }
    }
}