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


        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarEmpresa();
            }
        });

        fetchProvincias();

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
                provinciaAdapter = new ArrayAdapter<>(RegistrarEmpresa.this, android.R.layout.simple_spinner_item, listaProvincias);
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
                localidadAdapter = new ArrayAdapter<>(RegistrarEmpresa.this, android.R.layout.simple_spinner_item, listaLocalidades);
                localidadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spLocalidad.setAdapter(localidadAdapter);
            });
        });
    }


    public void registrarEmpresa(){

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

        if(nombreEmpresa.isEmpty() || descripcion.isEmpty() || sector.isEmpty() || nIdentificacionFiscal.isEmpty() ||
        nombreUsuario.isEmpty() || contrasena.isEmpty() || repetirContrasena.isEmpty() || email.isEmpty() ||
        telefono.isEmpty() || direccion.isEmpty() || (spProvincia.getSelectedItemPosition()==0) ){
            Toast.makeText(RegistrarEmpresa.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!contrasena.equals(repetirContrasena)) {
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            txtContrasena.requestFocus();
            return;
        }

        Localidad Loc = (Localidad) spLocalidad.getSelectedItem();
        int idLocSelec = Loc.getId_localidad();

        Usuario user = new Usuario(nombreUsuario,contrasena,2);
        Empresa emp = new Empresa(nombreEmpresa, descripcion,sector, nIdentificacionFiscal, email, telefono, direccion, idLocSelec,user);

        EmpresaRepository er = new EmpresaRepository();
        UsuarioRepository ur = new UsuarioRepository();

        int idUsuario = ur.registrarUsuario(user);
        if (idUsuario != -1) {
            //Toast.makeText(this, "Usuario registrado con éxito.", Toast.LENGTH_SHORT).show();
            if(idUsuario ==0){
                Toast.makeText(this, "El usuario ya existe!", Toast.LENGTH_SHORT).show();
                txtNombreUser.requestFocus();
                return;
            }
            if (er.registrarEmpresa(emp,idUsuario)) {
                Toast.makeText(this, "Empresa registrada con éxito.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(this, "Error al registrar la empresa", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Error al registrar el usuario.", Toast.LENGTH_SHORT).show();
        }
    }
}