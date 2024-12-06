package com.example.tpf_paii_android.adapters;

import android.annotation.SuppressLint;
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
import com.example.tpf_paii_android.modelos.Tutoria;

import java.util.List;
import java.util.Map;

public class TutoriasAdapter extends ListAdapter<Object, RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_CURSO = 0;
    private static final int VIEW_TYPE_TUTORIA = 1;

    private final Map<String, String> estudianteMap;

    public TutoriasAdapter(Map<String, String> estudianteMap) {
        super(new DiffUtil.ItemCallback<Object>() {
            // Compara si dos cursos son el mismo por ID
            @Override
            public boolean areItemsTheSame(@NonNull Object oldItem, @NonNull Object newItem) {
                if (oldItem instanceof Curso && newItem instanceof Curso) {
                    return ((Curso) oldItem).getIdCurso() == ((Curso) newItem).getIdCurso();
                } else if (oldItem instanceof Tutoria && newItem instanceof Tutoria) {
                    return ((Tutoria) oldItem).getId_tutoria() == ((Tutoria) newItem).getId_tutoria();
                }
                return false;
            }
            // Compara contenido de los cursos

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull Object oldItem, @NonNull Object newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.estudianteMap = estudianteMap;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CURSO) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tutoria, parent, false);
            return new CursoViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tutoria_asignada, parent, false);
            return new TutoriaViewHolder(view);
        }
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = getItem(position);
        if (holder.getItemViewType() == VIEW_TYPE_CURSO) {
            ((CursoViewHolder) holder).bind((Curso) item);
        } else {
            ((TutoriaViewHolder) holder).bind((Tutoria) item);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object item = getItem(position);
        if (item instanceof Curso) {
            return VIEW_TYPE_CURSO;
        } else {
            return VIEW_TYPE_TUTORIA;
        }
    }


    // Método para actualizar la lista de cursos
    public void setCursos(List<Curso> cursos) {
        submitList((List<Object>) (List<?>) cursos);
    }

    // Método para actualizar la lista de tutorias
    public void setTutorias(List<Tutoria> tutorias) {
        submitList((List<Object>) (List<?>) tutorias);
    }

    // ViewHolder para cursos
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
                    Curso curso = (Curso) getItem(position);
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

    // ViewHolder para tutoria
    public class TutoriaViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNombreEstudiante;
        private TextView txtNombreCurso;
        private TextView txtTemaTutoria;
        private TextView txtComentariosTutoria;
        private TextView txtFechaTutoria;


        public TutoriaViewHolder(View itemView) {
            super(itemView);
            txtNombreEstudiante = itemView.findViewById(R.id.txtNombreEstudiante);
            txtNombreCurso = itemView.findViewById(R.id.txtNombreCurso);
            txtTemaTutoria = itemView.findViewById(R.id.txtTemaTutoria);
            txtComentariosTutoria = itemView.findViewById(R.id.txtComentariosTutoria);
            txtFechaTutoria = itemView.findViewById(R.id.txtFechaTutoria);
        }

        public void bind(Tutoria tutoria) {
            String nombreEstudiante = estudianteMap.getOrDefault(tutoria.getId_estudiante(), "Desconocido");
            txtNombreEstudiante.setText("Estudiante: " + nombreEstudiante);
            txtNombreCurso.setText("Curso: " + tutoria.getId_curso().getNombreCurso());
            txtTemaTutoria.setText("Tema: " + tutoria.getTema());
            txtComentariosTutoria.setText("Comentarios: " + tutoria.getComentarios());
            txtFechaTutoria.setText("Fecha: " + tutoria.getFecha());
        }
    }

}