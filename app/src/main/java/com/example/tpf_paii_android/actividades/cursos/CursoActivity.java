package com.example.tpf_paii_android.actividades.cursos;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.adapters.CursoAdapter;
import com.example.tpf_paii_android.viewmodels.CursoViewModel;
import com.google.android.material.navigation.NavigationView;

import android.text.TextWatcher;
import java.util.ArrayList;

public class CursoActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCursos;
    private CursoAdapter cursoAdapter;
    private CursoViewModel cursoViewModel;
    private Button btnFiltrar;
    private EditText editTextBuscar;
    private static final int REQUEST_FILTRO = 1;
    private DrawerLayout drawerLayout;

    private int idUsuario;
    private String nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso);
        //simulo recibir los datos del login
        // Recuperar los datos del usuario
        //Intent intent = getIntent();
        idUsuario = 1; // intent.getIntExtra("id_usuario", -1);
        nombreUsuario = "prueba";//intent.getStringExtra("nombre_usuario");
        ///

        //inicializaciones + configs
        cursoViewModel = new ViewModelProvider(this).get(CursoViewModel.class);
        recyclerViewCursos = findViewById(R.id.recyclerViewCursos);
        btnFiltrar = findViewById(R.id.btnFiltrar);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2); // 2 columnas
        recyclerViewCursos.setLayoutManager(gridLayoutManager);
        recyclerViewCursos.setHasFixedSize(true);
        cursoAdapter = new CursoAdapter(new ArrayList<>());
        recyclerViewCursos.setAdapter(cursoAdapter);

        //menu hamburguesa
        drawerLayout = findViewById(R.id.drawer_layout);
        ImageView menuHamburguesa = findViewById(R.id.menu_hamburguesa);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        //listener
        menuHamburguesa.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

//        navigationView.setNavigationItemSelectedListener(menuItem -> {
//            int itemId = menuItem.getItemId();
//            if (itemId == R.id.nav_cursos) {
//                Toast.makeText(this, "Cursos seleccionados", Toast.LENGTH_SHORT).show();
//            } else if (itemId == R.id.nav_ofertas_empleo) {
//                Toast.makeText(this, "Ofertas de empleo seleccionadas", Toast.LENGTH_SHORT).show();
//            } else if (itemId == R.id.nav_tutorias) {
//                Toast.makeText(this, "Tutorías seleccionadas", Toast.LENGTH_SHORT).show();
//            } else if (itemId == R.id.nav_salir) {
//                Toast.makeText(this, "Salir", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//            drawerLayout.closeDrawer(Gravity.START);
//            return true;
//        });
        //fin menu//

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
