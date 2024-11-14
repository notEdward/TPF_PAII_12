package com.example.tpf_paii_android.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tpf_paii_android.modelos.TipoEmpleo;

import java.util.List;

public class TipoEmpleoAdapter extends ArrayAdapter<TipoEmpleo> {
    public TipoEmpleoAdapter(Context context, List<TipoEmpleo> tipoEmpleos) {
        super(context, android.R.layout.simple_spinner_item, tipoEmpleos);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        // Mostrar solo la descripción del tipo de empleo
        view.setText(getItem(position).getDescripcion());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        // Mostrar solo la descripción del tipo de empleo
        view.setText(getItem(position).getDescripcion());
        return view;
    }
}

