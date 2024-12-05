package com.example.tpf_paii_android.repositorios;

import android.os.Handler;
import android.os.Looper;
import com.example.tpf_paii_android.conexion_database.DatabaseConnection;
import com.example.tpf_paii_android.modelos.Curso;
import com.example.tpf_paii_android.modelos.Tutoria;

import java.sql.*;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public void getTutoriasPorTutor(int idTutor, DataCallback<List<Tutoria>> callback) {
        executor.execute(() -> {
            List<Tutoria> tutorias = new ArrayList<>();
            String query =
                    "SELECT " +
                            "    t.id_tutoria, " +
                            "    e.dni AS id_estudiante, " +
                            "    c.id_curso, " +
                            "    c.nombre_curso, " +
                            "    t.fecha, " +
                            "    t.tema, " +
                            "    t.comentarios " +
                            "FROM tutoria t " +
                            "JOIN estudiante e ON t.id_estudiante = e.id_usuario " +
                            "JOIN curso c ON t.id_curso = c.id_curso " +
                            "WHERE t.id_tutor = ?";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement preparedStatement = con.prepareStatement(query)) {

                preparedStatement.setInt(1, idTutor);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    // Crear un objeto Curso para asignar al objeto Tutoria
                    Curso curso = new Curso(
                            resultSet.getInt("id_curso"),
                            resultSet.getString("nombre_curso"),
                            null,
                            0,
                            null,
                            1 // Estado activo
                    );

                    // Crear instancia de Tutoria
                    Tutoria tutoria = new Tutoria(
                            resultSet.getInt("id_tutoria"),
                            null, // Suponiendo que no necesitas inicializar el Tutor
                            resultSet.getString("id_estudiante"),
                            curso,
                            resultSet.getDate("fecha"),
                            resultSet.getString("tema"),
                            resultSet.getString("comentarios")
                    );

                    tutorias.add(tutoria);
                }

                mainHandler.post(() -> callback.onSuccess(tutorias));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onFailure(e));
            }
        });
    }



    // Obtiene el mapa de estudiantes (id a nombre)
    public void getEstudiantesMap(DataCallback<Map<String, String>> callback) {
        executor.execute(() -> {
            Map<String, String> estudiantesMap = new HashMap<>();
            String query =
                    "SELECT u.id_usuario, u.nombre_usuario, e.nombre, e.apellido " +
                            "FROM usuario u " +
                            "JOIN estudiante e ON u.id_usuario = e.id_usuario";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement preparedStatement = con.prepareStatement(query)) {

                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String idUsuario = resultSet.getString("id_usuario");
                    String nombreUsuario = resultSet.getString("nombre_usuario");
                    String nombreEstudiante = resultSet.getString("nombre");
                    String apellidoEstudiante = resultSet.getString("apellido");

                    // Concatenamos el nombre completo
                    String nombreCompleto = nombreEstudiante + " " + apellidoEstudiante;

                    // Agregamos al mapa id_usuario -> nombre completo
                    estudiantesMap.put(idUsuario, nombreCompleto);
                }

                mainHandler.post(() -> callback.onSuccess(estudiantesMap));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onFailure(e));
            }
        });
    }

    // Obtiene el DNI del estudiante a partir del idUsuario
    public void getDniEstudiantePorIdUsuario(int idUsuario, DataCallback<String> callback) {
        executor.execute(() -> {
            final List<String> dniEstudiante = new ArrayList<>();
            String query =  "SELECT e.dni " +
                    "FROM estudiante e " +
                    "JOIN usuario u ON e.id_usuario = u.id_usuario " +
                    "WHERE u.id_usuario = ?";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement preparedStatement = con.prepareStatement(query)) {

                preparedStatement.setInt(1, idUsuario);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    dniEstudiante.add(resultSet.getString("dni"));
                }

                mainHandler.post(() -> callback.onSuccess(dniEstudiante.isEmpty() ? null : dniEstudiante.get(0)));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onFailure(e));
            }
        });
    }




    // Crear tutoría
    public void crearTutoria(int idTutor, String idEstudiante, int idCurso, Date fecha, String tema, String comentarios, DataCallback<Boolean> callback) {
        executor.execute(() -> {
            String query =  "INSERT INTO tutoria (id_tutor, id_estudiante, id_curso, fecha, tema, comentarios) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement preparedStatement = con.prepareStatement(query)) {

                preparedStatement.setInt(1, idTutor);
                preparedStatement.setString(2, idEstudiante);
                preparedStatement.setInt(3, idCurso);
                preparedStatement.setDate(4, fecha);
                preparedStatement.setString(5, tema);
                preparedStatement.setString(6, comentarios);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    mainHandler.post(() -> callback.onSuccess(true));  // Tutoría registrada correctamente
                } else {
                    mainHandler.post(() -> callback.onFailure(new Exception("Error al registrar la tutoría")));
                }
            } catch (Exception e) {
                mainHandler.post(() -> callback.onFailure(e));
            }
        });
    }

    // Obtener todos los tutores
    public void obtenerTutores(DataCallback<List<Integer>> callback) {
        executor.execute(() -> {
            String query = "SELECT id_tutor FROM tutor";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement preparedStatement = con.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                List<Integer> tutores = new ArrayList<>();
                while (resultSet.next()) {
                    tutores.add(resultSet.getInt("id_tutor"));
                }

                mainHandler.post(() -> callback.onSuccess(tutores));
            } catch (Exception e) {
                mainHandler.post(() -> callback.onFailure(e));
            }
        });
    }
}