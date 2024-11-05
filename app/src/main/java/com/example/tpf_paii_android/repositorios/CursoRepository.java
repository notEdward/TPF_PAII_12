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

public class CursoRepository {

    public interface DataCallback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }

    // todos los cursos async
    public void getAllCursos(DataCallback<ArrayList<Curso>> callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Curso> cursos = new ArrayList<>();
            String query = "SELECT * FROM curso";

            try {
                Class.forName(DatabaseConnection.driver);
                Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    Curso curso = new Curso(
                            resultSet.getInt("id_curso"),
                            resultSet.getString("nombre_curso"),
                            resultSet.getString("descripcion"),
                            resultSet.getInt("id_categoria"),
                            resultSet.getString("respuestas_correctas"),
                            resultSet.getString("estado")
                    );
                    cursos.add(curso);
                }

                resultSet.close();
                statement.close();
                con.close();
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
                return;
            }
            //devuelvo los resultados callback
            new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess((ArrayList<Curso>) cursos));
        });
    }
}
