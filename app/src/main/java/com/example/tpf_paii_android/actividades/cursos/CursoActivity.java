package com.example.tpf_paii_android.actividades.cursos;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.adapters.CursoAdapter;
import com.example.tpf_paii_android.viewmodels.CursoViewModel;
import android.text.TextWatcher;
import java.util.ArrayList;

public class CursoActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCursos;
    private CursoAdapter cursoAdapter;
    private CursoViewModel cursoViewModel;
    private Button btnFiltrar;
    private EditText editTextBuscar;
    private static final int REQUEST_FILTRO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso);
        //inicializaciones + configs
        cursoViewModel = new ViewModelProvider(this).get(CursoViewModel.class);
        recyclerViewCursos = findViewById(R.id.recyclerViewCursos);
        btnFiltrar = findViewById(R.id.btnFiltrar);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2); // 2 columnas
        recyclerViewCursos.setLayoutManager(gridLayoutManager);
        recyclerViewCursos.setHasFixedSize(true);
        cursoAdapter = new CursoAdapter(new ArrayList<>());
        recyclerViewCursos.setAdapter(cursoAdapter);

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
        // filtro cursos (escucho "en vivo")
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
        // ver filtros disponibles
        btnFiltrar.setOnClickListener(v -> {
            Intent intent = new Intent(CursoActivity.this, FiltroCursoActivity.class);
            startActivityForResult(intent, REQUEST_FILTRO); // Código de solicitud para el filtro
        });
        // filtro de cursos
        cursoViewModel.getCursosFiltrados().observe(this, cursos -> {
            if (cursos != null) {
                cursoAdapter.actualizarCursos(cursos);
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
}
