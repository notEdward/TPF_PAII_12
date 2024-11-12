package com.example.tpf_paii_android.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.actividades.ofertas.OfertaDetalleActivity;
import com.example.tpf_paii_android.modelos.OfertaEmpleo;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;

public class OfertaAdapter extends RecyclerView.Adapter<OfertaAdapter.OfertaViewHolder> {

    private List<OfertaEmpleo> ofertas;
    private Context context;

    public OfertaAdapter(Context context,List<OfertaEmpleo> ofertas) {
        this.context = context;
        this.ofertas = ofertas != null ? ofertas : new ArrayList<>();
    }

    public static class OfertaViewHolder extends RecyclerView.ViewHolder {
        ImageView imageOferta;
        TextView tituloTextView;
        TextView descripcionTextView;
        TextView tvVerOferta;

        public OfertaViewHolder(View itemView) {
            super(itemView);
            imageOferta = itemView.findViewById(R.id.imageOferta);
            tituloTextView = itemView.findViewById(R.id.tvTituloOferta);
            descripcionTextView = itemView.findViewById(R.id.tvDescripcionOferta);
//            verOfertaTextView = itemView.findViewById(R.id.tvVerOferta);
            tvVerOferta = itemView.findViewById(R.id.tvVerOferta);
        }

        // datos al la vista
        public void bind(OfertaEmpleo oferta) {
            tituloTextView.setText(oferta.getTitulo());
            descripcionTextView.setText(oferta.getDescripcion());

            //img por ahora hadcoded
            imageOferta.setImageResource(R.drawable.img3_tpf);
            tvVerOferta.setText("Ver oferta");
        }
    }

    @NonNull
    @Override
    public OfertaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_oferta, parent, false);
        return new OfertaViewHolder(view);
    }

@Override
public void onBindViewHolder(@NonNull OfertaViewHolder holder, int position) {
    OfertaEmpleo oferta = ofertas.get(position); // Obtiene la oferta actual
    holder.bind(oferta); // bind para establecer los datos en la vista

    // acción del botón "Ver oferta"
    holder.tvVerOferta.setOnClickListener(v -> {
        Intent intent = new Intent(context, OfertaDetalleActivity.class);
        intent.putExtra("id_oferta_empleo", oferta.getId_ofertaEmpleo());
        context.startActivity(intent);
    });
}

    @Override
    public int getItemCount() {
        return ofertas != null ? ofertas.size() : 0;
    }

    public void setOfertas(List<OfertaEmpleo> nuevasOfertas) {
        if (this.ofertas != null) {
            this.ofertas.clear();
            if (nuevasOfertas != null) {
                this.ofertas.addAll(nuevasOfertas);
            }
        } else {
            this.ofertas = nuevasOfertas != null ? nuevasOfertas : new ArrayList<>();
        }
        notifyDataSetChanged();
    }

}
