package com.example.tpf_paii_android.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.actividades.tutorias.ForoDetalleActivity;
import com.example.tpf_paii_android.modelos.Foro;

import java.util.ArrayList;
import java.util.List;

public class ForoAdapter extends RecyclerView.Adapter<ForoAdapter.ForoViewHolder> {

    private List<Foro> foros = new ArrayList<>();
    private Context context; // Contexto

    @Override
    public ForoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foro, parent, false);
        context = parent.getContext();
        return new ForoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ForoViewHolder holder, int position) {
        Foro foro = foros.get(position);
        holder.titulo.setText(foro.getTitulo());
        holder.nombreUsuario.setText(foro.getNombreUsuario());
        holder.ultimoMensaje.setText(foro.getDescripcionMensaje());
        holder.replicas.setText(String.valueOf(foro.getNumeroReplicas()));

        // titulo seleecionado
        holder.titulo.setOnClickListener(v -> {
            // intrnt
            Intent intent = new Intent(context, ForoDetalleActivity.class);
            intent.putExtra("idHilo", foro.getIdHilo());
            intent.putExtra("tituloForo", foro.getTitulo());
            intent.putExtra("mensajeForo", foro.getDescripcionMensaje());
            intent.putExtra("nombreUsuario", foro.getNombreUsuario());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return foros.size();
    }

    public void setForos(List<Foro> foros) {
        this.foros = foros;
        notifyDataSetChanged();
    }

    public static class ForoViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, nombreUsuario, ultimoMensaje, replicas;

        public ForoViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.textViewTituloForo);
            nombreUsuario = itemView.findViewById(R.id.textViewNombreUsuario);
            ultimoMensaje = itemView.findViewById(R.id.textViewUltimoMensaje);
            replicas = itemView.findViewById(R.id.textViewNumeroReplicas);
        }
    }
}

