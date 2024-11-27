package com.example.tpf_paii_android.actividades.ofertas;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tpf_paii_android.MyApp;
import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.actividades.menu_header.MenuHamburguesaActivity;
import com.example.tpf_paii_android.adapters.OfertaAdapter;
import com.example.tpf_paii_android.modelos.OfertaEmpleo;
import com.example.tpf_paii_android.viewmodels.OfertaViewModel;
import java.util.ArrayList;
import java.util.List;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class OfertaActivity extends MenuHamburguesaActivity {

    private RecyclerView recyclerView;
    private OfertaAdapter adapter;
    private OfertaViewModel ofertaViewModel;
    private EditText editTextBuscar;
    private List<OfertaEmpleo> listaOfertasEmpleo = new ArrayList<>();
    private Button btnFiltrar;
    private int idUsuario;
    private String nombreUsuario;
    private DrawerLayout drawerLayout;
    private String tipo_usuario;
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final int FILTER_REQUEST_CODE = 100;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oferta);
        btnFiltrar = findViewById(R.id.btnFiltrar);
        recyclerView = findViewById(R.id.recyclerViewOfertas);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2); // 2 columnas
        recyclerView.setLayoutManager(gridLayoutManager);
        Button btnCrear = findViewById(R.id.btnCrear);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        MyApp app = (MyApp) getApplication();
        idUsuario = app.getIdUsuario();
        tipo_usuario = app.getTipoUsuario();
        nombreUsuario = app.getNombreUsuario();
        setupDrawer(nombreUsuario, tipo_usuario);

        adapter = new OfertaAdapter(this,null, tipo_usuario);
        recyclerView.setAdapter(adapter);

        ofertaViewModel = new ViewModelProvider(this).get(OfertaViewModel.class);

        swipeRefreshLayout.setOnRefreshListener(() -> ofertaViewModel.loadOfertas());

        // Observa ofertas filtradas
        ofertaViewModel.getOfertasFiltradas().observe(this, ofertas -> {
            if (ofertas != null) {
                adapter.setOfertas(ofertas);
            }
            swipeRefreshLayout.setRefreshing(false);
        });
        ofertaViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(getApplicationContext(), "Error al cargar las ofertas", Toast.LENGTH_SHORT).show();
            }
        });

        // carga ofertas inicial
        ofertaViewModel.loadOfertas();

        // filtro buscar (por caracteres)
        editTextBuscar = findViewById(R.id.editTextBuscar);
        editTextBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ofertaViewModel.filtrarOfertas(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        //btn alta
        if ("Empresa".equals(tipo_usuario)) {
            btnCrear.setVisibility(View.VISIBLE);
            btnCrear.setOnClickListener(view -> {
                Intent intent = new Intent(OfertaActivity.this, AltaOfertaActivity.class);
                startActivity(intent);
            });
        } else {
            btnCrear.setVisibility(View.GONE);
        }

        // btn filtrar
        btnFiltrar.setOnClickListener(v -> {
            // Abrir la actividad de filtros
            Intent intent = new Intent(OfertaActivity.this, FiltroOfertaActivity.class);
            startActivityForResult(intent, FILTER_REQUEST_CODE);
        });

        // Observando el LiveData para saber si la baja fue exitosa
        ofertaViewModel.getBajaOfertaLiveData().observe(this, result -> {
            if (result != null) {
                if (result) {
                    Toast.makeText(OfertaActivity.this, "Oferta dada de baja", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OfertaActivity.this, "Error al dar de baja la oferta", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILTER_REQUEST_CODE && resultCode == RESULT_OK) {
            //modalidades, tipos de empleo y cursos del filtro elegido
            ArrayList<Integer> modalidadesSeleccionadas = data.getIntegerArrayListExtra("modalidadesSeleccionadas");
            ArrayList<Integer> tiposEmpleoSeleccionados = data.getIntegerArrayListExtra("tiposEmpleoSeleccionados");
            ArrayList<Integer> cursosSeleccionados = data.getIntegerArrayListExtra("cursosSeleccionados");

            filtroOfertas(modalidadesSeleccionadas, tiposEmpleoSeleccionados, cursosSeleccionados);
        }
    }
    private void filtroOfertas(ArrayList<Integer> modalidades, ArrayList<Integer> tiposEmpleo, ArrayList<Integer> cursos) {

        ofertaViewModel.obtenerOfertasConFiltros(modalidades, tiposEmpleo, cursos);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    public void darDeBajaOferta(int idOferta) {
        ofertaViewModel.actualizarEstadoOferta(idOferta, 0); // Cambiar el estado a 0
    }
}
