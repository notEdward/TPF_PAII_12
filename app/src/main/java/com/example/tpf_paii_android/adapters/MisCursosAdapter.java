package com.example.tpf_paii_android.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.modelos.MisCursoItem;

public class MisCursosAdapter extends ListAdapter<MisCursoItem, MisCursosAdapter.MisCursosViewHolder> {

    public MisCursosAdapter() {
        super(DIFF_CALLBACK); //el diff calback es para las listas para ver el nuevo contneido si es igual al anteroir
    }

    @Override
    public MisCursosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mis_curso, parent, false);
        return new MisCursosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MisCursosViewHolder holder, int position) {
        MisCursoItem item = getItem(position);
        holder.bind(item);
    }

    static class MisCursosViewHolder extends RecyclerView.ViewHolder {

        private TextView nombreCurso;
        private TextView estado;
        private TextView nota;
        private TextView fecha;

        public MisCursosViewHolder(View itemView) {
            super(itemView);
            nombreCurso = itemView.findViewById(R.id.textNombreCurso);
            estado = itemView.findViewById(R.id.textEstado);
            nota = itemView.findViewById(R.id.textNota);
            fecha = itemView.findViewById(R.id.textFecha);
        }

        public void bind(MisCursoItem item) {
            nombreCurso.setText(item.getNombreCurso());
            estado.setText(item.getEstado());

            // Si es una eval mostramos la nota y la fecha
            if (item.isEsEvaluacion()) {
                nota.setVisibility(View.VISIBLE);
                fecha.setVisibility(View.VISIBLE);
                nota.setText("Nota: " + item.getNotaObtenida());
                fecha.setText("Fecha: " + item.getFechaFinalizacion().toString());
            } else {
                nota.setVisibility(View.GONE);
                fecha.setVisibility(View.GONE);
            }
        }
    }

    private static final DiffUtil.ItemCallback<MisCursoItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<MisCursoItem>() {
        @Override
        public boolean areItemsTheSame(MisCursoItem oldItem, MisCursoItem newItem) {
            //para ver si son lo mimso
            return oldItem.getNombreCurso().equals(newItem.getNombreCurso());
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(MisCursoItem oldItem, MisCursoItem newItem) {
            // si son iguales son lo mismo
            return oldItem.equals(newItem);
        }
    };
}
