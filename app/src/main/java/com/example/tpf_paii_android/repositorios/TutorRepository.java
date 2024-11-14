package com.example.tpf_paii_android.repositorios;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.tpf_paii_android.conexion_database.DatabaseConnection;
import com.example.tpf_paii_android.modelos.Tutor;


import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


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


    // Registrar un nuevo Tutor
    public void registrarTutor(Tutor tutor, DataCallBack<Boolean> callback) {
        executor.execute(() -> {
            String query = "INSERT INTO tutor (dni, nombre, apellido, edad, id_genero, ocupacion, pasatiempos, info_adicional, id_usuario) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass)) {
                // Inicio transaccion para asegurar consistencia de datos
                con.setAutoCommit(false);

                // Primero registramos el usuario
                usuarioRepository.registrarUsuario(tutor, 3, new UsuarioRepository.DataCallBack<Integer>() {
                    @Override
                    public void onSuccess(Integer idUsuario) {
                        try (PreparedStatement tutorStatement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                            tutorStatement.setString(1, tutor.getDni());
                            tutorStatement.setString(2, tutor.getNombre());
                            tutorStatement.setString(3, tutor.getApellido());
                            tutorStatement.setInt(4, tutor.getEdad());
                            tutorStatement.setInt(5, tutor.getIdGenero());
                            tutorStatement.setString(6, tutor.getOcupacion());
                            tutorStatement.setString(7, tutor.getPasatiempos());
                            tutorStatement.setString(8, tutor.getInfoAdicional());
                            tutorStatement.setInt(9, idUsuario); // Relaciona el tutor con el ID del usuario

                            int filasAfectadas = tutorStatement.executeUpdate();
                            if (filasAfectadas > 0) {
                                con.commit(); // Confirma transaccion
                                mainHandler.post(() -> callback.onSuccess(true));
                            } else {
                                con.rollback();
                                mainHandler.post(() -> callback.onFailure(new SQLException("Error al registrar tutor")));
                                showToast("Error al registrar tutor");  // Usando context para mostrar Toast
                            }
                        } catch (SQLException e) {
                            try {
                                con.rollback();
                            } catch (SQLException rollbackEx) {
                                e.addSuppressed(rollbackEx);
                            }
                            mainHandler.post(() -> callback.onFailure(e));
                            showToast("Error al registrar tutor en la base de datos");
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        mainHandler.post(() -> callback.onFailure(e));
                        showToast("Error al registrar usuario");
                    }
                });
            } catch (SQLException e) {
                mainHandler.post(() -> callback.onFailure(e));
                showToast("Error al conectar con la base de datos");
            }
        });
    }

    // Método para mostrar un Toast
    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
