package com.example.tpf_paii_android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.modelos.Curso;

import java.util.List;

public class TutoriasAdapter extends ListAdapter<Curso, TutoriasAdapter.TutoriasViewHolder> {

    public TutoriasAdapter() {
        super(new DiffUtil.ItemCallback<Curso>() {
            // Compara si dos cursos son el mismo por ID
            @Override
            public boolean areItemsTheSame(@NonNull Curso oldItem, @NonNull Curso newItem) {
                return oldItem.getIdCurso() == newItem.getIdCurso();
            }
            // Compara contenido de los cursos
            @Override
            public boolean areContentsTheSame(@NonNull Curso oldItem, @NonNull Curso newItem) {
                return oldItem.equals(newItem);
            }
        });
    }

    @NonNull
    @Override
    public TutoriasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Crea la vista de cada elemento en el RecyclerVIEW, inflando item_tutoria.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tutoria, parent, false);
        return new TutoriasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutoriasViewHolder holder, int position) {
        // Asocia los datos de un curso especifico con la vista correspondiente a esa posicion
        Curso curso = getItem(position);
        holder.bind(curso);
    }

    // Método para actualizar la lista de cursos
    public void setCursos(List<Curso> cursos) {
        submitList(cursos);
    }

    // Vista individual de cada curso en el RecyclerVIEW
    public class TutoriasViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNombreCurso;

        public TutoriasViewHolder(View itemView) {
            super(itemView);
            txtNombreCurso = itemView.findViewById(R.id.txtNombreCurso);
        }

        // Vincula datos del curso al item
        public void bind(Curso curso) {
            txtNombreCurso.setText(curso.getNombreCurso());
        }
    }
}
