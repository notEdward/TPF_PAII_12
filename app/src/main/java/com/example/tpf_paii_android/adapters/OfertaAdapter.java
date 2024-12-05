package com.example.tpf_paii_android.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tpf_paii_android.R;
import com.example.tpf_paii_android.actividades.ofertas.ModificarOfertaActivity;
import com.example.tpf_paii_android.actividades.ofertas.OfertaActivity;
import com.example.tpf_paii_android.actividades.ofertas.OfertaDetalleActivity;
import com.example.tpf_paii_android.modelos.OfertaEmpleo;
import java.util.ArrayList;
import java.util.List;

public class OfertaAdapter extends RecyclerView.Adapter<OfertaAdapter.OfertaViewHolder> {

    private List<OfertaEmpleo> ofertas;
    private Context context;
    private String tipoUsuario; // Nuevo campo para el tipo de usuario
    private int idEspecifico;

    public OfertaAdapter(Context context, List<OfertaEmpleo> ofertas, String tipoUsuario, int idEspecifico) {
        this.context = context;
        this.ofertas = ofertas != null ? ofertas : new ArrayList<>();
        this.tipoUsuario = tipoUsuario;
        this.idEspecifico = idEspecifico;
    }

    public static class OfertaViewHolder extends RecyclerView.ViewHolder {
        ImageView imageOferta;
        TextView tituloTextView;
        TextView descripcionTextView;
        TextView tvVerOferta;

        public OfertaViewHolder(View itemView) {
            super(itemView);
            imageOferta = itemView.findViewById(R.id.imageOferta);
            tituloTextView = itemView.findViewById(R.id.tvTituloOferta);
            descripcionTextView = itemView.findViewById(R.id.tvDescripcionOferta);
            tvVerOferta = itemView.findViewById(R.id.tvVerOferta);
        }

    }

    @NonNull
    @Override
    public OfertaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_oferta, parent, false);
        return new OfertaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfertaViewHolder holder, int position) {
        OfertaEmpleo oferta = ofertas.get(position);
        holder.tituloTextView.setText(oferta.getTitulo());
        holder.descripcionTextView.setText(oferta.getDescripcion());

        Context context = holder.itemView.getContext();

        int imageResId = obtenerImagenPorCategoria(oferta.getIdCategoria());
        holder.imageOferta.setImageResource(imageResId);

        holder.itemView.setOnClickListener(v -> {
            if ("Empresa".equalsIgnoreCase(tipoUsuario) && (oferta.getIdEmpresa() == idEspecifico) ) {
                // desplegable de opciones
                PopupMenu popupMenu = new PopupMenu(context, holder.itemView);
                popupMenu.inflate(R.menu.menu_oferta_opciones);

                    popupMenu.setOnMenuItemClickListener(item -> {
                        if (item.getItemId() == R.id.action_ver) {
                            // Acción para "Ver" oferta
                            Intent intentVer = new Intent(context, OfertaDetalleActivity.class);
                            intentVer.putExtra("tituloOferta", oferta.getTitulo());
                            intentVer.putExtra("descripcionOferta", oferta.getDescripcion());
                            intentVer.putExtra("id_oferta_empleo", oferta.getId_ofertaEmpleo());
                            intentVer.putExtra("imageResId", imageResId);
                            context.startActivity(intentVer);
                            return true;

                        } else if (item.getItemId() == R.id.action_modificar) {
                            // Acción para "Modificar" oferta
                            Intent intentModificar = new Intent(context, ModificarOfertaActivity.class);
                            intentModificar.putExtra("id_oferta_empleo", oferta.getId_ofertaEmpleo());
                            intentModificar.putExtra("titulo", oferta.getTitulo());
                            intentModificar.putExtra("descripcion", oferta.getDescripcion());
                            intentModificar.putExtra("direccion", oferta.getDireccion());
                            intentModificar.putExtra("otrosRequisitos", oferta.getOtrosRequisitos());
                            intentModificar.putExtra("id_tipo_empleo", oferta.getIdTipoEmpleo());
                            intentModificar.putExtra("id_modalidad", oferta.getIdModalidad());
                            intentModificar.putExtra("id_nivel_educativo", oferta.getIdNivelEducativo());
                            intentModificar.putExtra("id_curso", oferta.getIdCurso());
                            intentModificar.putExtra("id_localidad", oferta.getIdLocalidad());
                            intentModificar.putExtra("imageResId", imageResId);
                            context.startActivity(intentModificar);
                            return true;

                        } else if (item.getItemId() == R.id.action_baja) {
                            // Acción para "Baja" oferta, mostrar un dialogo de confirmación
                            new AlertDialog.Builder(context)
                                    .setTitle("Confirmación")
                                    .setMessage("¿Está seguro que desea dar de baja esta oferta?\n\nLos estudiantes que se hayan postulado verán el estado como \"Finalizado\".")
                                    .setPositiveButton("Sí", (dialog, which) -> {
                                        // Notificar a la actividad para dar de baja la oferta
                                        if (context instanceof OfertaActivity) {
                                            ((OfertaActivity) context).darDeBajaOferta(oferta.getId_ofertaEmpleo());
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
                // Acción para usuario común (estudiante): solo abrir detalles de la oferta
                Intent intent = new Intent(context, OfertaDetalleActivity.class);
                intent.putExtra("tituloOferta", oferta.getTitulo());
                intent.putExtra("descripcionOferta", oferta.getDescripcion());
                intent.putExtra("id_oferta_empleo", oferta.getId_ofertaEmpleo());
                intent.putExtra("imageResId", imageResId);
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return ofertas != null ? ofertas.size() : 0;
    }

    public void setOfertas(List<OfertaEmpleo> nuevasOfertas) {
        if (this.ofertas != null) {
            this.ofertas.clear();
            if (nuevasOfertas != null) {
                this.ofertas.addAll(nuevasOfertas);
            }
        } else {
            this.ofertas = nuevasOfertas != null ? nuevasOfertas : new ArrayList<>();
        }
        notifyDataSetChanged();
    }
    private int obtenerImagenPorCategoria(int idCategoria) {
        switch (idCategoria) {
            case 1:
                return R.drawable.img_cat1;
            case 2:
                return R.drawable.img_cat2;
            case 3:
                return R.drawable.img_cat3;
            case 4:
                return R.drawable.img_cat4;
            case 5:
                return R.drawable.img_cat5;
            default:
                return R.drawable.img1_tpf;
        }
    }
}
