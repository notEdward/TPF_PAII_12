package com.example.tpf_paii_android.repositorios;

import android.os.Handler;
import android.os.Looper;
import com.example.tpf_paii_android.conexion_database.DatabaseConnection;
import com.example.tpf_paii_android.modelos.Curso;

import java.sql.*;
import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class TutoriasRepository {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface DataCallback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }

    public void getCursosPorEstudiante(int idUsuario, DataCallback<List<Curso>> callback) {
        executor.execute(() -> {
            ArrayList<Curso> cursos = new ArrayList<>();
            String query =  "SELECT c.id_curso, c.nombre_curso " + // Solo campos necesarios
                            "FROM curso c " +
                            "JOIN inscripciones i ON c.id_curso = i.id_curso " +
                            "WHERE i.id_usuario = ? AND i.estado_inscripcion = 'activo'";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement preparedStatement = con.prepareStatement(query)) {

                preparedStatement.setInt(1, idUsuario);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Curso curso = new Curso(
                            resultSet.getInt("id_curso"),
                            resultSet.getString("nombre_curso"),
                            null,
                            0,
                            null,
                            1     // Estado activo
                    );
                    cursos.add(curso);
                }

                mainHandler.post(() -> callback.onSuccess(cursos));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onFailure(e));
            }
        });
    }

}