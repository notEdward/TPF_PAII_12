package com.example.tpf_paii_android.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tpf_paii_android.modelos.Curso;

import java.util.List;

public class CursoOfertaAdapter extends ArrayAdapter<Curso> {
    public CursoOfertaAdapter(Context context, List<Curso> cursos) {
        super(context, android.R.layout.simple_spinner_item, cursos);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        // Mostrar solo el nombre del curso
        view.setText(getItem(position).getNombreCurso());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        // Mostrar solo el nombre del curso
        view.setText(getItem(position).getNombreCurso());
        return view;
    }
}
