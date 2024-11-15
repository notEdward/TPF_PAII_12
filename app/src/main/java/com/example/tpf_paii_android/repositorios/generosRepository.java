package com.example.tpf_paii_android.repositorios;

import com.example.tpf_paii_android.conexion_database.DatabaseConnection;
import com.example.tpf_paii_android.modelos.Genero;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class generosRepository {

public ArrayList<Genero> fetchGeneros() {
        ArrayList<Genero> listaGeneros = new ArrayList<>();

        // Conexión a la base de datos
        try {
            Class.forName(DatabaseConnection.driver);
            Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM genero");


            // Iterar sobre los resultados y agregarlos a la lista
            while (rs.next()) {
                Genero genero = new Genero();
                genero.setId_genero(rs.getInt("Id_genero"));
                genero.setDescripcion(rs.getString("descripcion"));
                listaGeneros.add(genero);
            }
            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace(); // Manejo básico de errores
        }
        return listaGeneros;
    }
}
