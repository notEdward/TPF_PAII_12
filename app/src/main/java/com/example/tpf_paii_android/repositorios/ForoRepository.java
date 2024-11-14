package com.example.tpf_paii_android.repositorios;

import android.content.Context;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tpf_paii_android.conexion_database.DatabaseConnection;
import com.example.tpf_paii_android.modelos.Foro;
import com.example.tpf_paii_android.modelos.ForoMensaje;

import java.util.List;

import android.os.Handler;
import android.os.Looper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ForoRepository {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface DataCallback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }

    public void getAllForos(DataCallback<List<Foro>> callback) {
        executor.execute(() -> {
            List<Foro> foros = new ArrayList<>();
            String query = "SELECT f.id_hilo, f.id_usuario_creador, f.nombreUsuario, f.titulo, f.descripcion_mensaje, f.fecha_creacion, " +
                    "(SELECT COUNT(*) FROM foro_mensajes WHERE foro_mensajes.id_hilo = f.id_hilo) AS numero_replicas " +
                    "FROM foro f " +
                    "ORDER BY f.fecha_creacion DESC";  // Query actualizada para contar las réplicas

            try {
                Class.forName(DatabaseConnection.driver);
                Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    Foro foro = new Foro(
                            resultSet.getInt("id_hilo"),
                            resultSet.getInt("id_usuario_creador"),
                            resultSet.getString("nombreUsuario"),
                            resultSet.getString("titulo"),
                            resultSet.getString("descripcion_mensaje"),
                            resultSet.getString("fecha_creacion"),
                            resultSet.getInt("numero_replicas") // Añadir número de réplicas al modelo
                    );
                    foros.add(foro);
                }

                resultSet.close();
                statement.close();
                con.close();
            } catch (Exception e) {
                mainHandler.post(() -> callback.onFailure(e));
                return;
            }
            mainHandler.post(() -> callback.onSuccess(foros));
        });
    }



    // Crear un nuevo foro
    public void createHilo(Foro foro, DataCallback<Boolean> callback) {
        executor.execute(() -> {
            String query = "INSERT INTO foro (id_usuario_creador, nombreUsuario, titulo, descripcion_mensaje, fecha_creacion) " +
                    "VALUES (?, ?, ?, ?, ?)";

            try {
                Class.forName(DatabaseConnection.driver);
                Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.setInt(1, foro.getIdUsuarioCreador());
                stmt.setString(2, foro.getNombreUsuario());
                stmt.setString(3, foro.getTitulo());
                stmt.setString(4, foro.getDescripcionMensaje());
                stmt.setString(5, foro.getFechaCreacion());
                int rowsInserted = stmt.executeUpdate();

                stmt.close();
                con.close();

                if (rowsInserted > 0) {
                    mainHandler.post(() -> callback.onSuccess(true));
                } else {
                    mainHandler.post(() -> callback.onFailure(new Exception("Error al insertar el foro")));
                }
            } catch (Exception e) {
                mainHandler.post(() -> callback.onFailure(e));
            }
        });
    }

    //responder
    public void agregarRespuesta(int idHilo, String nombreUsuario, String mensaje, DataCallback<Void> callback) {
        executor.execute(() -> {
            // Definir la consulta para insertar la respuesta en la tabla foro_mensajes
            String query = "INSERT INTO foro_mensajes (id_hilo, id_usuario, nombreUsuario, mensaje, fecha_mensaje) VALUES (?, ?, ?, ?, NOW())";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement stmt = con.prepareStatement(query)) {

                // Asignar los parámetros
                stmt.setInt(1, idHilo);  // id_hilo
                stmt.setInt(2, 1);        // id_usuario (simulamos un id de usuario 1 para este ejemplo, deberías obtenerlo del usuario actual)
                stmt.setString(3, nombreUsuario);  // nombreUsuario
                stmt.setString(4, mensaje);  // mensaje

                // Ejecutar la inserción
                stmt.executeUpdate();

                // Llamada de éxito
                mainHandler.post(() -> callback.onSuccess(null));

            } catch (Exception e) {
                // Manejo de errores
                mainHandler.post(() -> callback.onFailure(e));
            }
        });
    }

    public void getRespuestas(int idHilo, DataCallback<List<ForoMensaje>> callback) {
        executor.execute(() -> {
            List<ForoMensaje> respuestas = new ArrayList<>();
            String query = "SELECT fm.id_hilo, fm.nombreUsuario, fm.mensaje, fm.fecha_mensaje " +
                    "FROM foro_mensajes fm " +
                    "WHERE fm.id_hilo = ? " +
                    "ORDER BY fm.fecha_mensaje ASC";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement stmt = con.prepareStatement(query)) {

                // Asignar el id del hilo
                stmt.setInt(1, idHilo);

                ResultSet resultSet = stmt.executeQuery();

                while (resultSet.next()) {
                    ForoMensaje respuesta = new ForoMensaje(
                            resultSet.getInt("id_hilo"),
                            resultSet.getString("nombreUsuario"),
                            resultSet.getString("mensaje"),
                            resultSet.getString("fecha_mensaje")
                    );
                    respuestas.add(respuesta);
                }

                resultSet.close();
                // Llamada de éxito
                mainHandler.post(() -> callback.onSuccess(respuestas));

            } catch (Exception e) {
                // Manejo de errores
                mainHandler.post(() -> callback.onFailure(e));
            }
        });
    }

}

