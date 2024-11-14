package com.example.tpf_paii_android.actividades.menu_header;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;

import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.actividades.cursos.CursoActivity;
import com.example.tpf_paii_android.actividades.ofertas.OfertaActivity;
import com.example.tpf_paii_android.actividades.tutorias.ForosActivity;
import com.google.android.material.navigation.NavigationView;

public class MenuHamburguesaActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("WrongConstant")
    protected void setupDrawer(String nombreUsuario, String tipoUsuario) {
        drawerLayout = findViewById(R.id.drawer_layout);
        ImageView menuHamburguesa = findViewById(R.id.menu_hamburguesa);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        View headerView = navigationView.getHeaderView(0);
        TextView textoUsuario = headerView.findViewById(R.id.texto_usuario);
        TextView textoTipoUsuario = headerView.findViewById(R.id.texto_tipo_usuario);

        textoUsuario.setText(nombreUsuario);
        textoTipoUsuario.setText(tipoUsuario);

        menuHamburguesa.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.nav_cursos) {
                startActivity(new Intent(this, CursoActivity.class));
            } else if (itemId == R.id.nav_ofertas_empleo) {
                startActivity(new Intent(this, OfertaActivity.class));
            } else if (itemId == R.id.nav_tutorias) {
                startActivity(new Intent(this, ForosActivity.class));
                } else if (itemId == R.id.nav_salir) {
                Toast.makeText(this, "Salir", Toast.LENGTH_SHORT).show();
                finish();
            }
            drawerLayout.closeDrawer(Gravity.START);
            return true;
        });
    }
}
