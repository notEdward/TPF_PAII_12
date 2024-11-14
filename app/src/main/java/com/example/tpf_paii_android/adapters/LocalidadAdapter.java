package com.example.tpf_paii_android.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tpf_paii_android.modelos.Localidad;

import java.util.List;

public class LocalidadAdapter extends ArrayAdapter<Localidad> {
    public LocalidadAdapter(Context context, List<Localidad> localidades) {
        super(context, android.R.layout.simple_spinner_item, localidades);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setText(getItem(position).getNombre());
        return view;
    }
}

