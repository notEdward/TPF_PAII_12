package com.example.tpf_paii_android.repositorios;

import com.example.tpf_paii_android.conexion_database.DatabaseConnection;
import com.example.tpf_paii_android.modelos.Empresa;
import com.example.tpf_paii_android.modelos.Estudiante;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EstudianteRepository {

    public Boolean registrarEstudiante(Estudiante estudiante, int idUsuario) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> futureResult = executor.submit(() -> {
            boolean registrada = false;
            String query = "INSERT INTO estudiante (dni, id_usuario, nombre, apellido, " +
                    "id_genero, email, telefono, direccion, id_localidad, id_nivel_educativo, id_estado_nivel) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement ps = con.prepareStatement(query)) {

                ps.setString(1, estudiante.getDni());
                ps.setInt(2,idUsuario);
                ps.setString(3, estudiante.getNombre());
                ps.setString(4, estudiante.getApellido());
                ps.setInt(5, estudiante.getId_genero());
                ps.setString(6, estudiante.getEmail());
                ps.setString(7, estudiante.getTelefono());
                ps.setString(8, estudiante.getDireccion());
                ps.setInt(9, estudiante.getId_localidad());
                ps.setInt(10, estudiante.getId_nivelEducativo());
                ps.setInt(11, estudiante.getId_estadoNivelEducativo());

                // Ejecuta la consulta
                int filasAfectadas = ps.executeUpdate();

                // Verifica si la empresa fue registrada
                if (filasAfectadas > 0) {
                    registrada = true;
                }

            } catch (SQLException e) {
                System.err.println("Error al registrar la Estudiante: " + e.getMessage());
                e.printStackTrace();
            }

            return registrada;
        });

        try {
            return futureResult.get(); // Espera a que la tarea termine y devuelve el resultado
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        } finally {
            executor.shutdown();
        }
    }
}
