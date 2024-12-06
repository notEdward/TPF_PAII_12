package com.example.tpf_paii_android.actividades.ofertas;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.adapters.LocalidadAdapter;
import com.example.tpf_paii_android.adapters.ModalidadAdapter;
import com.example.tpf_paii_android.adapters.NivelEducativoAdapter;
import com.example.tpf_paii_android.adapters.ProvinciaAdapter;
import com.example.tpf_paii_android.adapters.TipoEmpleoAdapter;
import com.example.tpf_paii_android.modelos.Localidad;
import com.example.tpf_paii_android.modelos.Modalidad;
import com.example.tpf_paii_android.modelos.NivelEducativo;
import com.example.tpf_paii_android.modelos.OfertaEmpleo;
import com.example.tpf_paii_android.modelos.Provincia;
import com.example.tpf_paii_android.modelos.TipoEmpleo;
import com.example.tpf_paii_android.repositorios.OfertaRepository;
import com.example.tpf_paii_android.viewmodels.OfertaViewModel;

public class ModificarOfertaActivity extends AppCompatActivity {

    private EditText etTituloOferta, etDescripcionOferta, etDireccion, etOtroRequisito;
    private Button btnGuardarCambios;
    private ImageView imgOferta;
    private Spinner spinnerTipoEmpleo, spinnerModalidad, spinnerNivelEducativo, spinnerProvincia, spinnerLocalidad;

    private OfertaViewModel viewModel;

    private int idOfertaEmpleo;
    private int imageResId;
    private ProvinciaAdapter provinciaAdapter;
    private LocalidadAdapter localidadAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_oferta);

        // Inicializar vistas
        etTituloOferta = findViewById(R.id.etTituloOferta);
        etDescripcionOferta = findViewById(R.id.etDescripcionOferta);
        etDireccion = findViewById(R.id.etDireccion);
        etOtroRequisito = findViewById(R.id.etOtroRequisito);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);
        imgOferta = findViewById(R.id.imgOferta);
        spinnerTipoEmpleo = findViewById(R.id.spinnerTipoEmpleo);
        spinnerModalidad = findViewById(R.id.spinnerModalidad);
        spinnerNivelEducativo = findViewById(R.id.spinnerNivelEducativo);
        spinnerLocalidad = findViewById(R.id.spinnerLocalidad);
        spinnerProvincia = findViewById(R.id.spinnerProvincia);

        viewModel = new ViewModelProvider(this).get(OfertaViewModel.class);

        idOfertaEmpleo = getIntent().getIntExtra("id_oferta_empleo", -1);
        String titulo = getIntent().getStringExtra("titulo");
        String descripcion = getIntent().getStringExtra("descripcion");
        imageResId = getIntent().getIntExtra("imageResId", R.drawable.img1_tpf);

        if (idOfertaEmpleo == -1) {
            Toast.makeText(this, "Error al cargar los datos de la oferta", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        viewModel.cargarOferta(idOfertaEmpleo);
        viewModel.getOfertaLiveData().observe(this, oferta -> {
            if (oferta != null) {
                etTituloOferta.setText(oferta.getTitulo());
                etDescripcionOferta.setText(oferta.getDescripcion());
                etDireccion.setText(oferta.getDireccion());
                etOtroRequisito.setText(oferta.getOtrosRequisitos());
                imgOferta.setImageResource(imageResId);

                seleccionarTipoEmpleoEnSpinner(oferta.getTipoEmpleo());
                seleccionarNivelEducativoEnSpinner(oferta.getNivelEducativo());
                seleccionarModalidadEnSpinner(oferta.getModalidad());
                seleccionarLocalidadEnSpinner(oferta.getLocalidad());
                seleccionarProvinciaEnSpinner(oferta.getLocalidad());
            }
        });
        configurarObservadores();
        cargarDatos();

        viewModel.getExitoActualizacion().observe(this, exito -> {
            if (exito != null && exito) {
                Toast.makeText(this, "Oferta actualizada correctamente", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error al actualizar la oferta", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, "Error: " + error, Toast.LENGTH_LONG).show();
            }
        });

        btnGuardarCambios.setOnClickListener(v -> guardarCambios());
    }
    private void guardarCambios() {
        // Validar los campos de texto
        String nuevoTitulo = etTituloOferta.getText().toString().trim();
        String nuevaDescripcion = etDescripcionOferta.getText().toString().trim();
        String direccion = etDireccion.getText().toString().trim();
        String otrosRequisitos = etOtroRequisito.getText().toString().trim();

        //valido que no esten vacios los strings
        if (nuevoTitulo.isEmpty() || nuevaDescripcion.isEmpty() || direccion.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }
        TipoEmpleo tipoEmpleoSeleccionado = (TipoEmpleo) spinnerTipoEmpleo.getSelectedItem();
        Modalidad modalidadSeleccionada = (Modalidad) spinnerModalidad.getSelectedItem();
        NivelEducativo nivelEducativoSeleccionado = (NivelEducativo) spinnerNivelEducativo.getSelectedItem();
        Localidad localidadSeleccionada = (Localidad) spinnerLocalidad.getSelectedItem();
        Provincia provinciaSeleccionada = (Provincia) spinnerProvincia.getSelectedItem();
        //valido spinners
        if (tipoEmpleoSeleccionado == null || modalidadSeleccionada == null ||
                nivelEducativoSeleccionado == null || localidadSeleccionada == null || provinciaSeleccionada == null) {
            Toast.makeText(this, "Por favor, selecciona todas las opciones", Toast.LENGTH_SHORT).show();
            return;
        }

        OfertaEmpleo ofertaActualizada = new OfertaEmpleo();
        ofertaActualizada.setId_ofertaEmpleo(idOfertaEmpleo);
        ofertaActualizada.setTitulo(nuevoTitulo);
        ofertaActualizada.setDescripcion(nuevaDescripcion);
        ofertaActualizada.setDireccion(direccion);
        ofertaActualizada.setOtrosRequisitos(otrosRequisitos);

        ofertaActualizada.setIdTipoEmpleo(tipoEmpleoSeleccionado.getId_tipoEmpleo());
        ofertaActualizada.setIdModalidad(modalidadSeleccionada.getId_modalidad());
        ofertaActualizada.setIdNivelEducativo(nivelEducativoSeleccionado.getId_nivelEducativo());
        ofertaActualizada.setIdLocalidad(localidadSeleccionada.getId_localidad());

        viewModel.actualizarOferta(ofertaActualizada);
    }

    private void configurarObservadores() {
        viewModel.getTiposEmpleoLiveData().observe(this, tiposEmpleo -> {
            TipoEmpleoAdapter tipoEmpleoAdapter = new TipoEmpleoAdapter(this, tiposEmpleo);
            spinnerTipoEmpleo.setAdapter(tipoEmpleoAdapter);
        });
        viewModel.getModalidadesLiveData().observe(this, modalidades -> {
            ModalidadAdapter modalidadAdapter = new ModalidadAdapter(this, modalidades);
            spinnerModalidad.setAdapter(modalidadAdapter);
        });

        viewModel.getNivelesEducativosLiveData().observe(this, nivelesEducativos -> {
            NivelEducativoAdapter nivelEducativoAdapter = new NivelEducativoAdapter(this, nivelesEducativos);
            spinnerNivelEducativo.setAdapter(nivelEducativoAdapter);
        });
        viewModel.getProvinciasLiveData().observe(this, provincias -> {
            provinciaAdapter = new ProvinciaAdapter(this, provincias);
            spinnerProvincia.setAdapter(provinciaAdapter);
        });
        viewModel.getLocalidadesLiveData().observe(this, localidades -> {
            ArrayAdapter<Localidad> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, localidades);
            spinnerLocalidad.setAdapter(adapter);
        });
        viewModel.getLocalidadesLiveData().observe(this, localidades -> {
            localidadAdapter = new LocalidadAdapter(this, localidades);
            spinnerLocalidad.setAdapter(localidadAdapter);
        });

        //cambio automatico de provincia/localidad
        spinnerProvincia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
    private void seleccionarTipoEmpleoEnSpinner(TipoEmpleo tipoEmpleoSeleccionado) {
        if (tipoEmpleoSeleccionado != null) {
            viewModel.getTiposEmpleoLiveData().observe(this, tiposEmpleo -> {
                int posicion = -1;
                for (int i = 0; i < tiposEmpleo.size(); i++) {
                    if (tiposEmpleo.get(i).getId_tipoEmpleo() == tipoEmpleoSeleccionado.getId_tipoEmpleo()) {
                        posicion = i;
                        break;
                    }
                }

                if (posicion != -1) {
                    spinnerTipoEmpleo.setSelection(posicion);
                }
            });
        }
    }
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
                    spinnerNivelEducativo.setSelection(posicion);
                }
            });
        }
    }
    private void seleccionarModalidadEnSpinner(Modalidad modalidadSeleccionado) {
        if (modalidadSeleccionado != null) {
            viewModel.getModalidadesLiveData().observe(this, modalidad -> {
                int posicion = -1;
                for (int i = 0; i < modalidad.size(); i++) {
                    if (modalidad.get(i).getId_modalidad() == modalidadSeleccionado.getId_modalidad()) {
                        posicion = i;
                        break;
                    }
                }
                if (posicion != -1) {
                    spinnerModalidad.setSelection(posicion);
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
                    spinnerLocalidad.setSelection(posicion);
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
                    spinnerProvincia.setSelection(posicion);
                }
            });
        }
    }
    private void cargarDatos() {
        viewModel.cargarTiposEmpleo();
        viewModel.cargarNivelesEducativos();
        viewModel.cargarModalidades();
        viewModel.cargarLocalidades();
        viewModel.cargarProvincias();
    }
}

