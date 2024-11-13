package com.example.tpf_paii_android.repositorios;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.tpf_paii_android.actividades.registracion.RegistrarEmpresa;
import com.example.tpf_paii_android.conexion_database.DatabaseConnection;
import com.example.tpf_paii_android.modelos.Provincia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ProvinciaRepository {

    public ArrayList<Provincia> fetchProvincias() {
        ArrayList<Provincia> listaProvincias = new ArrayList<>();

        try {
            // Conectar a la base de datos
            Class.forName(DatabaseConnection.driver);
            Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
            Statement st = con.createStatement();

            // Ejecutar consulta para obtener las provincias
            ResultSet rs = st.executeQuery("SELECT * FROM provincia");

            // Recorrer el ResultSet y agregar las provincias a la lista
            while (rs.next()) {
                Provincia provincia = new Provincia();
                provincia.setId_provincia(rs.getInt("id_provincia"));
                provincia.setNombre(rs.getString("nombre"));
                listaProvincias.add(provincia);
            }
            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProvincias;
    }
}
