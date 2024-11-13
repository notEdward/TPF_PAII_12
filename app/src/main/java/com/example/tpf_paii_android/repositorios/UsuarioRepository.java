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
import java.sql.Statement;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UsuarioRepository {

    public interface DataCallback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }

//    public boolean registrarUsuario(Usuario user) {
//        boolean registrado = false;
//        String query = "INSERT INTO usuario (nombre_usuario, contrasena, id_tipo_usuario) VALUES (?, ?, ?)";
//
//        // Verifica si el usuario existe antes de registrarlo
//        if (!ExisteUsuario(user.getNombreUsuario())) {
//            try (Connection cn = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
//                 PreparedStatement ps = cn.prepareStatement(query)) {
//
//                ps.setString(1, user.getNombreUsuario());
//                ps.setString(2, user.getContrasenia());
//                ps.setInt(3, user.getId_tipoUsuario());
//
//                // Ejecuta la consulta y verifica si se registrp el usuario
//                int filasAfectadas = ps.executeUpdate();
//                registrado = filasAfectadas > 0;
//
//            } catch (SQLException e) {
//                System.err.println("Error al registrar el usuario: " + e.getMessage());
//                e.printStackTrace();
//            }
//        } else {
//            System.out.println("El usuario ya existe en la base de datos.");
//        }
//
//        return registrado;
//    }
//
//
//    public boolean ExisteUsuario(String nombreUser) {
//        String query1 = "SELECT * FROM usuario WHERE nombre_usuario = ?";
//        try (Connection cn = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
//             PreparedStatement ps = cn.prepareStatement(query1)) {
//
//            ps.setString(1, nombreUser);
//            ResultSet rs = ps.executeQuery();
//
//            return rs.next(); // Devuelve true si encuentra un registro
//
//        } catch (SQLException e) {
//            System.err.println("Error en la consulta de existencia de usuario: " + e.getMessage());
//            e.printStackTrace();
//            return false;
//        }
//    }

    //Simulacion Asincronica
    public Integer registrarUsuario(Usuario user) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> futureResult = executor.submit(() -> {
            String query = "INSERT INTO usuario (nombre_usuario, contrasena, id_tipo_usuario) VALUES (?, ?, ?)";
            int idGenerado = -1; // en caso de error

            // ver si existe
            if (!existeUsuario(user.getNombreUsuario())) {
                try (Connection cn = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                     PreparedStatement ps = cn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

                    ps.setString(1, user.getNombreUsuario());
                    ps.setString(2, user.getContrasenia());
                    ps.setInt(3, user.getId_tipoUsuario());

                    // vemos el id para dps ver el tema de las fk
                    int filasAfectadas = ps.executeUpdate();
                    if (filasAfectadas > 0) {
                        ResultSet generatedKeys = ps.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            idGenerado = generatedKeys.getInt(1); // el id q se creo
                        }
                    }
                } catch (SQLException e) {
                    System.err.println("Error al registrar el usuario: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("El usuario ya existe en la base de datos.");
            }
            return idGenerado;
        });

        try {
            return futureResult.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return -1; //  -1  error
        } finally {
            executor.shutdown();
        }
    }


    public Boolean existeUsuario(String nombreUser) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> futureResult = executor.submit(() -> {
            String query = "SELECT * FROM usuario WHERE nombre_usuario = ?";
            boolean existe = false;

            try (Connection cn = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement ps = cn.prepareStatement(query)) {

                ps.setString(1, nombreUser);
                ResultSet rs = ps.executeQuery();
                existe = rs.next();
                rs.close();

            } catch (SQLException e) {
                System.err.println("Error al verificar la existencia del usuario: " + e.getMessage());
                e.printStackTrace();
            }
            return existe;
        });

        try {
            return futureResult.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        } finally {
            executor.shutdown();
        }
    }


}
