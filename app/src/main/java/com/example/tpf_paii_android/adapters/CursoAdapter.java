package com.example.tpf_paii_android.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.actividades.cursos.CursoDetalleActivity;
import com.example.tpf_paii_android.modelos.Curso;
import java.util.List;

public class CursoAdapter extends RecyclerView.Adapter<CursoAdapter.CursoViewHolder> {
    private List<Curso> cursos;

    public CursoAdapter(List<Curso> cursos) {
        this.cursos = cursos;
    }

    public void actualizarCursos(List<Curso> cursosFiltrados) {
        this.cursos = cursosFiltrados;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CursoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_curso, parent, false);
        return new CursoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CursoViewHolder holder, int position) {
        Curso curso = cursos.get(position);
        
        holder.tvNombreCurso.setText(curso.getNombreCurso());
        holder.tvDescripcionCurso.setText(curso.getDescripcion());

        // Obtener el contexto desde el itemView
        Context context = holder.itemView.getContext();

        holder.tvVerCapacitacion.setOnClickListener(v -> {
            //ver dps detalles
            Intent intent = new Intent(context, CursoDetalleActivity.class);
            intent.putExtra("nombreCurso", curso.getNombreCurso());
            intent.putExtra("descripcionCurso", curso.getDescripcion());
            // Puedes añadir más datos si es necesario (ej. id del curso)
            context.startActivity(intent);
            //
        });
    }

    @Override
    public int getItemCount() {
        return cursos.size();
    }

    public static class CursoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreCurso;
        TextView tvDescripcionCurso;
        TextView tvVerCapacitacion;
        ImageView imageCurso;

        public CursoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreCurso = itemView.findViewById(R.id.tvNombreCurso);
            tvDescripcionCurso = itemView.findViewById(R.id.tvDescripcionCurso);
            tvVerCapacitacion = itemView.findViewById(R.id.tvVerCapacitacion);
            imageCurso = itemView.findViewById(R.id.imageCurso);
        }
    }
}