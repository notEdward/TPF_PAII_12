package com.example.tpf_paii_android.actividades.cursos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.modelos.CategoriaCurso;
import com.example.tpf_paii_android.repositorios.CursoRepository;
import com.example.tpf_paii_android.viewmodels.CursoViewModel;

import java.util.ArrayList;
import java.util.List;

public class FiltroCursoActivity extends AppCompatActivity {

    private LinearLayout linearLayoutCategorias;
    private CursoRepository cursoRepository;
    private Button btnAplicarFiltros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro_curso);

        linearLayoutCategorias = findViewById(R.id.linearLayoutCategorias);
        btnAplicarFiltros = findViewById(R.id.btnAplicarFiltros);
        cursoRepository = new CursoRepository();

        // Obtener y mostrar las categorías
        cursoRepository.obtenerCategorias(new CursoRepository.DataCallback<List<CategoriaCurso>>() {
            @Override
            public void onSuccess(List<CategoriaCurso> categorias) {
                mostrarCategorias(categorias);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(FiltroCursoActivity.this, "Error al cargar categorías", Toast.LENGTH_SHORT).show();
            }
        });

        btnAplicarFiltros.setOnClickListener(v -> {
            ArrayList<Integer> categoriasSeleccionadas = new ArrayList<>();
            for (int i = 0; i < linearLayoutCategorias.getChildCount(); i++) {
                CheckBox checkBox = (CheckBox) linearLayoutCategorias.getChildAt(i);
                if (checkBox.isChecked()) {
                    categoriasSeleccionadas.add((Integer) checkBox.getTag());
                }
            }

            Intent intent = new Intent();
            intent.putIntegerArrayListExtra("categoriasSeleccionadas", categoriasSeleccionadas);
            setResult(RESULT_OK, intent);
            finish();
        });

    }

    private void mostrarCategorias(List<CategoriaCurso> categorias) {
        linearLayoutCategorias.removeAllViews();

        for (CategoriaCurso categoria : categorias) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(categoria.getDescripcion());
            checkBox.setTag(categoria.getIdCategoria());
            linearLayoutCategorias.addView(checkBox);
        }
    }

}
