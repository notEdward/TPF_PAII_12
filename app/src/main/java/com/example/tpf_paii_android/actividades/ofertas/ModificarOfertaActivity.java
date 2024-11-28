package com.example.tpf_paii_android.actividades.ofertas;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.modelos.OfertaEmpleo;
import com.example.tpf_paii_android.repositorios.OfertaRepository;

public class ModificarOfertaActivity extends AppCompatActivity {

    private EditText etTituloOferta;
    private EditText etDescripcionOferta;
    private Button btnGuardarCambios;
    private OfertaRepository ofertaRepository;
    private int idOfertaEmpleo,imageResId;
    private ImageView imgOferta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_oferta);

        // Inicializar vistas
        etTituloOferta = findViewById(R.id.etTituloOferta);
        etDescripcionOferta = findViewById(R.id.etDescripcionOferta);
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios);
        imgOferta= findViewById(R.id.imgOferta);

        // Inicializar el repositorio
        ofertaRepository = new OfertaRepository();

        // Obtener datos de la oferta desde el Intent
        idOfertaEmpleo = getIntent().getIntExtra("id_oferta_empleo", -1);
        String titulo = getIntent().getStringExtra("titulo");
        String descripcion = getIntent().getStringExtra("descripcion");
        imageResId = getIntent().getIntExtra("imageResId", R.drawable.img1_tpf);

        // Verificar que se recibió un id de oferta válido
        if (idOfertaEmpleo == -1) {
            Toast.makeText(this, "Error al cargar los datos de la oferta", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Rellenar los campos con los datos de la oferta
        etTituloOferta.setText(titulo);
        etDescripcionOferta.setText(descripcion);
        imgOferta.setImageResource(imageResId);

        // Configurar botón para guardar los cambios
        btnGuardarCambios.setOnClickListener(v -> guardarCambios());
    }

    // Método para guardar los cambios en la oferta
    private void guardarCambios() {
        String nuevoTitulo = etTituloOferta.getText().toString().trim();
        String nuevaDescripcion = etDescripcionOferta.getText().toString().trim();

        if (nuevoTitulo.isEmpty() || nuevaDescripcion.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el objeto OfertaEmpleo actualizado
        OfertaEmpleo ofertaActualizada = new OfertaEmpleo();
        ofertaActualizada.setId_ofertaEmpleo(idOfertaEmpleo);
        ofertaActualizada.setTitulo(nuevoTitulo);
        ofertaActualizada.setDescripcion(nuevaDescripcion);

        // Llamar al repositorio para actualizar la oferta
        ofertaRepository.actualizarOferta(ofertaActualizada, new OfertaRepository.DataCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean exito) {
                if (exito) {
                    Toast.makeText(ModificarOfertaActivity.this, "Oferta actualizada correctamente", Toast.LENGTH_SHORT).show();
                    finish(); // Cierra la actividad y vuelve a la anterior
                } else {
                    Toast.makeText(ModificarOfertaActivity.this, "Error al actualizar la oferta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(ModificarOfertaActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
