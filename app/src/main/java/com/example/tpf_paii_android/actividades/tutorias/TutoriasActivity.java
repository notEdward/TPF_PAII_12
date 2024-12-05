package com.example.tpf_paii_android.actividades.tutorias;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.tpf_paii_android.MyApp;
import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.actividades.menu_header.MenuHamburguesaActivity;
import com.example.tpf_paii_android.adapters.TutoriasAdapter;
import com.example.tpf_paii_android.viewmodels.TutoriasViewModel;

import java.util.HashMap;
import java.util.Map;


public class TutoriasActivity extends MenuHamburguesaActivity {

    private RecyclerView recyclerViewTutorias;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TutoriasViewModel tutoriasViewModel;

    private TutoriasAdapter tutoriasAdapter;

    private String nombreUsuario;
    private String nombreTipoUsuario;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorias);

        TextView txtNombre = findViewById(R.id.txtNombre);
        TextView txtTipoUsuario = findViewById(R.id.txtTipoUsuario);

        // Obtengo datos de usuario desde MyApp
        MyApp app = (MyApp) getApplication();
        int idUsuario = app.getIdUsuario();
        nombreUsuario = app.getNombreUsuario();
        nombreTipoUsuario = app.getNombreTipoUsuario();

        // Config TextView
        txtNombre.setText(nombreUsuario);
        txtTipoUsuario.setText(nombreTipoUsuario);

        // Config Menu Hamburguesa
        setupDrawer(nombreUsuario, nombreTipoUsuario);

        // Inicia vistas comunes
        inicializarVistasComunes();

        // Configura la vista segun tipoUsuario
        if ("Estudiante".equals(nombreTipoUsuario)) {
            configVistaEstudiante(idUsuario);
        } else if ("Tutor".equals(nombreTipoUsuario)) {
            configVistaTutor(idUsuario);
        }
    }

    // VISTAS COMUNES
    private void inicializarVistasComunes() {
        recyclerViewTutorias = findViewById(R.id.recyclerViewTutorias);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutTutorias);
        recyclerViewTutorias.setLayoutManager(new LinearLayoutManager(this));
        tutoriasViewModel = new ViewModelProvider(this).get(TutoriasViewModel.class);
    }

    // Usuario ESTUDIANTE
    private void configVistaEstudiante(int idUsuario) {

        // Mapa estudiantes
        Map<String, String> estudianteMap = obtenerEstudiantesMap();

        // Config Adapter
        tutoriasAdapter = new TutoriasAdapter(estudianteMap);
        recyclerViewTutorias.setAdapter(tutoriasAdapter);

        // Observa cursos
        tutoriasViewModel.getCursosLiveData().observe(this, cursos -> {
            swipeRefreshLayout.setRefreshing(false);
            if (cursos == null || cursos.isEmpty()) {
                Toast.makeText(this, "No estas registrado en ningun curso.", Toast.LENGTH_SHORT).show();
            } else {
                tutoriasAdapter.setCursos(cursos);
            }
        });

        // Observa errores
        observarErrores();

        // SwipeRefresh Listener
        swipeRefreshLayout.setOnRefreshListener(() -> tutoriasViewModel.cargarCursosPorEstudiante(idUsuario));

        // Carga inicial
        swipeRefreshLayout.setRefreshing(true);
        tutoriasViewModel.cargarCursosPorEstudiante(idUsuario);
    }


    // Usuario TUTOR
    private void configVistaTutor(int idUsuario) {

        // Mapa estudiantes
        Map<String, String> estudianteMap = obtenerEstudiantesMap();

        // Config Adapter
        tutoriasAdapter = new TutoriasAdapter(estudianteMap);
        recyclerViewTutorias.setAdapter(tutoriasAdapter);

        // Observa las tutorías asignadas
        tutoriasViewModel.getTutoriasAsignadasLiveData().observe(this, tutorias -> {
            swipeRefreshLayout.setRefreshing(false);
            if (tutorias == null || tutorias.isEmpty()) {
                Toast.makeText(this, "No tenes tutorias asignadas.", Toast.LENGTH_SHORT).show();
                return;
            }else {
                tutoriasAdapter.setTutorias(tutorias);
            }
        });

        // Observa errores
        observarErrores();

        // SwipeRefresh Listener
        swipeRefreshLayout.setOnRefreshListener(() -> tutoriasViewModel.cargarTutoriasPorTutor(idUsuario));

        // Carga inicial
        swipeRefreshLayout.setRefreshing(true);
        tutoriasViewModel.cargarTutoriasPorTutor(idUsuario);
    }


    // Método para observar Errores
    private void observarErrores() {
        tutoriasViewModel.getErrorLiveData().observe(this, error -> {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show();
        });
    }


    // Método para obtener el mapa de estudiantes vacio
    private Map<String, String> obtenerEstudiantesMap() {
        return new HashMap<>();
    }
}
