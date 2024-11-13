package com.example.tpf_paii_android.repositorios;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.tpf_paii_android.conexion_database.DatabaseConnection;
import com.example.tpf_paii_android.modelos.Localidad;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LocalidadRepository {

    public ArrayList<Localidad> fetchLocalidadesByProvincia(int provinciaId) {
        ArrayList<Localidad> listaLocalidades = new ArrayList<>();

        try {
            // Conectar a la base de datos
            Class.forName(DatabaseConnection.driver);
            Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);

            // Consulta para obtener las localidades por id de provincia
            String query = "SELECT * FROM localidad WHERE id_provincia = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, provinciaId); // Establecemos el parámetro de la provincia
            ResultSet rs = ps.executeQuery();

            // Recorrer el ResultSet y agregar las localidades a la lista
            while (rs.next()) {
                Localidad localidad = new Localidad();
                localidad.setId_localidad(rs.getInt("id_localidad"));
                localidad.setNombre(rs.getString("nombre"));
                listaLocalidades.add(localidad);
            }

            // Cerrar los recursos
            rs.close();
            ps.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("LocalidadRepository", "Localidades cargadas: " + listaLocalidades.size());
        return listaLocalidades;
    }
}
