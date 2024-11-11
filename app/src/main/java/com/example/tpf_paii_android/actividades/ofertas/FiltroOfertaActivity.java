package com.example.tpf_paii_android.actividades.ofertas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.modelos.Curso;
import com.example.tpf_paii_android.modelos.Modalidad;
import com.example.tpf_paii_android.modelos.TipoEmpleo;
import com.example.tpf_paii_android.repositorios.OfertaRepository;

import java.util.ArrayList;
import java.util.List;

public class FiltroOfertaActivity extends AppCompatActivity {

    private LinearLayout linearLayoutModalidades, linearLayoutTiposEmpleo, linearLayoutCursos;
    private Button btnAplicarFiltros;
    private OfertaRepository ofertaRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro_oferta);

        linearLayoutModalidades = findViewById(R.id.linearLayoutModalidades);
        linearLayoutTiposEmpleo = findViewById(R.id.linearLayoutTiposEmpleo);
        linearLayoutCursos = findViewById(R.id.linearLayoutCursos);
        btnAplicarFiltros = findViewById(R.id.btnAplicarFiltros);
        ofertaRepository = new OfertaRepository();

        // modalidades
        ofertaRepository.obtenerModalidades(new OfertaRepository.DataCallback<List<Modalidad>>() {
            @Override
            public void onSuccess(List<Modalidad> modalidades) {
                mostrarModalidades(modalidades);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(FiltroOfertaActivity.this, "Error al cargar modalidades", Toast.LENGTH_SHORT).show();
            }
        });

        // tipos empleo
        ofertaRepository.obtenerTiposEmpleo(new OfertaRepository.DataCallback<List<TipoEmpleo>>() {
            @Override
            public void onSuccess(List<TipoEmpleo> tiposEmpleo) {
                mostrarTiposEmpleo(tiposEmpleo);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(FiltroOfertaActivity.this, "Error al cargar tipos de empleo", Toast.LENGTH_SHORT).show();
            }
        });

        //cursos
        ofertaRepository.obtenerCursos(new OfertaRepository.DataCallback<List<Curso>>() {
            @Override
            public void onSuccess(List<Curso> cursos) {
                mostrarCursos(cursos);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(FiltroOfertaActivity.this, "Error al cargar cursos", Toast.LENGTH_SHORT).show();
            }
        });

        // btn aplciar
        btnAplicarFiltros.setOnClickListener(v -> {
            ArrayList<Integer> modalidadesSeleccionadas = obtenerSeleccionados(linearLayoutModalidades);
            ArrayList<Integer> tiposEmpleoSeleccionados = obtenerSeleccionados(linearLayoutTiposEmpleo);
            ArrayList<Integer> cursosSeleccionados = obtenerSeleccionados(linearLayoutCursos);

            Intent intent = new Intent();
            intent.putIntegerArrayListExtra("modalidadesSeleccionadas", modalidadesSeleccionadas);
            intent.putIntegerArrayListExtra("tiposEmpleoSeleccionados", tiposEmpleoSeleccionados);
            intent.putIntegerArrayListExtra("cursosSeleccionados", cursosSeleccionados);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    private void mostrarModalidades(List<Modalidad> modalidades) {
        for (Modalidad modalidad : modalidades) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(modalidad.getDescripcion());
            checkBox.setTag(modalidad.getId_modalidad());
            linearLayoutModalidades.addView(checkBox);
        }
    }

    private void mostrarTiposEmpleo(List<TipoEmpleo> tiposEmpleo) {
        for (TipoEmpleo tipo : tiposEmpleo) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(tipo.getDescripcion());
            checkBox.setTag(tipo.getId_tipoEmpleo());
            linearLayoutTiposEmpleo.addView(checkBox);
        }
    }

    private void mostrarCursos(List<Curso> cursos) {
        for (Curso curso : cursos) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(curso.getNombreCurso());
            checkBox.setTag(curso.getIdCurso());
            linearLayoutCursos.addView(checkBox);
        }
    }

    private ArrayList<Integer> obtenerSeleccionados(LinearLayout layout) {
        ArrayList<Integer> seleccionados = new ArrayList<>();
        for (int i = 0; i < layout.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) layout.getChildAt(i);
            if (checkBox.isChecked()) {
                seleccionados.add((Integer) checkBox.getTag());
            }
        }
        return seleccionados;
    }
}
