package com.example.tpf_paii_android.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tpf_paii_android.modelos.Provincia;

import java.util.List;

public class ProvinciaAdapter extends ArrayAdapter<Provincia> {
    public ProvinciaAdapter(Context context, List<Provincia> provincias) {
        super(context, android.R.layout.simple_spinner_item, provincias);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setText(getItem(position).getNombre());
        return view;
    }
}
