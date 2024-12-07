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
import com.example.tpf_paii_android.modelos.Localidad;
import com.example.tpf_paii_android.modelos.Provincia;
import com.example.tpf_paii_android.modelos.Usuario;
import com.example.tpf_paii_android.repositorios.EmpresaRepository;

import com.example.tpf_paii_android.repositorios.UsuarioRepository;
import com.example.tpf_paii_android.viewmodels.EmpresaViewModel;

import java.util.List;

public class RegistrarEmpresa extends AppCompatActivity {

    private EmpresaViewModel empresaViewModel;
    private EditText txtNombre, txtDescripcion, txtSector, txtNidentificacion, txtNombreUser, txtContrasena,
            txtRepetirContrasena, txtEmail, txtTelefono, txtDireccion;
    private Spinner spProvincia, spLocalidad;
    private Button btnRegistrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_empresa);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtNombre = findViewById(R.id.txtNombreEmp);
        txtDescripcion = findViewById(R.id.txtDescripcionEmp);
        txtSector = findViewById(R.id.txtSectorEmp);
        txtNidentificacion = findViewById(R.id.txtNidentificacionEmp);
        txtNombreUser = findViewById(R.id.txtNombreUserEmp);
        txtContrasena = findViewById(R.id.txtContrasenaEmp);
        txtRepetirContrasena = findViewById(R.id.txtRepetirContrasenaEmp);
        txtEmail = findViewById(R.id.txtEmailEmp);
        txtTelefono = findViewById(R.id.txtTelefonoEmp);
        txtDireccion = findViewById(R.id.txtDireccionEmp);
        spProvincia = findViewById(R.id.spProvinciaEmp);
        spLocalidad = findViewById(R.id.spLocalidadEmp);
        btnRegistrarse = findViewById(R.id.btnRegistrarseEmp);

        // Configurar el ViewModel
        EmpresaRepository empresaRepository = new EmpresaRepository(this);
        EmpresaViewModel.Factory factory = new EmpresaViewModel.Factory(empresaRepository);
        empresaViewModel = new ViewModelProvider(this, factory).get(EmpresaViewModel.class);

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

        btnRegistrarse.setOnClickListener(v -> registrarEmpresa());
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

    public void registrarEmpresa() {

        if (!validarCampos()) return;

        // Crear los objetos necesarios
        Empresa empresa = crearEmpresa();
        Usuario usuario = empresa.getId_usuario();

        int idUsuario = registrarUsuario(usuario);
        if (idUsuario == 0) {
            mostrarMensaje("El usuario ya existe!");
            return;
        } else if (idUsuario == -1) {
            mostrarMensaje("Error al registrar el usuario.");
            return;
        }

        registrarEmpresaEnViewModel(empresa, idUsuario);
    }

    private boolean validarCampos() {
        if (txtNombre.getText().toString().isEmpty() || txtDescripcion.getText().toString().isEmpty() ||
            txtSector.getText().toString().isEmpty() || txtNidentificacion.getText().toString().isEmpty() ||
            txtNombreUser.getText().toString().isEmpty() || txtContrasena.getText().toString().isEmpty() ||
            txtRepetirContrasena.getText().toString().isEmpty() || txtEmail.getText().toString().isEmpty() ||
            txtTelefono.getText().toString().isEmpty() || txtDireccion.getText().toString().isEmpty() ||
            spProvincia.getSelectedItemPosition() == 0) {

            mostrarMensaje("Por favor, complete todos los campos.");
            return false;
        }

        if (txtNombre.getText().toString().length() > 50) {
            txtNombre.setError("El nombre de la empresa no puede exceder los 50 caracteres.");
            return false;
        }

        if (txtDescripcion.getText().toString().length() > 200) {
            txtDescripcion.setError("La descripción no puede exceder los 200 caracteres.");
            return false;
        }

        if (txtSector.getText().toString().length() > 50) {
            txtSector.setError("El sector no puede exceder los 50 caracteres.");
            return false;
        }

        if (txtNidentificacion.getText().toString().length() > 20) {
            txtNidentificacion.setError("La identificación fiscal no puede exceder los 20 caracteres.");
            return false;
        }

        if (txtEmail.getText().toString().length() > 50) {
            txtEmail.setError("El email no puede exceder los 50 caracteres.");
            return false;
        }

        String email = txtEmail.getText().toString().trim();
        if (!email.endsWith(".com")) {
            txtEmail.setError("El email debe terminar con '.com'.");
            return false;
        }

        if (txtTelefono.getText().toString().length() > 20) {
            txtTelefono.setError("El teléfono no puede exceder los 20 caracteres.");
            return false;
        }

        if (txtDireccion.getText().toString().length() > 150) {
            txtDireccion.setError("La dirección no puede exceder los 150 caracteres.");
            return false;
        }

        if (!txtContrasena.getText().toString().equals(txtRepetirContrasena.getText().toString())) {
            mostrarMensaje("Las contraseñas no coinciden.");
            txtContrasena.requestFocus();
            return false;
        }
        return true;
    }

    private Empresa crearEmpresa() {
        String nombreEmpresa = txtNombre.getText().toString();
        String descripcion = txtDescripcion.getText().toString();
        String sector = txtSector.getText().toString();
        String nIdentificacionFiscal = txtNidentificacion.getText().toString();
        String email = txtEmail.getText().toString();
        String telefono = txtTelefono.getText().toString();
        String direccion = txtDireccion.getText().toString();

        Localidad localidad = (Localidad) spLocalidad.getSelectedItem();
        int idLocalidadSeleccionada = localidad.getId_localidad();

        String nombreUsuario = txtNombreUser.getText().toString();
        String contrasena = txtContrasena.getText().toString();
        Usuario usuario = new Usuario(nombreUsuario, contrasena, 2);

        return new Empresa(nombreEmpresa, descripcion, sector, nIdentificacionFiscal, email, telefono, direccion, idLocalidadSeleccionada, usuario);
    }

    private int registrarUsuario(Usuario usuario) {
        UsuarioRepository usuarioRepository = new UsuarioRepository();
        return usuarioRepository.registrarUsuario(usuario);
    }

    private void registrarEmpresaEnViewModel(Empresa empresa, int idUsuario) {
        empresaViewModel.registrarEmpresa(empresa, idUsuario).observe(this, registrado -> {
            if (registrado) {
                mostrarMensaje("Empresa registrado con éxito.");
                irALogin();
            } else {
                mostrarMensaje("Error al registrar Empresa.");
            }
        });
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void irALogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}