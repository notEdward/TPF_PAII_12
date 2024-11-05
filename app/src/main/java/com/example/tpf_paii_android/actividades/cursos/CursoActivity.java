package com.example.tpf_paii_android.actividades.cursos;

import android.os.Bundle;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.modelos.Curso;
import com.example.tpf_paii_android.repositorios.CursoRepository;
import com.example.tpf_paii_android.adapters.CursoAdapter;
import java.util.ArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;

public class CursoActivity extends AppCompatActivity {

    private CursoRepository cursoRepository;
    private RecyclerView recyclerViewCursos;
    private CursoAdapter cursoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso);
        //inicializo repo
        cursoRepository = new CursoRepository();

        recyclerViewCursos = findViewById(R.id.recyclerViewCursos);
        recyclerViewCursos.setLayoutManager(new LinearLayoutManager(this));

        cargarCursos();
    }

    private void cargarCursos() {
          cursoRepository.getAllCursos(new CursoRepository.DataCallback<ArrayList<Curso>>() {
            @Override
            public void onSuccess(ArrayList<Curso> cursos) {
                //cursos para pasaar al recycler
                cursoAdapter = new CursoAdapter(cursos);
                recyclerViewCursos.setAdapter(cursoAdapter);
            }
            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getApplicationContext(), "Error al cargar los cursos", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
