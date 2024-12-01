package com.example.tpf_paii_android.actividades.cursos;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tpf_paii_android.MyApp;
import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.adapters.MisCursosAdapter;
import com.example.tpf_paii_android.viewmodels.CursoViewModel;

public class MisCursosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MisCursosAdapter adapter;
    private CursoViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_cursos);

        recyclerView = findViewById(R.id.recyclerViewMisCursos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewModel = new ViewModelProvider(this).get(CursoViewModel.class);

        adapter = new MisCursosAdapter();
        recyclerView.setAdapter(adapter);

        viewModel.getMisCursos().observe(this, misCursos -> {
            adapter.submitList(misCursos);
        });

        MyApp app = (MyApp) getApplication();
        viewModel.cargarDatos(app.getIdUsuario());
    }
}

