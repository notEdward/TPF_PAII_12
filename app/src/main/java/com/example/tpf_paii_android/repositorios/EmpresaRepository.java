package com.example.tpf_paii_android.repositorios;

import com.example.tpf_paii_android.conexion_database.DatabaseConnection;
import com.example.tpf_paii_android.modelos.Empresa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class EmpresaRepository {

public Boolean registrarEmpresa(Empresa empresa, int idUsuario) {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Future<Boolean> futureResult = executor.submit(() -> {
        boolean registrada = false;
        String queryEmpresa = "INSERT INTO empresa (nombre, descripcion, sector, n_identificacion_fiscal, " +
                "direccion, id_localidad, email, telefono, id_usuario) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
             PreparedStatement ps = con.prepareStatement(queryEmpresa)) {

            ps.setString(1, empresa.getNombre());
            ps.setString(2, empresa.getDescripcion());
            ps.setString(3, empresa.getSector());
            ps.setString(4, empresa.getN_identificacionFiscal());
            ps.setString(5, empresa.getDireccion());
            ps.setInt(6, empresa.getId_localidad());
            ps.setString(7, empresa.getEmail());
            ps.setString(8, empresa.getTelefono());
            ps.setInt(9, idUsuario);

            // Ejecuta la consulta
            int filasAfectadas = ps.executeUpdate();

            // Verifica si la empresa fue registrada
            if (filasAfectadas > 0) {
                registrada = true;
            }

        } catch (SQLException e) {
            System.err.println("Error al registrar la empresa: " + e.getMessage());
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