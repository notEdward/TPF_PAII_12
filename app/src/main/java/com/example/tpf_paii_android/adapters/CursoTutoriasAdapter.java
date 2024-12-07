package com.example.tpf_paii_android.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.actividades.tutorias.SolicitarTutoriaActivity;
import com.example.tpf_paii_android.modelos.Curso;

import java.util.List;

public class CursoTutoriasAdapter extends ListAdapter<Curso, CursoTutoriasAdapter.CursoViewHolder> {

    public CursoTutoriasAdapter() {
        super(new DiffUtil.ItemCallback<Curso>() {
            @Override
            public boolean areItemsTheSame(@NonNull Curso oldItem, @NonNull Curso newItem) {
                return oldItem.getIdCurso() == newItem.getIdCurso();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Curso oldItem, @NonNull Curso newItem) {
                return oldItem.equals(newItem);
            }
        });
    }

    @NonNull
    @Override
    public CursoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tutoria, parent, false);
        return new CursoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CursoViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class CursoViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtNombreCurso;
        private final Button btnSolicitarTutoria;

        public CursoViewHolder(View itemView) {
            super(itemView);
            txtNombreCurso = itemView.findViewById(R.id.txtNombreCurso);
            btnSolicitarTutoria = itemView.findViewById(R.id.btnSolicitarTutoria);

            btnSolicitarTutoria.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Curso curso = getItem(position);
                    Intent intent = new Intent(itemView.getContext(), SolicitarTutoriaActivity.class);
                    intent.putExtra("idCurso", curso.getIdCurso());
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        public void bind(Curso curso) {
            txtNombreCurso.setText(curso.getNombreCurso());
        }
    }

    // Método para actualizar la lista de cursos
    public void setCursos(List<Curso> cursos) {
        submitList(cursos);
    }
}

