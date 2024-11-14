package com.example.tpf_paii_android.actividades.tutorias;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tpf_paii_android.MyApp;
import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.actividades.menu_header.MenuHamburguesaActivity;
import com.example.tpf_paii_android.adapters.ForoAdapter;
import com.example.tpf_paii_android.modelos.Foro;
import com.example.tpf_paii_android.viewmodels.ForoViewModel;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import java.util.List;

public class ForosActivity extends MenuHamburguesaActivity {

    private ForoViewModel foroViewModel;
    private RecyclerView recyclerViewForos;
    private ForoAdapter foroAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foros);

        MyApp app = (MyApp) getApplication();
        int idUsuario = app.getIdUsuario();
        String tipo_usuario = app.getTipoUsuario();
        String nombreUsuario = app.getNombreUsuario();

        setupDrawer(nombreUsuario, tipo_usuario);
        // Configuración del SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        // Configuración del RecyclerView
        recyclerViewForos = findViewById(R.id.recyclerViewForos);
        recyclerViewForos.setLayoutManager(new LinearLayoutManager(this));
        foroAdapter = new ForoAdapter();
        recyclerViewForos.setAdapter(foroAdapter);

        // Inicialización del ViewModel
        foroViewModel = new ViewModelProvider(this).get(ForoViewModel.class);

        // Observar la lista de foros
        foroViewModel.getForos().observe(this, foros -> {
            if (foros != null) {
                foroAdapter.setForos(foros);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        // Obtener los foros
        foroViewModel.getForos();

        // pull to refresh test
        swipeRefreshLayout.setOnRefreshListener(() -> {
            foroViewModel.getForos();
        });

        // Configurar botón "Añadir nuevo debate"
        findViewById(R.id.buttonAddHilo).setOnClickListener(v -> {
            // Abrir la actividad para crear un nuevo foro
            Intent intent = new Intent(ForosActivity.this, CrearHiloActivity.class);
            startActivity(intent);
        });


    }
}
