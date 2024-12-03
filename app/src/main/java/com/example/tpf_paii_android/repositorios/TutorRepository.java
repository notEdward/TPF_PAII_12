package com.example.tpf_paii_android.repositorios;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.tpf_paii_android.conexion_database.DatabaseConnection;
import com.example.tpf_paii_android.modelos.Tutor;


import java.sql.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class TutorRepository {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    // Repositorios usados
    private final UsuarioRepository usuarioRepository;

    private final Context context;

    public TutorRepository (Context context) {
        this.context = context;
        this.usuarioRepository = new UsuarioRepository(context);
    }

    public interface DataCallBack<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }



    public Tutor obtenerTutor(int idTutor) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Tutor> futureResult = executor.submit(() -> {
            String query = "SELECT nombre, apellido, edad, id_genero, ocupacion, pasatiempos, info_adicional " +
                    "FROM tutor WHERE id_tutor = ?";
            Tutor tutor = null;

            try (Connection cn = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement ps = cn.prepareStatement(query)) {

                ps.setInt(1, idTutor);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        tutor = new Tutor(
                                rs.getString("nombre"),
                                rs.getString("apellido"),
                                rs.getInt("edad"),
                                rs.getInt("id_genero"),
                                rs.getString("ocupacion"),
                                rs.getString("pasatiempos"),
                                rs.getString("info_adicional")
                        );
                    }
                }

            } catch (SQLException e) {
                System.err.println("Error al obtener el tutor: " + e.getMessage());
                e.printStackTrace();
            }

            return tutor;
        });

        try {
            return futureResult.get(); // Espera el resultado del hilo secundario
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null; // Error al obtener el tutor
        } finally {
            executor.shutdown();
        }
    }






    // Registrar un nuevo Tutor
    public void registrarTutor(Tutor tutor, DataCallBack<Boolean> callback) {
        executor.execute(() -> {
            String queryUsuario = "INSERT INTO usuario (nombre_usuario, contrasena, id_tipo_usuario) VALUES (?, ?, ?)";
            String queryTutor = "INSERT INTO tutor (dni, id_usuario, nombre, apellido, edad, id_genero, ocupacion, pasatiempos, info_adicional) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass)) {
                con.setAutoCommit(false); // Inicia la transacción

                // Paso 1: Inserta el usuario
                try (PreparedStatement usuarioStmt = con.prepareStatement(queryUsuario, Statement.RETURN_GENERATED_KEYS)) {
                    usuarioStmt.setString(1, tutor.getNombreUsuario());
                    usuarioStmt.setString(2, tutor.getContrasenia());
                    usuarioStmt.setInt(3, 3); // Tipo de usuario 3 para tutor

                    int filasUsuario = usuarioStmt.executeUpdate();
                    if (filasUsuario == 0) {
                        throw new SQLException("Error al registrar usuario");
                    }

                    // Obtiene el ID del usuario generado
                    int idUsuario;
                    try (ResultSet generatedKeys = usuarioStmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            idUsuario = generatedKeys.getInt(1);
                        } else {
                            throw new SQLException("No se generó el ID para el usuario.");
                        }
                    }

                    // Paso 2: Inserta el tutor con el ID de usuario obtenido
                    try (PreparedStatement tutorStmt = con.prepareStatement(queryTutor)) {
                        tutorStmt.setString(1, tutor.getDni());
                        tutorStmt.setInt(2, idUsuario);
                        tutorStmt.setString(3, tutor.getNombre());
                        tutorStmt.setString(4, tutor.getApellido());
                        tutorStmt.setInt(5, tutor.getEdad());
                        tutorStmt.setInt(6, tutor.getIdGenero());
                        tutorStmt.setString(7, tutor.getOcupacion());
                        tutorStmt.setString(8, tutor.getPasatiempos());
                        tutorStmt.setString(9, tutor.getInfoAdicional());

                        int filasTutor = tutorStmt.executeUpdate();
                        if (filasTutor == 0) {
                            throw new SQLException("Error al registrar tutor");
                        }

                        // Confirma la transacción si ambas inserciones son exitosas
                        con.commit();
                        mainHandler.post(() -> callback.onSuccess(true));
                    } catch (SQLException e) {
                        // Rollback si la inserción de tutor falla
                        con.rollback();
                        mainHandler.post(() -> callback.onFailure(e));
                        e.printStackTrace();
                    }
                } catch (SQLException e) {
                    // Rollback si la inserción de usuario falla
                    con.rollback();
                    mainHandler.post(() -> callback.onFailure(e));
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                mainHandler.post(() -> callback.onFailure(e));
                e.printStackTrace();
            }
        });
    }



    // Método para mostrar un Toast
    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
