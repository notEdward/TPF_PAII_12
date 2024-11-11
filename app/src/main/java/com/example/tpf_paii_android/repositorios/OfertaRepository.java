package com.example.tpf_paii_android.repositorios;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.example.tpf_paii_android.conexion_database.DatabaseConnection;
import com.example.tpf_paii_android.modelos.Curso;
import com.example.tpf_paii_android.modelos.Modalidad;
import com.example.tpf_paii_android.modelos.OfertaEmpleo;
import com.example.tpf_paii_android.modelos.TipoEmpleo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OfertaRepository {

    public interface DataCallback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }

    public void getAllOfertas(DataCallback<ArrayList<OfertaEmpleo>> callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<OfertaEmpleo> ofertas = new ArrayList<>();
            String query = "SELECT * FROM ofertas_empleos";

            try {
                Class.forName(DatabaseConnection.driver);
                Connection connection = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    OfertaEmpleo oferta = new OfertaEmpleo(
                            resultSet.getInt("id_oferta_empleo"),
                            resultSet.getInt("id_empresa"),
                            resultSet.getString("titulo"),
                            resultSet.getString("descripcion"),
                            resultSet.getInt("id_tipo_empleo"),
                            resultSet.getInt("id_tipo_modalidad"),
                            resultSet.getInt("id_nivel_educativo"),
                            resultSet.getInt("id_curso"),
                            resultSet.getString("otros_requisitos"),
                            resultSet.getString("direccion"),
                            resultSet.getInt("id_localidad")
                    );
                    ofertas.add(oferta);
                }

                resultSet.close();
                statement.close();
                connection.close();
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
                return;
            }

            new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess((ArrayList<OfertaEmpleo>) ofertas));
        });
    }

    //Filtros de las ofertas
    // obtener modalidades
    public void obtenerModalidades(DataCallback<List<Modalidad>> callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Modalidad> modalidades = new ArrayList<>();
            String query = "SELECT * FROM modalidad";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 Statement statement = con.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    Modalidad modalidad = new Modalidad();
                    modalidad.setId_modalidad(resultSet.getInt("id_modalidad"));
                    modalidad.setDescripcion(resultSet.getString("descripcion"));
                    modalidades.add(modalidad);
                }

                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(modalidades));

            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        });
    }

    //  obtener tipos de empleo
    public void obtenerTiposEmpleo(DataCallback<List<TipoEmpleo>> callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<TipoEmpleo> tiposEmpleo = new ArrayList<>();
            String query = "SELECT * FROM tipo_empleo";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 Statement statement = con.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    TipoEmpleo tipoEmpleo = new TipoEmpleo();
                    tipoEmpleo.setId_tipoEmpleo(resultSet.getInt("id_tipo_empleo"));
                    tipoEmpleo.setDescripcion(resultSet.getString("descripcion"));
                    tiposEmpleo.add(tipoEmpleo);
                }

                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(tiposEmpleo));

            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        });
    }

    // obtener cursos
    public void obtenerCursos(DataCallback<List<Curso>> callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Curso> cursos = new ArrayList<>();
            String query = "SELECT * FROM curso";

            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL, DatabaseConnection.user, DatabaseConnection.pass);
                 Statement statement = con.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

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

                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(cursos));

            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        });
    }

    public void obtenerOfertasConFiltros(List<Integer> modalidadesSeleccionadas,
                                         List<Integer> tiposEmpleoSeleccionados,
                                         List<Integer> cursosSeleccionados,
                                         DataCallback<List<OfertaEmpleo>> callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<OfertaEmpleo> ofertas = new ArrayList<>();

            // consulta inicial
            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM ofertas_empleos WHERE 1=1");

            // Condiciones ( que esten en los ids seleccionados )
            if (!modalidadesSeleccionadas.isEmpty()) {
                queryBuilder.append(" AND id_tipo_modalidad IN (")
                        .append(TextUtils.join(",", modalidadesSeleccionadas))
                        .append(")");
            }

            if (!tiposEmpleoSeleccionados.isEmpty()) {
                queryBuilder.append(" AND id_tipo_empleo IN (")
                        .append(TextUtils.join(",", tiposEmpleoSeleccionados))
                        .append(")");
            }

            if (!cursosSeleccionados.isEmpty()) {
                queryBuilder.append(" AND id_curso IN (")
                        .append(TextUtils.join(",", cursosSeleccionados))
                        .append(")");
            }

            String query = queryBuilder.toString();
            try (Connection con = DriverManager.getConnection(DatabaseConnection.urlMySQL,
                    DatabaseConnection.user,
                    DatabaseConnection.pass);
                 Statement statement = con.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    OfertaEmpleo oferta = new OfertaEmpleo();
                    oferta.setId_ofertaEmpleo(resultSet.getInt("id_oferta_empleo"));
                    oferta.setId_empresa(resultSet.getInt("id_empresa"));
                    oferta.setTitulo(resultSet.getString("titulo"));
                    oferta.setDescripcion(resultSet.getString("descripcion"));
                    oferta.setId_tipoEmpleo(resultSet.getInt("id_tipo_empleo"));
                    oferta.setId_tipoModalidad(resultSet.getInt("id_tipo_modalidad"));
                    oferta.setId_nivelEducativo(resultSet.getInt("id_nivel_educativo"));
                    oferta.setId_curso(resultSet.getInt("id_curso"));
                    oferta.setOtrosRequisitos(resultSet.getString("otros_requisitos"));
                    oferta.setDireccion(resultSet.getString("direccion"));
                    oferta.setId_localidad(resultSet.getInt("id_localidad"));
                    ofertas.add(oferta);
                }

                new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(ofertas));

            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(e));
            }
        });
    }

    //fin filtros
}
