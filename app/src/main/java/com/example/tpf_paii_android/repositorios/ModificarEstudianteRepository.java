package com.example.tpf_paii_android.repositorios;

import android.os.Handler;
import android.os.Looper;

import com.example.tpf_paii_android.conexion_database.DatabaseConnection;
import com.example.tpf_paii_android.modelos.EstadoNivelEducativo;
import com.example.tpf_paii_android.modelos.Estudiante;
import com.example.tpf_paii_android.modelos.ExperienciaLaboral;
import com.example.tpf_paii_android.modelos.Genero;
import com.example.tpf_paii_android.modelos.Localidad;
import com.example.tpf_paii_android.modelos.NivelEducativo;
import com.example.tpf_paii_android.modelos.Provincia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ModificarEstudianteRepository {

    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public interface DataCallback<T> {
        void onSuccess(T result);

        void onFailure(Exception e);
    }
    //metodos
    public void obtenerDetalleEstudiante(int idUsuario, ModificarEstudianteRepository.DataCallback<Estudiante> callback) {
        executor.execute(() -> {
            String query = "SELECT e.*, n.id_nivel_educativo, n.descripcion AS nivelEducativo, " +
                    "en.id_estado_nivel, en.descripcion AS estadoNivel, " +
                    "l.id_localidad, l.nombre AS localidad, " +
                    "p.id_provincia, p.nombre AS provincia, " +
                    "g.id_genero, g.descripcion AS genero, " +
                    "ex.id_experiencia_laboral, ex.lugar_trabajo, ex.cargo_ocupado, ex.tareas_realizadas, ex.duracion " +
                    "FROM estudiante e " +
                    "JOIN nivel_educativo n ON e.id_nivel_educativo = n.id_nivel_educativo " +
                    "JOIN estado_nivel en ON e.id_estado_nivel = en.id_estado_nivel " +
                    "JOIN localidad l ON e.id_localidad = l.id_localidad " +
                    "JOIN provincia p ON l.id_provincia = p.id_provincia " +
                    "JOIN genero g ON e.id_genero = g.id_genero " +
                    "LEFT JOIN experiencia_laboral ex ON e.id_usuario = ex.id_usuario " +
                    "WHERE e.id_usuario = ?";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, idUsuario);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    Estudiante estudiante = new Estudiante();
                    estudiante.setNombre(rs.getString("nombre"));
                    estudiante.setApellido(rs.getString("apellido"));
                    estudiante.setDni(rs.getString("dni"));
                    estudiante.setEmail(rs.getString("email"));
                    estudiante.setTelefono(rs.getString("telefono"));
                    estudiante.setDireccion(rs.getString("direccion"));
                    //obejo nivel educativo
                    int idNivelEducativo = rs.getInt("id_nivel_educativo");
                    String descripcionNivelEducativo = rs.getString("nivelEducativo");
                    NivelEducativo nivelEducativo = new NivelEducativo(idNivelEducativo, descripcionNivelEducativo);
                    estudiante.setNivelEducativo(nivelEducativo);

                    // objeto estadonivel
                    int idEstadoNivel = rs.getInt("id_estado_nivel");
                    String descripcionEstadoNivel = rs.getString("estadoNivel");
                    EstadoNivelEducativo estadoNivelEducativo = new EstadoNivelEducativo(idEstadoNivel, descripcionEstadoNivel);
                    estudiante.setEstadoNivelEducativo(estadoNivelEducativo);

                    // objeto genero
                    int idGenero = rs.getInt("id_genero");
                    String descripcionGenero = rs.getString("genero");
                    Genero genero = new Genero(idGenero, descripcionGenero);
                    estudiante.setGenero(genero);

                    // objeto genero
                    int idExpLaboral = rs.getInt("id_experiencia_laboral");
                    String lugarTrabajo = rs.getString("lugar_trabajo");
                    String cargoOcupado = rs.getString("cargo_ocupado");
                    String tareasRealizadas = rs.getString("tareas_realizadas");
                    String duracion = rs.getString("duracion");
                    ExperienciaLaboral experienciaLaboral = new ExperienciaLaboral(idExpLaboral, lugarTrabajo, cargoOcupado, tareasRealizadas, duracion);
                    estudiante.setExperienciaLaboral(experienciaLaboral);

                    // obj local
                    Localidad localidad = new Localidad();
                    int idLocalidad = rs.getInt("id_localidad");
                    String nombreLocalidad = rs.getString("localidad");
                    int idProvincia_localidad = rs.getInt("id_provincia");
                    localidad.setId_localidad(idLocalidad);
                    localidad.setNombre(nombreLocalidad);
                    localidad.setId_provincia(idProvincia_localidad);
                    estudiante.setLocalidad(localidad);

                    // obj prov
                    int idProvincia = rs.getInt("id_provincia");
                    String nombreProvincia = rs.getString("provincia");
                    Provincia provincia = new Provincia(idProvincia, nombreProvincia);
                    localidad.setId_provincia(provincia);

                    estudiante.setLocalidad(localidad);

                    callback.onSuccess(estudiante);
                } else {
                    callback.onSuccess(null);
                }
             } catch (Exception e) {
                callback.onFailure(e);
            }
        });
    }
    public void obtenerNivelesEducativos(ModificarEstudianteRepository.DataCallback<List<NivelEducativo>> callback) {
        executor.execute(() -> {
            List<NivelEducativo> nivelesEducativos = new ArrayList<>();
            String query = "SELECT * FROM nivel_educativo";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 Statement statement = con.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    NivelEducativo nivel = new NivelEducativo();
                    nivel.setId_nivelEducativo(resultSet.getInt("id_nivel_educativo"));
                    nivel.setDescripcion(resultSet.getString("descripcion"));
                    nivelesEducativos.add(nivel);
                }

                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(nivelesEducativos));

            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        });
    }
    public void obtenerEstadoNivelEducativo(ModificarEstudianteRepository.DataCallback<List<EstadoNivelEducativo>> callback) {
        executor.execute(() -> {
            List<EstadoNivelEducativo> estadoNivelesEducativos = new ArrayList<>();
            String query = "SELECT * FROM estado_nivel";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 Statement statement = con.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    EstadoNivelEducativo estadoNivel = new EstadoNivelEducativo();
                    estadoNivel.setId_estadoNivelEducativo(resultSet.getInt("id_estado_nivel"));
                    estadoNivel.setDescripcion(resultSet.getString("descripcion"));
                    estadoNivelesEducativos.add(estadoNivel);
                }

                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(estadoNivelesEducativos));

            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        });
    }
    public void obtenerGeneros(ModificarEstudianteRepository.DataCallback<List<Genero>> callback) {
        executor.execute(() -> {
            List<Genero> generos = new ArrayList<>();
            String query = "SELECT * FROM genero";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 Statement statement = con.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    Genero genero = new Genero();
                    genero.setId_genero(resultSet.getInt("id_genero"));
                    genero.setDescripcion(resultSet.getString("descripcion"));
                    generos.add(genero);
                }

                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(generos));

            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        });
    }
    public void obtenerLocalidades(ModificarEstudianteRepository.DataCallback<List<Localidad>> callback) {
        executor.execute(() -> {
            List<Localidad> localidades = new ArrayList<>();
            String query = "SELECT * FROM localidad";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 Statement statement = con.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    Localidad localidad = new Localidad();
                    localidad.setId_localidad(resultSet.getInt("id_localidad"));
                    localidad.setNombre(resultSet.getString("nombre"));
                    localidad.setId_provincia(resultSet.getInt("id_provincia"));
                    localidades.add(localidad);
                }

                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(localidades));

            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        });
    }

    public void obtenerProvincias(ModificarEstudianteRepository.DataCallback<List<Provincia>> callback) {
        executor.execute(() -> {
            List<Provincia> provincias = new ArrayList<>();
            String query = "SELECT * FROM provincia";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 Statement statement = con.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    Provincia provincia = new Provincia();
                    provincia.setId_provincia(resultSet.getInt("id_provincia"));
                    provincia.setNombre(resultSet.getString("nombre"));
                    provincias.add(provincia);
                }

                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(provincias));

            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        });
    }
    public void obtenerLocalidadesPorProvincia(int idProvincia, ModificarEstudianteRepository.DataCallback<List<Localidad>> callback) {
        executor.execute(() -> {
            List<Localidad> localidades = new ArrayList<>();
            String query = "SELECT * FROM localidad WHERE id_provincia = ?";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 PreparedStatement preparedStatement = con.prepareStatement(query)) {

                preparedStatement.setInt(1, idProvincia);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Localidad localidad = new Localidad();
                        localidad.setId_localidad(resultSet.getInt("id_localidad"));
                        localidad.setNombre(resultSet.getString("nombre"));
                        localidad.setId_provincia(resultSet.getInt("id_provincia"));
                        localidades.add(localidad);
                    }

                    new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(localidades));

                }

            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        });
    }

    public void actualizarEstudiante(Estudiante estudiante, DataCallback<Boolean> callback) {
        executor.execute(() -> {
            try (Connection connection = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass)) {
                connection.setAutoCommit(false);

                String updateEstudianteQuery = "UPDATE estudiante SET nombre = ?, apellido = ?, id_genero = ?, email = ?, " +
                        "telefono = ?, direccion = ?, id_localidad = ?, id_nivel_educativo = ?, id_estado_nivel = ? " +
                        "WHERE id_usuario = ?";
                try (PreparedStatement statementEstudiante = connection.prepareStatement(updateEstudianteQuery)) {
                    statementEstudiante.setString(1, estudiante.getNombre());
                    statementEstudiante.setString(2, estudiante.getApellido());
                    statementEstudiante.setInt(3, estudiante.getId_genero());
                    statementEstudiante.setString(4, estudiante.getEmail());
                    statementEstudiante.setString(5, estudiante.getTelefono());
                    statementEstudiante.setString(6, estudiante.getDireccion());
                    statementEstudiante.setInt(7, estudiante.getId_localidad());
                    statementEstudiante.setInt(8, estudiante.getId_nivelEducativo());
                    statementEstudiante.setInt(9, estudiante.getId_estadoNivelEducativo());
                    statementEstudiante.setInt(10, estudiante.getIdUsuario());

                    statementEstudiante.executeUpdate();
                }

                // Actualizar tabla experiencia_laboral
                String updateExperienciaQuery = "UPDATE experiencia_laboral SET lugar_trabajo = ?, cargo_ocupado = ?, tareas_realizadas = ?, duracion = ? " +
                        "WHERE id_usuario = ?";
                try (PreparedStatement statementExperiencia = connection.prepareStatement(updateExperienciaQuery)) {
                    ExperienciaLaboral experiencia = estudiante.getExperienciaLaboral();
                    statementExperiencia.setString(1, experiencia.getLugar());
                    statementExperiencia.setString(2, experiencia.getCargo());
                    statementExperiencia.setString(3, experiencia.getTareas());
                    statementExperiencia.setString(4, experiencia.getDuracion());
                    statementExperiencia.setInt(5, estudiante.getIdUsuario());

                    statementExperiencia.executeUpdate();
                }

                connection.commit(); // Confirmar transacción
                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(true));

            } catch (Exception e) {
                e.printStackTrace();
                try (Connection connection = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass)) {
                    connection.rollback(); // Revertir cambios si ocurre un error
                } catch (Exception rollbackException) {
                    rollbackException.printStackTrace();
                }
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        });
    }


    //fin metodos
    public void shutdownExecutor() {
        executor.shutdown();
    }
}
