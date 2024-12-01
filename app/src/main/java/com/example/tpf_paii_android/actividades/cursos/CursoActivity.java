package com.example.tpf_paii_android.actividades.cursos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tpf_paii_android.MyApp;
import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.actividades.menu_header.MenuHamburguesaActivity;
import com.example.tpf_paii_android.adapters.CursoAdapter;
import com.example.tpf_paii_android.viewmodels.CursoViewModel;
import android.text.TextWatcher;
import java.util.ArrayList;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class CursoActivity extends MenuHamburguesaActivity {

    private RecyclerView recyclerViewCursos;
    private CursoAdapter cursoAdapter;
    private CursoViewModel cursoViewModel;
    private Button btnFiltrar;
    private Button btnCrear, btnMisCursos;
    private EditText editTextBuscar;
    private static final int REQUEST_FILTRO = 1;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int idUsuario;
    private String nombreUsuario;
    private String tipo_usuario;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso);

        MyApp app = (MyApp) getApplication();
        int idUsuario = app.getIdUsuario();
        tipo_usuario = app.getTipoUsuario();
        String nombreUsuario = app.getNombreUsuario();

        setupDrawer(nombreUsuario, tipo_usuario);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        //inicializaciones + configs
        cursoViewModel = new ViewModelProvider(this).get(CursoViewModel.class);
        recyclerViewCursos = findViewById(R.id.recyclerViewCursos);
        btnFiltrar = findViewById(R.id.btnFiltrar);
        btnCrear = findViewById(R.id.btnCrear); // admin
        btnMisCursos = findViewById(R.id.btnMisCursos);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2); // 2 columnas
        recyclerViewCursos.setLayoutManager(gridLayoutManager);
        recyclerViewCursos.setHasFixedSize(true);
        cursoAdapter = new CursoAdapter(new ArrayList<>(), tipo_usuario);
        recyclerViewCursos.setAdapter(cursoAdapter);

        swipeRefreshLayout.setOnRefreshListener(() -> cursoViewModel.cargarCursos());

        if ("ADMIN".equals(tipo_usuario.toUpperCase())) {
            btnCrear.setVisibility(View.VISIBLE);
        }
        if (!"Estudiante".equalsIgnoreCase(tipo_usuario)) {
            btnMisCursos.setVisibility(View.GONE);
        }
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
        ////
        btnMisCursos.setOnClickListener(v -> {
            Intent intent = new Intent(CursoActivity.this, MisCursosActivity.class);
            startActivity(intent);
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
            swipeRefreshLayout.setRefreshing(false);
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
