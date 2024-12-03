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
import com.example.tpf_paii_android.modelos.Curso;
import com.example.tpf_paii_android.viewmodels.TutoriasViewModel;

import java.util.List;

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

        // Accede a MyApp
        MyApp app = (MyApp) getApplication();

        // Obtengo datos desde MyApp
        int idUsuario = app.getIdUsuario();
        nombreUsuario = app.getNombreUsuario();
        nombreTipoUsuario = app.getNombreTipoUsuario();

        // Config TextView
        txtNombre.setText(nombreUsuario);
        txtTipoUsuario.setText(nombreTipoUsuario);

        setupDrawer(nombreUsuario, nombreTipoUsuario);

        // Inicializa las vistas
        recyclerViewTutorias = findViewById(R.id.recyclerViewTutorias);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayoutTutorias);

        recyclerViewTutorias.setLayoutManager(new LinearLayoutManager(this));

        // Config ViewModel
        tutoriasViewModel = new ViewModelProvider(this).get(TutoriasViewModel.class);

        // Crea el adaptador
        tutoriasAdapter = new TutoriasAdapter();
        recyclerViewTutorias.setAdapter(tutoriasAdapter);

        // Observa LiveData de cursos
        tutoriasViewModel.getCursosLiveData().observe(this, this::mostrarCursos);

        // Observa errores
        tutoriasViewModel.getErrorLiveData().observe(this, error -> {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show();
        });

        // SwipeRefresh Listener
        swipeRefreshLayout.setOnRefreshListener(() -> tutoriasViewModel.cargarCursosPorEstudiante(idUsuario));

        // Carga inicial
        swipeRefreshLayout.setRefreshing(true);
        tutoriasViewModel.cargarCursosPorEstudiante(idUsuario);
    }

    private void mostrarCursos(List<Curso> cursos) {
        swipeRefreshLayout.setRefreshing(false);
        if (cursos == null || cursos.isEmpty()) {
            Toast.makeText(this, "No estás registrado a ningun curso.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Actualiza adaptador con la lista de cursos
        tutoriasAdapter.setCursos(cursos);
    }

}