package com.example.tpf_paii_android.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
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
import com.example.tpf_paii_android.modelos.Tutoria;

import java.util.List;
import java.util.Map;

public class TutoriasAdapter extends ListAdapter<Tutoria, TutoriasAdapter.TutoriaViewHolder> {

    private final Map<String, String> estudianteMap;
    private final OnTutoriaFinalizadaListener onTutoriaFinalizadaListener;

    public TutoriasAdapter(Map<String, String> estudianteMap, OnTutoriaFinalizadaListener onTutoriaFinalizadaListener) {
        super(new DiffUtil.ItemCallback<Tutoria>() {
            @Override
            public boolean areItemsTheSame(@NonNull Tutoria oldItem, @NonNull Tutoria newItem) {
                return oldItem.getId_tutoria() == newItem.getId_tutoria();
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull Tutoria oldItem, @NonNull Tutoria newItem) {
                return oldItem.equals(newItem);
            }
        });
        this.estudianteMap = estudianteMap;
        this.onTutoriaFinalizadaListener = onTutoriaFinalizadaListener;
    }

    @NonNull
    @Override
    public TutoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tutoria_asignada, parent, false);
        return new TutoriaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutoriaViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public class TutoriaViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtNombreEstudiante, txtNombreCurso, txtTemaTutoria, txtComentariosTutoria, txtFechaTutoria;
        private final Button btnFinalizarTutoria;

        public TutoriaViewHolder(View itemView) {
            super(itemView);
            txtNombreEstudiante = itemView.findViewById(R.id.txtNombreEstudiante);
            txtNombreCurso = itemView.findViewById(R.id.txtNombreCurso);
            txtTemaTutoria = itemView.findViewById(R.id.txtTemaTutoria);
            txtComentariosTutoria = itemView.findViewById(R.id.txtComentariosTutoria);
            txtFechaTutoria = itemView.findViewById(R.id.txtFechaTutoria);
            btnFinalizarTutoria = itemView.findViewById(R.id.btnFinalizarTutoria);

            btnFinalizarTutoria.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Tutoria tutoria = getItem(position);
                    if (tutoria != null) {
                        onTutoriaFinalizadaListener.onTutoriaFinalizada(tutoria.getId_tutoria());
                    } else {
                        Log.e("TutoriasAdapter", "Tutoría nula en la posición " + position);
                    }
                }
            });
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

    public interface OnTutoriaFinalizadaListener {
        void onTutoriaFinalizada(int idTutoria);
    }

    // Método para actualizar la lista de tutorias
    public void setTutorias(List<Tutoria> tutorias) {
        submitList(tutorias);
    }
}

