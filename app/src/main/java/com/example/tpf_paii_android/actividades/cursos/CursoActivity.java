package com.example.tpf_paii_android.actividades.cursos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tpf_paii_android.MyApp;
import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.actividades.menu_header.MenuHamburguesaActivity;
import com.example.tpf_paii_android.actividades.ofertas.OfertaActivity;
import com.example.tpf_paii_android.adapters.CursoAdapter;
import com.example.tpf_paii_android.viewmodels.CursoViewModel;
import com.google.android.material.navigation.NavigationView;

import android.text.TextWatcher;
import java.util.ArrayList;

public class CursoActivity extends MenuHamburguesaActivity {

    private RecyclerView recyclerViewCursos;
    private CursoAdapter cursoAdapter;
    private CursoViewModel cursoViewModel;
    private Button btnFiltrar;
    private Button btnCrear;
    private EditText editTextBuscar;
    private static final int REQUEST_FILTRO = 1;
    private DrawerLayout drawerLayout;

    private int idUsuario;
    private String nombreUsuario;
    private String tipo_usuario;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso);

        //simulo recibir los datos del login
        // Recuperar los datos del usuario
        //Intent intent = getIntent();
//        idUsuario = 1; // intent.getIntExtra("id_usuario", -1);
//        nombreUsuario = "prueba";//intent.getStringExtra("nombre_usuario");
//        tipo_usuario = "admin";
        ///
        // Trato de traer los valores de la actividad anterior, sino pongo por default
//        Intent initIntent = getIntent();
//        idUsuario = initIntent.hasExtra("idUsuario") ? initIntent.getIntExtra("idUsuario", 1) : 1;
//        nombreUsuario = initIntent.hasExtra("nombreUsuario") ? initIntent.getStringExtra("nombreUsuario") : "admin";
//        tipo_usuario = initIntent.hasExtra("tipo_usuario") ? initIntent.getStringExtra("tipo_usuario") : "admin";
//        setupDrawer(nombreUsuario, tipo_usuario);
        MyApp app = (MyApp) getApplication();
        int idUsuario = app.getIdUsuario();
        tipo_usuario = app.getTipoUsuario();
        String nombreUsuario = app.getNombreUsuario();

        setupDrawer(nombreUsuario, tipo_usuario);

        //inicializaciones + configs
        cursoViewModel = new ViewModelProvider(this).get(CursoViewModel.class);
        recyclerViewCursos = findViewById(R.id.recyclerViewCursos);
        btnFiltrar = findViewById(R.id.btnFiltrar);
        btnCrear = findViewById(R.id.btnCrear); // admin
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2); // 2 columnas
        recyclerViewCursos.setLayoutManager(gridLayoutManager);
        recyclerViewCursos.setHasFixedSize(true);
        cursoAdapter = new CursoAdapter(new ArrayList<>(), tipo_usuario);
        recyclerViewCursos.setAdapter(cursoAdapter);

        if ("admin".equals(tipo_usuario)) {
            btnCrear.setVisibility(View.VISIBLE);
        }
        //menu hamburguesa
//        setupDrawer(nombreUsuario, tipo_usuario);

        ////FILTROS POR LUPITA
        editTextBuscar = findViewById(R.id.editTextBuscar); // campo de busq
        cursoViewModel.getCursosFiltrados().observe(this, cursos -> {
            if (cursos != null) {
                cursoAdapter.actualizarCursos(cursos);
            }
        });
        cursoViewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(getApplicationContext(), "Error al cargar los cursos", Toast.LENGTH_SHORT).show();
            }
        });
        // live listenr
        editTextBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cursoViewModel.filtrarCursos(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        ////////
        // ver filtros disponibles listeners
        btnFiltrar.setOnClickListener(v -> {
            Intent intent = new Intent(CursoActivity.this, FiltroCursoActivity.class);
            startActivityForResult(intent, REQUEST_FILTRO); //cod q recibo del filtro
        });
        // filtro de cursos
        cursoViewModel.getCursosFiltrados().observe(this, cursos -> {
            if (cursos != null) {
                cursoAdapter.actualizarCursos(cursos);
            }
        });
        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CursoActivity.this, CrearCursoActivity.class);
                startActivity(intent);
            }
        });

        // Observando el LiveData para saber si la baja fue exitosa
        cursoViewModel.getBajaCursoLiveData().observe(this, result -> {
            if (result != null) {
                if (result) {
                    Toast.makeText(CursoActivity.this, "Curso dado de baja", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CursoActivity.this, "Error al dar de baja el curso", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cursoViewModel.cargarCursos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_FILTRO && resultCode == RESULT_OK && data != null) {
            ArrayList<Integer> categoriasSeleccionadas = data.getIntegerArrayListExtra("categoriasSeleccionadas");
            if (categoriasSeleccionadas != null) {
                cursoViewModel.filtrarPorCategorias(categoriasSeleccionadas);
            }
        }
    }

    public void darDeBajaCurso(int idCurso) {
        cursoViewModel.actualizarEstadoCurso(idCurso, 0); // Cambiar el estado a 0
    }

}
