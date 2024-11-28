package com.example.tpf_paii_android.actividades.cursos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.viewmodels.CursoViewModel;


public class ModificarCursoActivity extends AppCompatActivity {

    private TextView txtNombreCurso, txtDescripcionCurso;
    private EditText etDescripcionCurso;
    private ImageView imgCurso;
    private Button btnGuardarCambios;
    private CursoViewModel cursoViewModel;
    private int idCurso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_curso);

        txtNombreCurso = findViewById(R.id.txtNombreCurso);
        txtDescripcionCurso = findViewById(R.id.txtDescripcionCurso);
        etDescripcionCurso = findViewById(R.id.etDescripcionCurso);
        imgCurso = findViewById(R.id.imgCurso);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);

        cursoViewModel = new ViewModelProvider(this).get(CursoViewModel.class);

        Intent intent = getIntent();
        idCurso = intent.getIntExtra("idCurso", -1);
        String nombreCurso = intent.getStringExtra("nombreCurso");
        String descripcionCurso = intent.getStringExtra("descripcionCurso");
        int imagenCursoResId = intent.getIntExtra("imageResId", R.drawable.img1_tpf);  // ID de recurso de imagen

        txtNombreCurso.setText(nombreCurso);
        txtDescripcionCurso.setText(descripcionCurso);
        imgCurso.setImageResource(imagenCursoResId);


        etDescripcionCurso.setText(descripcionCurso);

        btnGuardarCambios.setOnClickListener(v -> {
            String nuevaDescripcion = etDescripcionCurso.getText().toString();
            cursoViewModel.modificarDescripcionCurso(idCurso, nuevaDescripcion);
            Toast.makeText(this, "Descripción actualizada correctamente", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}

