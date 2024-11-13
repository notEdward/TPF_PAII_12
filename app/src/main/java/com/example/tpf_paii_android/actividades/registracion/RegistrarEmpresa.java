package com.example.tpf_paii_android.actividades.registracion;

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
import com.example.tpf_paii_android.modelos.Empresa;
import com.example.tpf_paii_android.modelos.Localidad;
import com.example.tpf_paii_android.modelos.Provincia;
import com.example.tpf_paii_android.modelos.Usuario;
import com.example.tpf_paii_android.repositorios.EmpresaRepository;
import com.example.tpf_paii_android.repositorios.LocalidadRepository;
import com.example.tpf_paii_android.repositorios.ProvinciaRepository;
import com.example.tpf_paii_android.repositorios.UsuarioRepository;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegistrarEmpresa extends AppCompatActivity {

    private EditText txtNombre, txtDescripcion, txtSector, txtNidentificacion,
            txtNombreUser, txtContrasena, txtRepetirContrasena,
            txtEmail, txtTelefono, txtDireccion;
    private Spinner spProvincia, spLocalidad;
    private Button btnRegistrarse;

    private ArrayAdapter<Provincia> provinciaAdapter;
    private ArrayAdapter<Localidad> localidadAdapter;
    private boolean isInitialLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar_empresa);

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

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegistrarEmpresa.this, "click.", Toast.LENGTH_SHORT).show();
                registrarEmpresa();
            }
        });

        fetchProvincias();

        // Establecer un listener para el Spinner de provincias
        spProvincia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (!isInitialLoad) {
                    Provincia provinciaSeleccionada = (Provincia) spProvincia.getSelectedItem();
                    if (provinciaSeleccionada != null) {
                        fetchLocalidadesByProvincia(provinciaSeleccionada.getId_provincia());
                    }
                } else {
                    isInitialLoad = false; // Marca que la carga inicial ya pasó
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No hacer nada si no se selecciona nada
            }
        });

    }

    public void fetchProvincias() {
        // Ejecutar la consulta en un hilo secundario
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {

            ProvinciaRepository provinciaRepository = new ProvinciaRepository();
            ArrayList<Provincia> listaProvincias = provinciaRepository.fetchProvincias();

            // Actualizar la UI en el hilo principal
            new Handler(Looper.getMainLooper()).post(() -> {
                provinciaAdapter = new ArrayAdapter<>(RegistrarEmpresa.this, android.R.layout.simple_spinner_item, listaProvincias);
                provinciaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spProvincia.setAdapter(provinciaAdapter);
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
                localidadAdapter = new ArrayAdapter<>(RegistrarEmpresa.this, android.R.layout.simple_spinner_item, listaLocalidades);
                localidadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spLocalidad.setAdapter(localidadAdapter);
            });
        });
    }

    public void registrarEmpresa(){

        Toast.makeText(this, "Intentando registrar la empresa...", Toast.LENGTH_SHORT).show();

        String nombreEmpresa = txtNombre.getText().toString();
        String descripcion = txtDescripcion.getText().toString();
        String sector = txtSector.getText().toString();
        String nIdentificacionFiscal = txtNidentificacion.getText().toString();
        String nombreUsuario = txtNombreUser.getText().toString();
        String contrasena = txtContrasena.getText().toString();
        String repetirContrasena = txtRepetirContrasena.getText().toString();
        String email = txtEmail.getText().toString();
        String telefono = txtTelefono.getText().toString();
        String direccion = txtDireccion.getText().toString();

        if (!contrasena.equals(repetirContrasena)) {
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario user = new Usuario(nombreUsuario,contrasena,2);
        Empresa emp = new Empresa(nombreEmpresa, descripcion,sector, nIdentificacionFiscal, email, telefono, direccion, 2,user);

        EmpresaRepository er = new EmpresaRepository();
        UsuarioRepository ur = new UsuarioRepository();
        if(ur.registrarUsuario(user)){
            Toast.makeText(this, "usuario registrada con éxito.", Toast.LENGTH_SHORT).show();
            if(er.registrarEmpresa(emp)){
                Toast.makeText(this, "Empresa registrada con éxito.", Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(this, "Empresa error", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(this, "user error.", Toast.LENGTH_SHORT).show();
    }
}