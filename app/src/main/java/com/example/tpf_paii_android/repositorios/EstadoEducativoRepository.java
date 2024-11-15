package com.example.tpf_paii_android.repositorios;

import com.example.tpf_paii_android.conexion_database.DatabaseConnection;
import com.example.tpf_paii_android.modelos.EstadoNivelEducativo;
import com.example.tpf_paii_android.modelos.NivelEducativo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class EstadoEducativoRepository {

    public ArrayList<EstadoNivelEducativo> fetchEstadoNivelesEducativos() {
        ArrayList<EstadoNivelEducativo> listaEstados = new ArrayList<>();

        // Conexión a la base de datos
        try {
            Class.forName(DatabaseConnection.driver);
            Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM estado_nivel");


            // Iterar sobre los resultados y agregarlos a la lista
            while (rs.next()) {
                EstadoNivelEducativo nivel = new EstadoNivelEducativo();
                nivel.setId_estadoNivelEducativo(rs.getInt("id_estado_nivel"));
                nivel.setDescripcion(rs.getString("descripcion"));
                listaEstados.add(nivel);
            }
            rs.close();
            st.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace(); // Manejo básico de errores
        }
        return listaEstados;
    }

}
