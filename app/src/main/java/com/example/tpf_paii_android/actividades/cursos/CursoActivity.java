package com.example.tpf_paii_android.actividades.cursos;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.adapters.CursoAdapter;
import com.example.tpf_paii_android.viewmodels.CursoViewModel;

import java.util.ArrayList;

public class CursoActivity extends AppCompatActivity {

    private CursoViewModel cursoViewModel;
    private RecyclerView recyclerViewCursos;
    private CursoAdapter cursoAdapter;
    private EditText editTextBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso);

        recyclerViewCursos = findViewById(R.id.recyclerViewCursos);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2); // 2 columnas
        recyclerViewCursos.setLayoutManager(gridLayoutManager);
        recyclerViewCursos.setHasFixedSize(true);

        cursoAdapter = new CursoAdapter(new ArrayList<>());
        recyclerViewCursos.setAdapter(cursoAdapter);
        //campo de txt para buscar
        editTextBuscar = findViewById(R.id.editTextBuscar);

        cursoViewModel = new ViewModelProvider(this).get(CursoViewModel.class);

        // cambio en filtros
        cursoViewModel.getCursosFiltrados().observe(this, cursos -> {
            if (cursos != null) {
                cursoAdapter.actualizarCursos(cursos);
            }
        });

        // veo errores
        cursoViewModel.getError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(getApplicationContext(), "Error al cargar los cursos", Toast.LENGTH_SHORT).show();
            }
        });

        cursoViewModel.cargarCursos();
        // filtro cursos (escucho)
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
    }
}
