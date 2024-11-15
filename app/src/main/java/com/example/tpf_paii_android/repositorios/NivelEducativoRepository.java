package com.example.tpf_paii_android.repositorios;

import com.example.tpf_paii_android.conexion_database.DatabaseConnection;
import com.example.tpf_paii_android.modelos.Genero;
import com.example.tpf_paii_android.modelos.NivelEducativo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class NivelEducativoRepository {

    public ArrayList<NivelEducativo> fetchNivelesEducativos() {
        ArrayList<NivelEducativo> listaNiveles = new ArrayList<>();

        // Conexión a la base de datos
        try {
            Class.forName(DatabaseConnection.driver);
            Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM nivel_educativo");


            // Iterar sobre los resultados y agregarlos a la lista
            while (rs.next()) {
                NivelEducativo nivel = new NivelEducativo();
                nivel.setId_nivelEducativo(rs.getInt("id_nivel_educativo"));
                nivel.setDescripcion(rs.getString("descripcion"));
                listaNiveles.add(nivel);
            }
            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace(); // Manejo básico de errores
        }
        return listaNiveles;
    }

}
