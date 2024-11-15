package com.example.tpf_paii_android.repositorios;

import com.example.tpf_paii_android.conexion_database.DatabaseConnection;
import com.example.tpf_paii_android.modelos.Empresa;
import com.example.tpf_paii_android.modelos.ExperienciaLaboral;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExpLaboralRepository {


    public Boolean registrarExpLaboral(ExperienciaLaboral expLab, int idUsuario) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> futureResult = executor.submit(() -> {
            boolean registrada = false;
            String queryEmpresa = "INSERT INTO experiencia_laboral (id_usuario, lugar_trabajo, " +
                                  "cargo_ocupado, tareas_realizadas, duracion) " +
                                  "VALUES (?, ?, ?, ?, ?)";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement ps = con.prepareStatement(queryEmpresa)) {

                ps.setInt(1, idUsuario);
                ps.setString(2, expLab.getLugar());
                ps.setString(3, expLab.getCargo());
                ps.setString(4, expLab.getTareas());
                ps.setString(5, expLab.getDuracion());

                // Ejecuta la consulta
                int filasAfectadas = ps.executeUpdate();

                // Verifica si la empresa fue registrada
                if (filasAfectadas > 0) {
                    registrada = true;
                }

            } catch (SQLException e) {
                System.err.println("Error al registrar la Exp Laboral: " + e.getMessage());
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
