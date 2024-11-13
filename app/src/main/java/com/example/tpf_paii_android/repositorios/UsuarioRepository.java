package com.example.tpf_paii_android.repositorios;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.tpf_paii_android.conexion_database.DatabaseConnection;
import com.example.tpf_paii_android.modelos.Usuario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UsuarioRepository {

    public boolean registrarUsuario(Usuario user) {
        boolean registrado = false;
        String query = "INSERT INTO usuario (nombre_usuario, contrasena, id_tipo_usuario) VALUES (?, ?, ?)";

        // Verifica si el usuario existe antes de registrarlo
        if (!ExisteUsuario(user.getNombreUsuario())) {
            try (Connection cn = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement ps = cn.prepareStatement(query)) {

                ps.setString(1, user.getNombreUsuario());
                ps.setString(2, user.getContrasenia());
                ps.setInt(3, user.getId_tipoUsuario());

                // Ejecuta la consulta y verifica si se registrp el usuario
                int filasAfectadas = ps.executeUpdate();
                registrado = filasAfectadas > 0;

            } catch (SQLException e) {
                System.err.println("Error al registrar el usuario: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("El usuario ya existe en la base de datos.");
        }

        return registrado;
    }


    public boolean ExisteUsuario(String nombreUser) {
        String query1 = "SELECT * FROM usuario WHERE nombre_usuario = ?";
        try (Connection cn = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
             PreparedStatement ps = cn.prepareStatement(query1)) {

            ps.setString(1, nombreUser);
            ResultSet rs = ps.executeQuery();

            return rs.next(); // Devuelve true si encuentra un registro

        } catch (SQLException e) {
            System.err.println("Error en la consulta de existencia de usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
