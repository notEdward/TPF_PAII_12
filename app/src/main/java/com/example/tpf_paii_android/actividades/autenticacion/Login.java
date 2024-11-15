package com.example.tpf_paii_android.actividades.autenticacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.tpf_paii_android.R;

import com.example.tpf_paii_android.MyApp;
import com.example.tpf_paii_android.actividades.cursos.CursoActivity;
import com.example.tpf_paii_android.actividades.ofertas.OfertaActivity;
import com.example.tpf_paii_android.actividades.tutorias.ForosActivity;
import com.example.tpf_paii_android.utilidades.TipoUsuarioUtils;
import com.example.tpf_paii_android.viewmodels.LoginViewModel;


public class Login extends AppCompatActivity {

    private EditText txtUsuario, txtContrasena;
    private Button btnIniciarSesion;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializa las vistas
        txtUsuario = findViewById(R.id.txtUsuario);
        txtContrasena = findViewById(R.id.txtContrasena);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);

        btnIniciarSesion.setOnClickListener(v -> {
            String usuario = txtUsuario.getText().toString().trim();
            String contrasena = txtContrasena.getText().toString().trim();

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(Login.this, "Por favor ingrese usuario y contraseña.", Toast.LENGTH_SHORT).show();
                return;
            }

            loginViewModel.authenticate(usuario, contrasena);

            loginViewModel.getUsuarioLiveData().observe(this, usuarioLogin -> {
                if (usuarioLogin != null) {
                    MyApp app = (MyApp) getApplication();
                    app.setIdUsuario(usuarioLogin.getIdUsuario());
                    app.setNombreUsuario(usuarioLogin.getNombreUsuario());
                    //auxiliar para converir
                    String tipoUsuarioString = TipoUsuarioUtils.getTipoUsuario(usuarioLogin.getTipoUsuario());
                    app.setTipoUsuario(tipoUsuarioString);

                    // Redirigir según el tipo de usuario
                    switch (usuarioLogin.getTipoUsuario()) {
                        case 1:
                            startActivity(new Intent(Login.this, CursoActivity.class));
                            break;
                        case 2:
                            startActivity(new Intent(Login.this, OfertaActivity.class));
                            break;
                        case 3:
                            startActivity(new Intent(Login.this, ForosActivity.class));
                        case 4:
                            startActivity(new Intent(Login.this, CursoActivity.class));
                            break;
                    }
                    finish();
                }
            });

            loginViewModel.getErrorMessage().observe(this, error -> {
                if (error != null) {
                    Toast.makeText(Login.this, error, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    // Método para el textView Registrarse
    public void Registrarse(View view){
        Intent registrarse = new Intent(this, IniciarRegistro.class);
        startActivity(registrarse);
    }

}