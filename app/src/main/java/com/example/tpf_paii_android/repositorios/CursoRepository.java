package com.example.tpf_paii_android.repositorios;

import android.os.Handler;
import android.os.Looper;
import com.example.tpf_paii_android.conexion_database.DatabaseConnection;
import com.example.tpf_paii_android.modelos.CategoriaCurso;
import com.example.tpf_paii_android.modelos.Curso;
import com.example.tpf_paii_android.modelos.Evaluacion;
import com.example.tpf_paii_android.modelos.Inscripcion;
import com.example.tpf_paii_android.modelos.InscripcionEstado;
import com.example.tpf_paii_android.modelos.Opcion;
import com.example.tpf_paii_android.modelos.Pregunta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CursoRepository {

    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface DataCallback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }

    // todos los cursos async
    public void getAllCursos(DataCallback<ArrayList<Curso>> callback) {
        executor.execute(() -> {
            List<Curso> cursos = new ArrayList<>();
            String query = "SELECT * FROM curso WHERE estado = 1";

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
                            resultSet.getInt("estado")
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

    public void obtenerCategorias(DataCallback<List<CategoriaCurso>> callback) {
        executor.execute(() -> {
            List<CategoriaCurso> categorias = new ArrayList<>();
            String query = "SELECT * FROM categoria_curso";

            try {
                Class.forName(DatabaseConnection.driver);
                Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    CategoriaCurso categoria = new CategoriaCurso(
                            resultSet.getInt("id_categoria"),
                            resultSet.getString("descripcion")
                    );
                    categorias.add(categoria);
                }

                resultSet.close();
                statement.close();
                con.close();
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
                return;
            }
            new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(categorias));
        });
    }

    public void registrarInscripcion(Inscripcion inscripcion, DataCallback<Boolean> callback) {
        executor.execute(() -> {
            String query = "INSERT INTO inscripciones (id_curso, id_usuario, fecha_inscripcion, estado_inscripcion) VALUES (?, ?, ?, ?)";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement statement = con.prepareStatement(query)) {

                Class.forName(DatabaseConnection.driver);

                statement.setInt(1, inscripcion.getIdCurso());
                statement.setInt(2, inscripcion.getIdUsuario());
                statement.setDate(3, new java.sql.Date(inscripcion.getFechaInscripcion().getTime()));
                statement.setString(4, inscripcion.getEstadoInscripcion());

                int rowsInserted = statement.executeUpdate();

                mainHandler.post(() -> {
                    if (rowsInserted > 0) {
                        callback.onSuccess(true);
                    } else {
                        callback.onFailure(new SQLException("No se pudo insertar la inscripción"));
                    }
                });
            } catch (Exception e) {
                mainHandler.post(() -> callback.onFailure(e));
            }
        });
    }

public void verificarInscripcionEstado(int idCurso, int idUsuario, DataCallback<InscripcionEstado> callback) {
    executor.execute(() -> {
        String query = "SELECT id_inscripcion, estado_inscripcion FROM inscripciones WHERE id_curso = ? AND id_usuario = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
             PreparedStatement statement = con.prepareStatement(query)) {

            Class.forName(DatabaseConnection.driver);
            statement.setInt(1, idCurso);
            statement.setInt(2, idUsuario);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int idInscripcion = resultSet.getInt("id_inscripcion");
                String estadoInscripcion = resultSet.getString("estado_inscripcion");

                InscripcionEstado inscripcionEstado = new InscripcionEstado(idInscripcion, estadoInscripcion);
                mainHandler.post(() -> callback.onSuccess(inscripcionEstado)); // Devolver el idInscripcion y estado
            } else {
                mainHandler.post(() -> callback.onSuccess(null)); // Si no existe inscripción
            }
        } catch (Exception e) {
            mainHandler.post(() -> callback.onFailure(e)); // Manejo de error
        }
    });
}


    public void obtenerPreguntasConOpciones(int idCurso, DataCallback<List<Pregunta>> callback) {
        executor.execute(() -> {
            List<Pregunta> preguntas = new ArrayList<>();
            String queryPreguntas = "SELECT * FROM preguntas WHERE id_curso = ?";
            String queryOpciones = "SELECT * FROM opciones WHERE id_pregunta = ?";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement stmtPreguntas = con.prepareStatement(queryPreguntas)) {

                Class.forName(DatabaseConnection.driver);

                // Consulta para obtener todas las preguntas del curso
                stmtPreguntas.setInt(1, idCurso);
                ResultSet rsPreguntas = stmtPreguntas.executeQuery();

                while (rsPreguntas.next()) {
                    int idPregunta = rsPreguntas.getInt("id_pregunta");
                    String textoPregunta = rsPreguntas.getString("pregunta");
                    String tipoPregunta = rsPreguntas.getString("tipo_pregunta");

                    Pregunta pregunta = new Pregunta(idCurso, textoPregunta, tipoPregunta);

                    // Ahora obtenemos las opciones para esta pregunta
                    List<Opcion> opciones = new ArrayList<>();
                    try (PreparedStatement stmtOpciones = con.prepareStatement(queryOpciones)) {
                        stmtOpciones.setInt(1, idPregunta);
                        ResultSet rsOpciones = stmtOpciones.executeQuery();

                        while (rsOpciones.next()) {
                            int idOpcion = rsOpciones.getInt("id_opcion");
                            String opcionTexto = rsOpciones.getString("opcion_texto");
                            boolean esCorrecta = rsOpciones.getBoolean("es_correcta");

                            opciones.add(new Opcion(idOpcion, idPregunta, opcionTexto, esCorrecta));
                        }
                    }

                    pregunta.setOpciones(opciones);  // Asignamos las opciones a la pregunta
                    preguntas.add(pregunta);  // Añadimos la pregunta a la lista
                }

                rsPreguntas.close();
                mainHandler.post(() -> callback.onSuccess(preguntas));

            } catch (Exception e) {
                mainHandler.post(() -> callback.onFailure(e));
            }
        });
    }

public void registrarEvaluacion(Evaluacion evaluacion, DataCallback<Boolean> callback) {
    executor.execute(() -> {
        String queryEvaluacion = "INSERT INTO evaluaciones (id_inscripcion, nota_obtenida, fecha_finalizacion) VALUES (?, ?, ?)";
        String queryInscripcion = "UPDATE inscripciones SET estado_inscripcion = ? WHERE id_inscripcion = ?";

        try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
             PreparedStatement statementEvaluacion = con.prepareStatement(queryEvaluacion);
             PreparedStatement statementInscripcion = con.prepareStatement(queryInscripcion)) {

            Class.forName(DatabaseConnection.driver);

            // Registrar la evaluación
            statementEvaluacion.setInt(1, evaluacion.getIdInscripcion());
            statementEvaluacion.setInt(2, evaluacion.getNotaObtenida());
            statementEvaluacion.setDate(3, new java.sql.Date(evaluacion.getFechaFinalizacion().getTime()));

            int rowsInserted = statementEvaluacion.executeUpdate();

            if (rowsInserted > 0) {
                // Actualizar estado de la inscripción
                statementInscripcion.setString(1, "finalizado");
                statementInscripcion.setInt(2, evaluacion.getIdInscripcion());

                int rowsUpdated = statementInscripcion.executeUpdate();

                // Verificar si ambas operaciones fueron exitosas
                if (rowsUpdated > 0) {
                    mainHandler.post(() -> callback.onSuccess(true)); // Ambas operaciones fueron exitosas
                } else {
                    mainHandler.post(() -> callback.onFailure(new SQLException("No se pudo actualizar el estado de la inscripción")));
                }
            } else {
                mainHandler.post(() -> callback.onFailure(new SQLException("No se pudo insertar la evaluación")));
            }

        } catch (Exception e) {
            mainHandler.post(() -> callback.onFailure(e));
        }
    });
}

    public void guardarCurso(Curso curso, List<Pregunta> preguntas, List<Opcion> opciones, DataCallback<Boolean> callback) {
        executor.execute(() -> {
            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass)) {
                // Guardar el curso
                String queryCurso = "INSERT INTO curso (nombre_curso, descripcion, id_categoria, respuestas_correctas, estado) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement stmt = con.prepareStatement(queryCurso, Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, curso.getNombreCurso());
                    stmt.setString(2, curso.getDescripcion());
                    stmt.setInt(3, curso.getIdCategoria());
                    stmt.setString(4, curso.getRespuestasCorrectas());
                    stmt.setInt(5, curso.getEstado());
                    stmt.executeUpdate();
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int idCurso = generatedKeys.getInt(1);

                        List<Integer> preguntaIds = guardarPreguntas(con, idCurso, preguntas);

                        //ids de preguntas a las opciones y guardarlas
                        asignarIdsPreguntaAOpciones(preguntaIds, opciones);
                        guardarOpciones(con, opciones);
                    }
                }
                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(true));
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        });
    }

    private List<Integer> guardarPreguntas(Connection con, int idCurso, List<Pregunta> preguntas) throws SQLException {
        List<Integer> preguntaIds = new ArrayList<>();
        String queryPregunta = "INSERT INTO preguntas (id_curso, pregunta, tipo_pregunta) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(queryPregunta, Statement.RETURN_GENERATED_KEYS)) {
            for (Pregunta pregunta : preguntas) {
                stmt.setInt(1, idCurso);
                stmt.setString(2, pregunta.getPregunta());
                stmt.setString(3, pregunta.getTipoPregunta());
                stmt.addBatch();
            }
            stmt.executeBatch();

            ResultSet rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                preguntaIds.add(rs.getInt(1));
            }
        }
        return preguntaIds;
    }

    private void asignarIdsPreguntaAOpciones(List<Integer> preguntaIds, List<Opcion> opciones) {
        for (int i = 0; i < opciones.size(); i++) {
            // Asocia cada opción con el ID de su pregunta correspondiente
            opciones.get(i).setIdPregunta(preguntaIds.get(i / 2));  // Asume 3 opciones por pregunta
        }
    }

    private void guardarOpciones(Connection con, List<Opcion> opciones) throws SQLException {
        String queryOpcion = "INSERT INTO opciones (id_pregunta, opcion_texto, es_correcta) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(queryOpcion)) {
            for (Opcion opcion : opciones) {
                stmt.setInt(1, opcion.getIdPregunta());
                stmt.setString(2, opcion.getOpcionTexto());
                stmt.setBoolean(3, opcion.isEsCorrecta());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public void modificarDescripcionCurso(int idCurso, String nuevaDescripcion, DataCallback<Boolean> callback) {
        executor.execute(() -> {
            try {
                Class.forName(DatabaseConnection.driver);
                Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                String query = "UPDATE curso SET descripcion = ? WHERE id_curso = ?";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1, nuevaDescripcion);
                statement.setInt(2, idCurso);

                int rowsAffected = statement.executeUpdate();

                statement.close();
                con.close();

                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(rowsAffected > 0));
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        });
    }


//baja
public void actualizarEstadoCurso(int idCurso, int nuevoEstado, DataCallback<Boolean> callback) {
    executor.execute(() -> {
        try {
            Class.forName(DatabaseConnection.driver);
            Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);

            // estado del curso a 0 (inactivo)
            String query = "UPDATE curso SET estado = ? WHERE id_curso = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, nuevoEstado);
            statement.setInt(2, idCurso);

            int rowsAffected = statement.executeUpdate();
            statement.close();
            con.close();

            new Handler(Looper.getMainLooper()).post(() -> {
                callback.onSuccess(rowsAffected > 0);
            });
        } catch (Exception e) {
            new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
        }
    });
}
//optimizacin de recursos
    public void shutdownExecutor() {
        executor.shutdown();
    }

}