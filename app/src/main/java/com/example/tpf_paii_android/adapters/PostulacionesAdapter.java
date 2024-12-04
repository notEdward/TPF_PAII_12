package com.example.tpf_paii_android.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.modelos.PostulacionItem;
import com.example.tpf_paii_android.actividades.ofertas.DetalleEstudianteActivity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PostulacionesAdapter extends RecyclerView.Adapter<PostulacionesAdapter.PostulacionesViewHolder> {
    private final List<PostulacionItem> postulaciones;
    private final String tipoUsuario;
    private final Context context;
    private final Set<String> shownTitles = new HashSet<>();
//hash para ver si ya existe con el mismo nombre y no repetir

    public PostulacionesAdapter(List<PostulacionItem> postulaciones, String tipoUsuario, Context context) {
        this.postulaciones = postulaciones;
        this.tipoUsuario = tipoUsuario;
        this.context = context;
    }

    @NonNull
    @Override
    public PostulacionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_postulacion, parent, false);
        return new PostulacionesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostulacionesViewHolder holder, int position) {
        PostulacionItem postulacion = postulaciones.get(position);

        //validacion de usuario para ver opciones
        // si es empresa vemos los detalles sino solo el estado
        if (tipoUsuario.equalsIgnoreCase("Empresa")) {
            if (shownTitles.contains(postulacion.getTitulo())) {
                holder.textTitulo.setVisibility(View.GONE);
            } else {
                holder.textTitulo.setText(postulacion.getTitulo());
                holder.textTitulo.setVisibility(View.VISIBLE);
                shownTitles.add(postulacion.getTitulo());
            }
            holder.textDetalle.setText("Usuario: " + postulacion.getNombreUsuario());
            holder.btnVerDetalle.setVisibility(View.VISIBLE);

            holder.btnVerDetalle.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetalleEstudianteActivity.class);
                intent.putExtra("idPostulacion", postulacion.getIdPostulacion());
                intent.putExtra("idUsuario", postulacion.getIdUsuario());
                context.startActivity(intent);
            });

        } else if (tipoUsuario.equalsIgnoreCase("Estudiante")) {
            // Para estudiante, solo mostramos el título y detalles
            holder.textTitulo.setText(postulacion.getTitulo());
            holder.textDetalle.setText(
                    "Fecha: " + postulacion.getFechaPostulacion() +
                    "\nEstado: " + postulacion.getEstadoPostulacion()
            );
            holder.btnVerDetalle.setVisibility(View.GONE); // si es estudiante chau boton
        }
    }

    @Override
    public int getItemCount() {
        return postulaciones.size();
    }

    static class PostulacionesViewHolder extends RecyclerView.ViewHolder {
        TextView textTitulo;
        TextView textDetalle;
        Button btnVerDetalle;

        public PostulacionesViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitulo = itemView.findViewById(R.id.textTituloPostulacion);
            textDetalle = itemView.findViewById(R.id.textDetallePostulacion);
            btnVerDetalle = itemView.findViewById(R.id.btnVerDetalle);
        }
    }
}
