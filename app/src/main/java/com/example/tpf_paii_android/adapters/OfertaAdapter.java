package com.example.tpf_paii_android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.modelos.OfertaEmpleo;

import java.util.ArrayList;
import java.util.List;

public class OfertaAdapter extends RecyclerView.Adapter<OfertaAdapter.OfertaViewHolder> {

    private List<OfertaEmpleo> ofertas;
    private final OnOfertaClickListener listener;

    public interface OnOfertaClickListener {
        void onVerOfertaClick(OfertaEmpleo oferta);
    }

    public OfertaAdapter(List<OfertaEmpleo> ofertas, OnOfertaClickListener listener) {
        this.ofertas = ofertas != null ? ofertas : new ArrayList<>();
        this.listener = listener;
    }

    public static class OfertaViewHolder extends RecyclerView.ViewHolder {
        ImageView imageOferta;
        TextView tituloTextView;
        TextView descripcionTextView;
        TextView verOfertaTextView;

        public OfertaViewHolder(View itemView) {
            super(itemView);
            imageOferta = itemView.findViewById(R.id.imageOferta);
            tituloTextView = itemView.findViewById(R.id.tvTituloOferta);
            descripcionTextView = itemView.findViewById(R.id.tvDescripcionOferta);
            verOfertaTextView = itemView.findViewById(R.id.tvVerOferta);
        }

        // datos al la vista
        public void bind(OfertaEmpleo oferta, OnOfertaClickListener listener) {
            tituloTextView.setText(oferta.getTitulo());
            descripcionTextView.setText(oferta.getDescripcion());

            //img por ahora hadcoded
            imageOferta.setImageResource(R.drawable.img3_tpf);

            // ver el ver oferta dps
            verOfertaTextView.setOnClickListener(v -> listener.onVerOfertaClick(oferta));
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
        holder.bind(ofertas.get(position), listener);
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
