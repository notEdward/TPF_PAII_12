package com.example.tpf_paii_android.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.actividades.cursos.CursoActivity;
import com.example.tpf_paii_android.actividades.cursos.CursoDetalleActivity;
import com.example.tpf_paii_android.actividades.cursos.ModificarCursoActivity;
import com.example.tpf_paii_android.modelos.Curso;
import com.example.tpf_paii_android.repositorios.CursoRepository;

import java.util.List;

public class CursoAdapter extends RecyclerView.Adapter<CursoAdapter.CursoViewHolder> {
    private List<Curso> cursos;
    private String tipoUsuario;

    public CursoAdapter(List<Curso> cursos, String tipoUsuario) {
        this.cursos = cursos;
        this.tipoUsuario = tipoUsuario;
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

        Context context = holder.itemView.getContext();

        holder.itemView.setOnClickListener(v -> {
            if ("admin".equals(tipoUsuario)) {
                // Crear el PopupMenu para opciones de administrador
                PopupMenu popupMenu = new PopupMenu(context, holder.itemView);
                popupMenu.inflate(R.menu.menu_opciones_curso);

                popupMenu.setOnMenuItemClickListener(item -> {
                    if (item.getItemId() == R.id.opcion_ver) {
                        // Acción para "Ver" curso
                        Intent intentVer = new Intent(context, CursoDetalleActivity.class);
                        intentVer.putExtra("nombreCurso", curso.getNombreCurso());
                        intentVer.putExtra("descripcionCurso", curso.getDescripcion());
                        intentVer.putExtra("idCurso", curso.getIdCurso());
                        context.startActivity(intentVer);
                        return true;

                    } else if (item.getItemId() == R.id.opcion_modificar) {
                        // Acción para "Modificar" curso
//                        Intent intentModificar = new Intent(context, ModificarCursoActivity.class);
//                        intentModificar.putExtra("idCurso", curso.getIdCurso());
//                        context.startActivity(intentModificar);
//                        return true;
                        Intent intentModificar = new Intent(context, ModificarCursoActivity.class);
                        intentModificar.putExtra("idCurso", curso.getIdCurso());
                        intentModificar.putExtra("nombreCurso", curso.getNombreCurso());
                        intentModificar.putExtra("descripcionCurso", curso.getDescripcion());
//                        intentModificar.putExtra("imagenCursoResId", curso.getImagenResId()); // ID de recurso de imagen
                        context.startActivity(intentModificar);
                        return true;

                    } else if (item.getItemId() == R.id.opcion_baja) {
                        // Acción para "Baja" curso, mostrar un dialogo de confirmación
                        new AlertDialog.Builder(context)
                                .setTitle("Confirmación")
                                .setMessage("¿Está seguro que desea dar de baja este curso?")
                                .setPositiveButton("Sí", (dialog, which) -> {
                                    // Notificar a la actividad para dar de baja el curso
                                    if (context instanceof CursoActivity) {
                                        ((CursoActivity) context).darDeBajaCurso(curso.getIdCurso());
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                        return true;
                    }

                    return false;

                });

                popupMenu.show();
            } else {
                // Acción para usuario común: solo abrir detalles
                Intent intent = new Intent(context, CursoDetalleActivity.class);
                intent.putExtra("nombreCurso", curso.getNombreCurso());
                intent.putExtra("descripcionCurso", curso.getDescripcion());
                intent.putExtra("idCurso", curso.getIdCurso());
                context.startActivity(intent);
            }
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
            imageCurso = itemView.findViewById(R.id.imageCurso);
        }
    }
}
