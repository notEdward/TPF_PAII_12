package com.example.tpf_paii_android.actividades.ofertas;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.tpf_paii_android.MyApp;
import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.adapters.CursoOfertaAdapter;
import com.example.tpf_paii_android.adapters.LocalidadAdapter;
import com.example.tpf_paii_android.adapters.ModalidadAdapter;
import com.example.tpf_paii_android.adapters.NivelEducativoAdapter;
import com.example.tpf_paii_android.adapters.ProvinciaAdapter;
import com.example.tpf_paii_android.adapters.TipoEmpleoAdapter;
import com.example.tpf_paii_android.modelos.Curso;
import com.example.tpf_paii_android.modelos.Localidad;
import com.example.tpf_paii_android.modelos.Modalidad;
import com.example.tpf_paii_android.modelos.NivelEducativo;
import com.example.tpf_paii_android.modelos.OfertaEmpleo;
import com.example.tpf_paii_android.modelos.Provincia;
import com.example.tpf_paii_android.modelos.TipoEmpleo;
import com.example.tpf_paii_android.viewmodels.OfertaViewModel;

public class AltaOfertaActivity extends AppCompatActivity {
    private OfertaViewModel ofertaViewModel;

    private Spinner spinnerTipoEmpleo;
    private Spinner spinnerModalidad;
    private Spinner spinnerNivelEducativo;
    private Spinner spinnerCurso;
    private Spinner spinnerLocalidad;
    private Spinner spinnerProvincia;
    private EditText etTitulo, etDescripcion, etDireccion;
//    private Spinner spinnerTipoEmpleo, spinnerModalidad, spinnerNivelEducativo, spinnerCurso, spinnerProvincia, spinnerLocalidad;
    private Button btnCrearOferta;
    private ProvinciaAdapter provinciaAdapter;
    private LocalidadAdapter localidadAdapter;

    private int idUsuario;
    private String nombreUsuario;
    private String tipo_usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_oferta);
        ofertaViewModel = new ViewModelProvider(this).get(OfertaViewModel.class);
        etTitulo = findViewById(R.id.etTitulo);
        etDescripcion = findViewById(R.id.etDescripcion);
        etDireccion = findViewById(R.id.etDireccion);
        spinnerTipoEmpleo = findViewById(R.id.spinnerTipoEmpleo);
        spinnerModalidad = findViewById(R.id.spinnerModalidad);
        spinnerNivelEducativo = findViewById(R.id.spinnerNivelEducativo);
        spinnerCurso = findViewById(R.id.spinnerCurso);
        spinnerLocalidad = findViewById(R.id.spinnerLocalidad);
        spinnerProvincia = findViewById(R.id.spinnerProvincia);
        btnCrearOferta = findViewById(R.id.btnCrearOferta);

//        idUsuario = 4;
//        nombreUsuario = "prueba";
//        tipo_usuario = "Empresa";
        MyApp app = (MyApp) getApplication();
        idUsuario = app.getIdUsuario();
        tipo_usuario = app.getTipoUsuario();
        nombreUsuario = app.getNombreUsuario();
        // Cargar datos en los spinners
        cargarDatosSpinners();

        configurarObservadores();
        cargarDatos();
        // Cargar provincias
        ofertaViewModel.cargarProvincias();

        // Observar las provincias
        ofertaViewModel.getProvinciasLiveData().observe(this, provincias -> {
            provinciaAdapter = new ProvinciaAdapter(this, provincias);
            spinnerProvincia.setAdapter(provinciaAdapter);
        });

        // Al seleccionar una provincia, cargar las localidades
        spinnerProvincia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Provincia selectedProvincia = (Provincia) parentView.getItemAtPosition(position);
                ofertaViewModel.cargarLocalidadesPorProvincia(selectedProvincia.getId_provincia());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No hacer nada si no se selecciona una provincia
            }
        });

        // Observar las localidades y actualizar el spinner de localidades
        ofertaViewModel.getLocalidadesLiveData().observe(this, localidades -> {
            localidadAdapter = new LocalidadAdapter(this, localidades);
            spinnerLocalidad.setAdapter(localidadAdapter);
        });

        btnCrearOferta.setOnClickListener(view -> {
            // Obtener datos del formulario
            String titulo = etTitulo.getText().toString();
            String descripcion = etDescripcion.getText().toString();
            String direccion = etDireccion.getText().toString();

            // Obtener valores seleccionados de los spinners
            TipoEmpleo tipoEmpleoSeleccionado = (TipoEmpleo) spinnerTipoEmpleo.getSelectedItem();
            Modalidad modalidadSeleccionada = (Modalidad) spinnerModalidad.getSelectedItem();
            NivelEducativo nivelEducativoSeleccionado = (NivelEducativo) spinnerNivelEducativo.getSelectedItem();
            Curso cursoSeleccionado = (Curso) spinnerCurso.getSelectedItem();
            Localidad localidadSeleccionada = (Localidad) spinnerLocalidad.getSelectedItem();
            Provincia provinciaSeleccionada = (Provincia) spinnerProvincia.getSelectedItem();

            // Verificar si los campos obligatorios no están vacíos
            if (titulo.isEmpty() || descripcion.isEmpty() || direccion.isEmpty() ||
                    tipoEmpleoSeleccionado == null || modalidadSeleccionada == null || nivelEducativoSeleccionado == null ||
                    cursoSeleccionado == null || localidadSeleccionada == null || provinciaSeleccionada == null) {

                Toast.makeText(AltaOfertaActivity.this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            // Obtener el id_empresa a partir del id_usuario
            ofertaViewModel.obtenerIdEmpresaPorUsuario(idUsuario, new DataCallback<Integer>() {
                @Override
                public void onSuccess(Integer idEmpresa) {
                    // Crear un objeto OfertaEmpleo con los parámetros adaptados
                    OfertaEmpleo nuevaOferta = new OfertaEmpleo(
                            idEmpresa, // ID de la empresa obtenido
                            titulo,    // Título de la oferta
                            descripcion, // Descripción de la oferta
                            tipoEmpleoSeleccionado.getId_tipoEmpleo(), // ID del tipo de empleo
                            modalidadSeleccionada.getId_modalidad(), // ID de la modalidad
                            nivelEducativoSeleccionado.getId_nivelEducativo(), // ID del nivel educativo
                            cursoSeleccionado.getIdCurso(), // ID del curso seleccionado
                            "otros_requisitos", // Otros requisitos (puedes ajustarlo según sea necesario)
                            direccion, // Dirección de la oferta
                            localidadSeleccionada.getId_localidad() // ID de la localidad seleccionada
                    );

                    // Llamar al ViewModel para guardar la oferta
                    ofertaViewModel.guardarOferta(nuevaOferta);
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(AltaOfertaActivity.this, "Error al obtener el id_empresa: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

    }

    private void cargarDatosSpinners() {
        // Llamadas al repositorio para cargar los datos en cada Spinner
    }

    private void configurarObservadores() {
        ofertaViewModel.getTiposEmpleoLiveData().observe(this, tiposEmpleo -> {
            TipoEmpleoAdapter tipoEmpleoAdapter = new TipoEmpleoAdapter(this, tiposEmpleo);
            spinnerTipoEmpleo.setAdapter(tipoEmpleoAdapter);
        });

        ofertaViewModel.getModalidadesLiveData().observe(this, modalidades -> {
            ModalidadAdapter modalidadAdapter = new ModalidadAdapter(this, modalidades);
            spinnerModalidad.setAdapter(modalidadAdapter);
        });

        ofertaViewModel.getNivelesEducativosLiveData().observe(this, nivelesEducativos -> {
            NivelEducativoAdapter nivelEducativoAdapter = new NivelEducativoAdapter(this, nivelesEducativos);
            spinnerNivelEducativo.setAdapter(nivelEducativoAdapter);
        });

        ofertaViewModel.getCursosLiveData().observe(this, cursos -> {
            CursoOfertaAdapter cursoAdapter = new CursoOfertaAdapter(this, cursos);
            spinnerCurso.setAdapter(cursoAdapter);
        });

        ofertaViewModel.getLocalidadesLiveData().observe(this, localidades -> {
            ArrayAdapter<Localidad> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, localidades);
            spinnerLocalidad.setAdapter(adapter);
        });

        ofertaViewModel.getOfertaCreadaExitosamente().observe(this, creadaExitosamente -> {
            if (creadaExitosamente != null && creadaExitosamente) {
                Toast.makeText(AltaOfertaActivity.this, "Oferta creada exitosamente", Toast.LENGTH_SHORT).show();
                finish();  // Regresa a la actividad anterior (OfertaActivity)
            } else if (creadaExitosamente != null) {
                Toast.makeText(AltaOfertaActivity.this, "Error al crear la oferta", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarDatos() {
        ofertaViewModel.cargarTiposEmpleo();
        ofertaViewModel.cargarModalidades();
        ofertaViewModel.cargarNivelesEducativos();
        ofertaViewModel.cargarCursos();
        ofertaViewModel.cargarLocalidades();
    }
    public interface DataCallback<T> {
        void onSuccess(T data);
        void onFailure(Exception e);
    }
}
