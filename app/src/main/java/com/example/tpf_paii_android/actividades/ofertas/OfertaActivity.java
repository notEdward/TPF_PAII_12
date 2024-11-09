package com.example.tpf_paii_android.actividades.ofertas;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.adapters.OfertaAdapter;
import com.example.tpf_paii_android.modelos.OfertaEmpleo;
import com.example.tpf_paii_android.viewmodels.OfertaViewModel;


public class OfertaActivity extends AppCompatActivity implements OfertaAdapter.OnOfertaClickListener {

    private RecyclerView recyclerView;
    private OfertaAdapter adapter;
    private OfertaViewModel ofertaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oferta);

        recyclerView = findViewById(R.id.recyclerViewOfertas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new OfertaAdapter(null, this);
        recyclerView.setAdapter(adapter);

        ofertaViewModel = new ViewModelProvider(this).get(OfertaViewModel.class);

        ofertaViewModel.getOfertasLiveData().observe(this, ofertas -> {
            if (ofertas != null) {
                adapter.setOfertas(ofertas);
            }
        });

        ofertaViewModel.loadOfertas();
    }


    @Override
    public void onVerOfertaClick(OfertaEmpleo oferta) {
    //dps ver el botno ver oferta
        System.out.println("Oferta seleccionada: " + oferta.getTitulo());
    }
}
