package com.example.tpf_paii_android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.modelos.Opcion;
import com.example.tpf_paii_android.modelos.Pregunta;

import java.util.ArrayList;
import java.util.List;

public class PreguntasAdapter extends RecyclerView.Adapter<PreguntasAdapter.PreguntaViewHolder> {

    private List<Pregunta> preguntas = new ArrayList<>();

    public void setPreguntas(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
        notifyDataSetChanged();
    }

    public List<Pregunta> getPreguntas() {
        return preguntas;
    }

    @NonNull
    @Override
    public PreguntaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pregunta, parent, false);
        return new PreguntaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PreguntaViewHolder holder, int position) {
        Pregunta pregunta = preguntas.get(position);
        holder.bind(pregunta);
    }

    @Override
    public int getItemCount() {
        return preguntas.size();
    }

    public class PreguntaViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtPregunta;
        private final RadioGroup opcionesGroup;

        public PreguntaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPregunta = itemView.findViewById(R.id.txtPregunta);
            opcionesGroup = itemView.findViewById(R.id.opcionesGroup);
        }

        public void bind(Pregunta pregunta) {
            txtPregunta.setText(pregunta.getPregunta());

            opcionesGroup.removeAllViews();

            // opcioes como radiobuttons
            for (Opcion opcion : pregunta.getOpciones()) {
                RadioButton radioButton = new RadioButton(itemView.getContext());
                radioButton.setText(opcion.getOpcionTexto());
                radioButton.setId(opcion.getIdOpcion());
                opcionesGroup.addView(radioButton);

                // Marcar la opción seleccionada si ya existe
                if (pregunta.getRespuestaSeleccionada() != null &&
                        pregunta.getRespuestaSeleccionada().getIdOpcion() == opcion.getIdOpcion()) {
                    radioButton.setChecked(true);
                }
            }

            // Listener para actualizar la respuesta seleccionada
            opcionesGroup.setOnCheckedChangeListener((group, checkedId) -> {
                for (Opcion opcion : pregunta.getOpciones()) {
                    if (opcion.getIdOpcion() == checkedId) {
                        pregunta.setRespuestaSeleccionada(opcion);
                        break;
                    }
                }
            });
        }
    }
}
